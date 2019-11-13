package com.deyatech.station.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.station.vo.ResourceManagementVo;
import com.deyatech.station.vo.TemplateVo;

import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-13
 */
public interface ResourceManagementService {

    /**
     * 翻页检索资源
     *
     * @param resource
     * @return
     */
    IPage<TemplateVo> pageByResourceManagement(ResourceManagementVo resource);

    /**
     * 删除内容资源
     *
     * @param templateIds
     * @return
     */
    int deleteBytemplateIds(List<String> templateIds);
}
