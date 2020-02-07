package com.deyatech.station.mapper;

import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.entity.CatalogUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 栏目用户关联信息 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-10-10
 */
public interface CatalogUserMapper extends BaseMapper<CatalogUser> {
    /**
     * 删除用户栏目根据栏目编号
     *
     * @param catalogIds
     * @return
     */
    int removeUserCatalogByCatalogIds(@Param("catalogIds") List<String> catalogIds);

}
