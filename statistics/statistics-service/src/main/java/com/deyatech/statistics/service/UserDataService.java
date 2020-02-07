package com.deyatech.statistics.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.statistics.vo.DepartmentUserDataQueryVo;
import com.deyatech.statistics.vo.DepartmentUserDataResultVo;

import java.util.Map;

/**
 * <p>
 *  用户数据统计 服务类
 * </p>
 *
 * @author ycx
 * @since 2020-01-06
 */
public interface UserDataService {
    /**
     * 检索部门用户统计数据
     *
     * @param queryVo
     * @return
     */
    Map<String, Object> getDepartmentUserData(DepartmentUserDataQueryVo queryVo) throws Exception;

    /**
     * 检索用户栏目统计数据
     *
     * @param queryVo
     * @return
     */
    Map<String, Object> getUserCatalogData(DepartmentUserDataQueryVo queryVo) throws Exception;

    /**
     * 检索用户栏目内容数据
     *
     * @param queryVo
     * @return
     */
    IPage<DepartmentUserDataResultVo> getUserCatalogTemplateData(DepartmentUserDataQueryVo queryVo);
}
