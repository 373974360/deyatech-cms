package com.deyatech.generate.service;

import com.deyatech.generate.entity.Page;
import com.deyatech.generate.vo.PageVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  页面管理 服务类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-01
 */
public interface PageService extends BaseService<Page> {

    /**
     * 单个将对象转换为vo页面管理
     *
     * @param page
     * @return
     */
    PageVo setVoProperties(Page page);

    /**
     * 批量将对象转换为vo页面管理
     *
     * @param pages
     * @return
     */
    List<PageVo> setVoProperties(Collection pages);

    /**
     * 验证当前输入页面路径是否已经存在
     * @param page
     * @return
     */
    String existsPagePath(Page page);
}
