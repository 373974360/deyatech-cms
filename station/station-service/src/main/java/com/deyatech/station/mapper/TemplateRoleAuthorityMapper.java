package com.deyatech.station.mapper;

import com.deyatech.station.entity.TemplateRoleAuthority;
import com.deyatech.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色内容权限 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-11-01
 */
public interface TemplateRoleAuthorityMapper extends BaseMapper<TemplateRoleAuthority> {

    /**
     * 关联站点个数
     *
     * @param roleIds
     * @return
     */
    Map<String, String> getStationCount(@Param("roleIds") List<String> roleIds);

    /**
     * 关联栏目个数
     *
     * @param roleIds
     * @return
     */
    Map<String, String> getCatalogCount(@Param("roleIds") List<String> roleIds);

    /**
     * 关联内容个数
     *
     * @param roleIds
     * @return
     */
    Map<String, String> getContentCount(@Param("roleIds") List<String> roleIds);
}
