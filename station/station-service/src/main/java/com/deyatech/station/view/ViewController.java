package com.deyatech.station.view;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.entity.RecordSatisfaction;
import com.deyatech.appeal.feign.AppealFeign;
import com.deyatech.appeal.vo.ModelVo;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.feign.AssemblyFeign;
import com.deyatech.assembly.vo.ApplyOpenModelVo;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.interview.entity.Category;
import com.deyatech.interview.feign.InterviewFeign;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.entity.Template;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.station.view.utils.ViewUtils;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.template.feign.TemplateFeign;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    AppealFeign appealFeign;
    @Autowired
    AssemblyFeign assemblyFeign;
    @Autowired
    InterviewFeign interviewFeign;

    @Autowired
    RedisTemplate redisTemplate;

    //自定义模板的参数名  tm=模板路径
    private static final String TEMPLATE_PARAMS_NAME = "tm";


    /**
     * 动态首页
     *
     * @return
     */
    @GetMapping(value = "/index/{siteId}", produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String indexPage(HttpServletRequest request,@PathVariable("siteId")String siteId){
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
        Map<String,Object> varMap = new HashMap<>();
        ViewUtils.requestParams(varMap,request);
        //是否自定义模板
        String template = request.getParameter(TEMPLATE_PARAMS_NAME);
        if(StringUtils.isBlank(template)){
            template = templateFeign.getTemplateDefaultIndex().getData();
        }
        StationGroup site = siteCache.getStationGroupById(siteId);
        varMap.put("site",site);
        return templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
    }

    /**
     * 搜索页
     *
     * @return
     */
    @GetMapping(value = "/search/{siteId}", produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String searchPage(HttpServletRequest request,@PathVariable("siteId")String siteId){
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
        Map<String,Object> varMap = new HashMap<>();
        ViewUtils.requestParams(varMap,request);
        //是否自定义模板
        String template = request.getParameter(TEMPLATE_PARAMS_NAME);
        if(StringUtils.isBlank(template)){
            template = templateFeign.getTemplateDefaultSearch().getData();
        }
        StationGroup site = siteCache.getStationGroupById(siteId);
        varMap.put("site",site);
        varMap.putAll(templateService.search(varMap));
        return templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
    }


    /**
     * 栏目列表页 和 频道页
     * @return
     */
    @GetMapping(value = "/catagory/{siteId}", produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String listAndIndexPage(HttpServletRequest request, @PathVariable("siteId") String siteId, @RequestParam("namePath") String namePath){
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
        Map<String,Object> varMap = new HashMap<>();
        Map<String,String> map = ViewUtils.analysisNamePath(namePath,"catagory");
        Catalog catalog = new Catalog();
        catalog.setSiteId(siteId);
        catalog.setPathName(map.get("pathName"));
        catalog = catalogService.getByBean(catalog);
        varMap.put("site",siteCache.getStationGroupById(siteId));
        if (ObjectUtil.isNull(catalog)) {
            varMap.put("message","栏目信息不存在!");
            return errorPage(siteTemplateRoot,varMap);
        }
        varMap.put("catalog",catalog);
        varMap.put("rootCatalog",getRootCatalog(siteId,catalog.getId()));
        varMap.put("pageNo",map.get("pageNo"));
        ViewUtils.requestParams(varMap,request);
        //是否自定义模板
        String template = request.getParameter(TEMPLATE_PARAMS_NAME);
        if(StringUtils.isBlank(template)){
            if(map.get("type").equals("index")){
                template = catalog.getIndexTemplate();
            }else{
                template = catalog.getListTemplate();
            }
        }
        if(StrUtil.isBlank(template)){
            varMap.put("message","模板读取失败!");
            return errorPage(siteTemplateRoot,varMap);
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
    @GetMapping(value = "/info/{siteId}", produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String contentPage(HttpServletRequest request, @PathVariable("siteId") String siteId,@RequestParam("namePath") String namePath) {
        Map<String,String> map = ViewUtils.analysisNamePath(namePath,"content");
        Map<String,Object> varMap = new HashMap<>();
        Catalog catalog = new Catalog();
        catalog.setSiteId(siteId);
        catalog.setPathName(map.get("pathName"));
        catalog = catalogService.getByBean(catalog);
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
        varMap.put("site",siteCache.getStationGroupById(siteId));
        varMap.put("catalog",catalog);
        varMap.put("rootCatalog",getRootCatalog(siteId,catalog.getId()));
        ViewUtils.requestParams(varMap,request);
        String templatePath = "";
        //新闻详情
        if(map.get("type").equals("info")){
            Template template = templateService.getById(map.get("infoId"));
            if (ObjectUtil.isNull(template)) {
                varMap.put("message","内容详情不存在!");
                return errorPage(siteTemplateRoot,varMap);
            }
            TemplateVo templateVo = templateService.setVoProperties(template);
            templatePath = templateVo.getTemplatePath();
            varMap.put("infoData",templateVo);
        }
        //诉求详情
        if(map.get("type").equals("appeal")){
            String infoId = map.get("infoId");
            RecordVo recordVo;
            //infoId为search时 标识根据信件编码和查询码 查询详情
            if(infoId.equals("search")){
                recordVo = appealFeign.queryAppeal(varMap.get("sqCode").toString(),varMap.get("queryCode").toString()).getData();
            }else{
                recordVo = appealFeign.getAppealById(infoId).getData();
                if (ObjectUtil.isNull(recordVo)) {
                    varMap.put("message","诉求详情不存在!");
                    return errorPage(siteTemplateRoot,varMap);
                }
            }
            ModelVo modelVo = appealFeign.getModelById(recordVo.getModelId()).getData();
            if (ObjectUtil.isNull(modelVo)) {
                varMap.put("message","诉求业务模型不存在!");
                return errorPage(siteTemplateRoot,varMap);
            }
            varMap.put("appealData",recordVo);
            templatePath = modelVo.getViewTemplet();
        }
        //依申请公开详情
        if(map.get("type").equals("applyopen")){
            String infoId = map.get("infoId");
            ApplyOpenRecordVo applyOpenRecordVo;
            //infoId为search时 标识根据信件编码和查询码 查询详情
            if(infoId.equals("search")){
                applyOpenRecordVo = assemblyFeign.queryApplyOpen(varMap.get("ysqCode").toString(),varMap.get("queryCode").toString()).getData();
            }else{
                applyOpenRecordVo = assemblyFeign.getApplyOpenById(map.get("infoId")).getData();
                if (ObjectUtil.isNull(applyOpenRecordVo)) {
                    varMap.put("message","依申请公开详情不存在!");
                    return errorPage(siteTemplateRoot,varMap);
                }
            }
            ApplyOpenModelVo applyOpenModelVo = assemblyFeign.getApplyOpenModelById(applyOpenRecordVo.getModelId()).getData();
            if (ObjectUtil.isNull(applyOpenModelVo)) {
                varMap.put("message","依申请公开模型不存在!");
                return errorPage(siteTemplateRoot,varMap);
            }
            varMap.put("applyOpenData",applyOpenRecordVo);
            templatePath = applyOpenModelVo.getViewTemplet();
        }
        //在线访谈详情
        if(map.get("type").equals("interview")){
            String infoId = map.get("infoId");
            com.deyatech.interview.vo.ModelVo interviewVo = interviewFeign.getInterviewById(infoId).getData();
            if (ObjectUtil.isNull(interviewVo)) {
                varMap.put("message","在线访谈信息不存在!");
                return errorPage(siteTemplateRoot,varMap);
            }
            Category category = interviewFeign.getInterviewCatagoryById(interviewVo.getCategoryId()).getData();
            if (ObjectUtil.isNull(category)) {
                varMap.put("message","在线访谈目录不存在!");
                return errorPage(siteTemplateRoot,varMap);
            }
            varMap.put("interviewData",interviewVo);
            templatePath = category.getDetailPageTemplate();
        }
        //是否自定义模板
        String template = request.getParameter(TEMPLATE_PARAMS_NAME);
        if(StringUtils.isBlank(template)){
            template = templatePath;
        }
        if(StrUtil.isBlank(template)){
            varMap.put("message","模板读取失败!");
            return errorPage(siteTemplateRoot,varMap);
        }
        return templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
    }
    /**
     * 表单页
     *
     * @param siteId
     * @param namePath
     * @return
     */
    @GetMapping(value = "/form/{siteId}", produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String formPage(HttpServletRequest request, @PathVariable("siteId") String siteId,@RequestParam("namePath") String namePath) {
        Map<String,String> map = ViewUtils.analysisNamePath(namePath,"form");
        Map<String,Object> varMap = new HashMap<>();
        Catalog catalog = new Catalog();
        catalog.setSiteId(siteId);
        catalog.setPathName(map.get("pathName"));
        catalog = catalogService.getByBean(catalog);
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
        varMap.put("site",siteCache.getStationGroupById(siteId));
        varMap.put("catalog",catalog);
        varMap.put("rootCatalog",getRootCatalog(siteId,catalog.getId()));
        ViewUtils.requestParams(varMap,request);
        String templatePath = "";
        //诉求表单
        if(map.get("type").equals("appeal")){
            ModelVo modelVo = appealFeign.getModelById(map.get("modelId")).getData();
            varMap.put("modelData",modelVo);
            templatePath = modelVo.getFormTemplet();
        }
        //依申请公开表单
        if(map.get("type").equals("applyopen")){
            ApplyOpenModelVo modelVo = assemblyFeign.getApplyOpenModelById(map.get("modelId")).getData();
            varMap.put("modelData",modelVo);
            templatePath = modelVo.getFormTemplet();
        }
        //是否自定义模板
        String template = request.getParameter(TEMPLATE_PARAMS_NAME);
        if(StringUtils.isBlank(template)){
            template = templatePath;
        }
        if(StrUtil.isBlank(templatePath)){
            varMap.put("message","模板读取失败!");
            return errorPage(siteTemplateRoot,varMap);
        }
        return templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
    }

    /**
     * 表单提交
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/submit")
    public RestResult submit(HttpServletRequest request) {
        if (!validateVerifyCode(redisTemplate, request.getParameter("verifyCode"), request.getParameter("random"))) {
            return RestResult.build(HttpStatus.HTTP_INTERNAL_ERROR, "验证码错误", false);
        }
        String actionType = request.getParameter("actionType");
        Map<String,Object> varMap = new HashMap<>();
        ViewUtils.requestParams(varMap,request);
        //诉求提交
        if(actionType.equals("insertAppeal")){
            Record record = new Record();
            BeanUtil.copyProperties(varMap,record);
            return RestResult.ok(appealFeign.insertAppeal(record));
        }
        //诉求满意度提交
        if(actionType.equals("insertAppealSatis")){
            RecordSatisfaction recordSatisfaction = new RecordSatisfaction();
            BeanUtil.copyProperties(varMap,recordSatisfaction);
            return RestResult.ok(appealFeign.insertAppealSatis(recordSatisfaction));
        }
        //依申请公开提交
        if(actionType.equals("insertApplyOpen")){
            ApplyOpenRecord applyOpenRecord = new ApplyOpenRecord();
            BeanUtil.copyProperties(varMap,applyOpenRecord);
            return RestResult.ok(assemblyFeign.insertApplyOpen(applyOpenRecord));
        }
        return RestResult.ok();
    }


    public String errorPage(String siteTemplateRoot,Map<String,Object> varMap){
        return templateFeign.thyToString(siteTemplateRoot,templateFeign.getTemplateDefaultError().getData(),varMap).getData();
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
