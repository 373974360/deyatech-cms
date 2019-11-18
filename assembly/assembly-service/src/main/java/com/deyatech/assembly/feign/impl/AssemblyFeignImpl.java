package com.deyatech.assembly.feign.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.assembly.entity.ApplyOpenModel;
import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.feign.AssemblyFeign;
import com.deyatech.assembly.service.ApplyOpenModelService;
import com.deyatech.assembly.service.ApplyOpenRecordService;
import com.deyatech.assembly.service.IndexCodeService;
import com.deyatech.assembly.vo.ApplyOpenModelVo;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
import com.deyatech.common.entity.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/11/5 14:26
 */
@RestController
public class AssemblyFeignImpl implements AssemblyFeign {

    @Autowired
    ApplyOpenRecordService applyOpenRecordService;
    @Autowired
    ApplyOpenModelService applyOpenModelService;
    @Autowired
    IndexCodeService indexCodeService;

    @Override
    public RestResult<Page<ApplyOpenRecordVo>> getApplyOpenList(Map<String, Object> maps, Integer page, Integer pageSize) {
        IPage<ApplyOpenRecordVo> records = applyOpenRecordService.getApplyOpenList(maps,page,pageSize);
        records.setRecords(applyOpenRecordService.setVoProperties(records.getRecords()));
        return RestResult.ok(records);
    }

    @Override
    public RestResult<ApplyOpenRecordVo> queryApplyOpen(String ysqCode, String queryCode) {
        return RestResult.ok(applyOpenRecordService.queryApplyOpen(ysqCode,queryCode));
    }

    @Override
    public RestResult insertApplyOpen(ApplyOpenRecord record) {
        return RestResult.ok(applyOpenRecordService.insertApplyOpenRecord(record));
    }

    @Override
    public RestResult<ApplyOpenRecordVo> getApplyOpenById(String id) {
        ApplyOpenRecord record = applyOpenRecordService.getById(id);
        return RestResult.ok(applyOpenRecordService.setVoProperties(record));
    }

    @Override
    public RestResult<ApplyOpenModelVo> getApplyOpenModelById(String id) {
        ApplyOpenModel model = applyOpenModelService.getById(id);
        return RestResult.ok(applyOpenModelService.setVoProperties(model));
    }

    @Override
    public RestResult<List<DepartmentVo>> getPartDept(String modelId) {
        return RestResult.ok(applyOpenRecordService.getCompetentDept(modelId));
    }

    @Override
    public RestResult<String> getNextIndexCodeBySiteId(String siteId) {
        return RestResult.ok(indexCodeService.getNextIndexCodeBySiteId(siteId));
    }
}
