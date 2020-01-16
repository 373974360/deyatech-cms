package com.deyatech.station.view;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.entity.RecordSatisfaction;
import com.deyatech.appeal.feign.AppealFeign;
import com.deyatech.appeal.vo.ModelVo;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.apply.entity.OpenRecord;
import com.deyatech.apply.feign.ApplyFeign;
import com.deyatech.apply.vo.OpenModelVo;
import com.deyatech.apply.vo.OpenRecordVo;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.interview.entity.Category;
import com.deyatech.interview.feign.InterviewFeign;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.MaterialService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.station.view.utils.ViewUtils;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.template.feign.TemplateFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 描述：网站前台控制器
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 10:12
 */
@RestController
@Slf4j
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
    ApplyFeign applyFeign;
    @Autowired
    InterviewFeign interviewFeign;

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    MaterialService materialService;

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
        String content = templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
        return analysisInclude(content,siteTemplateRoot,varMap);
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
        varMap.put("siteId",siteId);
        varMap.put("site",site);
        varMap = templateService.search(varMap);
        String content = templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
        return analysisInclude(content,siteTemplateRoot,varMap);
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
        varMap.put("site",siteCache.getStationGroupById(siteId));
        CatalogVo catalogVo = getCatalogByPathName(siteId,map.get("pathName"));
        if (ObjectUtil.isNull(catalogVo) || StrUtil.isBlank(catalogVo.getId())) {
            return "栏目信息不存在!";
        }
        varMap.put("catalog",catalogVo);
        varMap.put("rootCatalog",getRootCatalog(siteId,catalogVo.getId()));
        varMap.put("pageNo",map.get("pageNo"));
        ViewUtils.requestParams(varMap,request);
        //是否自定义模板
        String template = request.getParameter(TEMPLATE_PARAMS_NAME);
        if(StringUtils.isBlank(template)){
            if(map.get("type").equals("index")){
                template = catalogVo.getIndexTemplate();
            }else{
                template = catalogVo.getListTemplate();
            }
        }
        if(StrUtil.isBlank(template)){
            return "模板读取失败!";
        }
        varMap.put("namePath",map.get("pathName"));
        String content = templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
        return analysisInclude(content,siteTemplateRoot,varMap);
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
        Map<String,String> map = ViewUtils.analysisNamePath(namePath,"details");
        Map<String,Object> varMap = new HashMap<>();
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
        varMap.put("site",siteCache.getStationGroupById(siteId));
        CatalogVo catalogVo = getCatalogByPathName(siteId,map.get("pathName"));
        if (ObjectUtil.isNull(catalogVo) || StrUtil.isBlank(catalogVo.getId())) {
            return "栏目信息不存在!";
        }
        varMap.put("catalog",catalogVo);
        varMap.put("rootCatalog",getRootCatalog(siteId,catalogVo.getId()));
        ViewUtils.requestParams(varMap,request);
        String templatePath = "";
        //新闻详情
        if(map.get("type").equals("info")){
            TemplateVo templateVo = templateService.getTemplateById(map.get("infoId"));
            if (ObjectUtil.isNull(templateVo) || StrUtil.isBlank(templateVo.getId())) {
                return "内容详情不存在!";
            }
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
            }
            if (ObjectUtil.isNull(recordVo) || StrUtil.isBlank(recordVo.getId())) {
                return "诉求详情不存在!";
            }
            ModelVo modelVo = appealFeign.getModelById(recordVo.getModelId()).getData();
            if (ObjectUtil.isNull(modelVo) || StrUtil.isBlank(modelVo.getId())) {
                return "诉求业务模型不存在!";
            }
            varMap.put("appealData",recordVo);
            templatePath = modelVo.getViewTemplet();
        }
        //依申请公开详情
        if(map.get("type").equals("applyopen")){
            String infoId = map.get("infoId");
            OpenRecordVo applyOpenRecordVo;
            //infoId为search时 标识根据信件编码和查询码 查询详情
            if(infoId.equals("search")){
                applyOpenRecordVo = applyFeign.queryApplyOpen(varMap.get("ysqCode").toString(),varMap.get("queryCode").toString()).getData();
            }else{
                applyOpenRecordVo = applyFeign.getApplyOpenById(map.get("infoId")).getData();
            }
            if (ObjectUtil.isNull(applyOpenRecordVo) || StrUtil.isBlank(applyOpenRecordVo.getId())) {
                return "依申请公开详情不存在!";
            }
            OpenModelVo applyOpenModelVo = applyFeign.getApplyOpenModelById(applyOpenRecordVo.getModelId()).getData();
            if (ObjectUtil.isNull(applyOpenModelVo) || StrUtil.isBlank(applyOpenModelVo.getId())) {
                return "依申请公开模型不存在!";
            }
            varMap.put("applyOpenData",applyOpenRecordVo);
            templatePath = applyOpenModelVo.getViewTemplet();
        }
        //在线访谈详情
        if(map.get("type").equals("interview")){
            String infoId = map.get("infoId");
            com.deyatech.interview.vo.ModelVo interviewVo = interviewFeign.getInterviewById(infoId).getData();
            if (ObjectUtil.isNull(interviewVo) || StrUtil.isBlank(interviewVo.getId())) {
                return "在线访谈信息不存在!";
            }
            Category category = interviewFeign.getInterviewCatagoryById(interviewVo.getCategoryId()).getData();
            if (ObjectUtil.isNull(category) || StrUtil.isBlank(category.getId())) {
                return "在线访谈目录不存在!";
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
            return "模板读取失败!";
        }
        String content = templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
        return analysisInclude(content,siteTemplateRoot,varMap);
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
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(siteId);
        Map<String,Object> varMap = new HashMap<>();
        varMap.put("site",siteCache.getStationGroupById(siteId));
        CatalogVo catalogVo = getCatalogByPathName(siteId,map.get("pathName"));
        if (ObjectUtil.isNull(catalogVo) || StrUtil.isBlank(catalogVo.getId())) {
            return "栏目信息不存在!";
        }
        varMap.put("catalog",catalogVo);
        varMap.put("rootCatalog",getRootCatalog(siteId,catalogVo.getId()));
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
            OpenModelVo modelVo = applyFeign.getApplyOpenModelById(map.get("modelId")).getData();
            varMap.put("modelData",modelVo);
            templatePath = modelVo.getFormTemplet();
        }
        //是否自定义模板
        String template = request.getParameter(TEMPLATE_PARAMS_NAME);
        if(StringUtils.isBlank(template)){
            template = templatePath;
        }
        if(StrUtil.isBlank(templatePath)){
            return "模板读取失败!";
        }
        String content = templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
        return analysisInclude(content,siteTemplateRoot,varMap);
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
            return RestResult.ok(appealFeign.insertAppeal(record).getData());
        }
        //诉求满意度提交
        if(actionType.equals("insertAppealSatis")){
            RecordSatisfaction recordSatisfaction = new RecordSatisfaction();
            BeanUtil.copyProperties(varMap,recordSatisfaction);
            return RestResult.ok(appealFeign.insertAppealSatis(recordSatisfaction).getData());
        }
        //依申请公开提交
        if(actionType.equals("insertApplyOpen")){
            OpenRecord applyOpenRecord = new OpenRecord();
            BeanUtil.copyProperties(varMap,applyOpenRecord);
            return RestResult.ok(applyFeign.insertApplyOpen(applyOpenRecord).getData());
        }
        return RestResult.ok();
    }

    /**
     * 更新并获取浏览次数
     *
     * @param id
     * @return
     * */
    @GetMapping(value = "/getTemplateClickCount")
    public RestResult getTemplateClickCount(@RequestParam("id") String id) {
        return RestResult.ok(templateService.getTemplateClickCount(id));
    }

    /**
     * 查看图片
     *
     * @param siteId 站点编号
     * @param url 上传返回的url地址
     * @param response
     */
    @GetMapping("/showImage")
    public void showImage(String siteId, String url, HttpServletResponse response) {
        showImage(materialService.getFilePath(siteId, url), response);
    }
    /**
     * 查看图片
     *
     * @param filePath
     * @param response
     */
    private void showImage(String filePath, HttpServletResponse response) {
        if (StrUtil.isEmpty(filePath)) {
            log.error("图片路径为空");
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            log.error("图片不存在：" + filePath);
            return;
        }
        filePath = filePath.replace("\\","/");
        FileInputStream in = null;
        OutputStream out = null;
        try {
            response.setContentType("image/jpeg");
            in = new FileInputStream(filePath);
            out = response.getOutputStream();
            IOUtils.copy(in, out);
        } catch (IOException e) {
            log.error("图片读取失败", e);
        } finally {
            close(in);
            close(out);
        }
    }


    /**
     * 下载文件
     *
     * @param siteId
     * @param url
     * @param response
     */
    @GetMapping("/download")
    public void download(String siteId, String url, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(materialService.getFilePath(siteId, url), request, response);
    }
    /**
     * 下载文件
     *
     * @param filePath
     * @param request
     * @param response
     */
    private void downloadFile(String filePath, HttpServletRequest request, HttpServletResponse response) {
        if (StrUtil.isEmpty(filePath)) {
            log.error("文件路径为空");
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            log.error("文件不存在：" + filePath);
            return;
        }
        FileInputStream in = null;
        OutputStream out = null;
        try {
            String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType(request.getServletContext().getMimeType(filePath));
            in = new FileInputStream(filePath);
            out = response.getOutputStream();
            IOUtils.copy(in, out);
        } catch (IOException e) {
            log.error("文件读取失败", e);
        } finally {
            close(in);
            close(out);
        }
    }


    /**
     * 解析页面中的include(html)
     *
     * 静态页 使用 nginx ssi 引入页面 没有问题
     * 动态页 使用 nginx ssi 引入页面的时候，会无线加载本页面，目前还没有找出是什么问题
     * 暂时使用以下方法解决动态页不能 引入页面的问题
     * */
    public String analysisInclude(String pageContent,String rootPath,Map<String,Object> varMap){
        varMap.put("type","include");
        rootPath = rootPath.replace("/template","");
        //获取模板中 <!--#include virtual='***.html'--> 的列表
        String array[] = pageContent.split("<!--");
        List<String> templatePathArray = new ArrayList<>();
        if(ArrayUtil.isNotEmpty(array)){
            for(String str:array){
                if(str.indexOf("#include")>=0){
                    templatePathArray.add(str.substring(str.indexOf("/"),str.indexOf(".html")+5));
                }
            }
        }
        //替换 <!--#include virtual='***.html'--> 为引用资源的内容 并解析
        if(!templatePathArray.isEmpty()&&templatePathArray.size()>0){
            for(String tempPath:templatePathArray){
                String includeStr = "<!--#include virtual='"+tempPath+"'-->";
                String includeContent = templateFeign.thyToString(rootPath,tempPath,varMap).getData();
                pageContent = pageContent.replace(includeStr,includeContent);
            }
        }
        return pageContent;
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
    public CatalogVo getCatalogByPathName(String siteId,String pathName) {
        Collection<CatalogVo> catalogVoCollection = siteCache.getCatalogTreeBySiteId(siteId);
        CatalogVo catalogVo = getCatalogByPathName(catalogVoCollection,pathName);
        return catalogVo;
    }
    public CatalogVo getCatalogByPathName(Collection<CatalogVo> catalogVoCollection,String pathName ){
        CatalogVo catalogVo = null;
        for(CatalogVo catalogVo1:catalogVoCollection){
            if(catalogVo1.getPathName().equals(pathName)){
                catalogVo = catalogVo1;
                break;
            }else if(ObjectUtil.isNotNull(catalogVo1.getChildren())){
                catalogVo = getCatalogByPathName(catalogVo1.getChildren(),pathName);
                if(ObjectUtil.isNotNull(catalogVo)){
                    break;
                }
            }
        }
        return catalogVo;
    }

}
