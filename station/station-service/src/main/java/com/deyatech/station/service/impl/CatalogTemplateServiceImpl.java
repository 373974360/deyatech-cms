package com.deyatech.station.service.impl;

import com.deyatech.station.entity.CatalogTemplate;
import com.deyatech.station.vo.CatalogAggregationVo;
import com.deyatech.station.vo.CatalogTemplateVo;
import com.deyatech.station.mapper.CatalogTemplateMapper;
import com.deyatech.station.service.CatalogTemplateService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 栏目内容关联 服务实现类
 * </p>
 *
 * @Author ycx
 * @since 2020-01-15
 */
@Service
public class CatalogTemplateServiceImpl extends BaseServiceImpl<CatalogTemplateMapper, CatalogTemplate> implements CatalogTemplateService {

    /**
     * 单个将对象转换为vo栏目内容关联
     *
     * @param catalogTemplate
     * @return
     */
    @Override
    public CatalogTemplateVo setVoProperties(CatalogTemplate catalogTemplate){
        CatalogTemplateVo catalogTemplateVo = new CatalogTemplateVo();
        BeanUtil.copyProperties(catalogTemplate, catalogTemplateVo);
        return catalogTemplateVo;
    }

    /**
     * 批量将对象转换为vo栏目内容关联
     *
     * @param catalogTemplates
     * @return
     */
    @Override
    public List<CatalogTemplateVo> setVoProperties(Collection catalogTemplates){
        List<CatalogTemplateVo> catalogTemplateVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(catalogTemplates)) {
            for (Object catalogTemplate : catalogTemplates) {
                CatalogTemplateVo catalogTemplateVo = new CatalogTemplateVo();
                BeanUtil.copyProperties(catalogTemplate, catalogTemplateVo);
                catalogTemplateVos.add(catalogTemplateVo);
            }
        }
        return catalogTemplateVos;
    }

    /**
     * 检索聚合内容ID
     *
     * @param aggregation
     * @param offset
     * @param size
     * @return
     */
    @Override
    public List<String> getAggregationTemplateId(CatalogAggregationVo aggregation, long offset, long size) {
        return baseMapper.getAggregationTemplateId(aggregation, offset, size);
    }

    /**
     * 添加聚合栏目内容
     *
     * @param list
     * @return
     */
    @Override
    public int insertCatalogTemplate(List<CatalogTemplate> list) {
        return baseMapper.insertCatalogTemplate(list);
    }
}
