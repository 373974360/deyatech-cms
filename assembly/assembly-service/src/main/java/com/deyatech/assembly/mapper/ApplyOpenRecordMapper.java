package com.deyatech.assembly.mapper;

import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
import com.deyatech.common.base.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-10-16
 */
public interface ApplyOpenRecordMapper extends BaseMapper<ApplyOpenRecord> {
    /**
     * 模型使用统计
     * @return
     */
    List<ApplyOpenRecordVo> countApplyOpenModel();
}
