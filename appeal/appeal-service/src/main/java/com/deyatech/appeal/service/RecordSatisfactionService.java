package com.deyatech.appeal.service;

import com.deyatech.appeal.entity.RecordSatisfaction;
import com.deyatech.appeal.vo.RecordSatisfactionVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-21
 */
public interface RecordSatisfactionService extends BaseService<RecordSatisfaction> {

    /**
     * 单个将对象转换为vo
     *
     * @param recordSatisfaction
     * @return
     */
    RecordSatisfactionVo setVoProperties(RecordSatisfaction recordSatisfaction);

    /**
     * 批量将对象转换为vo
     *
     * @param recordSatisfactions
     * @return
     */
    List<RecordSatisfactionVo> setVoProperties(Collection recordSatisfactions);

    /**
     * 根据信件ID获取满意度评价结果
     *
     * @param appealId
     * @return
     */
    List<RecordSatisfactionVo> getAppealSatisCountByAppealId(String appealId);
}
