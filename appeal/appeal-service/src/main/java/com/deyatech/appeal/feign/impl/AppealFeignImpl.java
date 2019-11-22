package com.deyatech.appeal.feign.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.appeal.entity.*;
import com.deyatech.appeal.feign.AppealFeign;
import com.deyatech.appeal.service.*;
import com.deyatech.appeal.vo.ModelVo;
import com.deyatech.appeal.vo.RecordSatisfactionVo;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.common.entity.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/10/30 20:19
 */
@RestController
public class AppealFeignImpl implements AppealFeign {

    @Autowired
    RecordService recordService;
    @Autowired
    ModelService modelService;
    @Autowired
    PurposeService purposeService;
    @Autowired
    SatisfactionService satisfactionService;
    @Autowired
    RecordSatisfactionService recordSatisfactionService;

    @Override
    public RestResult<Page<RecordVo>> getAppealList(Map<String, Object> maps, Integer page, Integer pageSize) {
        IPage<RecordVo> records = recordService.getAppealList(maps,page,pageSize);
        records.setRecords(recordService.setVoProperties(records.getRecords()));
        return RestResult.ok(records);
    }

    @Override
    public RestResult<RecordVo> queryAppeal(String sqCode, String queryCode) {
        return RestResult.ok(recordService.queryAppeal(sqCode,queryCode));
    }

    @Override
    public RestResult insertAppeal(Record record) {
        return RestResult.ok(recordService.insertAppeal(record));
    }

    @Override
    public RestResult<RecordVo> getAppealById(String id) {
        Record record = recordService.getById(id);
        return RestResult.ok(recordService.setVoProperties(record));
    }

    @Override
    public RestResult<ModelVo> getModelById(String id) {
        Model model = modelService.getById(id);
        return RestResult.ok(modelService.setVoProperties(model));
    }

    @Override
    public RestResult<List<Purpose>> getAllPurpose() {
        return RestResult.ok(purposeService.list());
    }

    @Override
    public RestResult<List<DepartmentVo>> getPartDept(String modelId) {
        return RestResult.ok(recordService.getCompetentDept(modelId));
    }

    @Override
    public RestResult<List<Satisfaction>> getAllSatisfaction() {
        return RestResult.ok(satisfactionService.list());
    }

    @Override
    public RestResult insertAppealSatis(RecordSatisfaction recordSatisfaction) {
        return RestResult.ok(recordSatisfactionService.save(recordSatisfaction));
    }

    @Override
    public RestResult<List<RecordSatisfactionVo>> getAppealSatisCountByAppealId(String appealId) {
        return RestResult.ok(recordSatisfactionService.getAppealSatisCountByAppealId(appealId));
    }
}
