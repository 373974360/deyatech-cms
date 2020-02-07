package com.deyatech.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
                String[] idtype = userId.split("_");
                StationGroupUser sgu = new StationGroupUser();
                sgu.setStationGroupId(stationGroupId);
                sgu.setUserId(idtype[0]);
                sgu.setType(Integer.parseInt(idtype[1]));
                list.add(sgu);
            }
            this.saveOrUpdateBatch(list);
        }
    }

    /**
     * 设置站点部门用户
     *
     * @param stationGroupId
     * @param departmentId
     * @param type
     */
    @Override
    public void setStationGroupDepartmentUsers(String stationGroupId, String departmentId, int type) {
        StationGroupUser stationGroupUser = new StationGroupUser();
        stationGroupUser.setStationGroupId(stationGroupId);
        // 根据站点ID删除
        this.removeByBean(stationGroupUser);
        // 站点部门及子部门所有的用户
        List<StationGroupUserVo> selectedUserList = baseMapper.departmentAndSubsidiaryDepartmentUser(departmentId);
        if (CollectionUtil.isNotEmpty(selectedUserList)) {
            List<StationGroupUser> list = new ArrayList<>();
            for (StationGroupUserVo user : selectedUserList) {
                StationGroupUser sgu = new StationGroupUser();
                sgu.setStationGroupId(stationGroupId);
                sgu.setUserId(user.getUserId());
                sgu.setType(type);
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
     * @param departmentId
     * @return
     */
    @Override
    public Map<String, Object> getStationGroupUser(String stationGroupId, String departmentId) {
        // 部门名称映射
        Map<String, String> departmentNameMap = this.getDepartmentNameMap();
        Map<String, Object> data = new HashMap<>();
        // 已选择用户
        List<StationGroupUserVo> selectedUserList = this.selectedUser(stationGroupId);
        if (CollectionUtil.isNotEmpty(selectedUserList)) {
            // 站点部门及子部门所有的用户
            List<StationGroupUserVo> departmentUserList = baseMapper.departmentAndSubsidiaryDepartmentUser(departmentId);
            if (CollectionUtil.isNotEmpty(departmentUserList)) {
                selectedUserList.stream().parallel().forEach(sgu -> {
                    boolean exists = departmentUserList.stream().parallel().anyMatch(u -> u.getUserId().equals(sgu.getUserId()));
                    sgu.setSelectable(!exists);
                });
            }
        }
        setUserTreePositionName(selectedUserList, departmentNameMap);
        data.put("selectedUserList", selectedUserList);

        // 未选择用户
        List<StationGroupUserVo> unselectedUserList = this.unselectedUser(stationGroupId);
        setUserTreePositionName(unselectedUserList, departmentNameMap);
        data.put("unselectedUserList", unselectedUserList);
        return data;
    }

    /**
     * 设置用户部门层级
     *
     * @param list
     * @param departmentNameMap
     */
    private void setUserTreePositionName(List<StationGroupUserVo> list, Map<String, String> departmentNameMap) {
        if (CollectionUtil.isEmpty(list) || Objects.isNull(departmentNameMap)) {
            return;
        }
        list.stream().parallel().forEach(item -> {
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
        });
    }

    /**
     * 部门ID和名称映射
     *
     * @return
     */
    private Map<String, String> getDepartmentNameMap() {
        // 部门名称映射
        Map<String, String> departmentNameMap = new HashMap<>();
        List<Department> departmentList = adminFeign.getAllDepartments().getData();
        if (CollectionUtil.isNotEmpty(departmentList)) {
            departmentNameMap = departmentList.stream().collect(Collectors.toMap(Department::getId, Department::getName));
        }
        return departmentNameMap;
    }

    /**
     * 翻页检索站点用户列表
     *
     * @param stationGroupUserVo
     * @return
     */
    @Override
    public IPage<StationGroupUserVo> pageStationGroupUser(StationGroupUserVo stationGroupUserVo) {
        Map<String, String> departmentNameMap = this.getDepartmentNameMap();
        IPage<StationGroupUserVo> page = baseMapper.pageStationGroupUser(this.getPageByBean(stationGroupUserVo), stationGroupUserVo);
        List<StationGroupUserVo> list = page.getRecords();
        setUserTreePositionName(list, departmentNameMap);
        return page;
    }

    /**
     * 获取站点分配的用户树
     *
     * @param siteId
     * @return
     */
    @Override
    public List<StationGroupUserVo> getUserTreeBySiteId(String siteId) {
        List<StationGroupUserVo> tree = new ArrayList<>();
        List<StationGroupUserVo> list = baseMapper.selectedUser(siteId);
        if (CollectionUtil.isNotEmpty(list)) {
            Map<String, String> idNameMap = this.getDepartmentNameMap();
            for (StationGroupUserVo user: list) {
                idNameMap.put(user.getUserId(), user.getName());
                String[] level = (user.getUserTreePositionId().substring(1) + "&" + user.getUserId()).split("&");
                addUser(level, tree, idNameMap);
            }
        }
        return tree;
    }

    private void addUser(String[] level, List<StationGroupUserVo> children, Map<String, String> idNameMap) {
        if (level != null) {
            StationGroupUserVo node = null;
            for (String id : level) {
                node = addNode(idNameMap.get(id), id, children);
                children = node.getChildren();
            }
            if (Objects.nonNull(node)) {
                node.setChildren(null);
            }
        }
    }

    private StationGroupUserVo addNode(String label, String value, List<StationGroupUserVo> children) {
        for (StationGroupUserVo node : children) {
            if (value.equals(node.getValue()))
                return node;
        }
        StationGroupUserVo node = new StationGroupUserVo();
        node.setLabel(label);
        node.setValue(value);
        node.setChildren(new ArrayList<>());
        children.add(node);
        return node;
    }

    /**
     * 是否站点管理员
     *
     * @param stationGroupId
     * @param userId
     * @return
     */
    @Override
    public boolean isSiteAdmin(String stationGroupId, String userId) {
        int count = baseMapper.countStationGroupAdmin(stationGroupId, userId);
        return count > 0 ? true : false;
    }
}
