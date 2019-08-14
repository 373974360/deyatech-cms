package com.deyatech.template.thymeleaf.utils;

import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Set;

@Component
public class CmsExpressionObjectFactory implements IExpressionObjectFactory {

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return ImmutableSet.of(
                TemplateConstants.TEMPLATE_OBJ_CATALOG_UTIL,
                TemplateConstants.TEMPLATE_OBJ_INFO_UTIL,
                TemplateConstants.TEMPLATE_OBJ_USER_UTIL,
                TemplateConstants.TEMPLATE_OBJ_SITE_UTIL,
                TemplateConstants.TEMPLATE_OBJ_FORMAT_UTIL
        );
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        return applicationContext.getBean(expressionObjectName);
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return true;
    }
}
