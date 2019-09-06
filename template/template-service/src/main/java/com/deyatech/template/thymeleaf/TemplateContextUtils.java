package com.deyatech.template.thymeleaf;

import com.deyatech.common.entity.RestResult;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.template.thymeleaf.tools.CatalogExpressionObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 各类模板渲染数据工具类
 */
@Component
@Slf4j
public class TemplateContextUtils {


    @Autowired
    ThymeleafUtil thymeleafUtil;
    @Autowired
    StationFeign stationFeign;
    @Autowired
    CatalogExpressionObject catalogExpressionObject;

    /**
     * 内容页模板变量
     *
     * @return
     */
    public Map<String, Object> contentPageVarMap(TemplateVo templateVo) {
        Map<String,Object> map = new HashMap<>();
        map.put("site",stationFeign.getStationGroupById(templateVo.getSiteId()).getData());
        map.put("catalog",catalogExpressionObject.getCatalog(templateVo.getSiteId(),templateVo.getCmsCatalogId()));
        map.put("infoData",templateVo);
        return map;
    }



    /**
     * 生成内容页的静态页面
     */
    public boolean genStaticContentPage(TemplateVo templateVo) {
        String siteRootPath = stationFeign.getStationGroupRootPath(templateVo.getSiteId()).getData();
        String templateRootPath = stationFeign.getStationGroupTemplatePathBySiteId(templateVo.getSiteId()).getData();
        boolean result = false;
        try {
            if (templateRootPath == null) {
                throw new RuntimeException(String.format("模板根路径获取失败(缓存中没有%s)", templateVo.getSiteId()));
            }
            //生成内容页的静态页面
            Map<String, Object> varMap = this.contentPageVarMap(templateVo);
            File distFile = new File(siteRootPath, templateVo.getUrl());
            String templatePath = templateVo.getTemplatePath();//内容模板路径
            Map<String, Object> models = new HashMap<>();
            models.put("distFile", distFile);
            models.put("varMap", varMap);
            getThyToStaticFile(templateRootPath, templatePath, models);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成静态页面发生错误", e);
        }
        return result;
    }
    public RestResult getThyToStaticFile(String templateRootPath, String templatePath, Map<String, Object> models) {
        File distFile = null;
        try {
            distFile = new File(models.get("distFile").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> varMap = null;
        try {
            varMap = (Map<String, Object>) models.get("varMap");
        } catch (Exception e) {
            e.printStackTrace();
        }
        thymeleafUtil.thyToStaticFile(templateRootPath, templatePath, distFile, varMap);
        return RestResult.ok();
    }
}
