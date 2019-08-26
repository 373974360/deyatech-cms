package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 11:42
 */

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 模板内置对象 - 栏目工具
 */
@Component(TemplateConstants.TEMPLATE_OBJ_CATALOG_UTIL)
public class CatalogExpressionObject {

    @Autowired
    StationFeign stationFeign;

    /**
     * 根据条件获取站点栏目
     * @param siteId 站点ID
     * @return
     */
    public Collection<CatalogVo> getCatalogTree(String siteId) {
        Collection<CatalogVo> catalogVoCollection = stationFeign.getCatalogTreeBySiteId(siteId).getData();
        return catalogVoCollection;
    }


    /**
     * 根据条件获取站点栏目
     * @param siteId 站点ID
     * @param parentId 父节点ID
     * @return
     */
    public Collection<CatalogVo> getCatalogChildrenTree(String siteId,String parentId) {
        Collection<CatalogVo> catalogVoCollection = stationFeign.getCatalogTreeBySiteId(siteId).getData();
        List<CatalogVo> rootCatalogs = CollectionUtil.newArrayList();
        for(CatalogVo catalogVo:catalogVoCollection){
            if(catalogVo.getId().equals(parentId)){
                rootCatalogs = catalogVo.getChildren();
            }
        }
        return rootCatalogs;
    }

    /**
     * 根据条件获取当前位置
     * @param siteId 站点ID
     * @param catalogId 当前栏目ID
     * @return
     */
    public Collection<CatalogVo> getCatalogPosition(String siteId,String catalogId) {
        List<CatalogVo> catalogPosition = CollectionUtil.newArrayList();
        Collection<CatalogVo> catalogVoCollection = stationFeign.getCatalogTreeBySiteId(siteId).getData();
        CatalogVo catalogVo = getCatalog(catalogVoCollection,catalogId);
        if(ObjectUtil.isNotNull(catalogVo)){
            if(StrUtil.isNotBlank(catalogVo.getTreePosition())){
                String temp[] = catalogVo.getTreePosition().substring(1).split("&");
                for(int i=0;i<temp.length-1;i++){
                    catalogPosition.add(getCatalog(catalogVoCollection,temp[i]));
                }
            }
            catalogPosition.add(catalogVo);
        }
        return catalogPosition;
    }

    /**
     * 根据条件获取栏目信息
     * @param siteId 站点ID
     * @param catalogId 当前栏目ID
     * @return
     */
    public CatalogVo getCatalog(String siteId,String catalogId) {
        Collection<CatalogVo> catalogVoCollection = stationFeign.getCatalogTreeBySiteId(siteId).getData();
        CatalogVo catalogVo = getCatalog(catalogVoCollection,catalogId);
        return catalogVo;
    }



    /**
     * 根据条件获取当前栏目的顶级栏目信息
     * @param siteId 站点ID
     * @param catalogId 当前栏目ID
     * @return
     */
    public CatalogVo getRootCatalog(String siteId,String catalogId) {
        CatalogVo resultCatalogVo = null;
        Collection<CatalogVo> catalogVoCollection = stationFeign.getCatalogTreeBySiteId(siteId).getData();
        CatalogVo catalogVo = getCatalog(catalogVoCollection,catalogId);
        if(ObjectUtil.isNotNull(catalogVo)){
            if(StrUtil.isNotBlank(catalogVo.getTreePosition())){
                String temp[] = catalogVo.getTreePosition().substring(1).split("&");
                resultCatalogVo = getCatalog(catalogVoCollection,temp[0]);
            }
        }
        return resultCatalogVo;
    }

    public CatalogVo getCatalog(Collection<CatalogVo> catalogVoCollection,String catalogId ){
        CatalogVo catalogVo = null;
        for(CatalogVo catalogVo1:catalogVoCollection){
            if(catalogVo1.getId().equals(catalogId)){
                catalogVo = catalogVo1;
                break;
            }else if(ObjectUtil.isNotNull(catalogVo1.getChildren())){
                catalogVo = getCatalog(catalogVo1.getChildren(),catalogId);
                if(ObjectUtil.isNotNull(catalogVo)){
                    break;
                }
            }
        }
        return catalogVo;
    }

}
