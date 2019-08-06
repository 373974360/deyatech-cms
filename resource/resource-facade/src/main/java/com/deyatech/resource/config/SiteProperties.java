package com.deyatech.resource.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 站群配置文件
 */
@ConfigurationProperties(prefix = "site")
@Configuration
public class SiteProperties {
    /**
     * 站群根目录
     */
    private String hostsRoot;
    private String nginxConfigDir;
    private Integer nginxPort;
    private String nginxProxyPass;
    private String elasticSearchHostname;
    private Integer elasticSearchPort;
    private boolean debug = false;

    public String getElasticSearchHostname() {
        return elasticSearchHostname;
    }

    public void setElasticSearchHostname(String elasticSearchHostname) {
        this.elasticSearchHostname = elasticSearchHostname;
    }

    public Integer getElasticSearchPort() {
        return elasticSearchPort;
    }

    public void setElasticSearchPort(Integer elasticSearchPort) {
        this.elasticSearchPort = elasticSearchPort;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getHostsRoot() {
        return hostsRoot;
    }

    public void setHostsRoot(String hostsRoot) {
        this.hostsRoot = hostsRoot;
    }

    public String getNginxConfigDir() {
        return nginxConfigDir;
    }

    public void setNginxConfigDir(String nginxConfigDir) {
        this.nginxConfigDir = nginxConfigDir;
    }

    public Object getNginxPort() {
        return nginxPort;
    }

    public void setNginxPort(Integer nginxPort) {
        this.nginxPort = nginxPort;
    }

    public String getNginxProxyPass() {
        return nginxProxyPass;
    }

    public void setNginxProxyPass(String nginxProxyPass) {
        this.nginxProxyPass = nginxProxyPass;
    }
}
