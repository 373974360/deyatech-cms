package com.deyatech.statistics.mapper;

import com.deyatech.statistics.vo.UserDataQueryVo;

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
     * 统计用户部门的栏目发稿量
     *
     * @param queryVo
     * @return
     */
    List<Map<String, Object>> countDepartmentUserTreeData(UserDataQueryVo queryVo);

    /**
     * 条件最小日期
     *
     * @param queryVo
     * @return
     */
    String getUserDepartmentCatalogTemplatMinDate(UserDataQueryVo queryVo);

    /**
     * 条件最大日期
     *
     * @param queryVo
     * @return
     */
    String getUserDepartmentCatalogTemplatMaxDate(UserDataQueryVo queryVo);

}
