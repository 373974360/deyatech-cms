package com.deyatech.appeal.service;

import com.deyatech.appeal.entity.Purpose;
import com.deyatech.appeal.vo.PurposeVo;
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
public interface PurposeService extends BaseService<Purpose> {

    /**
     * 单个将对象转换为vo
     *
     * @param purpose
     * @return
     */
    PurposeVo setVoProperties(Purpose purpose);

    /**
     * 批量将对象转换为vo
     *
     * @param purposes
     * @return
     */
    List<PurposeVo> setVoProperties(Collection purposes);
}
