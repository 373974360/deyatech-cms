package com.deyatech.station.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.service.TemplateService;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.service.CatalogService;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.entity.CascaderResult;
import com.deyatech.common.utils.CascaderUtil;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 栏目 前端控制器
 * </p>
 * @author: csm.
 * @since 2019-08-02
 */
@Slf4j
@RestController
@RequestMapping("/station/catalog")
@Api(tags = {"栏目接口"})
public class CatalogController extends BaseController {
    @Autowired
    CatalogService catalogService;
    @Autowired
    TemplateService templateService;
    @Autowired
    SiteCache siteCache;
    /**
     * 单个保存或者更新栏目
     *
     * @param catalogVo
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新栏目", notes="根据栏目对象保存或者更新栏目信息")
    @ApiImplicitParam(name = "catalogVo", value = "栏目扩展对象", required = true, dataType = "CatalogVo", paramType = "query")
    public RestResult saveOrUpdate(CatalogVo catalogVo) {
        log.info(String.format("保存或者更新栏目: %s ", JSONUtil.toJsonStr(catalogVo)));
        int count = templateService.countTemplateByCatalogId(catalogVo.getParentId());
        if (count > 0) {
            return RestResult.error("当前栏目下已存在内容，不能添加栏目");
        }
        boolean result = catalogService.saveOrUpdate(catalogVo);
        siteCache.cacheSite(catalogVo.getSiteId());
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新栏目
     *
     * @param catalogList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新栏目", notes="根据栏目对象集合批量保存或者更新栏目信息")
    @ApiImplicitParam(name = "catalogList", value = "栏目对象集合", required = true, allowMultiple = true, dataType = "Catalog", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Catalog> catalogList) {
        log.info(String.format("批量保存或者更新栏目: %s ", JSONUtil.toJsonStr(catalogList)));
        boolean result = catalogService.saveOrUpdateBatch(catalogList);
        return RestResult.ok(result);
    }

    /**
     * 根据Catalog对象属性逻辑删除栏目
     *
     * @param catalog
     * @return
     */
    @PostMapping("/removeByCatalog")
    @ApiOperation(value="根据Catalog对象属性逻辑删除栏目", notes="根据栏目对象逻辑删除栏目信息")
    @ApiImplicitParam(name = "catalog", value = "栏目对象", required = true, dataType = "Catalog", paramType = "query")
    public RestResult<Boolean> removeByCatalog(Catalog catalog) {
        log.info(String.format("根据Catalog对象属性逻辑删除栏目: %s ", catalog));
        boolean result = catalogService.removeByBean(catalog);
        siteCache.cacheSite(catalog.getSiteId());
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除栏目
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除栏目", notes="根据栏目对象ID批量逻辑删除栏目信息")
    @ApiImplicitParam(name = "ids", value = "栏目对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除栏目: %s ", JSONUtil.toJsonStr(ids)));
        int count = 0;
        if (CollectionUtil.isNotEmpty(ids)) {
            for (String id : ids) {
                count += templateService.countTemplateByCatalogId(id);
            }
        }
        if (count > 0) {
            return RestResult.error("当前栏目下已存在内容，不能删除栏目");
        }
        String siteId = null;
        if (CollectionUtil.isNotEmpty(ids)) {
            Catalog catalog = catalogService.getById(ids.get(0));
            if (Objects.nonNull(catalog)) {
                siteId = catalog.getSiteId();
            }
        }
        boolean result = catalogService.removeByIds(ids);
        if (Objects.nonNull(siteId)) {
            siteCache.cacheSite(siteId);
        }
        return RestResult.ok(result);
    }

    /**
     * 根据Catalog对象属性获取栏目
     *
     * @param catalog
     * @return
     */
    @GetMapping("/getByCatalog")
    @ApiOperation(value="根据Catalog对象属性获取栏目", notes="根据栏目对象属性获取栏目信息")
    @ApiImplicitParam(name = "catalog", value = "栏目对象", required = false, dataType = "Catalog", paramType = "query")
    public RestResult<CatalogVo> getByCatalog(Catalog catalog) {
        catalog = catalogService.getByBean(catalog);
        CatalogVo catalogVo = catalogService.setVoProperties(catalog);
        log.info(String.format("根据id获取栏目：%s", JSONUtil.toJsonStr(catalogVo)));
        return RestResult.ok(catalogVo);
    }

    /**
     * 根据Catalog对象属性检索所有栏目
     *
     * @param catalog
     * @return
     */
    @GetMapping("/listByCatalog")
    @ApiOperation(value="根据Catalog对象属性检索所有栏目", notes="根据Catalog对象属性检索所有栏目信息")
    @ApiImplicitParam(name = "catalog", value = "栏目对象", required = false, dataType = "Catalog", paramType = "query")
    public RestResult<Collection<CatalogVo>> listByCatalog(Catalog catalog) {
        Collection<Catalog> catalogs = catalogService.listByBean(catalog);
        Collection<CatalogVo> catalogVos = catalogService.setVoProperties(catalogs);
        log.info(String.format("根据Catalog对象属性检索所有栏目: %s ",JSONUtil.toJsonStr(catalogVos)));
        return RestResult.ok(catalogVos);
    }

    /**
     * 根据Catalog对象属性分页检索栏目
     *
     * @param catalog
     * @return
     */
    @GetMapping("/pageByCatalog")
    @ApiOperation(value="根据Catalog对象属性分页检索栏目", notes="根据Catalog对象属性分页检索栏目信息")
    @ApiImplicitParam(name = "catalog", value = "栏目对象", required = false, dataType = "Catalog", paramType = "query")
    public RestResult<IPage<CatalogVo>> pageByCatalog(Catalog catalog) {
        IPage<CatalogVo> catalogs = catalogService.pageByBean(catalog);
        catalogs.setRecords(catalogService.setVoProperties(catalogs.getRecords()));
        log.info(String.format("根据Catalog对象属性分页检索栏目: %s ",JSONUtil.toJsonStr(catalogs)));
        return RestResult.ok(catalogs);
    }

    /**
     * 获取栏目的tree对象
     *
     * @param catalog
     * @return
     */
    @GetMapping("/getTree")
    @ApiOperation(value="获取栏目的tree对象", notes="获取栏目的tree对象")
    public RestResult<Collection<CatalogVo>> getCatalogTree(Catalog catalog) {
        Collection<CatalogVo> catalogTree = catalogService.getCatalogTree(catalog);
        log.info(String.format("获取栏目的tree对象: %s ",JSONUtil.toJsonStr(catalogTree)));
        return RestResult.ok(catalogTree);
    }

    /**
     * 获取栏目的tree对象
     *
     * @param siteIds
     * @return
     */
    @RequestMapping("/getCatalogTreeBySiteIds")
    @ApiOperation(value="获取栏目的tree对象", notes="获取栏目的tree对象")
    public RestResult<Collection<CatalogVo>> getCatalogTreeBySiteIds(@RequestParam("siteIds[]") List<String> siteIds) {
        Collection<CatalogVo> catalogTree = catalogService.getCatalogTreeBySiteIds(siteIds);
        log.info(String.format("获取栏目的tree对象: %s ",JSONUtil.toJsonStr(catalogTree)));
        return RestResult.ok(catalogTree);
    }

    /**
     * 获取栏目的级联对象
     *
     * @param catalog
     * @return
     */
    @GetMapping("/getCascader")
    @ApiOperation(value="获取栏目的级联对象", notes="获取栏目的级联对象")
    @ApiImplicitParam(name = "catalog", value = "catalog", required = false, dataType = "Catalog", paramType = "query")
    public RestResult<List<CascaderResult>> getCascader(Catalog catalog) {
        Collection<CatalogVo> catalogVos = catalogService.getCatalogTree(catalog);
        List<CascaderResult> cascaderResults = CascaderUtil.getResult("Id", "Name","TreePosition", catalog.getId(), catalogVos);
        log.info(String.format("获取栏目的级联对象: %s ",JSONUtil.toJsonStr(cascaderResults)));
        return RestResult.ok(cascaderResults);
    }

    /**
     * 验证当前输入栏目名称是否已经存在
     *
     * @param catalog
     * @return
     */
    @GetMapping("/existsName")
    @ApiOperation(value="验证当前输入栏目名称是否已经存在", notes="验证当前输入栏目名称是否已经存在")
    @ApiImplicitParam(name = "catalog", value = "catalog", required = false, dataType = "Catalog", paramType = "query")
    public RestResult<Boolean> existsName(Catalog catalog) {
        log.info(String.format("验证当前输入栏目名称是否已经存在: %s ", JSONUtil.toJsonStr(catalog)));
        boolean result = catalogService.existsName(catalog);
        return RestResult.ok(result);
    }

    /**
     * 验证当前输入栏目别名是否已经存在
     *
     * @param catalog
     * @return
     */
    @GetMapping("/existsAliasName")
    @ApiOperation(value="验证当前输入栏目别名是否已经存在", notes="验证当前输入栏目别名是否已经存在")
    @ApiImplicitParam(name = "catalog", value = "catalog", required = false, dataType = "Catalog", paramType = "query")
    public RestResult<Boolean> existsAliasName(Catalog catalog) {
        log.info(String.format("验证当前输入栏目别名是否已经存在: %s ", JSONUtil.toJsonStr(catalog)));
        boolean result = catalogService.existsAliasName(catalog);
        return RestResult.ok(result);
    }

    /**
     * 验证当前输入栏目英文名称是否已经存在
     *
     * @param catalog
     * @return
     */
    @GetMapping("/existsEname")
    @ApiOperation(value="验证当前输入栏目英文名称是否已经存在", notes="验证当前输入栏目英文名称是否已经存在")
    @ApiImplicitParam(name = "catalog", value = "catalog", required = false, dataType = "Catalog", paramType = "query")
    public RestResult<Boolean> existsEname(Catalog catalog) {
        log.info(String.format("验证当前输入栏目英文名称是否已经存在: %s ", JSONUtil.toJsonStr(catalog)));
        boolean result = catalogService.existsEname(catalog);
        return RestResult.ok(result);
    }

    /**
     * 检查栏目下有无内容
     *
     * @param id
     * @return
     */
    @RequestMapping("/hasTemplate")
    @ApiOperation(value="检查栏目下有无内容", notes="检查栏目下有无内容")
    @ApiImplicitParam(name = "id", value = "栏目编号", required = true, dataType = "String", paramType = "query")
    public RestResult<Boolean> hasTemplate(String id) {
        log.info(String.format("检查栏目下有无内容: id = %s",id));
        int count = templateService.countTemplateByCatalogId(id);
        return RestResult.ok(count > 0 ? true : false);
    }

    /**
     * 获取用户栏目的tree对象
     *
     * @param catalog
     * @return
     */
    @GetMapping("/getUserCatalogTree")
    @ApiOperation(value="获取用户栏目的tree对象", notes="获取用户栏目的tree对象")
    public RestResult<Collection<CatalogVo>> getUserCatalogTree(Catalog catalog) {
        Collection<CatalogVo> catalogTree = catalogService.getUserCatalogTree(catalog);
        log.info(String.format("获取栏目的tree对象: %s ",JSONUtil.toJsonStr(catalogTree)));
        return RestResult.ok(catalogTree);
    }
}
