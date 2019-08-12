package com.deyatech.monitor.service;

import com.deyatech.monitor.entity.Site;
import com.deyatech.monitor.vo.SiteVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  监控配置表 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-07-29
 */
public interface SiteService extends BaseService<Site> {

    /**
     * 单个将对象转换为vo监控配置表
     *
     * @param site
     * @return
     */
    SiteVo setVoProperties(Site site);

    /**
     * 批量将对象转换为vo监控配置表
     *
     * @param sites
     * @return
     */
    List<SiteVo> setVoProperties(Collection sites);
}
