package com.deyatech.resource.service;

import com.deyatech.common.base.BaseService;
import com.deyatech.resource.entity.StationGroupDepartmentAdmin;
import com.deyatech.resource.vo.StationGroupDepartmentAdminVo;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  站部门管理员 服务类
 * </p>
 *
 * @Author ycx
 * @since 2020-02-06
 */
public interface StationGroupDepartmentAdminService extends BaseService<StationGroupDepartmentAdmin> {

    /**
     * 单个将对象转换为vo站部门管理员
     *
     * @param stationGroupDepartmentAdmin
     * @return
     */
    StationGroupDepartmentAdminVo setVoProperties(StationGroupDepartmentAdmin stationGroupDepartmentAdmin);

    /**
     * 批量将对象转换为vo站部门管理员
     *
     * @param stationGroupDepartmentAdmins
     * @return
     */
    List<StationGroupDepartmentAdminVo> setVoProperties(Collection stationGroupDepartmentAdmins);

    /**
     * 是否站点部门管理员
     *
     * @param siteId
     * @param userId
     * @return
     */
    boolean isSiteDepartmentAdmin(String siteId, String userId);
}
