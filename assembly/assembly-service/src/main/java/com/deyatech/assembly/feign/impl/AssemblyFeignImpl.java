package com.deyatech.assembly.feign.impl;

import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.assembly.feign.AssemblyFeign;
import com.deyatech.assembly.service.IndexCodeService;
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
    IndexCodeService indexCodeService;


    @Override
    public RestResult<String> getNextIndexCodeBySiteId(String siteId) {
        return RestResult.ok(indexCodeService.getNextIndexCodeBySiteId(siteId));
    }

    @Override
    public RestResult<String> updateNextSerialBySiteId(String siteId, int value) {
        indexCodeService.updateNextSerialBySiteId(siteId, value);
        return RestResult.ok();
    }
}
