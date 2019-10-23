package com.deyatech.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.entity.StationGroupClassification;
import com.deyatech.resource.vo.StationGroupClassificationVo;
import com.deyatech.resource.vo.StationGroupVo;
import com.deyatech.common.base.BaseService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  站点 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
public interface StationGroupService extends BaseService<StationGroup> {

    /**
     * 根据StationGroupClassification对象属性检索的tree对象
     *
     * @param classification
     * @return
     */
    Collection<StationGroupClassificationVo> getClassificationStationTree(String userId, StationGroupClassification classification);

    /**
     * 单个将对象转换为vo站点
     *
     * @param stationGroup
     * @return
     */
    StationGroupVo setVoProperties(StationGroup stationGroup);

    /**
     * 批量将对象转换为vo站点
     *
     * @param stationGroups
     * @return
     */
    List<StationGroupVo> setVoProperties(Collection stationGroups);

    /**
     * 根据分类编号统计站点个数
     *
     * @param classificationId
     * @return
     */
    long countStationGroupByClassificationId(String classificationId);

    /**
     * 根据分类编号列表统计站站点数
     * @param list
     * @return
     */
    long countStationGroupByClassificationIdList(List<String> list);

    /**
     * 根据条件翻页查询站点
     *
     * @param stationGroupVo
     * @return
     */
    IPage<StationGroupVo> pageSelectByStationGroupVo(StationGroupVo stationGroupVo);

    /**
     * 根据条件查询所有站点
     * @param stationGroupVo
     * @return
     */
    Collection<StationGroupVo> listSelectByStationGroupVo(StationGroupVo stationGroupVo);

    /**
     * 统计名称件数
     *
     * @param id
     * @param name
     * @return
     */
    long countName(String id, String name);

    /**
     * 统计英文名称件数
     *
     * @param id
     * @param englishName
     * @return
     */
    long countEnglishName(String id, String englishName);

    /**
     * 统计简称件数
     *
     * @param id
     * @param abbreviation
     * @return
     */
    long countAbbreviation(String id, String abbreviation);

    /**
     * 启用或停用站点
     *
     * @param id
     * @param flag
     * @return
     */
    boolean runOrStopStationById(String id, String flag);

    /**
     * 根据编号检索站点
     *
     * @param id
     * @return
     */
    StationGroup getById(Serializable id);

    /**
     * 删除站点
     *
     * @param ids
     * @return
     */
    boolean removeStationGroupAndConfig(List<String> ids, Map<String, StationGroup> maps);

    /**
     * 更新或保存
     * @param stationGroup
     * @return
     */
    boolean saveOrUpdateAndNginx(StationGroup stationGroup);
}
