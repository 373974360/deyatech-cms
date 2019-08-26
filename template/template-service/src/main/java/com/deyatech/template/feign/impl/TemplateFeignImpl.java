package com.deyatech.template.feign.impl;

import com.deyatech.common.entity.RestResult;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.template.feign.TemplateFeign;
import com.deyatech.template.service.StationGitService;
import com.deyatech.template.thymeleaf.TemplateContextUtils;
import com.deyatech.template.thymeleaf.ThymeleafUtil;
import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/6 16:29
 */
@RestController
public class TemplateFeignImpl implements TemplateFeign {

    @Autowired
    StationGitService stationGitService;
    @Autowired
    private ThymeleafUtil thymeleafUtil;
    @Autowired
    TemplateContextUtils templateContextUtils;


    @Override
    public RestResult<String> getTemplateFiles(String siteId) {
        return RestResult.ok(stationGitService.getTemplateAllFiles(siteId));
    }
    @Override
    public RestResult<String> getPageSuffix() {
        return RestResult.ok(TemplateConstants.PAGE_SUFFIX);
    }

    @Override
    public RestResult generateStaticPage(String templateRootPath, String templatePath, File distFile, Map<String, Object> varMap) {
        thymeleafUtil.thyToStaticFile(templateRootPath,templatePath,distFile,varMap);
        return RestResult.ok();
    }

    @Override
    public RestResult generateStaticTemplate(TemplateVo templateVo) {
        return RestResult.ok(templateContextUtils.genStaticContentPage(templateVo));
    }

    @Override
    public RestResult<String> thyToString(String siteTemplateRoot, String templatePath, Map<String, Object> varMap) {
        return RestResult.ok(thymeleafUtil.thyToString(siteTemplateRoot,templatePath,varMap));
    }
}
