package com.deyatech.template.thymeleaf;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;

/**
 * 描述：使用线程安全的对象替代prefix设置，用于不同线程需要在prefix下渲染页面
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/9 09:47
 */
public class ThreadPrefixFileTemplateResolver extends FileTemplateResolver {
    ThreadLocal<String> prefix = new ThreadLocal<>();

    public void overridePrefixByThreadLocal(String prefix) {
        this.prefix.set(prefix);
    }

    @Override
    protected String computeResourceName(IEngineConfiguration configuration, String ownerTemplate, String template, String prefix, String suffix, boolean forceSuffix, Map<String, String> templateAliases, Map<String, Object> templateResolutionAttributes) {
        if (this.prefix.get() == null) {
            return super.computeResourceName(configuration, ownerTemplate, template, prefix, suffix, forceSuffix, templateAliases, templateResolutionAttributes);
        }
        return super.computeResourceName(configuration, ownerTemplate, template, this.prefix.get(), suffix, forceSuffix, templateAliases, templateResolutionAttributes);
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, String resourceName, String characterEncoding, Map<String, Object> templateResolutionAttributes) {
        return super.computeTemplateResource(configuration, ownerTemplate, template, resourceName, characterEncoding, templateResolutionAttributes);
    }
}
