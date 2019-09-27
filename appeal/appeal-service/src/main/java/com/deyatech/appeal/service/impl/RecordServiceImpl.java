package com.deyatech.appeal.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.appeal.entity.Model;
import com.deyatech.appeal.entity.Purpose;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.service.ModelService;
import com.deyatech.appeal.service.PurposeService;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.appeal.mapper.RecordMapper;
import com.deyatech.appeal.service.RecordService;
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

    private static final String DEFAULT_RANDON_STR = "A-Z0-9";

    /**
     * 单个将对象转换为vo
     *
     * @param record
     * @return
     */
    @Override
    public RecordVo setVoProperties(Record record){
        RecordVo recordVo = new RecordVo();
        BeanUtil.copyProperties(record, recordVo);
        //收件部门名称
        if(StrUtil.isNotBlank(record.getDeptId())){
            Department department = adminFeign.getDepartmentById(record.getDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                recordVo.setDeptName(department.getName());
            }
        }
        //处理部门名称
        if(StrUtil.isNotBlank(record.getProDeptId())){
            Department department = adminFeign.getDepartmentById(record.getProDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                recordVo.setProDeptName(department.getName());
            }
        }
        //回复部门名称
        if(StrUtil.isNotBlank(record.getReplyDeptId())){
            Department department = adminFeign.getDepartmentById(record.getReplyDeptId()).getData();
            if(ObjectUtil.isNotNull(department)){
                recordVo.setReplyDeptName(department.getName());
            }
        }
        //业务模型名称
        if(StrUtil.isNotBlank(record.getModelId())){
            Model model = modelService.getById(record.getModelId());
            if(ObjectUtil.isNotNull(model)){
                recordVo.setModelName(model.getModelName());
            }
        }
        //诉求目的名称
        if(StrUtil.isNotBlank(record.getPurId())){
            Purpose purpose = purposeService.getById(record.getPurId());
            if(ObjectUtil.isNotNull(purpose)){
                recordVo.setPurposeName(purpose.getPurposeName());
            }
        }
        return recordVo;
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
            for (Object record : records) {
                RecordVo recordVo = new RecordVo();
                BeanUtil.copyProperties(record, recordVo);
                //收件部门名称
                if(StrUtil.isNotBlank(recordVo.getDeptId())){
                    Department department = adminFeign.getDepartmentById(recordVo.getDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        recordVo.setDeptName(department.getName());
                    }
                }
                //处理部门名称
                if(StrUtil.isNotBlank(recordVo.getProDeptId())){
                    Department department = adminFeign.getDepartmentById(recordVo.getProDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        recordVo.setProDeptName(department.getName());
                    }
                }
                //回复部门名称
                if(StrUtil.isNotBlank(recordVo.getReplyDeptId())){
                    Department department = adminFeign.getDepartmentById(recordVo.getReplyDeptId()).getData();
                    if(ObjectUtil.isNotNull(department)){
                        recordVo.setReplyDeptName(department.getName());
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
                recordVos.add(recordVo);
            }
        }
        return recordVos;
    }

    @Override
    public IPage<RecordVo> pageRecordByBean(Record record,String[] timeFrame) {
        UserVo userVo = adminFeign.getUserByUserId(UserContextHelper.getUserId()).getData();
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotNull(timeFrame)){
            queryWrapper.between("create_time",timeFrame[0],timeFrame[1]);
        }
        if(StrUtil.isNotBlank(record.getModelId())){
            queryWrapper.eq("model_id",record.getModelId());
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
        if(record.getFlag() != null && record.getFlag() > 0){
            queryWrapper.eq("flag",record.getFlag());
        }
        if(record.getIsPublish() != null && record.getIsPublish() > 0){
            queryWrapper.eq("is_publish",record.getIsPublish());
        }
        if(ObjectUtil.isNotNull(userVo)){
            queryWrapper.eq("pro_dept_id",userVo.getDepartmentId());
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
}
