package com.deyatech.station.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Page;
import com.deyatech.station.service.PageCatalogService;
import com.deyatech.station.vo.PageVo;
import com.deyatech.station.service.PageService;
import com.deyatech.common.entity.RestResult;
import com.deyatech.template.feign.TemplateFeign;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 页面管理 前端控制器
 * </p>
 * @author: csm.
 * @since 2019-08-01
 */
@Slf4j
@RestController
@RequestMapping("/station/page")
@Api(tags = {"页面管理接口"})
public class PageController extends BaseController {
    @Autowired
    PageService pageService;
    @Autowired
    SiteCache siteCache;
    @Autowired
    TemplateFeign templateFeign;
    @Autowired
    PageCatalogService pageCatalogService;

    /**
     * 单个保存或者更新页面管理
     *
     * @param page
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新页面管理", notes="根据页面管理对象保存或者更新页面管理信息")
    @ApiImplicitParam(name = "page", value = "页面管理对象", required = true, dataType = "Page", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Page page,@RequestParam(value="ids[]",required=false) List<String> ids) {
        log.info(String.format("保存或者更新页面管理: %s ", JSONUtil.toJsonStr(page)));
        boolean result = pageService.saveOrUpdate(page);
        if(CollectionUtil.isNotEmpty(ids)){
            pageCatalogService.updatePageCatalogById(page.getId(),ids);
        }
        if(result){
            pageService.replayPage(page);
        }
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新页面管理
     *
     * @param pageList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新页面管理", notes="根据页面管理对象集合批量保存或者更新页面管理信息")
    @ApiImplicitParam(name = "pageList", value = "页面管理对象集合", required = true, allowMultiple = true, dataType = "Page", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Page> pageList) {
        log.info(String.format("批量保存或者更新页面管理: %s ", JSONUtil.toJsonStr(pageList)));
        boolean result = pageService.saveOrUpdateBatch(pageList);
        return RestResult.ok(result);
    }

    /**
     * 根据Page对象属性逻辑删除页面管理
     *
     * @param page
     * @return
     */
    @PostMapping("/removeByPage")
    @ApiOperation(value="根据Page对象属性逻辑删除页面管理", notes="根据页面管理对象逻辑删除页面管理信息")
    @ApiImplicitParam(name = "page", value = "页面管理对象", required = true, dataType = "Page", paramType = "query")
    public RestResult<Boolean> removeByPage(Page page) {
        log.info(String.format("根据Page对象属性逻辑删除页面管理: %s ", page));
        boolean result = pageService.removeByBean(page);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除页面管理
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除页面管理", notes="根据页面管理对象ID批量逻辑删除页面管理信息")
    @ApiImplicitParam(name = "ids", value = "页面管理对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除页面管理: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = pageService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Page对象属性获取页面管理
     *
     * @param page
     * @return
     */
    @GetMapping("/getByPage")
    @ApiOperation(value="根据Page对象属性获取页面管理", notes="根据页面管理对象属性获取页面管理信息")
    @ApiImplicitParam(name = "page", value = "页面管理对象", required = false, dataType = "Page", paramType = "query")
    public RestResult<PageVo> getByPage(Page page) {
        page = pageService.getByBean(page);
        PageVo pageVo = pageService.setVoProperties(page);
        log.info(String.format("根据id获取页面管理：%s", JSONUtil.toJsonStr(pageVo)));
        return RestResult.ok(pageVo);
    }

    /**
     * 根据Page对象属性检索所有页面管理
     *
     * @param page
     * @return
     */
    @GetMapping("/listByPage")
    @ApiOperation(value="根据Page对象属性检索所有页面管理", notes="根据Page对象属性检索所有页面管理信息")
    @ApiImplicitParam(name = "page", value = "页面管理对象", required = false, dataType = "Page", paramType = "query")
    public RestResult<Collection<PageVo>> listByPage(Page page) {
        Collection<Page> pages = pageService.listByBean(page);
        Collection<PageVo> pageVos = pageService.setVoProperties(pages);
        log.info(String.format("根据Page对象属性检索所有页面管理: %s ",JSONUtil.toJsonStr(pageVos)));
        return RestResult.ok(pageVos);
    }

    /**
     * 根据Page对象属性分页检索页面管理
     *
     * @param page
     * @return
     */
    @GetMapping("/pageByPage")
    @ApiOperation(value="根据Page对象属性分页检索页面管理", notes="根据Page对象属性分页检索页面管理信息")
    @ApiImplicitParam(name = "page", value = "页面管理对象", required = false, dataType = "Page", paramType = "query")
    public RestResult<IPage<PageVo>> pageByPage(Page page) {
        IPage<PageVo> pages = pageService.pageByBean(page);
        pages.setRecords(pageService.setVoProperties(pages.getRecords()));
        pages.setRecords(pages.getRecords().stream().sorted(Comparator.comparing(PageVo::getUpdateTime).reversed()).collect(Collectors.toList()));
        log.info(String.format("根据Page对象属性分页检索页面管理: %s ",JSONUtil.toJsonStr(pages)));
        return RestResult.ok(pages);
    }

    /**
     * 验证当前输入页面路径是否已经存在
     *
     * @param page
     * @return
     */
    @GetMapping("/existsPagePath")
    @ApiOperation(value="验证当前输入页面路径是否已经存在", notes="验证当前输入页面路径是否已经存在")
    @ApiImplicitParam(name = "page", value = "页面管理对象", required = true, dataType = "Page", paramType = "query")
    public RestResult<Boolean> existsPagePath(Page page) {
        log.info(String.format("验证当前输入页面路径是否已经存在: %s ", JSONUtil.toJsonStr(page)));
        String message = pageService.existsPagePath(page);
        if (StrUtil.isEmpty(message)) {
            return RestResult.ok();
        } else {
            return RestResult.ok(message);
        }
    }

    /**
     * 发布静态页
     *
     * @param page
     * @return
     */
    @PostMapping("/replay")
    @ApiOperation(value="发布静态页", notes="发布静态页")
    @ApiImplicitParam(name = "page", value = "页面管理对象", required = true, dataType = "Page", paramType = "query")
    public RestResult<Boolean> replay(Page page) {
        return RestResult.ok(pageService.replayPage(page));
    }


    /**
     * 验证模板是否存在
     *
     * @param page
     * @return
     */
    @GetMapping("/existsTemplatePath")
    @ApiOperation(value="验证当前输入页面路径是否已经存在", notes="验证当前输入页面路径是否已经存在")
    @ApiImplicitParam(name = "page", value = "页面管理对象", required = true, dataType = "Page", paramType = "query")
    public RestResult<Boolean> existsTemplatePath(Page page) {
        String templateRootPath = siteCache.getStationGroupTemplatePathBySiteId(page.getSiteId());
        String templatePath = page.getTemplatePath();
        RestResult<Boolean> result = templateFeign.existsTemplatePath(templateRootPath + templatePath);
        if (!result.getData()) {
            return RestResult.ok("模板地址不存在");
        } else {
            return RestResult.ok("");
        }
    }
}
