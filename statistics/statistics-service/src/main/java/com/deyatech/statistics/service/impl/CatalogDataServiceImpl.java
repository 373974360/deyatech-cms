package com.deyatech.statistics.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.statistics.mapper.CatalogDataMapper;
import com.deyatech.statistics.mapper.SiteDataMapper;
import com.deyatech.statistics.service.CatalogDataService;
import com.deyatech.statistics.service.SiteDataService;
import com.deyatech.statistics.vo.CatalogDataVo;
import com.deyatech.statistics.vo.SiteDataVo;
import com.deyatech.statistics.vo.UserDataQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/2/5 15:15
 */
@Slf4j
@Service
public class CatalogDataServiceImpl implements CatalogDataService {

    @Autowired
    CatalogDataMapper catalogDataMapper;
    @Autowired
    StationFeign stationFeign;

    @Override
    public List<CatalogDataVo> getCatalogCountList(UserDataQueryVo queryVo) {
        List<CatalogDataVo> result = CollectionUtil.newArrayList();
        List<CatalogDataVo> catalogDataVoList = catalogDataMapper.getCatalogCountList(queryVo);
        Collection<CatalogVo> catalogVoCollection = getCatalogChildrenTree(queryVo.getSiteId(),queryVo.getCatalogId());
        if(CollectionUtil.isNotEmpty(catalogVoCollection)){
            for(CatalogVo catalogVo:catalogVoCollection){
                CatalogDataVo catalogDataVo = new CatalogDataVo();
                catalogDataVo.setCatId(catalogVo.getId());
                catalogDataVo.setCatName(catalogVo.getName());
                catalogDataVo.setCount(sumCount(0,catalogVo,catalogDataVoList));
                catalogDataVo.setPubCount(sumPubCount(0,catalogVo,catalogDataVoList));
                //发稿率
                catalogDataVo.setReleaseRate();
                //日平均发稿量
                catalogDataVo.setReleaseAverage(queryVo.getStartTime(),queryVo.getEndTime());
                result.add(catalogDataVo);
            }
        }
        return result;
    }

    private int sumCount(int count,CatalogVo catalogVo,List<CatalogDataVo> catalogDataVoList){
        if(CollectionUtil.isNotEmpty(catalogVo.getChildren())){
            for(CatalogVo catalogVos:catalogVo.getChildren()){
                count += sumCount(count,catalogVos,catalogDataVoList);
            }
        }else{
            for(CatalogDataVo catalogDataVo:catalogDataVoList){
                if(catalogDataVo.getCatId().equals(catalogVo.getId())){
                    count += catalogDataVo.getCount();
                }
            }
        }
        return count;
    }

    private int sumPubCount(int count,CatalogVo catalogVo,List<CatalogDataVo> catalogDataVoList){
        if(CollectionUtil.isNotEmpty(catalogVo.getChildren())){
            for(CatalogVo catalogVos:catalogVo.getChildren()){
                count += sumCount(count,catalogVos,catalogDataVoList);
            }
        }else{
            for(CatalogDataVo catalogDataVo:catalogDataVoList){
                if(catalogDataVo.getCatId().equals(catalogVo.getId())){
                    count += catalogDataVo.getPubCount();
                }
            }
        }
        return count;
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
        for(CatalogVo catalogVo : catalogVoCollection){
            if(catalogVo.getId().equals(parentId)){
                if(CollectionUtil.isNotEmpty(catalogVo.getChildren())){
                    reslut = catalogVo.getChildren();
                }else{
                    Collection<CatalogVo> list = CollectionUtil.newArrayList();
                    list.add(catalogVo);
                    reslut = list;
                }
            }else if(CollectionUtil.isNotEmpty(catalogVo.getChildren())){
                reslut = getCatalogChildrenTree(catalogVo.getChildren(),reslut,parentId);
            }
        }
        return reslut;
    }
}
