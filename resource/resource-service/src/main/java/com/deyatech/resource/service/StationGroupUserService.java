package com.deyatech.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseService;
import com.deyatech.resource.entity.StationGroupUser;
import com.deyatech.resource.vo.StationGroupUserVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  站点用户关联 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-09-12
 */
public interface StationGroupUserService extends BaseService<StationGroupUser> {

    /**
     * 单个将对象转换为vo站点用户关联
     *
     * @param stationGroupUser
     * @return
     */
    StationGroupUserVo setVoProperties(StationGroupUser stationGroupUser);

    /**
     * 批量将对象转换为vo站点用户关联
     *
     * @param stationGroupUsers
     * @return
     */
    List<StationGroupUserVo> setVoProperties(Collection stationGroupUsers);

    /**
     * 设置站点用户
     *
     * @param stationGroupId
     * @param userIds
     */
    void setStationGroupUsers(String stationGroupId, List<String> userIds);

    /**
     * 设置站点部门用户
     *
     * @param stationGroupId
     * @param departmentId
     */
    void setStationGroupDepartmentUsers(String stationGroupId, String departmentId);

    /**
     * 删除站点用户关联根据站点编号
     *
     * @param stationSroupId
     * @return
     */
    int removeByStationGroupId(String stationSroupId);

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
     * @return
     */
    List<StationGroupUserVo> unselectedUser(String stationGroupId);

    /**
     * 获取站点用户数据，已选择和未选择
     *
     * @param stationGroupId
     * @param departmentId
     * @return
     */
    Map<String, Object> getStationGroupUser(String stationGroupId, String departmentId);

    /**
     * 翻页检索站点用户列表
     *
     * @param stationGroupUserVo
     * @return
     */
    IPage<StationGroupUserVo> pageStationGroupUser(StationGroupUserVo stationGroupUserVo);
}
