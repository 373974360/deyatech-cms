package com.deyatech.template.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/2 12:21
 */
@Component
@Slf4j
public class TemplateCache {
    @Autowired
    CacheManager cacheManager;

    public String getTemplate(String Cachekey,String key) {
        return this.cacheManager.getCache("template_"+Cachekey).get(key, String.class);
    }
}
