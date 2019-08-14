package com.deyatech.monitor.service;

import com.deyatech.monitor.entity.GroupSite;
import com.deyatech.monitor.vo.GroupSiteVo;
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
public interface GroupSiteService extends BaseService<GroupSite> {

    /**
     * 单个将对象转换为vo
     *
     * @param groupSite
     * @return
     */
    GroupSiteVo setVoProperties(GroupSite groupSite);

    /**
     * 批量将对象转换为vo
     *
     * @param groupSites
     * @return
     */
    List<GroupSiteVo> setVoProperties(Collection groupSites);

    /**
     * 设置任务站点
     *
     * @param groupId
     * @param siteIds
     */
    void setGroupSites(String groupId, List<String> siteIds);



    void deleteGroupSiteByGroupId(List<String> groupIds);
    void deleteGroupSiteBySiteId(List<String> siteIds);


}
