package com.deyatech.resource.mapper;

import com.deyatech.resource.entity.StationGroupDepartmentAdmin;
import com.deyatech.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 站部门管理员 Mapper 接口
 * </p>
 *
 * @Author ycx
 * @since 2020-02-06
 */
public interface StationGroupDepartmentAdminMapper extends BaseMapper<StationGroupDepartmentAdmin> {
    /**
     * 统计站点部门管理员
     *
     * @return
     */
    int countSiteDepartmentAdmin(@Param("siteId") String siteId, @Param("userId") String userId);
}
