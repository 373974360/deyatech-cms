package com.deyatech.station.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 站点配置文件
 */
@ConfigurationProperties(prefix = "site")
@Configuration
@Data
public class SiteProperties {
    /**
     * 站点根目录
     */
    private String hostsRoot;
    /**
     * nginx 配置文件目录
     */
    private String nginxConfigDir;
    private String nginxProxyPass;

    private String elasticSearchHostname;
    private Integer elasticSearchPort;
}
