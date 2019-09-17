package com.deyatech.generate.service;

import com.deyatech.common.base.BaseService;
import com.deyatech.generate.entity.PageCatalog;
import com.deyatech.generate.vo.PageCatalogVo;

import java.util.Collection;
import java.util.List;
/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/9/10 18:19
 */
public interface PageCatalogService extends BaseService<PageCatalog> {

    /**
     * 单个将对象转换为vo页面管理
     *
     * @param pageCatalog
     * @return
     */
    PageCatalogVo setVoProperties(PageCatalog pageCatalog);

    /**
     * 批量将对象转换为vo页面管理
     *
     * @param pageCatalogs
     * @return
     */
    List<PageCatalogVo> setVoProperties(Collection pageCatalogs);


    void updatePageCatalogById(String pageId,List<String> ids);
}
