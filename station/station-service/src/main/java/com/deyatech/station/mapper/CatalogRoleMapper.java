package com.deyatech.station.mapper;

import com.deyatech.station.entity.CatalogRole;
import com.deyatech.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色栏目关联 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-11-01
 */
public interface CatalogRoleMapper extends BaseMapper<CatalogRole> {
    /**
     * 删除角色栏目根据栏目编号
     *
     * @param catalogIds
     * @return
     */
    int removeRoleCatalogByCatalogIds(@Param("catalogIds") List<String> catalogIds);

}
