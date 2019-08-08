package com.deyatech.station.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.station.entity.Model;
import com.deyatech.station.vo.ModelVo;
import com.deyatech.station.mapper.ModelMapper;
import com.deyatech.station.service.ModelService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

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
    private ModelMapper modelMapper;
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
    public IPage<Model> pageByBean(Model entity) {
        return modelMapper.pageByBean(getPageByBean(entity), entity);
    }
}
