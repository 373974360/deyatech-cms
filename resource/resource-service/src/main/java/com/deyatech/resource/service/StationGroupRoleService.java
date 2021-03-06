package com.deyatech.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.vo.RoleUserVo;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.resource.entity.StationGroupRole;
import com.deyatech.resource.vo.StationGroupRoleVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  站点角色关联 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-31
 */
public interface StationGroupRoleService extends BaseService<StationGroupRole> {

    /**
     * 单个将对象转换为vo站点角色关联
     *
     * @param stationGroupRole
     * @return
     */
    StationGroupRoleVo setVoProperties(StationGroupRole stationGroupRole);

    /**
     * 批量将对象转换为vo站点角色关联
     *
     * @param stationGroupRoles
     * @return
     */
    List<StationGroupRoleVo> setVoProperties(Collection stationGroupRoles);

    /**
     * 设置角色站点
     *
     * @param roleId
     * @param stationGroupIds
     */
    void setRoleStationGroups(String roleId, List<String> stationGroupIds);
    /**
     * 设置角色站点
     *
     * @param stationGroupId
     * @param roleIds
     */
    void setStationGroupRoles(String stationGroupId, List<String> roleIds);

    /**
     * 删除站点角色关联
     *
     * @param StationGroupIds
     */
    void removeByStationGroupId(List<String> StationGroupIds);

    /**
     * 分页查询站关联的用户
     *
     * @param user
     * @return
     */
    IPage<UserVo> pageStationAssociationUser(String siteId, UserVo user);


    /**
     * 根据站点ID查询该站点关联的角色
     * @param stationGroupRoleVo
     * @return
     */
    IPage<StationGroupRoleVo> pageByStationGroupRoleVo(StationGroupRoleVo stationGroupRoleVo);
}
