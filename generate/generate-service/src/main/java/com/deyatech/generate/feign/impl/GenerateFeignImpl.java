package com.deyatech.generate.feign.impl;

import com.deyatech.common.entity.RestResult;
import com.deyatech.generate.feign.GenerateFeign;
import com.deyatech.generate.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/12/9 14:33
 */
@RestController
public class GenerateFeignImpl implements GenerateFeign {

    @Autowired
    PageService pageService;

    @Override
    public RestResult replyPageByCatalog(String catalogId) {
        return RestResult.ok(pageService.replyPageByCatalog(catalogId));
    }
}
