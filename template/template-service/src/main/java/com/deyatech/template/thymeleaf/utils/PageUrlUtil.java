package com.deyatech.template.thymeleaf.utils;

import com.deyatech.station.vo.CatalogVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/11/28 12:10
 */
public class PageUrlUtil {
    public static PageUtil setUrl(PageUtil pageUtil, CatalogVo catalogVo,String pageSuffix){

        //分页首页
        pageUtil.setFirstPageUrl(catalogVo.getLinkUrl());
        //分页 上一页
        String preUrl;
        if(pageUtil.getCurrent() <= 1){
            preUrl = "/"+catalogVo.getPathName()+"/list/1"+pageSuffix;
        }else{
            preUrl = "/"+catalogVo.getPathName()+"/list/"+(pageUtil.getCurrent()-1)+pageSuffix;
        }

        //分页末页
        String endUrl;
        //分页 下一页
        String nextUrl;
        if(pageUtil.getPages() == 0){
            endUrl = "/"+catalogVo.getPathName()+"/list/1"+pageSuffix;
            nextUrl = "/"+catalogVo.getPathName()+"/list/1"+pageSuffix;
        }else{
            endUrl = "/"+catalogVo.getPathName()+"/list/"+pageUtil.getPages()+pageSuffix;
            if(pageUtil.getCurrent() == pageUtil.getPages()){
                nextUrl = "/"+catalogVo.getPathName()+"/list/"+pageUtil.getCurrent()+pageSuffix;
            }else{
                nextUrl = "/"+catalogVo.getPathName()+"/list/"+(pageUtil.getCurrent()+1)+pageSuffix;
            }
        }
        pageUtil.setPrePageUrl(preUrl);
        pageUtil.setEndPageUrl(endUrl);
        pageUtil.setPrePageUrl(preUrl);
        pageUtil.setNextPageUrl(nextUrl);

        pageUtil.setCurrPageUrl("/"+catalogVo.getPathName()+"/list/"+pageUtil.getCurrent()+pageSuffix);

        List<String> pageUrlList = new ArrayList<>();
        if(pageUtil.getPages()>0){
            for(int i=0;i<pageUtil.getPages();i++){
                pageUrlList.add("/"+catalogVo.getPathName()+"/list/"+(i+1)+pageSuffix);
            }
        }
        pageUtil.setPageList(pageUrlList);
        return pageUtil;
    }

}
