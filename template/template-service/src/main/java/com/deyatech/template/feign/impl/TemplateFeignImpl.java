package com.deyatech.template.feign.impl;

import com.deyatech.common.entity.RestResult;
import com.deyatech.template.feign.TemplateFeign;
import com.deyatech.template.service.StationGitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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

    @Override
    public RestResult<String> getTemplateFiles(String siteId,String path) {
        return RestResult.ok(stationGitService.getTemplateFiles(siteId,path));
    }
}
