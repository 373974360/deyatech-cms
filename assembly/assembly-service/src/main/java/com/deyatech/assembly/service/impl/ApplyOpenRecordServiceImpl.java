package com.deyatech.assembly.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.assembly.entity.ApplyOpenModel;
import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.service.ApplyOpenModelService;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
import com.deyatech.assembly.mapper.ApplyOpenRecordMapper;
import com.deyatech.assembly.service.ApplyOpenRecordService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.utils.RandomStrg;
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
        if(StrUtil.isNotBlank(applyOpenRecordVo.getDoDept())){
            Department department = adminFeign.getDepartmentById(applyOpenRecordVo.getDoDept()).getData();
            if(ObjectUtil.isNotNull(department)){
                applyOpenRecordVo.setDoDeptName(department.getName());
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
                if(StrUtil.isNotBlank(applyOpenRecordVo.getDoDept())){
                    Department department = adminFeign.getDepartmentById(applyOpenRecordVo.getDoDept()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        applyOpenRecordVo.setDoDeptName(department.getName());
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
    public IPage<ApplyOpenRecordVo> pageApplyOpenRecordByBean(ApplyOpenRecord applyOpenRecord, String[] timeFrame) {
        QueryWrapper<ApplyOpenRecord> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotNull(timeFrame)){
            queryWrapper.between("create_time",timeFrame[0],timeFrame[1]);
        }
        if(StrUtil.isNotBlank(applyOpenRecord.getModelId())){
            queryWrapper.eq("model_id",applyOpenRecord.getModelId());
        }
        if(StrUtil.isNotBlank(applyOpenRecord.getYsqCode())){
            queryWrapper.eq("ysq_code",applyOpenRecord.getYsqCode());
        }
        if(applyOpenRecord.getFlag() != null && applyOpenRecord.getFlag() > 0){
            queryWrapper.eq("flag",applyOpenRecord.getFlag());
        }
        if(applyOpenRecord.getIsPublish() != null && applyOpenRecord.getIsPublish() > 0){
            queryWrapper.eq("is_publish",applyOpenRecord.getIsPublish());
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
        ApplyOpenModel Model = applyOpenModelService.getById(ModelId);
        return RandomStrg.getRandomStr(DEFAULT_RANDON_STR, Model.getQueryNum() + "");
    }

    @Override
    public String getYsqCode(String ModelId) {
        ApplyOpenModel Model = applyOpenModelService.getById(ModelId);
        //编码头＋日期＋随机码
        return Model.getCodePre() + DateUtil.format(new Date(),Model.getCodeRule()) + RandomStrg.getRandomStr(DEFAULT_RANDON_STR, Model.getCodeNum() + "");
    }
}
