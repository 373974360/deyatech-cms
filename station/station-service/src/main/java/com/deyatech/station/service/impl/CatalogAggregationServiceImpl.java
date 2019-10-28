package com.deyatech.station.service.impl;

import com.deyatech.station.entity.CatalogAggregation;
import com.deyatech.station.vo.CatalogAggregationVo;
import com.deyatech.station.mapper.CatalogAggregationMapper;
import com.deyatech.station.service.CatalogAggregationService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 聚合栏目 服务实现类
 * </p>
 *
 * @Author csm.
 * @since 2019-09-11
 */
@Service
public class CatalogAggregationServiceImpl extends BaseServiceImpl<CatalogAggregationMapper, CatalogAggregation> implements CatalogAggregationService {

    /**
     * 单个将对象转换为vo聚合栏目
     *
     * @param catalogAggregation
     * @return
     */
    @Override
    public CatalogAggregationVo setVoProperties(CatalogAggregation catalogAggregation){
        CatalogAggregationVo catalogAggregationVo = new CatalogAggregationVo();
        BeanUtil.copyProperties(catalogAggregation, catalogAggregationVo);
        return catalogAggregationVo;
    }

    /**
     * 批量将对象转换为vo聚合栏目
     *
     * @param catalogAggregations
     * @return
     */
    @Override
    public List<CatalogAggregationVo> setVoProperties(Collection catalogAggregations){
        List<CatalogAggregationVo> catalogAggregationVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(catalogAggregations)) {
            for (Object catalogAggregation : catalogAggregations) {
                CatalogAggregationVo catalogAggregationVo = new CatalogAggregationVo();
                BeanUtil.copyProperties(catalogAggregation, catalogAggregationVo);
                catalogAggregationVos.add(catalogAggregationVo);
            }
        }
        return catalogAggregationVos;
    }

    /**
     * 检索聚合对象
     *
     * @return
     */
    @Override
    public CatalogAggregationVo getCatalogAggregationById(String id) {
        return baseMapper.getCatalogAggregationById(id);
    }
}
