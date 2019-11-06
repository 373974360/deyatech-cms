package com.deyatech.appeal.feign.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.appeal.entity.Model;
import com.deyatech.appeal.entity.Purpose;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.feign.AppealFeign;
import com.deyatech.appeal.service.ModelService;
import com.deyatech.appeal.service.PurposeService;
import com.deyatech.appeal.service.RecordService;
import com.deyatech.appeal.vo.ModelVo;
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
}
