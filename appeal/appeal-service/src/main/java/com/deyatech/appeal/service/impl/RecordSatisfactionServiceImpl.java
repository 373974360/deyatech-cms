package com.deyatech.appeal.service.impl;

import com.deyatech.appeal.entity.RecordSatisfaction;
import com.deyatech.appeal.vo.RecordSatisfactionVo;
import com.deyatech.appeal.mapper.RecordSatisfactionMapper;
import com.deyatech.appeal.service.RecordSatisfactionService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-21
 */
@Service
public class RecordSatisfactionServiceImpl extends BaseServiceImpl<RecordSatisfactionMapper, RecordSatisfaction> implements RecordSatisfactionService {

    @Autowired
    RecordSatisfactionMapper recordSatisfactionMapper;

    /**
     * 单个将对象转换为vo
     *
     * @param recordSatisfaction
     * @return
     */
    @Override
    public RecordSatisfactionVo setVoProperties(RecordSatisfaction recordSatisfaction){
        RecordSatisfactionVo recordSatisfactionVo = new RecordSatisfactionVo();
        BeanUtil.copyProperties(recordSatisfaction, recordSatisfactionVo);
        return recordSatisfactionVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param recordSatisfactions
     * @return
     */
    @Override
    public List<RecordSatisfactionVo> setVoProperties(Collection recordSatisfactions){
        List<RecordSatisfactionVo> recordSatisfactionVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(recordSatisfactions)) {
            for (Object recordSatisfaction : recordSatisfactions) {
                RecordSatisfactionVo recordSatisfactionVo = new RecordSatisfactionVo();
                BeanUtil.copyProperties(recordSatisfaction, recordSatisfactionVo);
                recordSatisfactionVos.add(recordSatisfactionVo);
            }
        }
        return recordSatisfactionVos;
    }

    @Override
    public List<RecordSatisfactionVo> getAppealSatisCountByAppealId(String appealId) {
        return recordSatisfactionMapper.getAppealSatisCountByAppealId(appealId);
    }
}
