package com.deyatech.apply.feign.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.apply.entity.OpenModel;
import com.deyatech.apply.entity.OpenRecord;
import com.deyatech.apply.feign.ApplyFeign;
import com.deyatech.apply.service.OpenModelService;
import com.deyatech.apply.service.OpenRecordService;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.apply.vo.OpenModelVo;
import com.deyatech.apply.vo.OpenRecordVo;
import com.deyatech.common.entity.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/16 15:18
 */
@RestController
public class ApplyFeignImpl implements ApplyFeign {

    @Autowired
    OpenRecordService openRecordService;
    @Autowired
    OpenModelService openModelService;

    @Override
    public RestResult<Page<OpenRecordVo>> getApplyOpenList(Map<String, Object> maps, Integer page, Integer pageSize) {
        IPage<OpenRecordVo> records = openRecordService.getApplyOpenList(maps,page,pageSize);
        records.setRecords(openRecordService.setVoProperties(records.getRecords()));
        return RestResult.ok(records);
    }

    @Override
    public RestResult<OpenRecordVo> queryApplyOpen(String ysqCode, String queryCode) {
        return RestResult.ok(openRecordService.queryApplyOpen(ysqCode,queryCode));
    }

    @Override
    public RestResult insertApplyOpen(OpenRecord record) {
        return RestResult.ok(openRecordService.insertOpenRecord(record));
    }

    @Override
    public RestResult<OpenRecordVo> getApplyOpenById(String id) {
        OpenRecord record = openRecordService.getById(id);
        return RestResult.ok(openRecordService.setVoProperties(record));
    }

    @Override
    public RestResult<OpenModelVo> getApplyOpenModelById(String id) {
        OpenModel model = openModelService.getById(id);
        return RestResult.ok(openModelService.setVoProperties(model));
    }

    @Override
    public RestResult<List<DepartmentVo>> getPartDept(String modelId) {
        return RestResult.ok(openRecordService.getCompetentDept(modelId));
    }

}
