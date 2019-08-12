package com.deyatech.monitor.service.impl;

import com.deyatech.monitor.entity.SiteManager;
import com.deyatech.monitor.vo.SiteManagerVo;
import com.deyatech.monitor.mapper.SiteManagerMapper;
import com.deyatech.monitor.service.SiteManagerService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class SiteManagerServiceImpl extends BaseServiceImpl<SiteManagerMapper, SiteManager> implements SiteManagerService {

    @Autowired
    SiteManagerMapper siteManagerMapper;

    /**
     * 单个将对象转换为vo
     *
     * @param siteManager
     * @return
     */
    @Override
    public SiteManagerVo setVoProperties(SiteManager siteManager){
        SiteManagerVo siteManagerVo = new SiteManagerVo();
        BeanUtil.copyProperties(siteManager, siteManagerVo);
        return siteManagerVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param siteManagers
     * @return
     */
    @Override
    public List<SiteManagerVo> setVoProperties(Collection siteManagers){
        List<SiteManagerVo> siteManagerVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(siteManagers)) {
            for (Object siteManager : siteManagers) {
                SiteManagerVo siteManagerVo = new SiteManagerVo();
                BeanUtil.copyProperties(siteManager, siteManagerVo);
                siteManagerVos.add(siteManagerVo);
            }
        }
        return siteManagerVos;
    }

    @Override
    public void setSiteUsers(String siteId, List<String> userIds) {
        SiteManager siteUser = new SiteManager();
        siteUser.setSiteId(siteId);
        this.removeByBean(siteUser);
        if (CollectionUtil.isNotEmpty(userIds)) {
            List<SiteManager> list = new ArrayList<>();
            for (String userId : userIds) {
                SiteManager ur = new SiteManager();
                ur.setSiteId(siteId);
                ur.setManagerId(userId);
                list.add(ur);
            }
            this.saveOrUpdateBatch(list);
        }
    }



    @Override
    public void setUserSites(String userId, List<String> siteIds) {
        SiteManager siteUser = new SiteManager();
        siteUser.setManagerId(userId);
        this.removeByBean(siteUser);
        if (CollectionUtil.isNotEmpty(siteIds)) {
            List<SiteManager> list = new ArrayList<>();
            for (String siteId : siteIds) {
                SiteManager ur = new SiteManager();
                ur.setSiteId(siteId);
                ur.setManagerId(userId);
                list.add(ur);
            }
            this.saveOrUpdateBatch(list);
        }
    }

    @Override
    public void deleteSiteManagerByManagerId(List<String> managerIds) {
        siteManagerMapper.deleteSiteManagerByManagerId(managerIds);
    }

    @Override
    public void deleteSiteManagerBySiteId(List<String> siteIds) {
        siteManagerMapper.deleteSiteManagerBySiteId(siteIds);
    }
}
