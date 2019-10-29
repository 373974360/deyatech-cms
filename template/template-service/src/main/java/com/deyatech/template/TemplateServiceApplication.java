package com.deyatech.template;

import cn.hutool.json.JSONObject;
import com.deyatech.common.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * cms模板模块微服务启动类
 * </p>
 *
 * @author: lee.
 * @since 2019-07-31 16:45
 */
@EnableFeignClients({"com.deyatech.*.feign"})
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.deyatech.**.mapper")
@ComponentScan("com.deyatech")
@ServletComponentScan("com.deyatech.common.filter")
@EnableSwagger2
@EnableCaching
@RestController
public class TemplateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateServiceApplication.class, args);
    }

    @Bean
    GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @GetMapping("/health")
    public JSONObject health() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "UP");
        return jsonObject;
    }
}
