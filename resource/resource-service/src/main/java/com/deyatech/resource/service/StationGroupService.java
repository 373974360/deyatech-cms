package com.deyatech.resource.service;

import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.vo.StationGroupVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  站群 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
public interface StationGroupService extends BaseService<StationGroup> {

    /**
     * 单个将对象转换为vo站群
     *
     * @param stationGroup
     * @return
     */
    StationGroupVo setVoProperties(StationGroup stationGroup);

    /**
     * 批量将对象转换为vo站群
     *
     * @param stationGroups
     * @return
     */
    List<StationGroupVo> setVoProperties(Collection stationGroups);

    /**
     * 根据分类编号统计站群个数
     *
     * @param classificationId
     * @return
     */
    long countStationGroupByClassificationId(String classificationId);
}
