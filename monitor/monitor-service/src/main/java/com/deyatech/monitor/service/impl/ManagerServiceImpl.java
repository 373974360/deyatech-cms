package com.deyatech.monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.monitor.entity.Manager;
import com.deyatech.monitor.mapper.ManagerMapper;
import com.deyatech.monitor.service.ManagerService;
import com.deyatech.monitor.service.SiteManagerService;
import com.deyatech.monitor.vo.ManagerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-07-29
 */
@Service
public class ManagerServiceImpl extends BaseServiceImpl<ManagerMapper, Manager> implements ManagerService {
    @Autowired
    SiteManagerService siteManagerService;
    /**
     * 单个将对象转换为vo
     *
     * @param manager
     * @return
     */
    @Override
    public ManagerVo setVoProperties(Manager manager){
        ManagerVo managerVo = new ManagerVo();
        BeanUtil.copyProperties(manager, managerVo);
        return managerVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param managers
     * @return
     */
    @Override
    public List<ManagerVo> setVoProperties(Collection managers){
        List<ManagerVo> managerVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(managers)) {
            for (Object manager : managers) {
                ManagerVo managerVo = new ManagerVo();
                BeanUtil.copyProperties(manager, managerVo);
                managerVos.add(managerVo);
            }
        }
        return managerVos;
    }

    /**
     * 翻页检索
     *
     * @param manager
     * @return
     */
    @Override
    public IPage<ManagerVo> pageByManager(Manager manager) {
        return baseMapper.pageByManager(this.getPageByBean(manager), manager);
    }
}
