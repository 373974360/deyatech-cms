package com.deyatech.resource.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.entity.User;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.resource.entity.StationGroupUser;
import com.deyatech.resource.vo.StationGroupUserVo;
import org.apache.ibatis.annotations.Param;

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
//    List<StationGroupUserVo> unselectedUserDefault(String departmentId);

    /**
     * 部门及子部门所有的用户
     *
     * @param departmentId
     * @return
     */
    List<StationGroupUserVo> departmentAndSubsidiaryDepartmentUser(String departmentId);

    /**
     * 删除站点关联的用户
     *
     * @param stationSroupId
     * @return
     */
    int removeByStationGroupId(String stationSroupId);

    /**
     * 翻页检索站点用户列表
     * 
     * @param page
     * @param stationGroupUserVo
     * @return
     */
    IPage<StationGroupUserVo> pageStationGroupUser(@Param("page") IPage page, @Param("stationGroupUserVo") StationGroupUserVo stationGroupUserVo);

    /**
     * 站点管理员
     *
     * @param stationGroupId
     * @param userId
     * @return
     */
    int countStationGroupAdmin(@Param("stationGroupId") String stationGroupId, @Param("userId") String userId);

}
