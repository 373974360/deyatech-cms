package com.deyatech.station.cache;

import com.deyatech.admin.entity.Department;
import com.deyatech.admin.entity.User;
import com.deyatech.common.Constants;
import com.deyatech.station.config.SiteProperties;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.feign.ResourceFeign;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.mapper.TemplateMapper;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.station.vo.CatalogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
    @Autowired
    CatalogService catalogService;
    @Autowired
    TemplateService templateService;


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
    public SiteProperties getSiteProperties() {
        SiteProperties siteProperties = this.cacheManager.getCache(CacheNames.SITE_PROPERTIES_CACHE_KEY).get("siteProperties", SiteProperties.class);
        if (siteProperties == null) {
            log.error(String.format("缓存中没有获取到站点%s根路径的信息", "siteProperties"));
        }
        return siteProperties;
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
     * 获取栏目信息
     *
     * @param siteId
     * @return
     */
    public Collection<CatalogVo> getCatalogTreeBySiteId(String siteId) {
        Collection<CatalogVo> catalogVoCollection = this.cacheManager.getCache(CacheNames.CATALOG_CACHE_KEY).get(siteId, Collection.class);
        if (catalogVoCollection == null) {
            log.error(String.format("缓存中没有获取到站点%s的信息", siteId));
        }
        return catalogVoCollection;
    }
    /**
     * 用户信息
     *
     * @return
     */
    public List<User> getAllUser() {
        return this.cacheManager.getCache(CacheNames.USER_CACHE_KEY).get(CacheNames.USER_CACHE_KEY, List.class);
    }

    /**
     * 部门信息
     *
     * @return
     */
    public List<Department> getAllDepartment() {
        return this.cacheManager.getCache(CacheNames.DEPARTMENT_CACHE_KEY).get(CacheNames.DEPARTMENT_CACHE_KEY, List.class);
    }

    /**
     * 本地缓存站点信息
     */
    public void cacheSite() {
        if (inited) {
            log.warn("reinitialization site info caches");
        }
        //站点信息
        try {
            //clearCache();
            this.cacheManager.getCache(CacheNames.USER_CACHE_KEY).put(CacheNames.USER_CACHE_KEY, templateService.getAllUser());
            this.cacheManager.getCache(CacheNames.DEPARTMENT_CACHE_KEY).put(CacheNames.DEPARTMENT_CACHE_KEY, templateService.getAllDepartment());
            List<StationGroup> allStationGroup = resourceFeign.getStationGroupAll().getData();
            for (StationGroup stationGroup : allStationGroup) {
                log.debug("缓存站点信息{}", stationGroup.getId());
                this.cacheManager.getCache(CacheNames.STATION_GROUP_CACHE_KEY).put(stationGroup.getId(), stationGroup);
                log.debug("缓存站点模板路径信息{}", stationGroup.getId());
                this.cacheManager.getCache(CacheNames.STATION_GROUP_TEMPLATE_ROOT_CACHE_KEY).put(stationGroup.getId(), this.siteProperties.getHostsRoot() + stationGroup.getEnglishName() + Constants.TEMPLATE_DIR_NAME);
                log.debug("缓存站点根路径信息{}", stationGroup.getId());
                this.cacheManager.getCache(CacheNames.STATION_GROUP_ROOT_CACHE_KEY).put(stationGroup.getId(), this.siteProperties.getHostsRoot() + stationGroup.getEnglishName());
                log.debug("缓存栏目信息{}", stationGroup.getId());
                Catalog catalog = new Catalog();
                catalog.setSiteId(stationGroup.getId());
                this.cacheManager.getCache(CacheNames.CATALOG_CACHE_KEY).put(stationGroup.getId(),this.catalogService.getCatalogTree(catalog));
            }
            log.debug("缓存nginx配置文件根目录{}");
            SiteProperties cacheSiteProperties = new SiteProperties();
            cacheSiteProperties.setElasticSearchHostname(siteProperties.getElasticSearchHostname());
            cacheSiteProperties.setElasticSearchPort(siteProperties.getElasticSearchPort());
            cacheSiteProperties.setHostsRoot(siteProperties.getHostsRoot());
            cacheSiteProperties.setNginxConfigDir(siteProperties.getNginxConfigDir());
            cacheSiteProperties.setNginxProxyPass(siteProperties.getNginxProxyPass());
            this.cacheManager.getCache(CacheNames.SITE_PROPERTIES_CACHE_KEY).put("siteProperties", cacheSiteProperties);
        } catch (Exception e) {
            log.error("缓存站点信息失败", e);
            throw new RuntimeException("缓存站点信息失败", e);
        }
    }
    public void reloadCache(){
        clearCache();
        cacheSite();
    }
    public void clearCache(){
        this.cacheManager.getCache("template_*").clear();
        this.cacheManager.getCache(CacheNames.USER_CACHE_KEY).clear();
        this.cacheManager.getCache(CacheNames.DEPARTMENT_CACHE_KEY).clear();
        this.cacheManager.getCache(CacheNames.STATION_GROUP_CACHE_KEY).clear();
        this.cacheManager.getCache(CacheNames.STATION_GROUP_TEMPLATE_ROOT_CACHE_KEY).clear();
        this.cacheManager.getCache(CacheNames.STATION_GROUP_ROOT_CACHE_KEY).clear();
        this.cacheManager.getCache(CacheNames.CATALOG_CACHE_KEY).clear();
        this.cacheManager.getCache(CacheNames.SITE_PROPERTIES_CACHE_KEY).clear();
    }
    public void clearCache(String namePath){
        this.cacheManager.getCache("template_"+namePath).clear();
    }
    public void cacheSite(String siteId) {
        if (inited) {
            log.warn("reinitialization site info caches");
        }
        //站点信息
        try {
            log.debug("缓存栏目信息{}",siteId);
            Catalog catalog = new Catalog();
            catalog.setSiteId(siteId);
            this.cacheManager.getCache(CacheNames.CATALOG_CACHE_KEY).put(siteId,this.catalogService.getCatalogTree(catalog));
        } catch (Exception e) {
            log.error("缓存站点信息失败", e);
            throw new RuntimeException("缓存站点信息失败", e);
        }
    }

    public void cacheTemplate(String Cachekey,String key,String context) {
        this.cacheManager.getCache("template_"+Cachekey).put(key, context);
    }
}
