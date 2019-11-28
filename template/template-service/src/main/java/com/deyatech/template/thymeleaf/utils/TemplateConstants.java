package com.deyatech.template.thymeleaf.utils;

public class TemplateConstants {
    /**
     * 静态页面后缀
     */
    public static final String PAGE_SUFFIX = ".html";
    /**
     * 默认首页模板
     */
    public static final String TEMPLATE_DEFAULT_INDEX = "/index/index.html";
    /**
     * 默认搜索模板
     */
    public static final String TEMPLATE_DEFAULT_SEARCH = "/other/searchResult.html";
    /**
     * 默认错误模板
     */
    public static final String TEMPLATE_DEFAULT_ERROR = "/other/error.html";
    /**
     * 模板自定义内置对象-栏目工具
     */
    public static final String TEMPLATE_OBJ_CATALOG_UTIL = "CataData";
    /**
     * 模板自定义内置对象-数据工具
     */
    public static final String TEMPLATE_OBJ_INFO_UTIL = "InfoData";
    /**
     * 模板自定义内置对象-用户工具
     */
    public static final String TEMPLATE_OBJ_USER_UTIL = "UserData";
    /**
     * 模板自定义内置对象-站点工具
     */
    public static final String TEMPLATE_OBJ_SITE_UTIL = "SiteData";
    /**
     * 模板自定义内置对象-诉求工具
     */
    public static final String TEMPLATE_OBJ_APPEAL_UTIL = "AppealData";
    /**
     * 模板自定义内置对象-依申请公开
     */
    public static final String TEMPLATE_OBJ_APPLYOPEN_UTIL = "ApplyOpenData";
    /**
     * 模板自定义内置对象-在线访谈
     */
    public static final String TEMPLATE_OBJ_INTERVIEW_UTIL = "InterviewData";
}
