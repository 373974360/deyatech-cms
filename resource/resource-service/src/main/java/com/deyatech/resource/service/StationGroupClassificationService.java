package com.deyatech.resource.service;

import com.deyatech.resource.entity.StationGroupClassification;
import com.deyatech.resource.vo.StationGroupClassificationVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
public interface StationGroupClassificationService extends BaseService<StationGroupClassification> {

    /**
     * 根据StationGroupClassification对象属性检索的tree对象
     *
     * @param stationGroupClassification
     * @return
     */
    Collection<StationGroupClassificationVo> getStationGroupClassificationTree(StationGroupClassification stationGroupClassification);

    /**
     * 单个将对象转换为vo
     *
     * @param stationGroupClassification
     * @return
     */
    StationGroupClassificationVo setVoProperties(StationGroupClassification stationGroupClassification);

    /**
     * 批量将对象转换为vo
     *
     * @param stationGroupClassifications
     * @return
     */
    List<StationGroupClassificationVo> setVoProperties(Collection stationGroupClassifications);

    /**
     * 根据分类编号统计名称件数
     *
     * @param parentId
     * @return
     */
    long countNameByParentId(String parentId, String name);

    /**
     * 根据分类编号统计英文名称件数
     *
     * @param parentId
     * @return
     */
    long countEnglishNameByParentId(String parentId, String englishName);
}
