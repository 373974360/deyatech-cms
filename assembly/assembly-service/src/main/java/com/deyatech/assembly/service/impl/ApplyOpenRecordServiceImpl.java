package com.deyatech.assembly.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.assembly.entity.ApplyOpenModel;
import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.service.ApplyOpenModelService;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
import com.deyatech.assembly.mapper.ApplyOpenRecordMapper;
import com.deyatech.assembly.service.ApplyOpenRecordService;
import com.deyatech.common.Constants;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.common.utils.RandomStrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-16
 */
@Service
public class ApplyOpenRecordServiceImpl extends BaseServiceImpl<ApplyOpenRecordMapper, ApplyOpenRecord> implements ApplyOpenRecordService {


    @Autowired
    AdminFeign adminFeign;
    @Autowired
    ApplyOpenModelService applyOpenModelService;

    private static final String DEFAULT_RANDON_STR = "A-Z0-9";

    /**
     * 单个将对象转换为vo
     *
     * @param applyOpenRecord
     * @return
     */
    @Override
    public ApplyOpenRecordVo setVoProperties(ApplyOpenRecord applyOpenRecord){
        ApplyOpenRecordVo applyOpenRecordVo = new ApplyOpenRecordVo();
        BeanUtil.copyProperties(applyOpenRecord, applyOpenRecordVo);
        if(StrUtil.isNotBlank(applyOpenRecordVo.getDeptId())){
            Department department = adminFeign.getDepartmentById(applyOpenRecordVo.getDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                applyOpenRecordVo.setDeptName(department.getName());
            }
        }
        if(StrUtil.isNotBlank(applyOpenRecordVo.getProDeptId())){
            Department department = adminFeign.getDepartmentById(applyOpenRecordVo.getProDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                applyOpenRecordVo.setProDeptName(department.getName());
            }
        }
        if(StrUtil.isNotBlank(applyOpenRecordVo.getModelId())){
            ApplyOpenModel model = applyOpenModelService.getById(applyOpenRecordVo.getModelId());
            if(ObjectUtil.isNotNull(model)){
                applyOpenRecordVo.setModelName(model.getModelName());
            }
        }
        if(StrUtil.isNotBlank(applyOpenRecordVo.getReplyDeptId())){
            Department department = adminFeign.getDepartmentById(applyOpenRecordVo.getReplyDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                applyOpenRecordVo.setReplyDeptName(department.getName());
            }
        }
        return applyOpenRecordVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param applyOpenRecords
     * @return
     */
    @Override
    public List<ApplyOpenRecordVo> setVoProperties(Collection applyOpenRecords){
        List<ApplyOpenRecordVo> applyOpenRecordVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(applyOpenRecords)) {
            for (Object applyOpenRecord : applyOpenRecords) {
                ApplyOpenRecordVo applyOpenRecordVo = new ApplyOpenRecordVo();
                BeanUtil.copyProperties(applyOpenRecord, applyOpenRecordVo);
                if(StrUtil.isNotBlank(applyOpenRecordVo.getDeptId())){
                    Department department = adminFeign.getDepartmentById(applyOpenRecordVo.getDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        applyOpenRecordVo.setDeptName(department.getName());
                    }
                }
                if(StrUtil.isNotBlank(applyOpenRecordVo.getProDeptId())){
                    Department department = adminFeign.getDepartmentById(applyOpenRecordVo.getProDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        applyOpenRecordVo.setProDeptName(department.getName());
                    }
                }
                if(StrUtil.isNotBlank(applyOpenRecordVo.getModelId())){
                    ApplyOpenModel model = applyOpenModelService.getById(applyOpenRecordVo.getModelId());
                    if(ObjectUtil.isNotNull(model)){
                        applyOpenRecordVo.setModelName(model.getModelName());
                    }
                }
                if(StrUtil.isNotBlank(applyOpenRecordVo.getReplyDeptId())){
                    Department department = adminFeign.getDepartmentById(applyOpenRecordVo.getReplyDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        applyOpenRecordVo.setReplyDeptName(department.getName());
                    }
                }
                applyOpenRecordVos.add(applyOpenRecordVo);
            }
        }
        return applyOpenRecordVos;
    }

    @Override
    public IPage<ApplyOpenRecordVo> pageApplyOpenRecordByBean(ApplyOpenRecord applyOpenRecord,String[] timeFrame, String userDepartmentId) {
        QueryWrapper<ApplyOpenRecord> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotNull(timeFrame)){
            queryWrapper.between("create_time",timeFrame[0],timeFrame[1]);
        }
        if(StrUtil.isNotBlank(applyOpenRecord.getModelId())){
            queryWrapper.eq("model_id",applyOpenRecord.getModelId());
            QueryWrapper<ApplyOpenModel> query = new QueryWrapper<>();
            query.likeLeft("competent_dept", userDepartmentId);
            query.eq("id_", applyOpenRecord.getModelId());
            ApplyOpenModel model = applyOpenModelService.getOne(query);
            //当前登录用户的所属部门不是所选择业务的主管部门
            if(ObjectUtil.isNull(model)){
                queryWrapper.eq("pro_dept_id", userDepartmentId);
            }
        } else {
            QueryWrapper<ApplyOpenModel> query = new QueryWrapper<>();
            query.likeLeft("competent_dept", userDepartmentId);
            Collection<ApplyOpenModel> models = applyOpenModelService.list(query);
            if(models != null && !models.isEmpty()){
                //有主管业务查询主管业务的所有信息和处理部门是自己所属部门的信息
                queryWrapper.and(i -> i.in("model_id", models.stream().map(ApplyOpenModel::getId).collect(Collectors.toList()))
                        .or().eq("pro_dept_id", userDepartmentId));
            }else{
                //没有主管业务只查询处理部门是当前用户所属部门的信息
                queryWrapper.or().eq("pro_dept_id", userDepartmentId);
            }
        }
        if(StrUtil.isNotBlank(applyOpenRecord.getYsqCode())){
            queryWrapper.eq("ysq_code",applyOpenRecord.getYsqCode());
        }
        if(applyOpenRecord.getApplyFlag() != null){
            queryWrapper.eq("apply_flag",applyOpenRecord.getApplyFlag());
        }
        if(applyOpenRecord.getApplyStatus() != null){
            queryWrapper.eq("apply_status",applyOpenRecord.getApplyStatus());
        }
        if(applyOpenRecord.getIsBack() != null){
            queryWrapper.eq("is_back",applyOpenRecord.getIsBack());
        }
        if(applyOpenRecord.getLimitFlag() != null){
            queryWrapper.eq("limit_flag",applyOpenRecord.getLimitFlag());
        }
        if(applyOpenRecord.getIsPublish() != null){
            queryWrapper.eq("is_publish",applyOpenRecord.getIsPublish());
        }
        if(applyOpenRecord.getAlarmFlag() != null){
            queryWrapper.eq("alarm_flag",applyOpenRecord.getAlarmFlag());
        }
        IPage<ApplyOpenRecordVo> recordVoIPage = new Page<>(applyOpenRecord.getPage(),applyOpenRecord.getSize());
        IPage<ApplyOpenRecord> pages = super.page(getPageByBean(applyOpenRecord), queryWrapper);
        recordVoIPage.setRecords(setVoProperties(pages.getRecords()));
        recordVoIPage.setPages(pages.getPages());
        recordVoIPage.setTotal(pages.getTotal());
        return recordVoIPage;
    }

    @Override
    public String getQueryCode(String ModelId) {
        ApplyOpenModel model = applyOpenModelService.getById(ModelId);
        return RandomStrg.getRandomStr(DEFAULT_RANDON_STR, model.getQuerycodeCount() + "");
    }

    @Override
    public String getYsqCode(String ModelId) {
        ApplyOpenModel model = applyOpenModelService.getById(ModelId);
        //编码头＋日期＋随机码
        return model.getBusCode() + DateUtil.format(new Date(),model.getDayCode()) + RandomStrg.getRandomStr(DEFAULT_RANDON_STR, model.getRandomcodeCount() + "");
    }

    @Override
    public List<DepartmentVo> getCompetentDept(String modelId) {
        List<DepartmentVo> rootDepartments = CollectionUtil.newArrayList();
        List<Department> departments = adminFeign.getAllDepartments().getData();
        ApplyOpenModel model = applyOpenModelService.getById(modelId);
        String partDept = model.getPartDept();
        Map<String,String> deptMaps = new HashMap<>();
        if(StrUtil.isNotBlank(partDept)){
            for(String deptIds:partDept.split("&")){
                for(String deptId:deptIds.split(",")){
                    deptMaps.put(deptId,deptId);
                }
            }
        }
        List<DepartmentVo> departmentVos = CollectionUtil.newArrayList();
        if(CollectionUtil.isNotEmpty(departments)){
            for(Department dept:departments){
                if(deptMaps.containsKey(dept.getId())){
                    DepartmentVo departmentVo = new DepartmentVo();
                    BeanUtil.copyProperties(dept,departmentVo);
                    departmentVos.add(departmentVo);
                }
            }
        }
        for (DepartmentVo departmentVo : departmentVos) {
            departmentVo.setLabel(departmentVo.getName());
            if(StrUtil.isNotBlank(departmentVo.getTreePosition())){
                String[] split = departmentVo.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                departmentVo.setLevel(split.length);
            }else{
                departmentVo.setLevel(Constants.DEFAULT_ROOT_LEVEL);
            }
            if (ObjectUtil.equal(departmentVo.getParentId(), Constants.ZERO)) {
                rootDepartments.add(departmentVo);
            }
            for (DepartmentVo childVo : departmentVos) {
                if (ObjectUtil.equal(childVo.getParentId(), departmentVo.getId())) {
                    if (ObjectUtil.isNull(departmentVo.getChildren())) {
                        List<DepartmentVo> children = CollectionUtil.newArrayList();
                        children.add(childVo);
                        departmentVo.setChildren(children);
                    } else {
                        departmentVo.getChildren().add(childVo);
                    }
                }
            }
        }
        return rootDepartments;
    }

    @Override
    public ApplyOpenRecord insertApplyOpenRecord(ApplyOpenRecord applyOpenRecord) {
        ApplyOpenModel model = applyOpenModelService.getById(applyOpenRecord.getModelId());
        String[] competentDept = model.getCompetentDept().split(",");
        applyOpenRecord.setYsqCode(getYsqCode(model.getId()));
        applyOpenRecord.setQueryCode(getQueryCode(model.getId()));
        if(model.getAutoPublish().equals(YesNoEnum.YES.getCode())){
            applyOpenRecord.setIsPublish(YesNoEnum.YES.getCode());
        }else{
            applyOpenRecord.setIsPublish(YesNoEnum.NO.getCode());
        }
        applyOpenRecord.setApplyFlag(0);
        applyOpenRecord.setApplyStatus(0);
        applyOpenRecord.setIsBack(0);
        applyOpenRecord.setLimitFlag(0);
        applyOpenRecord.setAlarmFlag(0);
        //如果业务模式为转发 或者网民选择了 "我不知道部门" 则处理部门全部为 主管部门，由主管部门转发给办理部门
        if(model.getBusType() == 1 || applyOpenRecord.getDeptId().equals("-1") || StrUtil.isBlank(applyOpenRecord.getDeptId())){
            applyOpenRecord.setDeptId(competentDept[competentDept.length-1]);
        }
        // 默认情况下 提交部门就是要处理该信息的部门
        applyOpenRecord.setProDeptId(applyOpenRecord.getDeptId());
        //设置信件截止处理日期
        applyOpenRecord.setTimeLimit(adminFeign.workDayAfter(new Date(),model.getLimitDay()).getData());
        super.save(applyOpenRecord);
        return applyOpenRecord;
    }

    @Override
    public ApplyOpenRecordVo queryApplyOpen(String sqCode, String queryCode) {
        ApplyOpenRecord record = new ApplyOpenRecord();
        record.setYsqCode(sqCode);
        record.setQueryCode(queryCode);
        return setVoProperties(super.getByBean(record));
    }

    @Override
    public IPage<ApplyOpenRecordVo> getApplyOpenList(Map<String, Object> maps, Integer page, Integer pageSize) {
        QueryWrapper<ApplyOpenRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_publish",1)
                .eq("apply_flag",0);
        if(maps.containsKey("modelId")){
            queryWrapper.in("model_id",maps.get("modelId").toString().split(","));
        }
//        if(maps.containsKey("applyFlag")){
//            queryWrapper.in("apply_flag",maps.get("applyFlag").toString().split(","));
//        }
        if(maps.containsKey("applyStatus")){
            queryWrapper.in("apply_status",maps.get("applyStatus").toString().split(","));
        }
        if(maps.containsKey("deptId")){
            queryWrapper.eq("reply_dept_id",maps.get("deptId"));
        }
        if(maps.containsKey("ysqCode")){
            queryWrapper.eq("ysq_code",maps.get("ysqCode"));
        }
        if(maps.containsKey("queryCode")){
            queryWrapper.eq("query_code",maps.get("queryCode"));
        }
        if(maps.containsKey("orderby")){
            queryWrapper.orderByDesc(maps.get("orderby").toString());
        }else{
            queryWrapper.orderByDesc("reply_time");
        }
        IPage<ApplyOpenRecordVo> recordVoIPage = new Page<>(page,pageSize);
        ApplyOpenRecord record = new ApplyOpenRecord();
        record.setPage(page.longValue());
        record.setSize(pageSize.longValue());
        IPage<ApplyOpenRecord> pages = super.page(getPageByBean(record), queryWrapper);
        recordVoIPage.setRecords(setVoProperties(pages.getRecords()));
        recordVoIPage.setPages(pages.getPages());
        recordVoIPage.setTotal(pages.getTotal());
        recordVoIPage.setCurrent(pages.getCurrent());
        return recordVoIPage;
    }
}
