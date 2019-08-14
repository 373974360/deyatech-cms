package com.deyatech.template.thymeleaf.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * cms 表达式
 */

@Component
public class CmsDialect implements IExpressionObjectDialect {

    @Autowired
    CmsExpressionObjectFactory cmsExpressionObjectFactory;
    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return cmsExpressionObjectFactory;
    }

    @Override
    public String getName() {
        return "cms";
    }
}
