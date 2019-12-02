package com.deyatech.appeal.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.appeal.entity.Model;
import com.deyatech.appeal.entity.Purpose;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.service.ModelService;
import com.deyatech.appeal.service.PurposeService;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.appeal.mapper.RecordMapper;
import com.deyatech.appeal.service.RecordService;
import com.deyatech.common.Constants;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.entity.RestResult;
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
 * @since 2019-09-24
 */
@Service
public class RecordServiceImpl extends BaseServiceImpl<RecordMapper, Record> implements RecordService {

    @Autowired
    AdminFeign adminFeign;
    @Autowired
    ModelService modelService;
    @Autowired
    PurposeService purposeService;
    @Autowired
    RecordMapper recordMapper;

    private static final String DEFAULT_RANDON_STR = "A-Z0-9";

    /**
     * 单个将对象转换为vo
     *
     * @param record
     * @return
     */
    @Override
    public RecordVo setVoProperties(Record record){
        if(ObjectUtil.isNotNull(record)){
            RecordVo recordVo = new RecordVo();
            BeanUtil.copyProperties(record, recordVo);
            setProperties(recordVo, getDepartmentMap());
            return recordVo;
        }
        return null;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param records
     * @return
     */
    @Override
    public List<RecordVo> setVoProperties(Collection records){
        List<RecordVo> recordVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(records)) {
            Map<String, Department> departmentMap = getDepartmentMap();
            for (Object record : records) {
                RecordVo recordVo = new RecordVo();
                BeanUtil.copyProperties(record, recordVo);
                setProperties(recordVo, departmentMap);
                recordVos.add(recordVo);
            }
        }
        return recordVos;
    }

    private Map<String, Department> getDepartmentMap() {
        RestResult<List<Department>> result = adminFeign.getAllDepartments();
        if (result != null && CollectionUtil.isNotEmpty(result.getData())) {
            return result.getData().stream().collect(Collectors.toMap(Department::getId, d -> d));
        }
        return null;
    }

    private void setProperties(RecordVo recordVo, Map<String, Department> departmentMap) {
        if (departmentMap != null) {
            Department department = null;
            //收件部门名称
            if (StrUtil.isNotEmpty(recordVo.getDeptId())) {
                department = departmentMap.get(recordVo.getDeptId());
                recordVo.setDeptName(departmentMap.get(department.getId()).getName());
                if (StrUtil.isEmpty(department.getTreePosition())) {
                    recordVo.setTreePosition("&" + department.getId());
                } else {
                    recordVo.setTreePosition(department.getTreePosition() + "&" + department.getId());
                }
            }

            //处理部门名称
            if (StrUtil.isNotEmpty(recordVo.getProDeptId())) {
                department = departmentMap.get(recordVo.getProDeptId());
                recordVo.setProDeptName(department.getName());
            }

            //回复部门名称
            if (StrUtil.isNotEmpty(recordVo.getReplyDeptId())) {
                department = departmentMap.get(recordVo.getReplyDeptId());
                recordVo.setReplyDeptName(department.getName());
                if (StrUtil.isEmpty(department.getTreePosition())) {
                    recordVo.setReplyTreePosition("&" + department.getId());
                } else {
                    recordVo.setReplyTreePosition(department.getTreePosition() + "&" + department.getId());
                }
            }
        }
        //业务模型名称
        if(StrUtil.isNotBlank(recordVo.getModelId())){
            Model model = modelService.getById(recordVo.getModelId());
            if(ObjectUtil.isNotNull(model)){
                recordVo.setModelName(model.getModelName());
            }
        }
        //诉求目的名称
        if(StrUtil.isNotBlank(recordVo.getPurId())){
            Purpose purpose = purposeService.getById(recordVo.getPurId());
            if(ObjectUtil.isNotNull(purpose)){
                recordVo.setPurposeName(purpose.getPurposeName());
            }
        }
    }

    @Override
    public IPage<RecordVo> pageRecordByBean(Record record,String[] timeFrame, String userDepartmentId) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotNull(timeFrame)){
            queryWrapper.between("create_time",timeFrame[0],timeFrame[1]);
        }
        if(StrUtil.isNotBlank(record.getModelId())){
            queryWrapper.eq("model_id",record.getModelId());
            QueryWrapper<Model> query = new QueryWrapper<>();
            query.likeLeft("competent_dept", userDepartmentId);
            query.eq("id_", record.getModelId());
            Model model = modelService.getOne(query);
            //当前登录用户的所属部门不是所选择业务的主管部门
            if(ObjectUtil.isNull(model)){
                queryWrapper.eq("pro_dept_id", userDepartmentId);
            }
        } else {
            QueryWrapper<Model> query = new QueryWrapper<>();
            query.likeLeft("competent_dept", userDepartmentId);
            Collection<Model> models = modelService.list(query);
            if(models != null && !models.isEmpty()){
                //有主管业务查询主管业务的所有信息和处理部门是自己所属部门的信息
                queryWrapper.and(i -> i.in("model_id", models.stream().map(Model::getId).collect(Collectors.toList()))
                    .or().eq("pro_dept_id", userDepartmentId));
            }else{
                //没有主管业务只查询处理部门是当前用户所属部门的信息
                queryWrapper.or().eq("pro_dept_id", userDepartmentId);
            }
        }
        if(StrUtil.isNotBlank(record.getPurId())){
            queryWrapper.eq("pur_id",record.getPurId());
        }
        if(StrUtil.isNotBlank(record.getTitle())){
            queryWrapper.and(i -> i
                    .like("title", record.getTitle())
                    .or().like("user_name",record.getTitle())
                    .or().like("query_code",record.getTitle())
                    .or().like("card_id",record.getTitle())
            );
        }
        if(record.getSqFlag() != null){
            queryWrapper.eq("sq_flag",record.getSqFlag());
        }
        if(record.getSqStatus() != null){
            queryWrapper.eq("sq_status",record.getSqStatus());
        }
        if(record.getIsBack() != null){
            queryWrapper.eq("is_back",record.getIsBack());
        }
        if(record.getLimitFlag() != null){
            queryWrapper.eq("limit_flag",record.getLimitFlag());
        }
        if(record.getIsPublish() != null){
            queryWrapper.eq("is_publish",record.getIsPublish());
        }
        if(record.getAlarmFlag() != null){
            queryWrapper.eq("alarm_flag",record.getAlarmFlag());
        }
        IPage<RecordVo> recordVoIPage = new Page<>(record.getPage(),record.getSize());
        IPage<Record> pages = super.page(getPageByBean(record), queryWrapper);
        recordVoIPage.setRecords(setVoProperties(pages.getRecords()));
        recordVoIPage.setPages(pages.getPages());
        recordVoIPage.setTotal(pages.getTotal());
        return recordVoIPage;
    }

    @Override
    public String getQueryCode(String modelId) {
        Model model = modelService.getById(modelId);
        return RandomStrg.getRandomStr(DEFAULT_RANDON_STR, model.getQuerycodeCount() + "");
    }

    @Override
    public String getAppealCode(String modelId) {
        Model model = modelService.getById(modelId);
        //编码头＋日期＋随机码
        return model.getBusCode() + DateUtil.format(new Date(),model.getDayCode()) + RandomStrg.getRandomStr(DEFAULT_RANDON_STR, model.getRandomcodeCount() + "");
    }

    @Override
    public List<DepartmentVo> getCompetentDept(String modelId) {
        List<DepartmentVo> rootDepartments = CollectionUtil.newArrayList();
        List<Department> departments = adminFeign.getAllDepartments().getData();
        Model model = modelService.getById(modelId);
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
    public IPage<RecordVo> getAppealList(Map<String, Object> maps, Integer page, Integer pageSize) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_publish",1)
                .eq("is_open",1)
                .eq("sq_flag",0);
        if(maps.containsKey("modelId")){
            queryWrapper.in("model_id",maps.get("modelId").toString().split(","));
        }
//        if(maps.containsKey("sqFlag")){
//            queryWrapper.in("sq_flag",maps.get("sqFlag").toString().split(","));
//        }
        if(maps.containsKey("sqStatus")){
            queryWrapper.in("sq_status",maps.get("sqStatus").toString().split(","));
        }
        if(maps.containsKey("purId")){
            queryWrapper.in("pur_id",maps.get("purId").toString().split(","));
        }
        if(maps.containsKey("title")){
            queryWrapper.like("title",maps.get("title"));
        }
        if(maps.containsKey("deptId")){
            queryWrapper.eq("reply_dept_id",maps.get("deptId"));
        }
        if(maps.containsKey("sqCode")){
            queryWrapper.eq("sq_code",maps.get("sqCode"));
        }
        if(maps.containsKey("queryCode")){
            queryWrapper.eq("query_code",maps.get("queryCode"));
        }
        if(maps.containsKey("orderby")){
            queryWrapper.orderByDesc(maps.get("orderby").toString());
        }else{
            queryWrapper.orderByDesc("reply_time");
        }
        IPage<RecordVo> recordVoIPage = new Page<>(page,pageSize);
        Record record = new Record();
        record.setPage(page.longValue());
        record.setSize(pageSize.longValue());
        IPage<Record> pages = super.page(getPageByBean(record), queryWrapper);
        recordVoIPage.setRecords(setVoProperties(pages.getRecords()));
        recordVoIPage.setPages(pages.getPages());
        recordVoIPage.setTotal(pages.getTotal());
        recordVoIPage.setCurrent(pages.getCurrent());
        return recordVoIPage;
    }

    @Override
    public RecordVo queryAppeal(String sqCode, String queryCode) {
        Record record = new Record();
        record.setSqCode(sqCode);
        record.setQueryCode(queryCode);
        return setVoProperties(super.getByBean(record));
    }

    @Override
    public Record insertAppeal(Record record) {
        Model model = modelService.getById(record.getModelId());
        String[] competentDept = model.getCompetentDept().split(",");
        record.setSqCode(getAppealCode(model.getId()));
        record.setQueryCode(getQueryCode(model.getId()));

        if(model.getAutoPublish().equals(YesNoEnum.YES.getCode())){
            record.setIsPublish(YesNoEnum.YES.getCode());
        }else{
            record.setIsPublish(YesNoEnum.NO.getCode());
        }
        record.setSqFlag(0);
        record.setSqStatus(0);
        record.setIsBack(0);
        record.setLimitFlag(0);
        record.setAlarmFlag(0);
        //如果业务模式为转发 或者网民选择了 "我不知道部门" 则处理部门全部为 主管部门，由主管部门转发给办理部门
        if(model.getBusType() == 1 || record.getDeptId().equals("-1") || StrUtil.isBlank(record.getDeptId())){
            record.setDeptId(competentDept[competentDept.length-1]);
        }
        // 默认情况下 提交部门就是要处理该信息的部门
        record.setProDeptId(record.getDeptId());

        //设置信件截止处理日期
        record.setTimeLimit(adminFeign.workDayAfter(new Date(),model.getLimitDay()).getData());
        super.save(record);
        return record;
    }

    @Override
    public String getAllAppealCount(Map<String, Object> maps) {
        String countData = maps.get("countData").toString();
        //本年度
        if("years".equals(countData)) {
            maps.put("startData", DateUtil.format(new Date(),"yyyy")+"-01-01 00:00:00");
        }
        //昨日
        if("yesterday".equals(countData)) {
            maps.put("startData", DateUtil.format(DateUtil.offsetDay(DateUtil.parseDate(DateUtil.format(new Date(),"yyyy-MM-dd")),-1),"yyyy-MM-dd HH:mm:ss"));
            maps.put("endData", DateUtil.format(DateUtil.offsetDay(DateUtil.parseDate(DateUtil.format(new Date(),"yyyy-MM-dd")),-1),"yyyy-MM-dd")+" 23:59:59");
        }
        //上月
        if("ultimo".equals(countData)) {
            maps.put("ultimo",DateUtil.format(DateUtil.lastMonth(),"yyyy-MM"));
        }
        //本月
        if("instant".equals(countData)) {
            maps.put("instant", DateUtil.format(new Date(),"yyyy-MM"));
        }
        //当天
        if("curday".equals(countData)) {
            maps.put("startData",  DateUtil.format(new Date(),"yyyy-MM-dd")+" 00:00:00");
        }
        return recordMapper.getAllAppealCount(maps);
    }
}
