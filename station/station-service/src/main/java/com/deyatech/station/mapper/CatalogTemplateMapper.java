package com.deyatech.station.mapper;

import com.deyatech.station.entity.CatalogTemplate;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.vo.CatalogAggregationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 栏目内容关联 Mapper 接口
 * </p>
 *
 * @Author ycx
 * @since 2020-01-15
 */
public interface CatalogTemplateMapper extends BaseMapper<CatalogTemplate> {
    /**
     * 获取聚合的内容ID
     *
     * @param aggregation
     * @return
     */
    List<String> getAggregationTemplateId(@Param("aggregation") CatalogAggregationVo aggregation,
                                          @Param("offset") long offset,
                                          @Param("size") long size);

    /**
     * 添加聚合栏目内容
     *
     * @param list
     * @return
     */
    int insertCatalogTemplate(List<CatalogTemplate> list);

    /**
     * 根据栏目ID删除聚合关系
     *
     * @param catalogId
     * @return
     */
    int deleteAggregationByCatalogId(String catalogId);

    /**
     * 获取内容投递的栏目
     *
     * @param templateId
     * @return
     */
    List<String> getDeliverCatalog(String templateId);

    /**
     * 根据内容ID删除
     *
     * @param templateId
     * @return
     */
    int deleteByTemplateId(String templateId, int originType);

}
