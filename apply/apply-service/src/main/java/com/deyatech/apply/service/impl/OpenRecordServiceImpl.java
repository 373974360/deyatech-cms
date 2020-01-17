package com.deyatech.apply.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.entity.Role;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.apply.entity.OpenModel;
import com.deyatech.apply.entity.OpenRecord;
import com.deyatech.apply.service.OpenModelService;
import com.deyatech.apply.mapper.OpenRecordMapper;
import com.deyatech.apply.service.OpenRecordService;
import com.deyatech.apply.vo.OpenRecordVo;
import com.deyatech.apply.vo.RecordMenuVo;
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
public class OpenRecordServiceImpl extends BaseServiceImpl<OpenRecordMapper, OpenRecord> implements OpenRecordService {


    @Autowired
    AdminFeign adminFeign;
    @Autowired
    OpenModelService openModelService;

    private static final String DEFAULT_RANDON_STR = "A-Z0-9";
    private static final String TREE_DATA = "{\"treeData\":[{\"label\":\"发布管理\",\"children\":[{\"label\":\"未发布\",\"isPublish\":0,\"applyFlag\":0,\"applyStatus\":\"\",\"alarmFlag\":\"\",\"superviseFlag\":\"\"},{\"label\":\"已发布\",\"isPublish\":1,\"applyFlag\":0,\"applyStatus\":\"\",\"alarmFlag\":\"\",\"superviseFlag\":\"\"}]},{\"label\":\"信件管理\",\"children\":[{\"label\": \"待处理\",\"isPublish\":\"\",\"applyFlag\":0,\"applyStatus\":0,\"alarmFlag\":\"\",\"superviseFlag\":\"\"},{\"label\":\"已办结\",\"isPublish\":\"\",\"applyFlag\":0,\"applyStatus\":3,\"alarmFlag\":\"\",\"superviseFlag\":\"\"}]},{\"label\":\"督查督办\",\"children\":[{\"label\":\"督办件\",\"isPublish\":\"\",\"applyFlag\":0,\"applyStatus\":\"\",\"alarmFlag\":\"\",\"superviseFlag\":1},{\"label\":\"预警件\",\"isPublish\":\"\",\"applyFlag\":0,\"applyStatus\":\"\",\"alarmFlag\":1,\"superviseFlag\":\"\"},{\"label\":\"黄牌件\",\"isPublish\":\"\",\"applyFlag\":0,\"applyStatus\":\"\",\"alarmFlag\":2,\"superviseFlag\":\"\"},{\"label\":\"红牌件\",\"isPublish\":\"\",\"applyFlag\":0,\"applyStatus\":\"\",\"alarmFlag\":3,\"superviseFlag\":\"\"}]},{\"label\":\"已处理信件\",\"children\":[{\"label\":\"不予受理\",\"isPublish\":\"\",\"applyFlag\":2,\"applyStatus\":\"\",\"alarmFlag\":\"\",\"superviseFlag\":\"\"},{\"label\":\"无效件\",\"isPublish\":\"\",\"applyFlag\":-1,\"applyStatus\":\"\",\"alarmFlag\":\"\",\"superviseFlag\":\"\"}]}]}";

    /**
     * 单个将对象转换为vo
     *
     * @param openRecord
     * @return
     */
    @Override
    public OpenRecordVo setVoProperties(OpenRecord openRecord){
        OpenRecordVo openRecordVo = new OpenRecordVo();
        BeanUtil.copyProperties(openRecord, openRecordVo);
        if(StrUtil.isNotBlank(openRecordVo.getDeptId())){
            Department department = adminFeign.getDepartmentById(openRecordVo.getDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                openRecordVo.setDeptName(department.getName());
            }
        }
        if(StrUtil.isNotBlank(openRecordVo.getProDeptId())){
            Department department = adminFeign.getDepartmentById(openRecordVo.getProDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                openRecordVo.setProDeptName(department.getName());
            }
        }
        if(StrUtil.isNotBlank(openRecordVo.getModelId())){
            OpenModel model = openModelService.getById(openRecordVo.getModelId());
            if(ObjectUtil.isNotNull(model)){
                openRecordVo.setModelName(model.getModelName());
            }
        }
        if(StrUtil.isNotBlank(openRecordVo.getReplyDeptId())){
            Department department = adminFeign.getDepartmentById(openRecordVo.getReplyDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                openRecordVo.setReplyDeptName(department.getName());
            }
        }
        return openRecordVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param openRecords
     * @return
     */
    @Override
    public List<OpenRecordVo> setVoProperties(Collection openRecords){
        List<OpenRecordVo> openRecordVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(openRecords)) {
            for (Object openRecord : openRecords) {
                OpenRecordVo openRecordVo = new OpenRecordVo();
                BeanUtil.copyProperties(openRecord, openRecordVo);
                if(StrUtil.isNotBlank(openRecordVo.getDeptId())){
                    Department department = adminFeign.getDepartmentById(openRecordVo.getDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        openRecordVo.setDeptName(department.getName());
                    }
                }
                if(StrUtil.isNotBlank(openRecordVo.getProDeptId())){
                    Department department = adminFeign.getDepartmentById(openRecordVo.getProDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        openRecordVo.setProDeptName(department.getName());
                    }
                }
                if(StrUtil.isNotBlank(openRecordVo.getModelId())){
                    OpenModel model = openModelService.getById(openRecordVo.getModelId());
                    if(ObjectUtil.isNotNull(model)){
                        openRecordVo.setModelName(model.getModelName());
                    }
                }
                if(StrUtil.isNotBlank(openRecordVo.getReplyDeptId())){
                    Department department = adminFeign.getDepartmentById(openRecordVo.getReplyDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        openRecordVo.setReplyDeptName(department.getName());
                    }
                }
                openRecordVos.add(openRecordVo);
            }
        }
        return openRecordVos;
    }

    @Override
    public IPage<OpenRecordVo> pageOpenRecordByBean(OpenRecord openRecord,String[] timeFrame, String userDepartmentId) {
        QueryWrapper<OpenRecord> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotNull(timeFrame)){
            queryWrapper.between("create_time",timeFrame[0],timeFrame[1]);
        }
        boolean allModel = false;
        List<Role> roles = adminFeign.getRolesByUserId(UserContextHelper.getUserId()).getData();
        if(CollectionUtil.isNotEmpty(roles)){
            for (Role role:roles){
                //当前用户拥有系统类型的角色
                if(role.getType() == 3){
                    allModel = true;
                }
            }
        }
        if(allModel){
            if(StrUtil.isNotBlank(openRecord.getModelId())) {
                queryWrapper.eq("model_id", openRecord.getModelId());
            }
        }else{
            if(StrUtil.isNotBlank(openRecord.getModelId())){
                queryWrapper.eq("model_id",openRecord.getModelId());
                QueryWrapper<OpenModel> query = new QueryWrapper<>();
                query.likeLeft("competent_dept", userDepartmentId);
                query.eq("id_", openRecord.getModelId());
                OpenModel model = openModelService.getOne(query);
                //当前登录用户的所属部门不是所选择业务的主管部门
                if(ObjectUtil.isNull(model)){
                    queryWrapper.eq("pro_dept_id", userDepartmentId);
                }
            } else {
                QueryWrapper<OpenModel> query = new QueryWrapper<>();
                query.likeLeft("competent_dept", userDepartmentId);
                Collection<OpenModel> models = openModelService.list(query);
                if(models != null && !models.isEmpty()){
                    //有主管业务查询主管业务的所有信息和处理部门是自己所属部门的信息
                    queryWrapper.and(i -> i.in("model_id", models.stream().map(OpenModel::getId).collect(Collectors.toList()))
                            .or().eq("pro_dept_id", userDepartmentId));
                }else{
                    //没有主管业务只查询处理部门是当前用户所属部门的信息
                    queryWrapper.or().eq("pro_dept_id", userDepartmentId);
                }
            }
        }
        if(StrUtil.isNotBlank(openRecord.getYsqCode())){
            queryWrapper.like("ysq_code", openRecord.getYsqCode());
        }
        if(openRecord.getApplyFlag() != null){
            queryWrapper.eq("apply_flag",openRecord.getApplyFlag());
        }
        if(openRecord.getApplyStatus() != null){
            queryWrapper.eq("apply_status",openRecord.getApplyStatus());
        }
        if(openRecord.getIsPublish() != null){
            queryWrapper.eq("is_publish",openRecord.getIsPublish());
        }
        if(openRecord.getAlarmFlag() != null){
            queryWrapper.eq("alarm_flag",openRecord.getAlarmFlag());
        }
        if(openRecord.getSuperviseFlag() != null){
            queryWrapper.eq("supervise_flag",openRecord.getSuperviseFlag());
        }
        IPage<OpenRecordVo> recordVoIPage = new Page<>(openRecord.getPage(),openRecord.getSize());
        IPage<OpenRecord> pages = super.page(getPageByBean(openRecord), queryWrapper);
        recordVoIPage.setRecords(setVoProperties(pages.getRecords()));
        recordVoIPage.setPages(pages.getPages());
        recordVoIPage.setTotal(pages.getTotal());
        return recordVoIPage;
    }

    @Override
    public String getQueryCode(String ModelId) {
        OpenModel model = openModelService.getById(ModelId);
        return RandomStrg.getRandomStr(DEFAULT_RANDON_STR, model.getQuerycodeCount() + "");
    }

    @Override
    public String getYsqCode(String ModelId) {
        OpenModel model = openModelService.getById(ModelId);
        //编码头＋日期＋随机码
        return model.getBusCode() + DateUtil.format(new Date(),model.getDayCode()) + RandomStrg.getRandomStr(DEFAULT_RANDON_STR, model.getRandomcodeCount() + "");
    }

    @Override
    public List<DepartmentVo> getCompetentDept(String modelId) {
        List<DepartmentVo> rootDepartments = CollectionUtil.newArrayList();
        List<Department> departments = adminFeign.getAllDepartments().getData();
        OpenModel model = openModelService.getById(modelId);
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
            if (ObjectUtil.equal(departmentVo.getId(), model.getCompetentDept().split(",")[0])) {
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
    public OpenRecord insertOpenRecord(OpenRecord openRecord) {
        OpenModel model = openModelService.getById(openRecord.getModelId());
        openRecord.setYsqCode(getYsqCode(model.getId()));
        openRecord.setQueryCode(getQueryCode(model.getId()));
        if(model.getAutoPublish().equals(YesNoEnum.YES.getCode())){
            openRecord.setIsPublish(YesNoEnum.YES.getCode());
        }else{
            openRecord.setIsPublish(YesNoEnum.NO.getCode());
        }
        openRecord.setApplyFlag(0);
        openRecord.setApplyStatus(0);
        openRecord.setAlarmFlag(0);
        openRecord.setSuperviseFlag(0);
        //如果业务模式为转发 或者网民选择了 "我不知道部门" 则处理部门全部为 主管部门，由主管部门转发给办理部门
        //if(model.getBusType() == 1 || openRecord.getDeptId().equals("-1") || StrUtil.isBlank(openRecord.getDeptId())){
        //    openRecord.setDeptId(competentDept[competentDept.length-1]);
        //}
        // 默认情况下 提交部门就是要处理该信息的部门
        openRecord.setProDeptId(openRecord.getDeptId());
        //设置信件截止处理日期
        openRecord.setTimeLimit(adminFeign.workDayAfter(new Date(),model.getLimitDay()).getData());
        super.save(openRecord);
        return openRecord;
    }

    @Override
    public OpenRecordVo queryApplyOpen(String sqCode, String queryCode) {
        OpenRecord record = new OpenRecord();
        record.setYsqCode(sqCode);
        record.setQueryCode(queryCode);
        return setVoProperties(super.getByBean(record));
    }

    @Override
    public IPage<OpenRecordVo> getApplyOpenList(Map<String, Object> maps, Integer page, Integer pageSize) {
        QueryWrapper<OpenRecord> queryWrapper = new QueryWrapper<>();
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
        IPage<OpenRecordVo> recordVoIPage = new Page<>(page,pageSize);
        OpenRecord record = new OpenRecord();
        record.setPage(page.longValue());
        record.setSize(pageSize.longValue());
        IPage<OpenRecord> pages = super.page(getPageByBean(record), queryWrapper);
        recordVoIPage.setRecords(setVoProperties(pages.getRecords()));
        recordVoIPage.setPages(pages.getPages());
        recordVoIPage.setTotal(pages.getTotal());
        recordVoIPage.setCurrent(pages.getCurrent());
        return recordVoIPage;
    }

    /**
     * 模型使用统计
     * @return
     */
    @Override
    public List<OpenRecordVo> countOpenModel() {
        return baseMapper.countOpenModel();
    }

    @Override
    public List<RecordMenuVo> resetTreeLabel(String userDepartmentId) {
        List<RecordMenuVo> result = new ArrayList<>();
        JSONObject obj = JSONUtil.parseObj(TREE_DATA);
        obj.getJSONArray("treeData").forEach(obj1 -> {
            JSONObject obj2 = JSONUtil.parseObj(obj1);
            RecordMenuVo recordMenuVo = new RecordMenuVo();
            recordMenuVo.setLabel(obj2.getStr("label"));
            List<RecordMenuVo> childrenList = new ArrayList<>();
            obj2.getJSONArray("children").forEach(obj3 -> {
                JSONObject children = JSONUtil.parseObj(obj3);
                RecordMenuVo recordMenuVo1 = new RecordMenuVo();
                recordMenuVo1.setIsPublish(children.getInt("isPublish"));
                recordMenuVo1.setApplyFlag(children.getInt("applyFlag"));
                recordMenuVo1.setApplyStatus(children.getInt("applyStatus"));
                recordMenuVo1.setAlarmFlag(children.getInt("alarmFlag"));
                recordMenuVo1.setSuperviseFlag(children.getInt("superviseFlag"));
                int count = getApplyCount(userDepartmentId, recordMenuVo1);
                if (count > 0) {
                    recordMenuVo1.setLabel(children.getStr("label") + "(" + count + ")");
                } else {
                    recordMenuVo1.setLabel(children.getStr("label"));
                }
                childrenList.add(recordMenuVo1);
            });
            recordMenuVo.setChildren(childrenList);
            result.add(recordMenuVo);
        });
        return result;
    }
    private int getApplyCount(String userDepartmentId,RecordMenuVo recordMenuVo){
        boolean allModel = false;
        List<Role> roles = adminFeign.getRolesByUserId(UserContextHelper.getUserId()).getData();
        if(CollectionUtil.isNotEmpty(roles)){
            for (Role role:roles){
                //当前用户拥有系统类型的角色
                if(role.getType() == 3){
                    allModel = true;
                }
            }
        }
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<OpenModel> query = new QueryWrapper<>();
        if(!allModel){
            query.likeLeft("competent_dept", userDepartmentId);
        }
        Collection<OpenModel> models = openModelService.list(query);
        if(CollectionUtil.isNotEmpty(models)){
            map.put("model_id", models.stream().map(OpenModel::getId).collect(Collectors.toList()));
        }
        map.put("pro_dept_id",userDepartmentId);
        if(ObjectUtil.isNotNull(recordMenuVo.getApplyFlag())){
            map.put("apply_flag",recordMenuVo.getApplyFlag());
        }
        if(ObjectUtil.isNotNull(recordMenuVo.getApplyStatus())){
            map.put("apply_status",recordMenuVo.getApplyStatus());
        }
        if(ObjectUtil.isNotNull(recordMenuVo.getIsPublish())){
            map.put("is_publish",recordMenuVo.getIsPublish());
        }
        if(ObjectUtil.isNotNull(recordMenuVo.getAlarmFlag())){
            map.put("alarm_flag",recordMenuVo.getAlarmFlag());
        }
        if(ObjectUtil.isNotNull(recordMenuVo.getSuperviseFlag())){
            map.put("supervise_flag",recordMenuVo.getSuperviseFlag());
        }
        return baseMapper.getApplyCount(map);
    }
}
