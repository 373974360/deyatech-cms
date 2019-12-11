package com.deyatech.station.controller;

import cn.hutool.json.JSONUtil;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.entity.PageCatalog;
import com.deyatech.station.service.PageCatalogService;
import com.deyatech.station.vo.PageCatalogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 页面管理 前端控制器
 * </p>
 * @author: csm.
 * @since 2019-08-01
 */
@Slf4j
@RestController
@RequestMapping("/station/pageCatelog")
@Api(tags = {"页面绑定栏目管理接口"})
public class PageCatelogController extends BaseController {

    @Autowired
    PageCatalogService pageCatalogService;



    /**
     * 根据PageCatalog对象属性检索所有页面管理
     *
     * @param pageCatalog
     * @return
     */
    @GetMapping("/listByPageCatalog")
    @ApiOperation(value="根据PageCatalog对象属性检索所有页面绑定栏目管理", notes="根据PageCatalog对象属性检索所有页面绑定栏目管理信息")
    @ApiImplicitParam(name = "pageCatalog", value = "页面绑定栏目管理对象", required = false, dataType = "pageCatalog", paramType = "query")
    public RestResult<Collection<PageCatalogVo>> listByPage(PageCatalog pageCatalog) {
        Collection<PageCatalog> pageCatalogs = pageCatalogService.listByBean(pageCatalog);
        Collection<PageCatalogVo> pageCatalogVos = pageCatalogService.setVoProperties(pageCatalogs);
        log.info(String.format("根据Page对象属性检索所有页面管理: %s ",JSONUtil.toJsonStr(pageCatalogVos)));
        return RestResult.ok(pageCatalogVos);
    }

}
