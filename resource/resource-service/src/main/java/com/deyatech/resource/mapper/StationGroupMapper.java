package com.deyatech.resource.mapper;

import com.deyatech.resource.entity.StationGroup;
import com.deyatech.common.base.BaseMapper;

/**
 * <p>
 * 站群 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
public interface StationGroupMapper extends BaseMapper<StationGroup> {

    /**
     * 根据分类编号统计站群个数
     * @param classificationId
     * @return
     */
    long countStationGroupByClassificationId(String classificationId);
}
