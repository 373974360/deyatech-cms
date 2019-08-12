package com.deyatech.monitor.mapper;

import com.deyatech.monitor.entity.SiteManager;
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
public interface SiteManagerMapper extends BaseMapper<SiteManager> {

    void deleteSiteManagerByManagerId(@Param("managerIds") List<String> managerIds);

    void deleteSiteManagerBySiteId(@Param("siteIds") List<String> siteIds);

}
