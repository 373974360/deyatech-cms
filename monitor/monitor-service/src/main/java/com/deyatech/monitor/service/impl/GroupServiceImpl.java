package com.deyatech.monitor.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.deyatech.monitor.entity.Group;
import com.deyatech.monitor.vo.GroupVo;
import com.deyatech.monitor.mapper.GroupMapper;
import com.deyatech.monitor.service.GroupService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-07-29
 */
@Service
public class GroupServiceImpl extends BaseServiceImpl<GroupMapper, Group> implements GroupService {

    @Autowired
    GroupMapper groupMapper;

    /**
     * 单个将对象转换为vo
     *
     * @param group
     * @return
     */
    @Override
    public GroupVo setVoProperties(Group group){
        GroupVo groupVo = new GroupVo();
        BeanUtil.copyProperties(group, groupVo);
        return groupVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param groups
     * @return
     */
    @Override
    public List<GroupVo> setVoProperties(Collection groups){
        List<GroupVo> groupVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(groups)) {
            for (Object group : groups) {
                GroupVo groupVo = new GroupVo();
                BeanUtil.copyProperties(group, groupVo);
                groupVos.add(groupVo);
            }
        }
        return groupVos;
    }

    @Override
    public List<Group> getGroupList(String currTime) {
        return groupMapper.getGroupList(currTime);
    }
}
