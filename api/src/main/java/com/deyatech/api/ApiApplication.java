package com.deyatech.api;

import cn.hutool.json.JSONObject;
import com.deyatech.common.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * <p>
 * api模块微服务启动类
 * </p>
 *
 * @author: csm
 * @since: 2019/11/01
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.deyatech")
@EnableSwagger2
@EnableFeignClients({"com.deyatech.*.feign"})
@ServletComponentScan("com.deyatech.*.filter")
@RestController
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
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
