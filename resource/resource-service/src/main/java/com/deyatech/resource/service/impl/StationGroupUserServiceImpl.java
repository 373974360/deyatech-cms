package com.deyatech.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.entity.StationGroupUser;
import com.deyatech.resource.mapper.StationGroupUserMapper;
import com.deyatech.resource.service.StationGroupService;
import com.deyatech.resource.service.StationGroupUserService;
import com.deyatech.resource.vo.StationGroupUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 站点用户关联 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-09-12
 */
@Service
public class StationGroupUserServiceImpl extends BaseServiceImpl<StationGroupUserMapper, StationGroupUser> implements StationGroupUserService {

    @Autowired
    AdminFeign adminFeign;
    @Autowired
    StationGroupService stationGroupService;

    /**
     * 单个将对象转换为vo站点用户关联
     *
     * @param stationGroupUser
     * @return
     */
    @Override
    public StationGroupUserVo setVoProperties(StationGroupUser stationGroupUser){
        StationGroupUserVo stationGroupUserVo = new StationGroupUserVo();
        BeanUtil.copyProperties(stationGroupUser, stationGroupUserVo);
        return stationGroupUserVo;
    }

    /**
     * 批量将对象转换为vo站点用户关联
     *
     * @param stationGroupUsers
     * @return
     */
    @Override
    public List<StationGroupUserVo> setVoProperties(Collection stationGroupUsers){
        List<StationGroupUserVo> stationGroupUserVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(stationGroupUsers)) {
            for (Object stationGroupUser : stationGroupUsers) {
                StationGroupUserVo stationGroupUserVo = new StationGroupUserVo();
                BeanUtil.copyProperties(stationGroupUser, stationGroupUserVo);
                stationGroupUserVos.add(stationGroupUserVo);
            }
        }
        return stationGroupUserVos;
    }

    /**
     * 设置站点用户
     *
     * @param stationGroupId
     * @param userIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setStationGroupUsers(String stationGroupId, List<String> userIds) {
        StationGroupUser stationGroupUser = new StationGroupUser();
        stationGroupUser.setStationGroupId(stationGroupId);
        // 根据站点ID删除
        this.removeByBean(stationGroupUser);
        if (CollectionUtil.isNotEmpty(userIds)) {
            List<StationGroupUser> list = new ArrayList<>();
            for (String userId : userIds) {
                StationGroupUser sgu = new StationGroupUser();
                sgu.setStationGroupId(stationGroupId);
                sgu.setUserId(userId);
                list.add(sgu);
            }
            this.saveOrUpdateBatch(list);
        }
    }

    /**
     * 删除站点用户关联根据站点编号
     *
     * @param stationSroupId
     * @return
     */
    @Override
    public int removeByStationGroupId(String stationSroupId) {
        return baseMapper.removeByStationGroupId(stationSroupId);
    }

    /**
     * 已选择的用户
     *
     * @param stationGroupId
     * @return
     */
    @Override
    public List<StationGroupUserVo> selectedUser(String stationGroupId) {
        return baseMapper.selectedUser(stationGroupId);
    }

    /**
     * 未选择的用户
     *
     * @param stationGroupId
     * @return
     */
    @Override
    public List<StationGroupUserVo> unselectedUser(String stationGroupId) {
        return baseMapper.unselectedUser(stationGroupId);
    }

    /**
     * 获取站点用户数据，已选择和未选择
     *
     * @param stationGroupId
     * @return
     */
    @Override
    public Map<String, Object> getStationGroupUser(String stationGroupId) {
        StationGroup stationGroup = stationGroupService.getById(stationGroupId);
        // 部门名称映射
        Map<String, String> departmentNameMap = new HashMap<>();
        List<Department> departmentList = adminFeign.getAllDepartments().getData();
        if (CollectionUtil.isNotEmpty(departmentList)) {
            departmentNameMap = departmentList.stream().collect(Collectors.toMap(Department::getId, Department::getName));
        }
        Map<String, Object> data = new HashMap<>();
        boolean selectDefault = false;
        // 已选择用户
        List<StationGroupUserVo> selectedUserList = baseMapper.selectedUser(stationGroupId);
        if (CollectionUtil.isEmpty(selectedUserList)) {
            selectDefault = true;
            // 站点部门及子部门所有的用户
            selectedUserList = baseMapper.departmentAndSubsidiaryDepartmentUser(stationGroup.getDepartmentId());
        }
        setUserTreePositionName(selectedUserList, departmentNameMap);
        data.put("selectedUserList", selectedUserList);

        // 未选择用户
        List<StationGroupUserVo> unselectedUserList = selectDefault ? baseMapper.unselectedUserDefault(stationGroup.getDepartmentId()) : baseMapper.unselectedUser(stationGroupId);
        setUserTreePositionName(unselectedUserList, departmentNameMap);
        data.put("unselectedUserList", unselectedUserList);
        return data;
    }

    private void setUserTreePositionName(List<StationGroupUserVo> list, Map<String, String> departmentNameMap) {
        if (CollectionUtil.isEmpty(list) || Objects.isNull(departmentNameMap)) {
            return;
        }
        for(StationGroupUserVo item : list) {
            StringBuilder userTreePositionName = new StringBuilder();
            String ids[] = item.getUserTreePositionId().substring(1).split("&");
            if (Objects.nonNull(ids)) {
                for (String id : ids) {
                    if (Objects.nonNull(departmentNameMap.get(id))) {
                        userTreePositionName.append("＼");
                        userTreePositionName.append(departmentNameMap.get(id));
                    }
                }
            }
            item.setUserTreePositionName(userTreePositionName.toString());
        }
    }
}
