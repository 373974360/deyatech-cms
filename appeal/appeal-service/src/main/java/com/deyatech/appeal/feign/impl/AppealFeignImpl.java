package com.deyatech.appeal.feign.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.appeal.feign.AppealFeign;
import com.deyatech.appeal.service.RecordService;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.common.entity.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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
}
