package com.deyatech.station.view;

import cn.hutool.core.util.ObjectUtil;
import com.deyatech.common.base.BaseController;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.service.CatalogService;
import com.deyatech.template.feign.TemplateFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：网站前台控制器
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 10:12
 */
@RestController
@RequestMapping("/")
public class ViewController extends BaseController {

    @Autowired
    TemplateFeign templateFeign;
    @Autowired
    SiteCache siteCache;
    @Autowired
    CatalogService catalogService;
    /**
     * 静态页面后缀
     */
    public static final String TEMPLATE_DEFAULT_INDEX = "/index.html";

    /**
     * 动态首页
     *
     * @return
     */
    @GetMapping(value = "/{siteId}", produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String index(@PathVariable("siteId")String siteId){
        StationGroup site = siteCache.getStationGroupById(siteId);
        if(ObjectUtil.isNotNull(site)){
            String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
            Map<String,Object> varMap = new HashMap<>();
            varMap.put("site",site);
            return templateFeign.thyToString(siteTemplateRoot,TEMPLATE_DEFAULT_INDEX,varMap).getData();
        }else{
            return "站点不存在";
        }
    }

    /**
     * 动态栏目页
     * @param c 栏目ID
     * @param p 页码
     * @return
     */
    @GetMapping(value = "/list", produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String list(String c,String p){
        Catalog catalog = catalogService.getById(c);
        String siteId = catalog.getSiteId();
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
        Map<String,Object> varMap = new HashMap<>();
        varMap.put("site",siteCache.getStationGroupById(siteId));
        varMap.put("catalog",catalog);
        varMap.put("p",p);
        return templateFeign.thyToString(siteTemplateRoot,catalog.getListTemplate(),varMap).getData();
    }
}
