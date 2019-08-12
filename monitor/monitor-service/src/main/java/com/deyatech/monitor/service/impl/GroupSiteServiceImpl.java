package com.deyatech.monitor.service.impl;

import com.deyatech.monitor.entity.GroupSite;
import com.deyatech.monitor.vo.GroupSiteVo;
import com.deyatech.monitor.mapper.GroupSiteMapper;
import com.deyatech.monitor.service.GroupSiteService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class GroupSiteServiceImpl extends BaseServiceImpl<GroupSiteMapper, GroupSite> implements GroupSiteService {

    @Autowired
    GroupSiteMapper groupSiteMapper;

    /**
     * 单个将对象转换为vo
     *
     * @param groupSite
     * @return
     */
    @Override
    public GroupSiteVo setVoProperties(GroupSite groupSite){
        GroupSiteVo groupSiteVo = new GroupSiteVo();
        BeanUtil.copyProperties(groupSite, groupSiteVo);
        return groupSiteVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param groupSites
     * @return
     */
    @Override
    public List<GroupSiteVo> setVoProperties(Collection groupSites){
        List<GroupSiteVo> groupSiteVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(groupSites)) {
            for (Object groupSite : groupSites) {
                GroupSiteVo groupSiteVo = new GroupSiteVo();
                BeanUtil.copyProperties(groupSite, groupSiteVo);
                groupSiteVos.add(groupSiteVo);
            }
        }
        return groupSiteVos;
    }

    @Override
    public void setGroupSites(String groupId, List<String> siteIds) {
        GroupSite groupSite = new GroupSite();
        groupSite.setGroupId(groupId);
        this.removeByBean(groupSite);
        if (CollectionUtil.isNotEmpty(siteIds)) {
            List<GroupSite> list = new ArrayList<>();
            for (String siteId : siteIds) {
                GroupSite gs = new GroupSite();
                gs.setGroupId(groupId);
                gs.setSiteId(siteId);
                list.add(gs);
            }
            this.saveOrUpdateBatch(list);
        }
    }

    @Override
    public void deleteGroupSiteByGroupId(List<String> managerIds) {
        groupSiteMapper.deleteGroupSiteByGroupId(managerIds);
    }

    @Override
    public void deleteGroupSiteBySiteId(List<String> siteIds) {
        groupSiteMapper.deleteGroupSiteBySiteId(siteIds);
    }
}
