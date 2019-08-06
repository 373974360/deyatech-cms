package com.deyatech.station.mapper;

import com.deyatech.station.entity.Catalog;
import com.deyatech.common.base.BaseMapper;

/**
 * <p>
 * 栏目 Mapper 接口
 * </p>
 *
 * @Author csm.
 * @since 2019-08-02
 */
public interface CatalogMapper extends BaseMapper<Catalog> {

    /**
     * 查询最大排序号
     * @return
     */
    int selectMaxSortNo();

}
