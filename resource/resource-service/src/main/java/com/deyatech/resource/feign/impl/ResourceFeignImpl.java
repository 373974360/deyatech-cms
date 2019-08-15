package com.deyatech.resource.feign.impl;

import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.feign.ResourceFeign;
import com.deyatech.resource.service.StationGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/6 15:19
 */
@RestController
public class ResourceFeignImpl implements ResourceFeign {

    @Autowired
    StationGroupService stationGroupService;

    @Override
    public RestResult<List<StationGroup>> getStationGroupAll() {
        return RestResult.ok(stationGroupService.list());
    }
}
