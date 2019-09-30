package com.deyatech.interview.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.interview.entity.Model;
import com.deyatech.interview.vo.LiveImageVo;
import com.deyatech.interview.vo.LiveMessageVo;
import com.deyatech.interview.vo.ModelVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  访谈模型 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-26
 */
public interface ModelService extends BaseService<Model> {

    /**
     * 单个将对象转换为vo访谈模型
     *
     * @param model
     * @return
     */
    ModelVo setVoProperties(Model model);

    /**
     * 批量将对象转换为vo访谈模型
     *
     * @param models
     * @return
     */
    List<ModelVo> setVoProperties(Collection models);

    /**
     * 检索访谈模型根据分类和名称
     *
     * @param model
     * @return
     */
    IPage<ModelVo> pageByCategoryAndName(Model model);


    /**
     * 追加直播消息
     *
     * @param liveMessageVo
     * @return
     */
    Boolean operateLiveMessage(LiveMessageVo liveMessageVo);

    /**
     * 追加直播图片
     *
     * @param liveImageVo
     * @return
     */
    Boolean operateLiveImage(LiveImageVo liveImageVo);
}
