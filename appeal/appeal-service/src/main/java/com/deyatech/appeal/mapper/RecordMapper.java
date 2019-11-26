package com.deyatech.appeal.mapper;

import com.deyatech.appeal.entity.Record;
import com.deyatech.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-09-24
 */
public interface RecordMapper extends BaseMapper<Record> {

    String getAllAppealCount(@Param("map") Map<String,Object> map);
}
