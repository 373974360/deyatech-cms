package com.deyatech.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.station.entity.PageCatalog;
import com.deyatech.station.mapper.PageCatalogMapper;
import com.deyatech.station.service.PageCatalogService;
import com.deyatech.station.vo.PageCatalogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/9/10 18:19
 */
@Service
public class PageCatalogServiceImpl extends BaseServiceImpl<PageCatalogMapper, PageCatalog> implements PageCatalogService {

    @Autowired
    PageCatalogMapper pageCatalogMapper;

    /**
     * 单个将对象转换为vo页面管理
     *
     * @param pageCatalog
     * @return
     */
    @Override
    public PageCatalogVo setVoProperties(PageCatalog pageCatalog){
        PageCatalogVo pageCatalogVo = new PageCatalogVo();
        BeanUtil.copyProperties(pageCatalog, pageCatalogVo);
        return pageCatalogVo;
    }

    /**
     * 批量将对象转换为vo页面管理
     *
     * @param pageCatalogs
     * @return
     */
    @Override
    public List<PageCatalogVo> setVoProperties(Collection pageCatalogs){
        List<PageCatalogVo> pageCatalogVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(pageCatalogs)) {
            for (Object pageCatalog : pageCatalogs) {
                PageCatalogVo pageCatalogVo = new PageCatalogVo();
                BeanUtil.copyProperties(pageCatalog, pageCatalogVo);
                pageCatalogVos.add(pageCatalogVo);
            }
        }
        return pageCatalogVos;
    }

    @Override
    public void updatePageCatalogById(String pageId,List<String> ids) {
        pageCatalogMapper.clearPageCatalogById(pageId);
        for(String str:ids){
            PageCatalog pageCatalog = new PageCatalog();
            pageCatalog.setPageId(pageId);
            pageCatalog.setCatId(str);
            saveOrUpdate(pageCatalog);
        }
    }

}
