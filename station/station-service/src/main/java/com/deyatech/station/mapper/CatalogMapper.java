package com.deyatech.station.mapper;

import com.deyatech.station.entity.Catalog;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.vo.CatalogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 根据站点ID检索栏目
     *
     * @param list
     * @return
     */
    List<CatalogVo> getCatalogBySiteIds (@Param("list") List<String> list);
}
