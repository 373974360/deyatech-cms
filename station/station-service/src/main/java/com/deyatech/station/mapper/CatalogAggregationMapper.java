package com.deyatech.station.mapper;

import com.deyatech.station.entity.CatalogAggregation;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.vo.CatalogAggregationVo;

import java.util.List;

/**
 * <p>
 * 聚合栏目 Mapper 接口
 * </p>
 *
 * @Author csm.
 * @since 2019-09-11
 */
public interface CatalogAggregationMapper extends BaseMapper<CatalogAggregation> {

    /**
     * 获取聚合信息
     *
     * @param id
     * @return
     */
    CatalogAggregationVo getCatalogAggregationById(String id);

    /**
     * 删除聚合信息
     *
     * @param id
     */
    void deleteCatalogAggregationById(String id);

    /**
     * 获取站点聚合栏目
     *
     * @param siteId
     * @return
     */
    List<CatalogAggregationVo> getCatalogAggregationBySiteId(String siteId);

    /**
     * 获取聚合规则包含给定栏目的栏目
     *
     * @param catalogId
     * @return
     */
    List<CatalogAggregationVo> getCatalogAggregationByCatalogId(String catalogId);
}
