package com.deyatech.station.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.station.entity.ModelTemplate;
import com.deyatech.station.vo.ModelTemplateVo;
import com.deyatech.station.mapper.ModelTemplateMapper;
import com.deyatech.station.service.ModelTemplateService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 内容模型模版 服务实现类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-06
 */
@Service
public class ModelTemplateServiceImpl extends BaseServiceImpl<ModelTemplateMapper, ModelTemplate> implements ModelTemplateService {

    @Autowired
    ModelTemplateMapper modelTemplateMapper;

    /**
     * 单个将对象转换为vo内容模型模版
     *
     * @param modelTemplate
     * @return
     */
    @Override
    public ModelTemplateVo setVoProperties(ModelTemplate modelTemplate){
        ModelTemplateVo modelTemplateVo = new ModelTemplateVo();
        BeanUtil.copyProperties(modelTemplate, modelTemplateVo);
        return modelTemplateVo;
    }

    /**
     * 批量将对象转换为vo内容模型模版
     *
     * @param modelTemplates
     * @return
     */
    @Override
    public List<ModelTemplateVo> setVoProperties(Collection modelTemplates){
        List<ModelTemplateVo> modelTemplateVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(modelTemplates)) {
            for (Object modelTemplate : modelTemplates) {
                ModelTemplateVo modelTemplateVo = new ModelTemplateVo();
                BeanUtil.copyProperties(modelTemplate, modelTemplateVo);
                modelTemplateVos.add(modelTemplateVo);
            }
        }
        return modelTemplateVos;
    }

    @Override
    public IPage<ModelTemplateVo> pageByModelTemplate(ModelTemplate modelTemplate) {
        return modelTemplateMapper.pageByModelTemplate(getPageByBean(modelTemplate), modelTemplate);
    }

    /**
     * 根据ModelTemplate对象属性分页检索内容模型模版，按站点分组
     *
     * @param modelTemplate
     * @return
     */
    @Override
    public IPage<ModelTemplateVo> pageByModelTemplateGroupBySite(ModelTemplate modelTemplate) {
        modelTemplate.setDefaultFlag(true);
        IPage<ModelTemplateVo> modelTemplateVoIPage = modelTemplateMapper.pageByModelTemplate(getPageByBean(modelTemplate), modelTemplate);
        return modelTemplateVoIPage;
    }

    /**
     * 检查站点的内容模型是否已配置
     *
     * @param modelTemplate
     * @return
     */
    @Override
    public Boolean checkSiteContentModelExist(ModelTemplate modelTemplate) {
        QueryWrapper<ModelTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_model_id", modelTemplate.getContentModelId())
                .eq("site_id", modelTemplate.getSiteId());
        return super.count(queryWrapper) != 0;
    }

}
