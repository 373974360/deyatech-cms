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

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Enumeration;
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
    public static final String TEMPLATE_DEFAULT_INDEX = "/index/index.html";


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
     * 栏目列表页 和 频道页
     * @return
     */
    @GetMapping(value = "/c/{siteId}", produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String list(HttpServletRequest request, @PathVariable("siteId") String siteId, @RequestParam("namePath") String namePath){
        Map<String,String> map = analysisNamePath(namePath,"catagory");
        Catalog catalog = new Catalog();
        catalog.setSiteId(siteId);
        catalog.setPathName(map.get("pathName"));
        catalog = catalogService.getByBean(catalog);
        if (catalog == null) {
            return "栏目不存在";
        }
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
        Map<String,Object> varMap = new HashMap<>();
        varMap.put("site",siteCache.getStationGroupById(siteId));
        varMap.put("catalog",catalog);
        varMap.put("rootCatalog",getRootCatalog(siteId,catalog.getId()));
        varMap.put("pageNo",map.get("pageNo"));
        requestParams(varMap,request);
        String template;
        if(map.get("type").equals("index")){
            template = catalog.getIndexTemplate();
        }else{
            template = catalog.getListTemplate();
        }
        return templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
    }
    /**
     * 动态内容页
     *
     * @param siteId
     * @param namePath
     * @return
     */
    @GetMapping(value = "/m/{siteId}", produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String contentPage(HttpServletRequest request, @PathVariable("siteId") String siteId,@RequestParam("namePath") String namePath) {
        Map<String,String> map = analysisNamePath(namePath,"info");
        Template template = templateService.getById(map.get("infoId"));
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
        varMap.put("rootCatalog",getRootCatalog(siteId,catalog.getId()));
        requestParams(varMap,request);
        return templateFeign.thyToString(siteTemplateRoot,templateVo.getTemplatePath(),varMap).getData();
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

    /**
     * namePath 解析
     * */
    public static Map<String,String> analysisNamePath(String namePath,String type){
        Map<String,String> result = new HashMap<>();
        //栏目页
        if(type.equals("catagory")){
            String ext = namePath.substring(namePath.lastIndexOf("_")+1);
            String pathName = namePath.substring(0,namePath.lastIndexOf("_"));
            if(ext.equals("index")){
                result.put("type",ext);
            }else{
                //有页码
                if(!ext.equals("list")){
                    pathName = pathName.substring(0,pathName.lastIndexOf("_"));
                    result.put("pageNo",ext);
                }
                result.put("type","list");
            }
            result.put("pathName",pathName);
        }
        if(type.equals("info")){
            result.put("infoId",namePath.substring(0,namePath.lastIndexOf("_")));
        }
        return result;
    }

    /**
     * request参数解析
     * */
    public static Map<String,Object> requestParams(Map<String,Object> varMap,HttpServletRequest request){
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
            Object o = e.nextElement();
            String arr = (String) o;
            String value = request.getParameter(arr);
            varMap.put(arr,value);
        }
        return varMap;
    }

}
