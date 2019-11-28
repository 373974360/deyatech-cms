package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 11:42
 */

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.template.thymeleaf.utils.PageUtil;
import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 模板内置对象 - 内容工具
 */
@Component(TemplateConstants.TEMPLATE_OBJ_INFO_UTIL)
public class InfoDataExpressionObject {

    @Autowired
    StationFeign stationFeign;


    /**
     * 获取新闻列表
     *
     * @param maps 搜索条件
     * @param page 页码
     * @param pageSize 每页条数
     *
     * @return IPage<TemplateVo>
     * */
    public IPage<TemplateVo> getInfoList(Map<String,Object> maps, Integer page, Integer pageSize){
        if (page == null || page < 0) {
            page = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        IPage<TemplateVo> result = stationFeign.getTemplateListView(maps,page,pageSize).getData();

        PageUtil<TemplateVo> pageUtil = new PageUtil<>();
        pageUtil.setCurrent(result.getCurrent());
        pageUtil.setPages(result.getPages());
        pageUtil.setTotal(result.getTotal());
        pageUtil.setSize(result.getSize());
        pageUtil.setRecords(result.getRecords());

        Collection<CatalogVo> catalogVoCollection = stationFeign.getCatalogTreeBySiteId(maps.get("siteId").toString()).getData();
        CatalogVo catalogVo = getCatalog(catalogVoCollection,maps.get("catId").toString());

        pageUtil.setFirstPageUrl(catalogVo.getLinkUrl());
        pageUtil.setEndPageUrl("/"+catalogVo.getPathName()+"/list/"+pageUtil.getPages()+".jhtml");
        pageUtil.setCurrPageUrl("/"+catalogVo.getPathName()+"/list/"+pageUtil.getCurrent()+".jhtml");
        String preUrl;
        if(pageUtil.getCurrent() <= 1){
            preUrl = "/"+catalogVo.getPathName()+"/list/1.jhtml";
        }else{
            preUrl = "/"+catalogVo.getPathName()+"/list/"+(pageUtil.getCurrent()-1)+".jhtml";
        }
        pageUtil.setPrePageUrl(preUrl);

        String nextUrl;
        if(pageUtil.getPages() == pageUtil.getCurrent()){
            nextUrl = "/"+catalogVo.getPathName()+"/list/"+pageUtil.getPages()+".jhtml";
        }else{
            nextUrl = "/"+catalogVo.getPathName()+"/list/"+(pageUtil.getCurrent()+1)+".jhtml";
        }
        pageUtil.setPrePageUrl(preUrl);
        pageUtil.setNextPageUrl(nextUrl);

        List<String> pageUrlList = new ArrayList<>();
        if(pageUtil.getPages()>0){
            for(int i=0;i<pageUtil.getPages();i++){
                pageUrlList.add("/"+catalogVo.getPathName()+"/list/"+(i+1)+".jhtml");
            }
        }
        pageUtil.setPageList(pageUrlList);
        return pageUtil;
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
