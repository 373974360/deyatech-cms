package com.deyatech.appeal.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.appeal.entity.Model;
import com.deyatech.appeal.entity.Process;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.service.ModelService;
import com.deyatech.appeal.service.RecordService;
import com.deyatech.appeal.vo.ProcessVo;
import com.deyatech.appeal.mapper.ProcessMapper;
import com.deyatech.appeal.service.ProcessService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.workflow.feign.WorkflowFeign;
import com.deyatech.workflow.vo.ProcessInstanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-09-24
 */
@Service
public class ProcessServiceImpl extends BaseServiceImpl<ProcessMapper, Process> implements ProcessService {

    @Autowired
    AdminFeign adminFeign;
    @Autowired
    RecordService recordService;
    @Autowired
    ModelService modelService;
    @Autowired
    WorkflowFeign workflowFeign;

    /**
     * 单个将对象转换为vo
     *
     * @param process
     * @return
     */
    @Override
    public ProcessVo setVoProperties(Process process){
        ProcessVo processVo = new ProcessVo();
        BeanUtil.copyProperties(process, processVo);
        //处理部门名称
        if(StrUtil.isNotBlank(process.getProDeptId())){
            Department department = adminFeign.getDepartmentById(process.getProDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                processVo.setProDeptName(department.getName());
            }
        }
        //移交部门名称
        if(StrUtil.isNotBlank(process.getToDeptId())){
            Department department = adminFeign.getDepartmentById(process.getToDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                processVo.setToDeptName(department.getName());
            }
        }
        //处理人姓名
        if(StrUtil.isNotBlank(process.getCreateBy())){
            UserVo userVo = adminFeign.getUserByUserId(process.getCreateBy()).getData();
            if(ObjectUtil.isNotNull(userVo)){
                processVo.setCreateUserName(userVo.getName());
            }
        }
        return processVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param processs
     * @return
     */
    @Override
    public List<ProcessVo> setVoProperties(Collection processs){
        List<ProcessVo> processVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(processs)) {
            for (Object process : processs) {
                ProcessVo processVo = new ProcessVo();
                BeanUtil.copyProperties(process, processVo);
                //处理部门名称
                if(StrUtil.isNotBlank(processVo.getProDeptId())){
                    Department department = adminFeign.getDepartmentById(processVo.getProDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        processVo.setProDeptName(department.getName());
                    }
                }
                //移交部门名称
                if(StrUtil.isNotBlank(processVo.getToDeptId())){
                    Department department = adminFeign.getDepartmentById(processVo.getToDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        processVo.setToDeptName(department.getName());
                    }
                }
                //处理人姓名
                if(StrUtil.isNotBlank(processVo.getCreateBy())){
                    UserVo userVo = adminFeign.getUserByUserId(processVo.getCreateBy()).getData();
                    if(ObjectUtil.isNotNull(userVo)){
                        processVo.setCreateUserName(userVo.getName());
                    }
                }
                processVos.add(processVo);
            }
        }
        return processVos;
    }

    @Override
    public void doProcess(Process process, Record record) {
        UserVo userVo = adminFeign.getUserByUserId(UserContextHelper.getUserId()).getData();
        process.setProDeptId(userVo.getDepartmentId());
        Record oldRecord = recordService.getById(process.getSqId());
        boolean result = false;
        //办理状态为 受理信件（1） 设置信件状态为 已受理（2）
        if(process.getProType() == 1){
            oldRecord.setFlag(2);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setReplyTime(process.getProTime());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 不予受理（2）设置信件状态为 不予受理（3）
        if(process.getProType() == 2){
            oldRecord.setFlag(3);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setReplyTime(new Date());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 置为无效（3）设置信件状态为 无效（6）
        if(process.getProType() == 3){
            oldRecord.setFlag(6);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setReplyTime(new Date());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 申请延期（4）设置信件状态为 办理中（4）
        if(process.getProType() == 4){
            oldRecord.setFlag(4);
        }
        //办理状态为 延期审核（5）设置信件状态为 办理中（4）
        if(process.getProType() == 5){
            oldRecord.setFlag(4);
        }
        //办理状态为 转办信件（6） 设置信件状态为 办理中（4）
        if(process.getProType() == 6){
            oldRecord.setFlag(4);
            oldRecord.setProDeptId(process.getToDeptId());
        }
        //办理状态为 回复信件（7） 设置信件状态为 已办结（5）
        if(process.getProType() == 7){
            oldRecord.setFlag(5);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setIsOpen(record.getIsOpen());
            oldRecord.setIsPublish(record.getIsPublish());
            oldRecord.setReplyTime(process.getProTime());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 退回信件（8） 设置信件状态为 退回件（8）
        if(process.getProType() == 8){
            oldRecord.setFlag(8);
            oldRecord.setProDeptId(oldRecord.getDeptId());
            process.setToDeptId(oldRecord.getDeptId());
        }
        //办理状态为 发布信件（9）
        if(process.getProType() == 9){
            oldRecord.setTitle(record.getTitle());
            oldRecord.setContent(record.getContent());
            oldRecord.setIsOpen(record.getIsOpen());
            oldRecord.setIsPublish(record.getIsPublish());
            oldRecord.setReplyTime(process.getProTime());
            oldRecord.setReplyContent(process.getProContent());
        }

//        Model model = modelService.getById(oldRecord.getModelId());
//        if(model.getWorkflowType() == 1 && StrUtil.isNotBlank(model.getWorkflowId()) && oldRecord.getIsPublish() == 1){
//            // 启动工作流 TODO
//            ProcessInstanceVo processInstanceVo = new ProcessInstanceVo();
//            processInstanceVo.setActDefinitionKey(model.getWorkflowId());
//            processInstanceVo.setBusinessId(String.valueOf(System.currentTimeMillis()));
//            processInstanceVo.setSource("APPEAL");
//            processInstanceVo.setUserId(UserContextHelper.getUserId());
//            Map<String, Object> mapParams = CollectionUtil.newHashMap();
//            mapParams.put("title", oldRecord.getTitle());
//            mapParams.put("modelName", model.getModelName());
//            mapParams.put("sqCode", oldRecord.getSqCode());
//            processInstanceVo.setVariables(mapParams);
//            workflowFeign.startInstance(processInstanceVo);
//            oldRecord.setIsPublish(2);
//        }

        result = recordService.saveOrUpdate(oldRecord);
        if(result && process.getProType() < 9){
            super.saveOrUpdate(process);
        }
    }
}
