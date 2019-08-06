package com.deyatech.resource.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class StringHelper {
    public static String getObjectValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String processTemplate(String template, Map<String, Object> params) {
        for (String key : params.keySet()) {
            template = StringUtils.replace(template, "${" + key + "}", params.get(key).toString());
        }
        return template;
    }

    public static void main(String[] args) {

        Map<String, Object> map = new HashMap<>();
        map.put("sitePort", "8888");
        map.put("serverNames", StringUtils.join(new String[]{"test1.cms.deyatong.com", "test2.cms.deyatong.com"}, " "));
        map.put("siteRootDir", "/Users/yangyan");
        map.put("proxyPass", "http://localhsot:9999");
        map.put("siteId", "123");
        try {
            File siteNginxTemplateFile = ResourceUtils.getFile("classpath:nginx_site.template");
            String nginxTemplate = FileUtils.readFileToString(siteNginxTemplateFile);
            String nginxContent = StringHelper.processTemplate(nginxTemplate, map);

            String s = StringHelper.processTemplate(nginxContent, map);
            System.out.println(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
