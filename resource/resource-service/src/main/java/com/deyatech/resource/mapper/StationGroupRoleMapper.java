package com.deyatech.resource.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.entity.RoleUser;
import com.deyatech.admin.entity.User;
import com.deyatech.admin.vo.RoleUserVo;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.resource.entity.StationGroupRole;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.resource.vo.StationGroupRoleVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 站点角色关联 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-10-31
 */
public interface StationGroupRoleMapper extends BaseMapper<StationGroupRole> {
    /**
     * 分页查询站关联的用户
     *
     * @param user
     * @return
     */
    IPage<UserVo> pageStationAssociationUser(@Param("page") IPage<User> page, @Param("siteId") String siteId, @Param("user") UserVo user);

    /**
     * 根据站点ID查询该站点关联的角色
     * @param page
     * @return
     */
    IPage<StationGroupRoleVo> pageByStationGroupRoleVo(@Param("page") IPage<StationGroupRole> page, @Param("stationGroupRoleVo") StationGroupRoleVo stationGroupRoleVo);
}
