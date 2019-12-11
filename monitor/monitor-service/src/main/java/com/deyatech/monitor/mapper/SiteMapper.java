package com.deyatech.monitor.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.monitor.entity.Site;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.monitor.vo.SiteVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 监控配置表 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-07-29
 */
public interface SiteMapper extends BaseMapper<Site> {
    /**
     * 翻页检索
     *
     * @param page
     * @param site
     * @return
     */
    IPage<SiteVo> pageBySite(@Param("site") IPage<Site> page, @Param("site") Site site);

}
