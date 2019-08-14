package com.deyatech.monitor.mapper;

import com.deyatech.monitor.entity.GroupSite;
import com.deyatech.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-07-29
 */
public interface GroupSiteMapper extends BaseMapper<GroupSite> {

    void deleteGroupSiteByGroupId(@Param("groupIds") List<String> groupIds);

    void deleteGroupSiteBySiteId(@Param("siteIds") List<String> siteIds);

}
