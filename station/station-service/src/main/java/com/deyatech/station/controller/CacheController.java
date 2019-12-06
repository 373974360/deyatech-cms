package com.deyatech.station.controller;

import com.deyatech.common.entity.RestResult;
import com.deyatech.station.cache.SiteCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/12/6 14:34
 */
@Slf4j
@RestController
@RequestMapping("/station/cache")
@Api(tags = {"聚合栏目接口"})
public class CacheController {
    @Autowired
    SiteCache siteCache;

    /**
     * 刷新站点和栏目缓存
     *
     * @return CommonResult.ok()
     */
    @PostMapping("/reload")
    @ApiOperation(value = "刷新站点和栏目缓存", notes = "刷新站点和栏目缓存")
    public RestResult<Boolean> reloadCache() {
        siteCache.cacheSite();
        return RestResult.ok(true);
    }
}
