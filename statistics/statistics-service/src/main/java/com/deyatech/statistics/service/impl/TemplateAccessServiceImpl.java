package com.deyatech.statistics.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.statistics.entity.TemplateAccess;
import com.deyatech.statistics.vo.TemplateAccessVo;
import com.deyatech.statistics.mapper.TemplateAccessMapper;
import com.deyatech.statistics.service.TemplateAccessService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2020-01-19
 */
@Service
public class TemplateAccessServiceImpl extends BaseServiceImpl<TemplateAccessMapper, TemplateAccess> implements TemplateAccessService {

    @Autowired
    StationFeign stationFeign;


    /**
     * 单个将对象转换为vo
     *
     * @param templateAccess
     * @return
     */
    @Override
    public TemplateAccessVo setVoProperties(TemplateAccess templateAccess){
        TemplateAccessVo templateAccessVo = new TemplateAccessVo();
        BeanUtil.copyProperties(templateAccess, templateAccessVo);
        return templateAccessVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param templateAccesss
     * @return
     */
    @Override
    public List<TemplateAccessVo> setVoProperties(Collection templateAccesss){
        List<TemplateAccessVo> templateAccessVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(templateAccesss)) {
            for (Object templateAccess : templateAccesss) {
                TemplateAccessVo templateAccessVo = new TemplateAccessVo();
                BeanUtil.copyProperties(templateAccess, templateAccessVo);
                templateAccessVos.add(templateAccessVo);
            }
        }
        return templateAccessVos;
    }

    @Override
    public IPage<TemplateAccessVo> getAccessCountByCatalog(TemplateAccessVo templateAccessVo) {
        if(StrUtil.isNotBlank(templateAccessVo.getCatId())){
            List<String> catalogIdList = new ArrayList<>();
            Collection<CatalogVo> catalogList = getCatalogChildrenTree(templateAccessVo.getSiteId(),templateAccessVo.getCatId());
            // 加入当前栏目的子栏目ID
            catalogIdList.add(templateAccessVo.getCatId());
            getCatalogChildrenIds(catalogList,catalogIdList);
            templateAccessVo.setCatalogIdList(catalogIdList);
        }
        Page<TemplateAccessVo> pages = new Page();
        pages.setCurrent(templateAccessVo.getPage());
        pages.setSize(templateAccessVo.getSize());
        int totle = baseMapper.getAccessCountByCatalogCount(templateAccessVo);
        pages.setTotal(totle);
        if (totle > 0) {
            templateAccessVo.setPage((pages.getCurrent() - 1) * pages.getSize());
            List<TemplateAccessVo> list = baseMapper.getAccessCountByCatalog(templateAccessVo);
            pages.setRecords(list);
        }
        return pages;
    }

    @Override
    public IPage<TemplateAccessVo> getAccessCountByInfo(TemplateAccessVo templateAccessVo) {
        if(StrUtil.isNotBlank(templateAccessVo.getCatId())){
            List<String> catalogIdList = new ArrayList<>();
            Collection<CatalogVo> catalogList = getCatalogChildrenTree(templateAccessVo.getSiteId(),templateAccessVo.getCatId());
            // 加入当前栏目的子栏目ID
            catalogIdList.add(templateAccessVo.getCatId());
            getCatalogChildrenIds(catalogList,catalogIdList);
            templateAccessVo.setCatalogIdList(catalogIdList);
        }
        Page<TemplateAccessVo> pages = new Page();
        pages.setCurrent(templateAccessVo.getPage());
        pages.setSize(templateAccessVo.getSize());
        int totle = baseMapper.getAccessCountByInfoCount(templateAccessVo);
        pages.setTotal(totle);
        if (totle > 0) {
            templateAccessVo.setPage((pages.getCurrent() - 1) * pages.getSize());
            List<TemplateAccessVo> list = baseMapper.getAccessCountByInfo(templateAccessVo);
            pages.setRecords(list);
        }
        return pages;
    }

    /**
     * 根据条件获取站点栏目
     * @param siteId 站点ID
     * @param parentId 父节点ID
     * @return
     */
    public Collection<CatalogVo> getCatalogChildrenTree(String siteId,String parentId) {
        Collection<CatalogVo> catalogVoCollection = stationFeign.getCatalogTreeBySiteId(siteId).getData();
        Collection<CatalogVo> reslut = CollectionUtil.newArrayList();
        return getCatalogChildrenTree(catalogVoCollection,reslut,parentId);
    }
    public Collection<CatalogVo> getCatalogChildrenTree(Collection<CatalogVo> catalogVoCollection,Collection<CatalogVo> reslut,String parentId){
        if(parentId.equals("0")){
            return catalogVoCollection;
        }
        for(CatalogVo catalogVo:catalogVoCollection){
            if(catalogVo.getId().equals(parentId)){
                reslut = catalogVo.getChildren();
            }else if(CollectionUtil.isNotEmpty(catalogVo.getChildren())){
                reslut = getCatalogChildrenTree(catalogVo.getChildren(),reslut,parentId);
            }
        }
        return reslut;
    }
    public void getCatalogChildrenIds(Collection<CatalogVo> catalogVos,List<String> ids){
        if(CollectionUtil.isNotEmpty(catalogVos)){
            for(CatalogVo catalogVo:catalogVos){
                ids.add(catalogVo.getId());
                if(CollectionUtil.isNotEmpty(catalogVo.getChildren())){
                    getCatalogChildrenIds(catalogVo.getChildren(),ids);
                }
            }
        }
    }
}
