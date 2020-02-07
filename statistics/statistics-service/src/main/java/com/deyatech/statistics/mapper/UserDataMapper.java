package com.deyatech.statistics.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.entity.User;
import com.deyatech.statistics.vo.DepartmentUserDataQueryVo;
import com.deyatech.statistics.vo.DepartmentUserDataResultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  用户数据统计 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2020-01-19
 */
public interface UserDataMapper {

    /**
     * 发稿人信息
     *
     * @param list
     * @return
     */
    List<User> getUserInfo(@Param("list") List<String> list);

    // 用户

    /**
     * 统计用户发稿量
     *
     * @param queryVo
     * @return
     */
    List<Map<String, Object>> countUserContentData(@Param("queryVo") DepartmentUserDataQueryVo queryVo);

    /**
     * 统计用户发稿量 最小日期
     *
     * @param queryVo
     * @return
     */
    String getUserContentDataMinDate(@Param("queryVo") DepartmentUserDataQueryVo queryVo);

    /**
     * 统计用户发稿量 最大日期
     *
     * @param queryVo
     * @return
     */
    String getUserContentDataMaxDate(@Param("queryVo") DepartmentUserDataQueryVo queryVo);

    // 栏目

    /**
     * 统计用户栏目发稿量
     *
     * @param queryVo
     * @return
     */
    List<Map<String, Object>> countCatalogContentData(@Param("queryVo") DepartmentUserDataQueryVo queryVo);

    /**
     * 统计用户栏目发稿量 最小日期
     *
     * @param queryVo
     * @return
     */
    String getUserCatalogContentDataMinDate(@Param("queryVo") DepartmentUserDataQueryVo queryVo);

    /**
     * 统计用户栏目发稿量 最大日期
     *
     * @param queryVo
     * @return
     */
    String getUserCatalogContentDataMaxDate(@Param("queryVo") DepartmentUserDataQueryVo queryVo);

    /**
     * 用户栏目内容
     *
     * @param page
     * @param queryVo
     * @return
     */
    IPage<DepartmentUserDataResultVo> pageUserCatalogTemplate(@Param("page") IPage page, @Param("queryVo") DepartmentUserDataQueryVo queryVo);
}
