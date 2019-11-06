package com.deyatech.assembly.feign.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.feign.AssemblyFeign;
import com.deyatech.assembly.service.ApplyOpenModelService;
import com.deyatech.assembly.service.ApplyOpenRecordService;
import com.deyatech.assembly.vo.ApplyOpenModelVo;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
import com.deyatech.common.entity.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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

    @Override
    public RestResult<Page<ApplyOpenRecordVo>> getApplyOpenList(Map<String, Object> maps, Integer page, Integer pageSize) {
        return null;
    }

    @Override
    public RestResult<ApplyOpenRecordVo> queryApplyOpen(String ysqCode, String queryCode) {
        return null;
    }

    @Override
    public RestResult insertApplyOpen(ApplyOpenRecord record) {
        return null;
    }

    @Override
    public RestResult<ApplyOpenRecordVo> getApplyOpenById(String id) {
        return null;
    }

    @Override
    public RestResult<ApplyOpenModelVo> getApplyOpenModelById(String id) {
        return null;
    }
}
