package com.deyatech.station.service;

import com.deyatech.station.entity.TemplateRoleAuthority;
import com.deyatech.station.vo.TemplateRoleAuthorityVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  角色内容权限 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-01
 */
public interface TemplateRoleAuthorityService extends BaseService<TemplateRoleAuthority> {

    /**
     * 单个将对象转换为vo角色内容权限
     *
     * @param templateRoleAuthority
     * @return
     */
    TemplateRoleAuthorityVo setVoProperties(TemplateRoleAuthority templateRoleAuthority);

    /**
     * 批量将对象转换为vo角色内容权限
     *
     * @param templateRoleAuthoritys
     * @return
     */
    List<TemplateRoleAuthorityVo> setVoProperties(Collection templateRoleAuthoritys);

    /**
     * 关联站点个数
     *
     * @param roleIds
     * @return
     */
    Map<String, String> getStationCount(List<String> roleIds);

    /**
     * 关联栏目个数
     *
     * @param roleIds
     * @return
     */
    Map<String, String> getCatalogCount(List<String> roleIds);

    /**
     * 关联内容个数
     *
     * @param roleIds
     * @return
     */
    Map<String, String> getContentCount(List<String> roleIds);
}
