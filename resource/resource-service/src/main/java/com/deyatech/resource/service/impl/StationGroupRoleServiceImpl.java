package com.deyatech.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.resource.entity.StationGroupRole;
import com.deyatech.resource.vo.StationGroupRoleVo;
import com.deyatech.resource.mapper.StationGroupRoleMapper;
import com.deyatech.resource.service.StationGroupRoleService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 站点角色关联 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-31
 */
@Service
public class StationGroupRoleServiceImpl extends BaseServiceImpl<StationGroupRoleMapper, StationGroupRole> implements StationGroupRoleService {

    /**
     * 单个将对象转换为vo站点角色关联
     *
     * @param stationGroupRole
     * @return
     */
    @Override
    public StationGroupRoleVo setVoProperties(StationGroupRole stationGroupRole){
        StationGroupRoleVo stationGroupRoleVo = new StationGroupRoleVo();
        BeanUtil.copyProperties(stationGroupRole, stationGroupRoleVo);
        return stationGroupRoleVo;
    }

    /**
     * 批量将对象转换为vo站点角色关联
     *
     * @param stationGroupRoles
     * @return
     */
    @Override
    public List<StationGroupRoleVo> setVoProperties(Collection stationGroupRoles){
        List<StationGroupRoleVo> stationGroupRoleVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(stationGroupRoles)) {
            for (Object stationGroupRole : stationGroupRoles) {
                StationGroupRoleVo stationGroupRoleVo = new StationGroupRoleVo();
                BeanUtil.copyProperties(stationGroupRole, stationGroupRoleVo);
                stationGroupRoleVos.add(stationGroupRoleVo);
            }
        }
        return stationGroupRoleVos;
    }

    /**
     * 设置角色站点
     *
     * @param roleId
     * @param stationGroupIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setRoleStationGroups(String roleId, List<String> stationGroupIds) {
        StationGroupRole stationGroupRole = new StationGroupRole();
        stationGroupRole.setRoleId(roleId);
        // 根据角色ID删除
        this.removeByBean(stationGroupRole);
        // 添加角色站点
        if (CollectionUtil.isNotEmpty(stationGroupIds)) {
            List<StationGroupRole> list = new ArrayList<>();
            for (String stationGroupId : stationGroupIds) {
                StationGroupRole sgr = new StationGroupRole();
                sgr.setRoleId(roleId);
                sgr.setStationGroupId(stationGroupId);
                list.add(sgr);
            }
            this.saveOrUpdateBatch(list);
        }
    }

    /**
     * 删除站点角色关联
     *
     * @param StationGroupIds
     */
    @Override
    public void removeByStationGroupId(List<String> StationGroupIds) {
        if (CollectionUtil.isEmpty(StationGroupIds)) return;
        QueryWrapper<StationGroupRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("station_group_id", StationGroupIds);
        super.remove(queryWrapper);
    }
}
