package com.deyatech.template.thymeleaf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 各类模板渲染数据工具类
 */
@Component
@Slf4j
public class TemplateContextUtils {


    @Autowired
    ThymeleafUtil thymeleafUtil;

    /**
     * 内容页模板变量
     *
     * @return
     */
    public Map<String, Object> contentPageVarMap() {

        return null;
    }

}
