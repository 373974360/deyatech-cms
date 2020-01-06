package com.deyatech.cas.config;

import cn.hutool.core.util.StrUtil;
import com.ucap.cas.client.authentication.AuthenticationFilter;
import lombok.Data;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author doukang
 * @description CAS客户端配置
 * @date 2019/6/6 14:21
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cas")
public class CasConfig {

    public static final String SESSION_KEY = "Deyatech-Token";

    private String serverUrlPrefix;

    private String serverLoginUrl;

    private String clientHostUrl;

    private String authenticationUrlPatterns;

    private String ignorePattern;

    /**
     * 用于单点登出功能
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new SingleSignOutHttpSessionListener());
        registrationBean.setOrder(1);
        return registrationBean;
    }

    /**
     * 单点登出过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean<SingleSignOutFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SingleSignOutFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("casServerUrlPrefix", serverUrlPrefix);
        registrationBean.setOrder(2);
        return registrationBean;
    }

    /**
     * 用户认证过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter());
        for (String url : authenticationUrlPatterns.split(",")) {
            registrationBean.addUrlPatterns(url);
        }
        registrationBean.addInitParameter("casServerLoginUrl", serverLoginUrl);
        registrationBean.addInitParameter("serverName", clientHostUrl);
        if (StrUtil.isNotBlank(ignorePattern)) {
            registrationBean.addInitParameter("ignorePattern", ignorePattern);
        }
        registrationBean.setOrder(3);
        return registrationBean;
    }

    /**
     * Ticket校验过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean validationFilter() {
        FilterRegistrationBean<Cas20ProxyReceivingTicketValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        for (String url : authenticationUrlPatterns.split(",")) {
            registrationBean.addUrlPatterns(url);
        }
        registrationBean.addInitParameter("casServerUrlPrefix", serverUrlPrefix);
        registrationBean.addInitParameter("serverName", clientHostUrl);
        registrationBean.setOrder(4);
        return registrationBean;
    }

    /**
     * wrapper过滤器：用户获取用户信息
     * @return
     */
    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilter() {
        FilterRegistrationBean<HttpServletRequestWrapperFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HttpServletRequestWrapperFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(5);
        return registrationBean;
    }

    /**
     * 作用同上，只需要配置二者其一即可
     * @return
     */
    @Bean
    public FilterRegistrationBean assertionThreadLocalFilter() {
        FilterRegistrationBean<AssertionThreadLocalFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AssertionThreadLocalFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(6);
        return registrationBean;
    }

}
