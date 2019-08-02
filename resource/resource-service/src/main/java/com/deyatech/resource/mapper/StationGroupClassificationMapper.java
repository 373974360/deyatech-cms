package com.deyatech.resource.mapper;

import com.deyatech.resource.entity.StationGroupClassification;
import com.deyatech.common.base.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
public interface StationGroupClassificationMapper extends BaseMapper<StationGroupClassification> {

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
