package com.deyatech.template.thymeleaf;

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
        } catch (Exception e) {
            log.error(String.format("静态化页面发生错误 , 模板 %s , 数据 %s", templatePath, varMap), e);
            throw new RuntimeException(String.format("静态化页面发生错误 , 模板 %s , 数据 %s", templatePath, varMap), e);
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
        if (StringUtils.isBlank(templatePath)) {
            log.error("模板文件路径为为空");
            return "";
        } else if (!templatePath.endsWith(TemplateConstants.PAGE_SUFFIX)) {
            templatePath += TemplateConstants.PAGE_SUFFIX;
        }
        init(siteTemplateRoot);
        context.setVariables(varMap);
        String process = null;
        try {
            process = templateEngine.process(templatePath, context);
            return process;
        } catch (Exception e) {
            log.error("模板渲染错误", e);
            if (isDebug) {
                return ExceptionUtils.getStackTrace(e);
            }
        }
        return "";
    }
}
