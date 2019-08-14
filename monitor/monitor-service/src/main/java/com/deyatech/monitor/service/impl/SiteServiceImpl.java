package com.deyatech.monitor.service.impl;

import com.deyatech.monitor.entity.Site;
import com.deyatech.monitor.vo.SiteVo;
import com.deyatech.monitor.mapper.SiteMapper;
import com.deyatech.monitor.service.SiteService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 监控配置表 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-07-29
 */
@Service
public class SiteServiceImpl extends BaseServiceImpl<SiteMapper, Site> implements SiteService {

    /**
     * 单个将对象转换为vo监控配置表
     *
     * @param site
     * @return
     */
    @Override
    public SiteVo setVoProperties(Site site){
        SiteVo siteVo = new SiteVo();
        BeanUtil.copyProperties(site, siteVo);
        return siteVo;
    }

    /**
     * 批量将对象转换为vo监控配置表
     *
     * @param sites
     * @return
     */
    @Override
    public List<SiteVo> setVoProperties(Collection sites){
        List<SiteVo> siteVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(sites)) {
            for (Object site : sites) {
                SiteVo siteVo = new SiteVo();
                BeanUtil.copyProperties(site, siteVo);
                siteVos.add(siteVo);
            }
        }
        return siteVos;
    }
}
