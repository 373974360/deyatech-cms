package com.deyatech.station.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baidu.aip.nlp.AipNlp;
import com.deyatech.common.exception.BusinessException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "baidu")
@Configuration
@Data
public class AipNlpConfig {
    private String appId;
    private String apiKey;
    private String secretKey;

    private static AipNlp aipNlp = null;

    public AipNlp getInstance() {
        if (StrUtil.isEmpty(this.appId) || StrUtil.isEmpty(this.apiKey) || StrUtil.isEmpty(this.secretKey)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "缺少配置信息");
        }
        if (aipNlp == null) {
            aipNlp = new AipNlp(this.appId, this.apiKey, this.secretKey);
            aipNlp.setConnectionTimeoutInMillis(60000);
            aipNlp.setSocketTimeoutInMillis(60000);
        }
        return aipNlp;
    }
}
