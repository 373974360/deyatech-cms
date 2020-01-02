package com.deyatech.appeal.service.impl;

import com.deyatech.appeal.entity.Model;
import com.deyatech.appeal.vo.ModelVo;
import com.deyatech.appeal.mapper.ModelMapper;
import com.deyatech.appeal.service.ModelService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-09-24
 */
@Service
public class ModelServiceImpl extends BaseServiceImpl<ModelMapper, Model> implements ModelService {

    /**
     * 单个将对象转换为vo
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
     * 批量将对象转换为vo
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
     * 返回
     * @param departmentIds
     * @return
     */
    @Override
    public long countModelByDepartmentId(List<String> departmentIds) {
        long total = 0;
        if (CollectionUtil.isNotEmpty(departmentIds)) {
            for (String id : departmentIds) {
                total += baseMapper.countModelByDepartmentId("%" + id + "%");
            }
        }
        return total;
    }
}
