package com.deyatech.station.feign.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.config.SiteProperties;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.TemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    @Autowired
    TemplateService templateService;

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

    @Override
    public RestResult<Page<TemplateVo>> getTemplateListView(Map<String, Object> maps, Integer page, Integer pageSize) {
        IPage<TemplateVo> templates = templateService.getTemplateListView(maps,page,pageSize);
        templates.setRecords(templateService.setVoProperties(templates.getRecords()));
        return RestResult.ok(templates);
    }
    @Override
    public RestResult<Integer> resetTemplateIndex(String siteId, String start, String end, String part, int number) {
        return RestResult.ok(templateService.resetTemplateIndex(siteId, start, end, part, number));
    }
}
