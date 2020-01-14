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
import com.deyatech.appeal.vo.ModelVo;
import com.deyatech.appeal.vo.ProcessVo;
import com.deyatech.appeal.mapper.ProcessMapper;
import com.deyatech.appeal.service.ProcessService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.enums.YesNoEnum;
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
    public boolean doProcess(Process process, Record record) {
        UserVo userVo = adminFeign.getUserByUserId(UserContextHelper.getUserId()).getData();
        process.setProDeptId(userVo.getDepartmentId());
        Record oldRecord = recordService.getById(process.getSqId());
        Model model = modelService.getById(oldRecord.getModelId());
        //办理状态为 转办信件（1）
        if(process.getProType() == 1){
            //办理中
            oldRecord.setSqStatus(1);
            //退回状态为 正常
            oldRecord.setIsBack(0);
            //设置处理部门
            oldRecord.setProDeptId(process.getToDeptId());
        }
        //办理状态为 回复信件（2）
        if(process.getProType() == 2){
            //已办结
            oldRecord.setSqStatus(3);
            //退回状态为 正常
            oldRecord.setIsBack(0);
            //设置回复部门
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setIsOpen(record.getIsOpen());
            //如果业务模型设置的自动发布
            if(model.getAutoPublish().equals(YesNoEnum.YES.getCode())){
                oldRecord.setIsPublish(YesNoEnum.YES.getCode());
            }
            oldRecord.setReplyTime(process.getProTime());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 发布信件（3）
        if(process.getProType() == 3){
            //退回状态为 正常
            oldRecord.setIsBack(0);
            oldRecord.setTitle(record.getTitle());
            oldRecord.setContent(record.getContent());
            oldRecord.setIsOpen(record.getIsOpen());
            oldRecord.setIsPublish(record.getIsPublish());
            oldRecord.setReplyTime(process.getProTime());
            oldRecord.setReplyContent(process.getProContent());
            if(StrUtil.isBlank(process.getProContent())){
                //已办结
                oldRecord.setSqStatus(3);
                //设置回复部门
                oldRecord.setReplyDeptId(userVo.getDepartmentId());
            }
        }
        //办理状态为 退回信件（4)
        if(process.getProType() == 4){
            //办理中
            oldRecord.setSqStatus(1);
            //退回状态为 退回
            oldRecord.setIsBack(1);
            //设置处理部门为收件部门
            oldRecord.setProDeptId(oldRecord.getDeptId());
            process.setToDeptId(oldRecord.getDeptId());
        }
        //办理状态为 置为无效（6）
        if(process.getProType() == 6){
            //信件状态 已办结
            oldRecord.setSqStatus(3);
            //信件标识 为无效件
            oldRecord.setSqFlag(-1);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setReplyTime(new Date());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 申请延期（7）
        if(process.getProType() == 7){
            //办理中
            oldRecord.setSqStatus(1);
            //延期标识为 申请延期
            oldRecord.setLimitFlag(2);
        }
        //办理状态为 不予受理（8）
        if(process.getProType() == 8){
            //信件状态 已办结
            oldRecord.setSqStatus(3);
            //信件标识 不予受理
            oldRecord.setSqFlag(2);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setReplyTime(new Date());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 不予受理（督办）
        if(process.getProType() == 9){
            //督办状态 为已督办
            oldRecord.setSuperviseFlag(1);
        }
        if(recordService.saveOrUpdate(oldRecord)){
            return super.saveOrUpdate(process);
        }
        return false;
    }

    @Override
    public boolean setRepeatProcess(List<String> ids) {
        Process process = new Process();
        if(CollectionUtil.isNotEmpty(ids)){
            for(String id:ids){
                Record oldRecord = recordService.getById(id);

                UserVo userVo = adminFeign.getUserByUserId(UserContextHelper.getUserId()).getData();
                process.setProDeptId(userVo.getDepartmentId());
                process.setSqId(id);
                process.setProType(5);
                //信件状态 已办结
                oldRecord.setSqStatus(3);
                //信件标识 为重复件
                oldRecord.setSqFlag(1);

                if(recordService.saveOrUpdate(oldRecord)){
                    super.saveOrUpdate(process);
                }
            }
        }
        return true;
    }
}
