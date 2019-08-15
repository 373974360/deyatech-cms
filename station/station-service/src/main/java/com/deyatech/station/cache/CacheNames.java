package com.deyatech.station.cache;

/**
 * 缓存名称
 */
public class CacheNames {

    /**
     * 缓存内容模型缓存 KEY= id，value = ContentModel 对象
     */
    public static final String CONTENT_MODEL = "contentModelKey";
    /**
     * 元数据集的所有缓存
     */
    public static final String META_DATA_COLLECTIONS = "metaDataCollections";
    /**
     * CMS的元数据集的缓存
     */
    public static final String META_DATA_COLLECTION_FOR_CMS = "cms";
    /**
     * 站点信息缓存  KEY = siteId，VALUE = stationGroupDto
     */
    public static final String STATION_GROUP_CACHE_KEY = "stationGroupCacheKey";
    /**
     * 站点主域名缓存 KEY = siteId，VALUE = 字符串主域名
     */
    public static final String STATION_GROUP_DOMAIN_CACHE_KEY = "stationGroupDomainKey";
    /**
     * 域名找站点缓存 KEY = 域名，VALUE = stationGroupDto
     */
    public static final String DOMAIN_CACHE_STATION_GROUP_CACHE_KEY = "stationGroupDomainKey";
    /**
     * 站点的模板存放路径 KEY = siteId，VALUE = 目录路径字符串
     */
    public static final String STATION_GROUP_TEMPLATE_ROOT_CACHE_KEY = "stationGroupTemplateRootKey";
    /**
     * 站点根路径 KEY = siteId，VALUE = 目录字符串
     */
    public static final String STATION_GROUP_ROOT_CACHE_KEY = "stationGroupRootKey";
    /**
     * 栏目 KEY = catalogId，VALUE = CmsCatalog
     */
    public static final String CATALOG_CACHE_KEY = "catalogKey";
    /**
     * 内容模型和栏目对应的使用的模板
     */
    public static final String CONTENT_MODEL_TEMPLATE_CACHE_KEY = "contentModelTemplate";
    /**
     * 字典 KEY = 字典id，value = 字典dto
     */
    static final String DICT_CACHE_KEY = "dictKey";
    /**
     * 字典项 KEY = 字典项id，value 是字典项dto
     */
    static final String DICT_ITEM_CACHE_KEY = "dictItemKey";
}
