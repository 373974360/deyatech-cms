package com.deyatech.station.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.entity.Model;
import com.deyatech.station.entity.ModelTemplate;
import com.deyatech.station.index.IndexService;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.ModelTemplateService;
import com.deyatech.station.vo.ModelVo;
import com.deyatech.station.mapper.ModelMapper;
import com.deyatech.station.service.ModelService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 内容模型 服务实现类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-06
 */
@Service
public class ModelServiceImpl extends BaseServiceImpl<ModelMapper, Model> implements ModelService {

    @Autowired
    private CatalogService catalogService;
    @Autowired
    private ModelTemplateService modelTemplateService;
    @Autowired
    private IndexService indexService;

    /**
     * 单个将对象转换为vo内容模型
     *
     * @param model
     * @return
     */
    @Override
    public ModelVo setVoProperties(Model model){
        ModelVo modelVo = new ModelVo();
        BeanUtil.copyProperties(model, modelVo);
        return modelVo;
    }

    /**
     * 批量将对象转换为vo内容模型
     *
     * @param models
     * @return
     */
    @Override
    public List<ModelVo> setVoProperties(Collection models){
        List<ModelVo> modelVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(models)) {
            for (Object model : models) {
                ModelVo modelVo = new ModelVo();
                BeanUtil.copyProperties(model, modelVo);
                modelVos.add(modelVo);
            }
        }
        return modelVos;
    }

    @Override
    public boolean saveOrUpdate(Model entity) {
        if (this.checkNameExist(entity)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "名称不能重复");
        }
        if (this.checkEnglishNameExist(entity)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "英文名不能重复");
        }
        boolean result = super.saveOrUpdate(entity);
        // 创建索引 TODO
        indexService.createIndex(this.getIndexByModelId(entity.getId()), true, entity.getId(), entity.getMetaDataCollectionId());
        return result;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        // 删除索引 TODO
        ids.stream().forEach(
                id -> indexService.deleteIndex(this.getIndexByModelId((String) id))
        );
        boolean result = super.removeByIds(ids);
        // 删除内容模型模版关联关系
        QueryWrapper<ModelTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("content_model_id", ids);
        modelTemplateService.remove(queryWrapper);
        return result;
    }

    @Override
    public IPage<Model> pageByBean(Model entity) {
        return baseMapper.pageByBean(getPageByBean(entity), entity);
    }

    /**
     * 判断Model对象中文名称是否存在
     *
     * @param model
     * @return
     */
    @Override
    public boolean checkNameExist(Model model) {
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name_", model.getName());
        if (StrUtil.isNotEmpty(model.getId())) {
            queryWrapper.ne("id_", model.getId());
        }
        return super.count(queryWrapper) > 0;
    }

    /**
     * 判断Model对象英文名称是否存在
     *
     * @param model
     * @return
     */
    @Override
    public boolean checkEnglishNameExist(Model model) {
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("english_name", model.getEnglishName());
        if (StrUtil.isNotEmpty(model.getId())) {
            queryWrapper.ne("id_", model.getId());
        }
        return super.count(queryWrapper) > 0;
    }

    /**
     * 根据站点id属性检索所有内容模型
     *
     * @param siteId
     * @return
     */
    @Override
    public Collection<Model> getAllModelBySiteId(String siteId) {
        return super.list();
//        Collection<Model> modelList = null;
//        // 通过查询ModelTemplate获取model id
//        QueryWrapper<ModelTemplate> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("content_model_id").eq("site_id", siteId);
//        List<ModelTemplate> modelTemplateList = modelTemplateService.list(queryWrapper);
//        List<String> modelIds = modelTemplateList.stream().map(m -> m.getContentModelId()).distinct().collect(Collectors.toList());
//        // 通过model id获取Model集合
//        if (CollectionUtil.isNotEmpty(modelIds)) {
//            modelList = super.listByIds(modelIds);
//        }
//        return modelList;
    }

    /**
     * 根据内容模型id获取索引
     * @param modelId
     * @return
     */
    @Override
    public String getIndexByModelId(String modelId) {
        Model model = super.getById(modelId);
        return "cms_" + model.getEnglishName();
    }

    /**
     * 根据栏目id属性检索所有内容模型
     *
     * @param catalogId
     * @return
     */
    @Override
    public Collection<Model> getModelByCatalogId(String catalogId) {
        Catalog catalog = catalogService.getById(catalogId);
        if (Objects.nonNull(catalog)) {
            String modelIds = catalog.getContentModelId();
            if (StrUtil.isNotEmpty(modelIds)) {
                QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("id_", Arrays.asList(modelIds.split(",")));
                return super.list(queryWrapper);
            }
        }
        return null;
//        Collection<Model> modelList = null;
//        // 通过查询ModelTemplate获取model id
//        QueryWrapper<ModelTemplate> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("content_model_id").eq("cms_catalog_id", catalogId);
//        List<ModelTemplate> modelTemplateList = modelTemplateService.list(queryWrapper);
//        List<String> modelIds = modelTemplateList.stream().map(m -> m.getContentModelId()).distinct().collect(Collectors.toList());
//        // 通过model id获取Model集合
//        if (CollectionUtil.isNotEmpty(modelIds)) {
//            modelList = super.listByIds(modelIds);
//        }
//        return modelList;
    }
}
