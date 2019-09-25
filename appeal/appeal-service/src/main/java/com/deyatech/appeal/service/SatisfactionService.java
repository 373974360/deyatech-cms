package com.deyatech.appeal.service;

import com.deyatech.appeal.entity.Satisfaction;
import com.deyatech.appeal.vo.SatisfactionVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-09-24
 */
public interface SatisfactionService extends BaseService<Satisfaction> {

    /**
     * 单个将对象转换为vo
     *
     * @param satisfaction
     * @return
     */
    SatisfactionVo setVoProperties(Satisfaction satisfaction);

    /**
     * 批量将对象转换为vo
     *
     * @param satisfactions
     * @return
     */
    List<SatisfactionVo> setVoProperties(Collection satisfactions);
}
