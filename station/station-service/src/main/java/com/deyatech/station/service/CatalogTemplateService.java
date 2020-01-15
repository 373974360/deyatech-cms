package com.deyatech.station.service;

import com.deyatech.station.entity.CatalogTemplate;
import com.deyatech.station.vo.CatalogTemplateVo;
import com.deyatech.common.base.BaseService;
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
}
