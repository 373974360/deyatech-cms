package com.deyatech.apply.mapper;

import com.deyatech.apply.entity.OpenRecord;
import com.deyatech.apply.vo.OpenRecordVo;
import com.deyatech.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-10-16
 */
public interface OpenRecordMapper extends BaseMapper<OpenRecord> {
    /**
     * 模型使用统计
     * @return
     */
    List<OpenRecordVo> countOpenModel();
    int getApplyCount(@Param("map") Map<String,Object> map);
}
