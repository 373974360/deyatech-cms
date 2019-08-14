package com.deyatech.monitor.service;

import com.deyatech.monitor.entity.SiteManager;
import com.deyatech.monitor.vo.SiteManagerVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-07-29
 */
public interface SiteManagerService extends BaseService<SiteManager> {

    /**
     * 单个将对象转换为vo
     *
     * @param siteManager
     * @return
     */
    SiteManagerVo setVoProperties(SiteManager siteManager);

    /**
     * 批量将对象转换为vo
     *
     * @param siteManagers
     * @return
     */
    List<SiteManagerVo> setVoProperties(Collection siteManagers);


    /**
     * 设置站点人员
     *
     * @param siteId
     * @param userIds
     */
    void setSiteUsers(String siteId, List<String> userIds);

    /**
     * 设置站点人员
     *
     * @param siteIds
     * @param userId
     */
    void setUserSites(String userId, List<String> siteIds);

    void deleteSiteManagerByManagerId(List<String> managerIds);
    void deleteSiteManagerBySiteId(List<String> siteIds);
}
