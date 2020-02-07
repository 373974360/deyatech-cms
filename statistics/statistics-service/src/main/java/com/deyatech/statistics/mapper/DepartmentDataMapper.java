package com.deyatech.statistics.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.statistics.vo.DepartmentUserDataQueryVo;
import com.deyatech.statistics.vo.DepartmentUserDataResultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  部门数据统计 Mapper 接口
 * </p>
 *
 * @Author ycx
 * @since 2020-05-05
 */
public interface DepartmentDataMapper {
    /**
     * 统计部门发稿量
     *
     * @param queryVo
     * @return
     */
    List<Map<String, Object>> countDepartmentContentData(@Param("queryVo") DepartmentUserDataQueryVo queryVo);

    /**
     * 统计部门发稿量 最小日期
     *
     * @param queryVo
     * @return
     */
    String getDepartmentContentDataMinDate(@Param("queryVo") DepartmentUserDataQueryVo queryVo);

    /**
     * 统计部门发稿量 最大日期
     *
     * @param queryVo
     * @return
     */
    String getDepartmentContentDataMaxDate(@Param("queryVo") DepartmentUserDataQueryVo queryVo);



    /**
     * 统计栏目发稿量
     *
     * @param queryVo
     * @return
     */
    List<Map<String, Object>> countCatalogContentData(@Param("queryVo") DepartmentUserDataQueryVo queryVo);

    /**
     * 统计栏目发稿量 最小日期
     *
     * @param queryVo
     * @return
     */
    String getCatalogContentDataMinDate(@Param("queryVo") DepartmentUserDataQueryVo queryVo);

    /**
     * 统计栏目发稿量 最大日期
     *
     * @param queryVo
     * @return
     */
    String getCatalogContentDataMaxDate(@Param("queryVo") DepartmentUserDataQueryVo queryVo);

    /**
     * 部门栏目内容
     *
     * @param page
     * @param queryVo
     * @return
     */
    IPage<DepartmentUserDataResultVo> pageDepartmentCatalogTemplate(@Param("page") IPage page, @Param("queryVo") DepartmentUserDataQueryVo queryVo);
}
