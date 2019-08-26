package com.deyatech.interview.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.interview.entity.Model;
import com.deyatech.interview.vo.ModelVo;
import com.deyatech.interview.mapper.ModelMapper;
import com.deyatech.interview.service.ModelService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

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
}
