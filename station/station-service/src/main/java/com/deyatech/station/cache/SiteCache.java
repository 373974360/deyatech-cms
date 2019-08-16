package com.deyatech.station.cache;

import com.deyatech.common.Constants;
import com.deyatech.resource.config.SiteProperties;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.feign.ResourceFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述：站点缓存
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/15 14:06
 */
@Component
@Slf4j
public class SiteCache {


    public boolean inited = false;
    @Autowired
    ResourceFeign resourceFeign;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    SiteProperties siteProperties;


    /**
     * 获取站点的模板根路径
     *
     * @param siteId
     * @return
     */
    public String getStationGroupTemplatePathBySiteId(String siteId) {
        String s = this.cacheManager.getCache(CacheNames.STATION_GROUP_TEMPLATE_ROOT_CACHE_KEY).get(siteId, String.class);
        if (s == null) {
            log.error(String.format("缓存中没有获取到站点%s的模板路径", siteId));

        }
        return s;
    }

    /**
     * 获取站点根路径
     *
     * @param siteId
     * @return
     */
    public String getStationGroupRootPath(String siteId) {
        String s = this.cacheManager.getCache(CacheNames.STATION_GROUP_ROOT_CACHE_KEY).get(siteId, String.class);
        if (s == null) {
            log.error(String.format("缓存中没有获取到站点%s根路径的信息", siteId));
        }
        return s;
    }

    /**
     * 获取nginx配置文件目录
     *
     * @return
     */
    public String getNginxConfigDir() {
        String s = this.cacheManager.getCache(CacheNames.NGINX_CONFIG_DIR_CACHE_KEY).get("nginxConfigDir", String.class);
        if (s == null) {
            log.error(String.format("缓存中没有获取到站点%s根路径的信息", "nginxConfigDir"));
        }
        return s;
    }



    /**
     * 获取站点信息
     *
     * @param siteId
     * @return
     */
    public StationGroup getStationGroupById(String siteId) {
        StationGroup stationGroup = this.cacheManager.getCache(CacheNames.STATION_GROUP_CACHE_KEY).get(siteId, StationGroup.class);
        if (stationGroup == null) {
            log.error(String.format("缓存中没有获取到站点%s的信息", siteId));
        }
        return stationGroup;
    }

    /**
     * 本地缓存站点信息
     */
    @Scheduled(initialDelay = 5000L, fixedRate = 10 * 60 * 1000)
    private void cacheSite() {
        if (inited) {
            log.warn("reinitialization site info caches");
        }
        //站点信息
        try {
            List<StationGroup> allStationGroup = resourceFeign.getStationGroupAll().getData();
            for (StationGroup stationGroup : allStationGroup) {
                log.debug("缓存站点信息{}", stationGroup.getId());
                this.cacheManager.getCache(CacheNames.STATION_GROUP_CACHE_KEY).put(stationGroup.getId(), stationGroup);
                log.debug("缓存站点模板路径信息{}", stationGroup.getId());
                this.cacheManager.getCache(CacheNames.STATION_GROUP_TEMPLATE_ROOT_CACHE_KEY).put(stationGroup.getId(), this.siteProperties.getHostsRoot() + stationGroup.getEnglishName() + Constants.TEMPLATE_DIR_NAME);
                log.debug("缓存站点根路径信息{}", stationGroup.getId());
                this.cacheManager.getCache(CacheNames.STATION_GROUP_ROOT_CACHE_KEY).put(stationGroup.getId(), this.siteProperties.getHostsRoot() + stationGroup.getEnglishName());
                log.debug("缓存nginx配置文件根目录{}");
                this.cacheManager.getCache(CacheNames.NGINX_CONFIG_DIR_CACHE_KEY).put("nginxConfigDir", this.siteProperties.getNginxConfigDir());
            }
        } catch (Exception e) {
            log.error("缓存站点信息失败", e);
            throw new RuntimeException("缓存站点信息失败", e);
        }
    }
}
