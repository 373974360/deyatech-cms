package com.deyatech.resource.mapper;

import com.deyatech.common.base.BaseMapper;
import com.deyatech.resource.entity.StationGroupUser;
import com.deyatech.resource.vo.StationGroupUserVo;

import java.util.List;

/**
 * <p>
 * 站点用户关联 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-09-12
 */
public interface StationGroupUserMapper extends BaseMapper<StationGroupUser> {

    /**
     * 已选择的用户
     *
     * @param stationGroupId
     * @return
     */
    List<StationGroupUserVo> selectedUser(String stationGroupId);

    /**
     * 未选择的用户
     *
     * @param stationGroupId
     * @return
     */
    List<StationGroupUserVo> unselectedUser(String stationGroupId);
    List<StationGroupUserVo> unselectedUserDefault(String departmentId);

    /**
     * 部门及子部门所有的用户
     *
     * @return
     */
    List<StationGroupUserVo> departmentAndSubsidiaryDepartmentUser(String stationGroupId);

    /**
     * 删除站点关联的用户
     *
     * @param stationSroupId
     * @return
     */
    int removeByStationGroupId(String stationSroupId);
}
