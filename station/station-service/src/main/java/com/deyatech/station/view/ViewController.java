package com.deyatech.station.view;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.common.base.BaseController;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.entity.Template;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.template.feign.TemplateFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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
    @Autowired
    TemplateService templateService;
    /**
     * 静态页面后缀
     */
    public static final String TEMPLATE_DEFAULT_INDEX = "/index.html";


    /**
     * 动态内容页
     *
     * @param siteId
     * @param id
     * @return
     */
    @GetMapping(value = "/m/{siteId}", produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String contentPage(@PathVariable("siteId") String siteId, @RequestParam("id") String id) {
        Template template = templateService.getById(id);
        if (template == null) {
            return "查询不到 ContentTemplate";
        }
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
        Catalog catalog = catalogService.getById(template.getCmsCatalogId());
        Map<String,Object> varMap = new HashMap<>();
        TemplateVo templateVo = templateService.setVoProperties(template);
        varMap.put("site",siteCache.getStationGroupById(siteId));
        varMap.put("infoData",templateVo);
        varMap.put("catalog",catalog);
        return templateFeign.thyToString(siteTemplateRoot,templateVo.getTemplatePath(),varMap).getData();
    }

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
     * 栏目列表页
     * @param p 页码
     * @return
     */
    @GetMapping(value = "/c/{siteId}", produces = {"text/html;charset=utf-8"})
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
        varMap.put("rootCatalog",getRootCatalog(siteId,catalog.getId()));
        varMap.put("p",p==null||p<=0?1:p);
        return templateFeign.thyToString(siteTemplateRoot,catalog.getListTemplate(),varMap).getData();
    }




    /**
     * 栏目频道页
     * @param p 页码
     * @return
     */
    @GetMapping(value = "/i/{siteId}", produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String catIndex(@PathVariable("siteId") String siteId,@RequestParam("namePath") String namePath,Integer p){
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
        varMap.put("rootCatalog",getRootCatalog(siteId,catalog.getId()));
        varMap.put("p",p==null||p<=0?1:p);
        return templateFeign.thyToString(siteTemplateRoot,catalog.getIndexTemplate(),varMap).getData();
    }

    /**
     * 根据条件获取当前栏目的顶级栏目信息
     * @param siteId 站点ID
     * @param catalogId 当前栏目ID
     * @return
     */
    public CatalogVo getRootCatalog(String siteId, String catalogId) {
        CatalogVo resultCatalogVo = null;
        Collection<CatalogVo> catalogVoCollection = siteCache.getCatalogTreeBySiteId(siteId);
        CatalogVo catalogVo = getCatalog(catalogVoCollection,catalogId);
        if(ObjectUtil.isNotNull(catalogVo)){
            if(StrUtil.isNotBlank(catalogVo.getTreePosition())){
                String temp[] = catalogVo.getTreePosition().substring(1).split("&");
                resultCatalogVo = getCatalog(catalogVoCollection,temp[0]);
            }else{
                resultCatalogVo = catalogVo;
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
