package com.deyatech.station.feign.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.config.SiteProperties;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.CatalogUserService;
import com.deyatech.station.service.MaterialService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.station.vo.CatalogUserVo;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.TemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    MaterialService materialService;
    @Autowired
    CatalogUserService catalogUserService;

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
        return RestResult.ok(templates);
    }
    @Override
    public RestResult<String> resetTemplateIndexCode(String siteId, String start, String end, String part, int number) {
        templateService.resetTemplateIndexCode(siteId, start, end, part, number);
        return RestResult.ok();
    }

    @Override
    public RestResult markMaterialUsePlace(String oldUrls, String newUrls, String usePlace) {
        List<String> oldUrlList = null;
        if (StrUtil.isNotEmpty(oldUrls)) {
            oldUrlList = Arrays.asList(oldUrls.split(","));
        }
        List<String> newUrlList = null;
        if (StrUtil.isNotEmpty(newUrls)) {
            newUrlList = Arrays.asList(newUrls.split(","));
        }
        materialService.markMaterialUsePlace(oldUrlList, newUrlList, usePlace);
        return RestResult.ok();
    }

    @Override
    public RestResult reloadCache() {
        siteCache.cacheSite();
        return RestResult.ok();
    }

    @Override
    public RestResult watermarkHandle(String siteId, String url) {
        materialService.watermarkHandle(siteId, url);
        return RestResult.ok();
    }

    @Override
    public RestResult<Map<String, Catalog>> getCatalogInfoMap(String siteId) {
        QueryWrapper<Catalog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site_id", siteId);
        return RestResult.ok(catalogService.list(queryWrapper).stream().collect(Collectors.toMap(Catalog::getId, c->c)));
    }

    @Override
    public RestResult<List<CatalogUserVo>> getCatalogUserListByCatalogId(String catalogId) {
        return RestResult.ok(catalogUserService.getCatalogUserListByCatalogId(catalogId));
    }
}
