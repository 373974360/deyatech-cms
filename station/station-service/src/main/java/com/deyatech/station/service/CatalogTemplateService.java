package com.deyatech.station.service;

import com.deyatech.common.base.BaseService;
import com.deyatech.station.entity.CatalogTemplate;
import com.deyatech.station.vo.CatalogAggregationVo;
import com.deyatech.station.vo.CatalogTemplateVo;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  栏目内容关联 服务类
 * </p>
 *
 * @Author ycx
 * @since 2020-01-15
 */
public interface CatalogTemplateService extends BaseService<CatalogTemplate> {

    /**
     * 单个将对象转换为vo栏目内容关联
     *
     * @param catalogTemplate
     * @return
     */
    CatalogTemplateVo setVoProperties(CatalogTemplate catalogTemplate);

    /**
     * 批量将对象转换为vo栏目内容关联
     *
     * @param catalogTemplates
     * @return
     */
    List<CatalogTemplateVo> setVoProperties(Collection catalogTemplates);

    /**
     * 检索聚合内容ID
     *
     * @param aggregation
     * @param offset
     * @param size
     * @return
     */
    List<String> getAggregationTemplateId(CatalogAggregationVo aggregation, long offset, long size);

    /**
     * 添加聚合栏目内容
     *
     * @param list
     * @return
     */
    int insertCatalogTemplate(List<CatalogTemplate> list);

    /**
     * 根据内容ID删除聚合关系
     *
     * @param templateId
     * @return
     */
    int deleteAggregationByTemplateId(String templateId);

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
     * 设置投递栏目
     *
     * @param templateId
     * @param catalogIds
     */
    void setDeliverCatalogs(String templateId, List<String> catalogIds);
}
