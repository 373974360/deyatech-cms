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
    @GetMapping(value = "/s/{siteId}", produces = {"text/html;charset=utf-8"})
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
    @GetMapping(value = "/c/{siteId}")
    @ResponseBody
    public String list(@PathVariable("siteId") String siteId,@RequestParam("namePath") String namePath,Integer p){
        Catalog catalog = new Catalog();
        catalog.setSiteId(siteId);
        catalog.setPathName(namePath);
        catalog = catalogService.getByBean(catalog);
        if (catalog == null) {
            return "栏目不存在";
        }
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
        Map<String,Object> varMap = new HashMap<>();
        varMap.put("site",siteCache.getStationGroupById(siteId));
        varMap.put("catalog",catalog);
        varMap.put("p",p==null||p<=0?1:p);
        return templateFeign.thyToString(siteTemplateRoot,catalog.getListTemplate(),varMap).getData();
    }
}
