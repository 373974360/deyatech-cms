package com.deyatech.appeal.service.impl;

import com.deyatech.appeal.entity.Purpose;
import com.deyatech.appeal.vo.PurposeVo;
import com.deyatech.appeal.mapper.PurposeMapper;
import com.deyatech.appeal.service.PurposeService;
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
public class PurposeServiceImpl extends BaseServiceImpl<PurposeMapper, Purpose> implements PurposeService {

    /**
     * 单个将对象转换为vo
     *
     * @param purpose
     * @return
     */
    @Override
    public PurposeVo setVoProperties(Purpose purpose){
        PurposeVo purposeVo = new PurposeVo();
        BeanUtil.copyProperties(purpose, purposeVo);
        return purposeVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param purposes
     * @return
     */
    @Override
    public List<PurposeVo> setVoProperties(Collection purposes){
        List<PurposeVo> purposeVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(purposes)) {
            for (Object purpose : purposes) {
                PurposeVo purposeVo = new PurposeVo();
                BeanUtil.copyProperties(purpose, purposeVo);
                purposeVos.add(purposeVo);
            }
        }
        return purposeVos;
    }
}
