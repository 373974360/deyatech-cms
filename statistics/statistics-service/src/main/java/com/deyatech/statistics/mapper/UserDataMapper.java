package com.deyatech.statistics.mapper;

import com.deyatech.admin.entity.User;
import com.deyatech.station.entity.Catalog;
import com.deyatech.statistics.vo.UserDataQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2020-01-19
 */
public interface UserDataMapper {

    /**
     * 获取用户管理的部门
     *
     * @return
     */
    List<String> getUserManagedDepartment(String userId);

    /**
     * 获取用户管理的站点ID
     *
     * @return
     */
    List<String> getUserManagedSiteId(String userId);

    /**
     * 发稿人信息
     *
     * @param list
     * @return
     */
    List<User> getUserInfo(@Param("list") List<String> list);

    /**
     * 统计用户发稿量
     *
     * @param queryVo
     * @return
     */
    List<Map<String, Object>> countUserContentData(@Param("queryVo") UserDataQueryVo queryVo);

    /**
     * 统计用户发稿量 最小日期
     *
     * @param queryVo
     * @return
     */
    String getUserContentDataMinDate(@Param("queryVo") UserDataQueryVo queryVo);

    /**
     * 统计用户发稿量 最大日期
     *
     * @param queryVo
     * @return
     */
    String getUserContentDataMaxDate(@Param("queryVo") UserDataQueryVo queryVo);

    /**
     * 栏目信息
     *
     * @param list
     * @return
     */
    List<Catalog> getCatalogInfo(@Param("list") List<String> list);

    /**
     * 统计用户栏目发稿量
     *
     * @param queryVo
     * @return
     */
    List<Map<String, Object>> countUserCatalogContentData(@Param("queryVo") UserDataQueryVo queryVo);

    /**
     * 统计用户栏目发稿量 最小日期
     *
     * @param queryVo
     * @return
     */
    String getUserCatalogContentDataMinDate(@Param("queryVo") UserDataQueryVo queryVo);

    /**
     * 统计用户栏目发稿量 最大日期
     *
     * @param queryVo
     * @return
     */
    String getUserCatalogContentDataMaxDate(@Param("queryVo") UserDataQueryVo queryVo);
}
