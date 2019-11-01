package com.deyatech.station.service;

import com.deyatech.station.entity.CatalogRole;
import com.deyatech.station.vo.CatalogRoleVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  角色栏目关联 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-01
 */
public interface CatalogRoleService extends BaseService<CatalogRole> {

    /**
     * 单个将对象转换为vo角色栏目关联
     *
     * @param catalogRole
     * @return
     */
    CatalogRoleVo setVoProperties(CatalogRole catalogRole);

    /**
     * 批量将对象转换为vo角色栏目关联
     *
     * @param catalogRoles
     * @return
     */
    List<CatalogRoleVo> setVoProperties(Collection catalogRoles);

    /**
     * 设置角色栏目
     *
     * @param roleId
     * @param catalogIds
     * @param siteId
     */
    void setRoleCatalogs(String roleId, List<String> catalogIds, String siteId);
}
