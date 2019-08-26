package com.deyatech.station.feign.impl;

import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.config.SiteProperties;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.vo.CatalogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

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
    @Autowired
    CatalogService catalogService;

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

    @Override
    public RestResult<SiteProperties> getSiteProperties() {
        return RestResult.ok(siteCache.getSiteProperties());
    }

    @Override
    public RestResult<Collection<CatalogVo>> getCatalogTreeBySiteId(String siteId) {
        return RestResult.ok(siteCache.getCatalogTreeBySiteId(siteId));
    }
}
