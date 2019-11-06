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

    }
}
