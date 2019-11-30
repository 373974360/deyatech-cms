package com.deyatech.assembly.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.assembly.entity.ApplyOpenModel;
import com.deyatech.assembly.entity.ApplyOpenProcess;
import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.service.ApplyOpenModelService;
import com.deyatech.assembly.service.ApplyOpenRecordService;
import com.deyatech.assembly.vo.ApplyOpenProcessVo;
import com.deyatech.assembly.mapper.ApplyOpenProcessMapper;
import com.deyatech.assembly.service.ApplyOpenProcessService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.enums.YesNoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-30
 */
@Service
public class ApplyOpenProcessServiceImpl extends BaseServiceImpl<ApplyOpenProcessMapper, ApplyOpenProcess> implements ApplyOpenProcessService {

    @Autowired
    AdminFeign adminFeign;
    @Autowired
    ApplyOpenRecordService applyOpenRecordService;
    @Autowired
    ApplyOpenModelService applyOpenModelService;
    /**
     * 单个将对象转换为vo
     *
     * @param applyOpenProcess
     * @return
     */
    @Override
    public ApplyOpenProcessVo setVoProperties(ApplyOpenProcess applyOpenProcess){
        ApplyOpenProcessVo applyOpenProcessVo = new ApplyOpenProcessVo();
        BeanUtil.copyProperties(applyOpenProcess, applyOpenProcessVo);
        //处理部门名称
        if(StrUtil.isNotBlank(applyOpenProcess.getProDeptId())){
            Department department = adminFeign.getDepartmentById(applyOpenProcess.getProDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                applyOpenProcessVo.setProDeptName(department.getName());
            }
        }
        //移交部门名称
        if(StrUtil.isNotBlank(applyOpenProcess.getToDeptId())){
            Department department = adminFeign.getDepartmentById(applyOpenProcess.getToDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                applyOpenProcessVo.setToDeptName(department.getName());
            }
        }
        //处理人姓名
        if(StrUtil.isNotBlank(applyOpenProcess.getCreateBy())){
            UserVo userVo = adminFeign.getUserByUserId(applyOpenProcess.getCreateBy()).getData();
            if(ObjectUtil.isNotNull(userVo)){
                applyOpenProcessVo.setCreateUserName(userVo.getName());
            }
        }
        return applyOpenProcessVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param applyOpenProcesss
     * @return
     */
    @Override
    public List<ApplyOpenProcessVo> setVoProperties(Collection applyOpenProcesss){
        List<ApplyOpenProcessVo> applyOpenProcessVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(applyOpenProcesss)) {
            for (Object applyOpenProcess : applyOpenProcesss) {
                ApplyOpenProcessVo applyOpenProcessVo = new ApplyOpenProcessVo();
                BeanUtil.copyProperties(applyOpenProcess, applyOpenProcessVo);
                //处理部门名称
                if(StrUtil.isNotBlank(applyOpenProcessVo.getProDeptId())){
                    Department department = adminFeign.getDepartmentById(applyOpenProcessVo.getProDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        applyOpenProcessVo.setProDeptName(department.getName());
                    }
                }
                //移交部门名称
                if(StrUtil.isNotBlank(applyOpenProcessVo.getToDeptId())){
                    Department department = adminFeign.getDepartmentById(applyOpenProcessVo.getToDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        applyOpenProcessVo.setToDeptName(department.getName());
                    }
                }
                //处理人姓名
                if(StrUtil.isNotBlank(applyOpenProcessVo.getCreateBy())){
                    UserVo userVo = adminFeign.getUserByUserId(applyOpenProcessVo.getCreateBy()).getData();
                    if(ObjectUtil.isNotNull(userVo)){
                        applyOpenProcessVo.setCreateUserName(userVo.getName());
                    }
                }
                applyOpenProcessVos.add(applyOpenProcessVo);
            }
        }
        return applyOpenProcessVos;
    }

    @Override
    public void doProcess(ApplyOpenProcess process, ApplyOpenRecord record) {
        UserVo userVo = adminFeign.getUserByUserId(UserContextHelper.getUserId()).getData();
        process.setProDeptId(userVo.getDepartmentId());
        ApplyOpenRecord oldRecord = applyOpenRecordService.getById(process.getSqId());
        ApplyOpenModel model = applyOpenModelService.getById(oldRecord.getModelId());
        boolean result = false;
        //办理状态为 转办信件（1）
        if(process.getProType() == 1){
            //办理中
            oldRecord.setApplyStatus(1);
            //退回状态为 正常
            oldRecord.setIsBack(0);
            //设置处理部门
            oldRecord.setProDeptId(process.getToDeptId());
        }
        //办理状态为 回复信件（2）
        if(process.getProType() == 2){
            //已办结
            oldRecord.setApplyStatus(3);
            //退回状态为 正常
            oldRecord.setIsBack(0);
            //设置回复部门
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
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
            oldRecord.setContent(record.getContent());
            oldRecord.setIsPublish(record.getIsPublish());
            oldRecord.setReplyTime(process.getProTime());
            oldRecord.setReplyContent(process.getProContent());
            if(StrUtil.isBlank(process.getProContent())){
                //已办结
                oldRecord.setApplyStatus(3);
                //设置回复部门
                oldRecord.setReplyDeptId(userVo.getDepartmentId());
            }
        }
        //办理状态为 退回信件（4)
        if(process.getProType() == 4){
            //办理中
            oldRecord.setApplyStatus(1);
            //退回状态为 退回
            oldRecord.setIsBack(1);
            //设置处理部门为收件部门
            oldRecord.setProDeptId(oldRecord.getDeptId());
            process.setToDeptId(oldRecord.getDeptId());
        }
        //办理状态为 信件判重（5）
        if(process.getProType() == 5){
            //信件状态 已办结
            oldRecord.setApplyStatus(3);
            //信件标识 为无效件
            oldRecord.setApplyFlag(1);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setReplyTime(new Date());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 置为无效（6）
        if(process.getProType() == 6){
            //信件状态 已办结
            oldRecord.setApplyStatus(3);
            //信件标识 为无效件
            oldRecord.setApplyFlag(-1);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setReplyTime(new Date());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 申请延期（7）
        if(process.getProType() == 7){
            //办理中
            oldRecord.setApplyStatus(1);
            //延期标识为 申请延期
            oldRecord.setLimitFlag(2);
        }
        //办理状态为 不予受理（8）
        if(process.getProType() == 8){
            //信件状态 已办结
            oldRecord.setApplyStatus(3);
            //信件标识 不予受理
            oldRecord.setApplyFlag(2);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setReplyTime(new Date());
            oldRecord.setReplyContent(process.getProContent());
        }
        applyOpenRecordService.saveOrUpdate(oldRecord);
        super.saveOrUpdate(process);
    }
}
