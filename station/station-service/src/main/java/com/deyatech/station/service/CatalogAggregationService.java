package com.deyatech.station.service;

import com.deyatech.station.entity.CatalogAggregation;
import com.deyatech.station.vo.CatalogAggregationVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  聚合栏目 服务类
 * </p>
 *
 * @Author csm.
 * @since 2019-09-11
 */
public interface CatalogAggregationService extends BaseService<CatalogAggregation> {

    /**
     * 单个将对象转换为vo聚合栏目
     *
     * @param catalogAggregation
     * @return
     */
    CatalogAggregationVo setVoProperties(CatalogAggregation catalogAggregation);

    /**
     * 批量将对象转换为vo聚合栏目
     *
     * @param catalogAggregations
     * @return
     */
    List<CatalogAggregationVo> setVoProperties(Collection catalogAggregations);

    /**
     * 检索聚合对象
     *
     * @return
     */
    CatalogAggregationVo getCatalogAggregationById(String id);

    /**
     * 删除聚合对象
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
