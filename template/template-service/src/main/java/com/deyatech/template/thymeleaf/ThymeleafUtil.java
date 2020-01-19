package com.deyatech.template.thymeleaf;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.template.cache.TemplateCache;
import com.deyatech.template.thymeleaf.utils.CmsDialect;
import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.AbstractContext;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 描述：模板工具类
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/9 09:32
 */
@Slf4j
@Component
public class ThymeleafUtil {

    static TemplateEngine templateEngine = new TemplateEngine();
    static ThreadPrefixFileTemplateResolver iTemplateResolver = new ThreadPrefixFileTemplateResolver();
    static AbstractContext context = null;
    boolean isDebug = false;

    @Autowired
    CmsDialect cmsDialect;

    @Autowired
    TemplateCache templateCache;

    /**
     * 通过spring的RequestContextHolder获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request;
    }

    /**
     * 通过spirng的RequestContextHolder获取response
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        return response;
    }

    private IContext init(String siteTemplateRoot) {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        if (request == null) {
            request = new MockHttpServletRequest(new MockServletContext(siteTemplateRoot));
        }
        if (response == null) {
            response = new MockHttpServletResponse();
        }
        context = new WebContext(request, response, request.getServletContext());
        if (StringUtils.isBlank(siteTemplateRoot) || !new File(siteTemplateRoot).exists()) {
            throw new RuntimeException("模板路径" + siteTemplateRoot + "有误，无法渲染");
        }
        iTemplateResolver.setCacheable(false);
        iTemplateResolver.overridePrefixByThreadLocal(siteTemplateRoot);
        if (!templateEngine.isInitialized()) {
            iTemplateResolver.setSuffix(TemplateConstants.PAGE_SUFFIX);
            templateEngine.addTemplateResolver(iTemplateResolver);
            templateEngine.addDialect(cmsDialect);
        }
        return context;
    }

    /**
     * 生成静态页面
     *
     * @param templateRootPath 模板根路径
     * @param templatePath 模板路径
     * @param distFile     输出文件路径
     * @param varMap       模板用到的变量
     */
    public void thyToStaticFile(String templateRootPath, String templatePath, File distFile, Map<String, Object> varMap) {
        if (templatePath == null) {
            throw new RuntimeException("模板文件路径为null");
        } else if (!templatePath.endsWith(TemplateConstants.PAGE_SUFFIX)) {
            templatePath += TemplateConstants.PAGE_SUFFIX;
        }
        //如果上级目录不存在，则自动创建上级目录
        if (!distFile.getParentFile().exists()) {
            distFile.getParentFile().mkdirs();
        }
        //渲染模板
        FileWriter write = null;
        try {
            write = new FileWriter(distFile);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("静态化页面发生错误", e);
        }
        try {
            init(templateRootPath);
            context.setVariables(varMap);
            templateEngine.process(templatePath, context, write);
            write.close();
        } catch (Exception e) {
            log.error(String.format("静态化页面发生错误 , 模板 %s , 数据 %s", templatePath, varMap), e);
            throw new RuntimeException("静态化页面发生错误");
        }
    }


    /**
     * 处理模板文件返回字符串
     *
     * @param siteTemplateRoot 模板根路径
     * @param templatePath 模板路径
     * @param varMap       模板用到的变量
     * @return
     */
    public String thyToString(String siteTemplateRoot, String templatePath, Map<String, Object> varMap) {
        long start = System.nanoTime();
        parseDate(varMap);
        if (StringUtils.isBlank(templatePath)) {
            varMap.put("message","模板路径为空！");
            context.setVariables(varMap);
            return templateEngine.process(TemplateConstants.TEMPLATE_DEFAULT_ERROR, context);
        } else if (!templatePath.endsWith(TemplateConstants.PAGE_SUFFIX)) {
            templatePath += TemplateConstants.PAGE_SUFFIX;
        }
        init(siteTemplateRoot);
        context.setVariables(varMap);
        String process = null;
        try {
            if(varMap.containsKey("namePath") && varMap.containsKey("pageNo") && varMap.containsKey("type") && !varMap.get("type").equals("include")){
                String cachekey = varMap.get("namePath").toString();
                String key = varMap.get("pageNo").toString();
                process = templateCache.getTemplate(cachekey,key);
            }else{
                process = templateEngine.process(templatePath, context);
            }
            log.info("内容检索耗时: " + getMillisTime(System.nanoTime() - start) + " 毫秒");
            return process;
        } catch (Exception e) {
            log.error("模板渲染错误", e);
            if (isDebug) {
                return ExceptionUtils.getStackTrace(e);
            }
        }
        return "";
    }

    private String getMillisTime(long time) {
        return String.valueOf(time / 1000000);
    }

    /**
     * 将map中 字符串类型的日期修改为date 类型
     * @param varMap
     * @return
     * */
    public static Map<String, Object> parseDate(Map<String, Object> varMap){
        for (Map.Entry<String, Object> entry : varMap.entrySet()) {
            Object value = varMap.get(entry.getKey());
            if (isDate(value.toString())){
                varMap.put(entry.getKey(),parseDateStr(value.toString()));
            }else if (value instanceof Map){
                parseDate((Map<String, Object>)value);
            }else if (entry.getKey().equals("records")){
                for(Map<String, Object> map:(List<Map<String, Object>>)value){
                    parseDate(map);
                }
            }
        }
        return varMap;
    }


    /**
     * 判断字符串是否为日期格式
     * @param strDate
     * @return
     * */
    public static boolean isDate(String strDate) {
        try {
            //替换掉 UTC 格式中的T和Z
            strDate = strDate.replace("T"," ").replace("Z","");
            if(strDate.indexOf(".") > -1){
                strDate = strDate.substring(0,strDate.indexOf("."));
            }
            DateUtil.parse(strDate,"yyyy-MM-dd HH:mm:ss");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static DateTime parseDateStr(String strDate){
        //替换掉 UTC 格式中的T和Z
        strDate = strDate.replace("T"," ").replace("Z","");
        if(strDate.indexOf(".") > -1){
            strDate = strDate.substring(0,strDate.indexOf("."));
        }
        return DateUtil.parse(strDate,"yyyy-MM-dd HH:mm:ss");
    }
}
