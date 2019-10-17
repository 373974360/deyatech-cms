package com.deyatech.assembly.service.impl;

import com.deyatech.assembly.entity.ApplyOpenModel;
import com.deyatech.assembly.vo.ApplyOpenModelVo;
import com.deyatech.assembly.mapper.ApplyOpenModelMapper;
import com.deyatech.assembly.service.ApplyOpenModelService;
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
 * @since 2019-10-16
 */
@Service
public class ApplyOpenModelServiceImpl extends BaseServiceImpl<ApplyOpenModelMapper, ApplyOpenModel> implements ApplyOpenModelService {

    /**
     * 单个将对象转换为vo
     *
     * @param applyOpenModel
     * @return
     */
    @Override
    public ApplyOpenModelVo setVoProperties(ApplyOpenModel applyOpenModel){
        ApplyOpenModelVo applyOpenModelVo = new ApplyOpenModelVo();
        BeanUtil.copyProperties(applyOpenModel, applyOpenModelVo);
        return applyOpenModelVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param applyOpenModels
     * @return
     */
    @Override
    public List<ApplyOpenModelVo> setVoProperties(Collection applyOpenModels){
        List<ApplyOpenModelVo> applyOpenModelVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(applyOpenModels)) {
            for (Object applyOpenModel : applyOpenModels) {
                ApplyOpenModelVo applyOpenModelVo = new ApplyOpenModelVo();
                BeanUtil.copyProperties(applyOpenModel, applyOpenModelVo);
                applyOpenModelVos.add(applyOpenModelVo);
            }
        }
        return applyOpenModelVos;
    }
}
