package com.deyatech.station.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.deyatech.common.enums.ContentOriginTypeEnum;
import com.deyatech.station.entity.CatalogTemplate;
import com.deyatech.station.vo.CatalogAggregationVo;
import com.deyatech.station.vo.CatalogTemplateVo;
import com.deyatech.station.mapper.CatalogTemplateMapper;
import com.deyatech.station.service.CatalogTemplateService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    /**
     * 根据内容ID删除聚合关系
     *
     * @param templateId
     * @return
     */
    @Override
    public int deleteAggregationByTemplateId(String templateId) {
        return baseMapper.deleteByTemplateId(templateId, ContentOriginTypeEnum.AGGREGATION.getCode());
    }

    /**
     * 根据栏目ID删除聚合关系
     *
     * @param catalogId
     * @return
     */
    @Override
    public int deleteAggregationByCatalogId(String catalogId) {
        return baseMapper.deleteAggregationByCatalogId(catalogId);
    }

    /**
     * 获取内容投递的栏目
     *
     * @param templateId
     * @return
     */
    @Override
    public List<String> getDeliverCatalog(String templateId) {
        return baseMapper.getDeliverCatalog(templateId);
    }

    /**
     * 设置投递栏目
     *
     * @param templateId
     * @param catalogIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDeliverCatalogs(String templateId, List<String> catalogIds) {
        if (StrUtil.isEmpty(templateId)) {
            return;
        }
        // 删除投递关系
        baseMapper.deleteByTemplateId(templateId, ContentOriginTypeEnum.DELIVER.getCode());
        if (CollectionUtil.isNotEmpty(catalogIds)) {
            List<CatalogTemplate> catalogTemplatesList = new ArrayList<>();
            for (String catalogId : catalogIds) {
                CatalogTemplate entity = new CatalogTemplate();
                entity.setId(IdWorker.getIdStr());
                entity.setCatalogId(catalogId);
                entity.setTemplateId(templateId);
                entity.setOriginType(ContentOriginTypeEnum.DELIVER.getCode());
                catalogTemplatesList.add(entity);
            }
            // 批量插入投递栏目内容关联关系
            baseMapper.insertCatalogTemplate(catalogTemplatesList);
        }
    }
}
