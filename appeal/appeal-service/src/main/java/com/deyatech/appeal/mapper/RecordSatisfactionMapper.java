package com.deyatech.appeal.mapper;

import com.deyatech.appeal.entity.RecordSatisfaction;
import com.deyatech.appeal.vo.RecordSatisfactionVo;
import com.deyatech.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-11-21
 */
public interface RecordSatisfactionMapper extends BaseMapper<RecordSatisfaction> {

    List<RecordSatisfactionVo> getAppealSatisCountByAppealId(@Param("appealId") String appealId);

}
