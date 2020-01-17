package com.deyatech.apply.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.apply.entity.OpenModel;
import com.deyatech.apply.mapper.OpenModelMapper;
import com.deyatech.apply.service.OpenModelService;
import com.deyatech.apply.vo.OpenModelVo;
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
public class OpenModelServiceImpl extends BaseServiceImpl<OpenModelMapper, OpenModel> implements OpenModelService {

    /**
     * 单个将对象转换为vo
     *
     * @param openModel
     * @return
     */
    @Override
    public OpenModelVo setVoProperties(OpenModel openModel){
        OpenModelVo openModelVo = new OpenModelVo();
        BeanUtil.copyProperties(openModel, openModelVo);
        return openModelVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param openModels
     * @return
     */
    @Override
    public List<OpenModelVo> setVoProperties(Collection openModels){
        List<OpenModelVo> openModelVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(openModels)) {
            for (Object openModel : openModels) {
                OpenModelVo openModelVo = new OpenModelVo();
                BeanUtil.copyProperties(openModel, openModelVo);
                openModelVos.add(openModelVo);
            }
        }
        return openModelVos;
    }

    /**
     * 翻页检索
     *
     * @param OpenModel
     * @return
     */
    @Override
    public IPage<OpenModelVo> pageByOpenModel(OpenModel OpenModel) {
        return baseMapper.pageByOpenModel(this.getPageByBean(OpenModel), OpenModel);
    }
}
