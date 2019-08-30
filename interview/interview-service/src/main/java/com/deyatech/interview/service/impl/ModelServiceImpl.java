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

    private String generateKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 追加直播消息
     *
     * @param modelId
     * @param liveMessageVo
     * @return
     */
    @Override
    public Boolean operateLiveMessage(String modelId, LiveMessageVo liveMessageVo) {
        try {
            List<LiveMessageVo> messageList;
            ObjectMapper mapper = new ObjectMapper();
            Model model = super.getById(modelId);
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
     * @param modelId
     * @param liveImageVo
     * @return
     */
    @Override
    public Boolean operateLiveImage(String modelId, LiveImageVo liveImageVo) {
        try {
            List<LiveImageVo> imageList;
            ObjectMapper mapper = new ObjectMapper();
            Model model = super.getById(modelId);
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
                        imageList.remove(index);
                    } else {
                        // 修改
                        flag = ",modify";
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
            if (super.updateById(image)) {
                liveImageVo.setKey(liveImageVo.getKey() + flag);
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
