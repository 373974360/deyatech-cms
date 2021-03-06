package com.deyatech.station.mapper;

import com.deyatech.station.entity.Catalog;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.vo.CatalogVo;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 栏目 Mapper 接口
 * </p>
 *
 * @Author csm.
 * @since 2019-08-02
 */
public interface CatalogMapper extends BaseMapper<Catalog> {

    /**
     * 查询最大排序号
     * @return
     */
    int selectMaxSortNo();

    /**
     * 根据站点ID检索栏目
     *
     * @param list
     * @return
     */
    List<CatalogVo> getCatalogBySiteIds (@Param("list") List<String> list);

    /**
     * 更新隐藏
     *
     * @param allowHidden
     * @param id
     * @return
     */
    int updateAllowHiddenById(@Param("allowHidden") int allowHidden, @Param("id") String id);

    /**
     * 更新归档
     *
     * @param placeOnFile
     * @param id
     * @return
     */
    int updatePlaceOnFileById(@Param("placeOnFile") int placeOnFile, @Param("id") String id);

    /**
     * 获取栏目使用的工作流ID
     * @return
     */
    List<String> getAllCatalogWorkFlowId();

    /**
     * 获取画面用栏目
     *
     * @return
     */
    List<CatalogVo> getCatalogList(@Param("catalog") Catalog catalog);

    /**
     * 统计栏目子节点数
     *
     * @return
     */
    List<Map<String, Object>> getCountChildrenCatalog();

    /**
     * 用户分配的栏目
     * @param catalog
     * @param userId
     * @return
     */
    List<CatalogVo> getUserRoleCatalogList(@Param("userId") String userId, @Param("catalog") Catalog catalog);
    List<CatalogVo> getUserCatalogList(@Param("userId") String userId, @Param("catalog") Catalog catalog);
    List<CatalogVo> getDepartmentCatalogList(@Param("departmentId") String userId, @Param("catalog") Catalog catalog);
}
