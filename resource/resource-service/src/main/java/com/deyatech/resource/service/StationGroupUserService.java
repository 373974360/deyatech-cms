package com.deyatech.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.resource.entity.StationGroupUser;
import com.deyatech.resource.vo.StationGroupUserVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

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
     * 设置用户站点
     *
     * @param userId
     * @param stationGroupIds
     */
    void setUserStationGroups(String userId, List<String> stationGroupIds);

    /**
     * 所有用户信息
     *
     * @param stationGroupUserVo
     * @return
     */
    IPage<StationGroupUserVo> pageByStationGroupUserVo(StationGroupUserVo stationGroupUserVo);

    /**
     * 删除站点用户关联根据站点编号
     *
     * @param stationGroupIds
     * @return
     */
    int removeByStationGroupId(List<String> stationGroupIds);
}
