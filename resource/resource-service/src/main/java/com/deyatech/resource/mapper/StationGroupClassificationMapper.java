package com.deyatech.resource.mapper;

import com.deyatech.resource.entity.StationGroupClassification;
import com.deyatech.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * 根据父分类编号统计名称件数
     *
     * @param id
     * @param parentId
     * @param name
     * @return
     */
    long countNameByParentId(@Param("id") String id, @Param("parentId") String parentId, @Param("name") String name);

    /**
     * 根据父分类编号统计英文名称件数
     *
     * @param id
     * @param parentId
     * @param englishName
     * @return
     */
    long countEnglishNameByParentId(@Param("id") String id, @Param("parentId") String parentId, @Param("englishName") String englishName);

    /**
     * 根据父分类编号统计分类件数
     *
     * @param list
     * @return
     */
    long countClassificationByParentIdList(List<String> list);
}
