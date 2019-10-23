package com.deyatech.resource.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.resource.entity.StationGroupUser;
import com.deyatech.resource.vo.StationGroupUserVo;
import com.deyatech.resource.mapper.StationGroupUserMapper;
import com.deyatech.resource.service.StationGroupUserService;
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
 * 站点用户关联 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-09-12
 */
@Service
public class StationGroupUserServiceImpl extends BaseServiceImpl<StationGroupUserMapper, StationGroupUser> implements StationGroupUserService {

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
     * 设置用户站点
     *
     * @param userId
     * @param stationGroupIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setUserStationGroups(String userId, List<String> stationGroupIds) {
        StationGroupUser stationGroupUser = new StationGroupUser();
        stationGroupUser.setUserId(userId);
        // 根据用户ID删除
        this.removeByBean(stationGroupUser);
        if (CollectionUtil.isNotEmpty(stationGroupIds)) {
            List<StationGroupUser> list = new ArrayList<>();
            for (String stationGroupId : stationGroupIds) {
                StationGroupUser sgu = new StationGroupUser();
                sgu.setUserId(userId);
                sgu.setStationGroupId(stationGroupId);
                list.add(sgu);
            }
            this.saveOrUpdateBatch(list);
        }
    }

    /**
     * 站点关联的所有用户信息
     *
     * @param stationGroupUserVo
     * @return
     */
    @Override
    public IPage<StationGroupUserVo> pageByStationGroupUserVo(StationGroupUserVo stationGroupUserVo) {
        return baseMapper.pageByStationGroupUserVo(getPageByBean(stationGroupUserVo), stationGroupUserVo);
    }

    /**
     * 删除站点用户关联根据站点编号
     *
     * @param stationGroupIds
     * @return
     */
    @Override
    public int removeByStationGroupId(List<String> stationGroupIds) {
        return baseMapper.removeByStationGroupId(stationGroupIds);
    }
}
