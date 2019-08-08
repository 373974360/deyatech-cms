package com.deyatech.resource.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.vo.StationGroupVo;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
     * 根据分类编号统计站站群数
     * @param classificationId
     * @return
     */
    long countStationGroupByClassificationId(String classificationId);

    /**
     * 根据分类编号列表统计站站群数
     * @param list
     * @return
     */
    long countStationGroupByClassificationIdList(List<String> list);

    /**
     * 根据条件翻页查询站群
     *
     * @param page
     * @param stationGroupVo
     * @return
     */
    IPage<StationGroupVo> pageSelectByStationGroupVo(@Param("page") Page page, @Param("stationGroupVo") StationGroupVo stationGroupVo);

    /**
     * 根据条件查询所有站群
     *
     * @param stationGroupVo
     * @return
     */
    Collection<StationGroupVo> listSelectByStationGroupVo(@Param("stationGroupVo") StationGroupVo stationGroupVo);

    /**
     * 统计名称件数
     *
     * @param id
     * @param name
     * @return
     */
    long countName(@Param("id") String id, @Param("name") String name);

    /**
     * 统计英文名称件数
     *
     * @param id
     * @param englishName
     * @return
     */
    long countEnglishName(@Param("id") String id, @Param("englishName") String englishName);

    /**
     * 统计简称件数
     *
     * @param id
     * @param abbreviation
     * @return
     */
    long countAbbreviation(@Param("id") String id, @Param("abbreviation") String abbreviation);

    /**
     * 修改状态根据编号
     *
     * @param id
     * @param enable
     * @return
     */
    long updateEnableById(@Param("id") String id, @Param("enable") int enable);
    long updateEnableByIds(@Param("list") List<String> list, @Param("enable") int enable);

    /**
     * 根据编号检索站群
     *
     * @return
     */
    StationGroup getStationGroupById(@Param("id") Serializable id);
}
