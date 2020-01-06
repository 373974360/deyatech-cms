package com.deyatech.cas.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * jwt配置信息
 * </p>
 *
 * @author: lee.
 * @since: 2018-12-21 15:20
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * 公钥存储路径
     */
    private String pubKeyPath;

    /**
     * 私钥存储路径
     */
    private String priKeyPath;

    /**
     * token头部标记名称
     */
    private String tokenHeader;

    /**
     * member - token头部标记名称
     */
    private String memberTokenHeader;

    /**
     * 缓存时间
     */
    private Integer buffer;

    /**
     * 过期时间
     */
    private Integer xpire;

}
