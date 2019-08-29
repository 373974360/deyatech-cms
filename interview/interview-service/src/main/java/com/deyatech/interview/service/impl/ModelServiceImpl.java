package com.deyatech.interview.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.interview.config.RabbitMQLiveConfig;
import com.deyatech.interview.entity.Model;
import com.deyatech.interview.vo.LiveImageVo;
import com.deyatech.interview.vo.LiveMessageVo;
import com.deyatech.interview.vo.ModelVo;
import com.deyatech.interview.mapper.ModelMapper;
import com.deyatech.interview.service.ModelService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.math.raw.Mod;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Objects;

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

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
    public IPage<ModelVo> pageByCategoryAndName(Model model) {
        return baseMapper.pageByCategoryAndName(getPageByBean(model), model);
    }

    /**
     * 追加直播消息
     *
     * @param liveMessageVo
     * @return
     */
    @Override
    public Boolean appendLiveMessage(String modelId, LiveMessageVo liveMessageVo) {
        try {
            List<LiveMessageVo> list;
            ObjectMapper mapper = new ObjectMapper();
            Model model = super.getById(modelId);
            if (Objects.nonNull(model) && StrUtil.isNotEmpty(model.getContent())) {
                list = mapper.readValue(model.getContent(), mapper.getTypeFactory().constructParametricType(List.class, LiveMessageVo.class));
                list.add(liveMessageVo);
            } else {
                list = new ArrayList<>();
                list.add(liveMessageVo);
            }
            Model message = new Model();
            message.setId(model.getId());
            message.setVersion(model.getVersion());
            message.setContent(mapper.writeValueAsString(list));
            if (super.updateById(message)) {
                rabbitTemplate.convertAndSend(RabbitMQLiveConfig.FANOUT_EXCHANGE_LIVE_MESSAGE, RabbitMQLiveConfig.ROUTING_KEY_LIVE_MESSAGE , liveMessageVo);
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
    public Boolean appendLiveImage(String modelId, LiveImageVo liveImageVo) {
        try {
            List<LiveImageVo> list;
            ObjectMapper mapper = new ObjectMapper();
            Model model = super.getById(modelId);
            if (Objects.nonNull(model) && StrUtil.isNotEmpty(model.getImages())) {
                list = mapper.readValue(model.getImages(), mapper.getTypeFactory().constructParametricType(List.class, LiveImageVo.class));
                list.add(liveImageVo);
            } else {
                list = new ArrayList<>();
                list.add(liveImageVo);
            }
            Model image = new Model();
            image.setId(model.getId());
            image.setVersion(model.getVersion());
            image.setImages(mapper.writeValueAsString(list));
            if (super.updateById(image)) {
                rabbitTemplate.convertAndSend(RabbitMQLiveConfig.FANOUT_EXCHANGE_LIVE_IMAGE, RabbitMQLiveConfig.ROUTING_KEY_LIVE_IMAGE , liveImageVo);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
