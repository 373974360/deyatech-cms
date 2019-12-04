package com.deyatech.interview.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.enums.MaterialUsePlaceEnum;
import com.deyatech.interview.config.RabbitMQLiveConfig;
import com.deyatech.interview.entity.Category;
import com.deyatech.interview.entity.Model;
import com.deyatech.interview.mapper.ModelMapper;
import com.deyatech.interview.service.CategoryService;
import com.deyatech.interview.service.ModelService;
import com.deyatech.interview.vo.CategoryVo;
import com.deyatech.interview.vo.LiveImageVo;
import com.deyatech.interview.vo.LiveMessageVo;
import com.deyatech.interview.vo.ModelVo;
import com.deyatech.station.feign.StationFeign;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 访谈模型 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-26
 */
@Service
public class ModelServiceImpl extends BaseServiceImpl<ModelMapper, Model> implements ModelService {
    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StationFeign stationFeign;

    /**
     * 单个将对象转换为vo访谈模型
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
     * 批量将对象转换为vo访谈模型
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

    /**
     * 检索访谈模型根据分类和名称
     *
     * @param model
     * @return
     */
    @Override
    public IPage<ModelVo> pageByCategoryAndName(String siteId, Model model) {
        return baseMapper.pageByCategoryAndName(getPageByBean(model), siteId, model);
    }

    private String generateKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 追加直播消息
     *
     * @param liveMessageVo
     * @return
     */
    @Override
    public Boolean operateLiveMessage(LiveMessageVo liveMessageVo) {
        try {
            List<LiveMessageVo> messageList;
            Model model = super.getById(liveMessageVo.getModelId());
            // 取出原来的内容
            if (Objects.nonNull(model) && StrUtil.isNotEmpty(model.getContent())) {
                messageList = mapper.readValue(model.getContent(), mapper.getTypeFactory().constructParametricType(List.class, LiveMessageVo.class));
            } else {
                messageList = new ArrayList<>();
            }
            String flag;
            // 添加
            if (StrUtil.isEmpty(liveMessageVo.getKey())) {
                flag = ",append";
                liveMessageVo.setKey(generateKey());
                messageList.add(liveMessageVo);
            } else {
                int index = -1;
                for (int i = 0; i < messageList.size(); i ++) {
                    if (liveMessageVo.getKey().equals(messageList.get(i).getKey())) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    if (StrUtil.isEmpty(liveMessageVo.getMessage())) {
                        // 删除
                        flag = ",delete";
                        messageList.remove(index);
                    } else {
                        // 修改
                        flag = ",modify";
                        messageList.set(index, liveMessageVo);
                    }
                } else {
                    return false;
                }
            }
            // 更新模型
            Model message = new Model();
            message.setId(model.getId());
            message.setVersion(model.getVersion());
            message.setContent(mapper.writeValueAsString(messageList));
            if (super.updateById(message)) {
                liveMessageVo.setKey(liveMessageVo.getKey() + flag);
                rabbitTemplate.convertAndSend(RabbitMQLiveConfig.FANOUT_EXCHANGE_LIVE_MESSAGE, "", liveMessageVo);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 追加直播图片
     *
     * @param liveImageVo
     * @return
     */
    @Override
    public Boolean operateLiveImage(LiveImageVo liveImageVo) {
        try {
            StringBuilder oldUrls = new StringBuilder();
            StringBuilder newUrls = new StringBuilder();
            List<LiveImageVo> imageList;
            Model model = super.getById(liveImageVo.getModelId());
            // 取出原来的图片
            if (Objects.nonNull(model) && StrUtil.isNotEmpty(model.getImages())) {
                imageList = mapper.readValue(model.getImages(), mapper.getTypeFactory().constructParametricType(List.class, LiveImageVo.class));
            } else {
                imageList = new ArrayList<>();
            }
            String flag;
            // 添加
            if (StrUtil.isEmpty(liveImageVo.getKey())) {
                flag = ",append";
                liveImageVo.setKey(generateKey());
                newUrls.append(",");
                newUrls.append(liveImageVo.getUrl());
                imageList.add(liveImageVo);
            } else {
                int index = -1;
                for (int i = 0; i < imageList.size(); i ++) {
                    if (liveImageVo.getKey().equals(imageList.get(i).getKey())) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    if (StrUtil.isEmpty(liveImageVo.getUrl())) {
                        // 删除
                        flag = ",delete";
                        oldUrls.append(",");
                        oldUrls.append(imageList.get(index).getUrl());
                        imageList.remove(index);
                    } else {
                        // 修改
                        flag = ",modify";
                        // 不相等
                        if (!imageList.get(index).getUrl().equals(liveImageVo.getUrl())) {
                            oldUrls.append(",");
                            oldUrls.append(imageList.get(index).getUrl());
                            newUrls.append(",");
                            newUrls.append(liveImageVo.getUrl());
                        }
                        imageList.set(index, liveImageVo);
                    }
                } else {
                    return false;
                }
            }
            // 更新模型
            Model image = new Model();
            image.setId(model.getId());
            image.setVersion(model.getVersion());
            image.setImages(mapper.writeValueAsString(imageList));
            String oldUrl = oldUrls.toString();
            if(StrUtil.isNotEmpty(oldUrl)) {
                oldUrl = oldUrl.substring(1);
            }
            String newUrl = newUrls.toString();
            if (StrUtil.isNotEmpty(newUrl)) {
                newUrl = newUrl.substring(1);
            }
            stationFeign.markMaterialUsePlace(oldUrl, newUrl, MaterialUsePlaceEnum.INTERVIEW_MODEL.getCode());
            if (super.updateById(image)) {
                liveImageVo.setKey(liveImageVo.getKey() + flag);
                rabbitTemplate.convertAndSend(RabbitMQLiveConfig.FANOUT_EXCHANGE_LIVE_IMAGE, "", liveImageVo);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public IPage<ModelVo> getInterviewList(Map<String, Object> maps, Integer page, Integer pageSize) {
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_publish",1);
        if(maps.containsKey("catId")){
            queryWrapper.in("category_id",maps.get("catId").toString().split(","));
        }else{
            if(maps.containsKey("siteId")){
                Category category = new Category();
                category.setSiteId(maps.get("siteId").toString());
                List<CategoryVo> categoryVos = categoryService.listByNameAndSiteId(category);
                if(!categoryVos.isEmpty()){
                    List<String> catIdList = new ArrayList<>();
                    for(CategoryVo categoryVo:categoryVos){
                        catIdList.add(categoryVo.getId());
                    }
                    queryWrapper.in("category_id",catIdList);
                }
            }
        }
        if(maps.containsKey("status")){
            queryWrapper.in("status_",maps.get("status").toString().split(","));
        }
        if(maps.containsKey("orderby")){
            queryWrapper.orderByDesc(maps.get("orderby").toString());
        }else{
            queryWrapper.orderByDesc("create_time");
        }
        IPage<ModelVo> modelVoIPage = new Page<>(page,pageSize);
        Model model = new Model();
        model.setPage(page.longValue());
        model.setSize(pageSize.longValue());
        IPage<Model> pages = super.page(getPageByBean(model), queryWrapper);
        modelVoIPage.setRecords(setVoProperties(pages.getRecords()));
        modelVoIPage.setPages(pages.getPages());
        modelVoIPage.setTotal(pages.getTotal());
        modelVoIPage.setCurrent(pages.getCurrent());
        return modelVoIPage;
    }

    /**
     * 保存访谈模型
     *
     * @param model
     * @return
     */
    @Override
    public boolean saveModel(Model model) {
        boolean isCreate = false;
        if (StrUtil.isEmpty(model.getId())) {
            isCreate = true;
        }
        boolean result = saveOrUpdate(model);
        if (result && isCreate) {
            if (StrUtil.isNotEmpty(model.getImages())) {
                try {
                    List<LiveImageVo> imageList = mapper.readValue(model.getImages(), mapper.getTypeFactory().constructParametricType(List.class, LiveImageVo.class));
                    for (LiveImageVo image : imageList) {
                        image.setModelId(model.getId());
                    }
                    model.setImages(mapper.writeValueAsString(imageList));
                    updateById(model);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
