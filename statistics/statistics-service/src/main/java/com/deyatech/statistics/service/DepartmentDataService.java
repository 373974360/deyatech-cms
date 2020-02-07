package com.deyatech.statistics.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.statistics.vo.DepartmentUserDataQueryVo;
import com.deyatech.statistics.vo.DepartmentUserDataResultVo;

import java.util.Map;

/**
 * <p>
 *  部门数据统计 服务类
 * </p>
 *
 * @author ycx
 * @since 2020-02-05
 */
public interface DepartmentDataService {
    /**
     * 检索部门统计数据
     *
     * @param queryVo
     * @return
     */
    Map<String, Object> getDepartmentData(DepartmentUserDataQueryVo queryVo) throws Exception;

    /**
     * 检索部门栏目统计数据
     *
     * @param queryVo
     * @return
     */
    Map<String, Object> getDepartmentCatalogData(DepartmentUserDataQueryVo queryVo) throws Exception;

    /**
     * 检索部门栏目内容数据
     *
     * @param queryVo
     * @return
     */
    IPage<DepartmentUserDataResultVo> getDepartmentCatalogTemplateData(DepartmentUserDataQueryVo queryVo);
}
