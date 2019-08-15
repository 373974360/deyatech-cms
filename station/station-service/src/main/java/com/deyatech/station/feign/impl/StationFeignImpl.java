package com.deyatech.station.feign.impl;

import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.feign.StationFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/15 14:19
 */

@RestController
public class StationFeignImpl implements StationFeign {

    @Autowired
    SiteCache siteCache;

    @Override
    public RestResult<String> getStationGroupTemplatePathBySiteId(String siteId) {
        return RestResult.ok(siteCache.getStationGroupTemplatePathBySiteId(siteId));
    }

    @Override
    public RestResult<String> getStationGroupRootPath(String siteId) {
        return RestResult.ok(siteCache.getStationGroupRootPath(siteId));
    }

    @Override
    public RestResult<StationGroup> getStationGroupById(String siteId) {
        return RestResult.ok(siteCache.getStationGroupById(siteId));
    }
}
