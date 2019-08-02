package com.deyatech.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 根据分类编号统计网站个数
     *
     * @param classificationId
     * @return
     */
    long countStationGroupByClassificationId(String classificationId);

    /**
     * 根据分类编号列表统计站网站数
     * @param list
     * @return
     */
    long countStationGroupByClassificationIdList(List<String> list);

    /**
     * 根据条件查询网站
     *
     * @param stationGroupVo
     * @return
     */
    IPage<StationGroupVo> pageSelectByCondition(StationGroupVo stationGroupVo);

    /**
     * 根据分类编号统计名称件数
     *
     * @param id
     * @param classificationId
     * @param name
     * @return
     */
    long countNameByClassificationId(String id, String classificationId, String name);

    /**
     * 根据分类编号统计英文名称件数
     *
     * @param id
     * @param classificationId
     * @param englishName
     * @return
     */
    long countEnglishNameByClassificationId(String id, String classificationId, String englishName);

    /**
     * 根据分类编号统计简称件数
     *
     * @param id
     * @param classificationId
     * @param abbreviation
     * @return
     */
    long countAbbreviationByClassificationId(String id, String classificationId, String abbreviation);
}
