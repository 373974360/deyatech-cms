package com.deyatech.station.controller;

import com.deyatech.station.entity.CatalogTemplate;
import com.deyatech.station.vo.CatalogTemplateVo;
import com.deyatech.station.service.CatalogTemplateService;
import com.deyatech.common.entity.RestResult;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 栏目内容关联 前端控制器
 * </p>
 * @author: ycx
 * @since 2020-01-15
 */
@Slf4j
@RestController
@RequestMapping("/station/catalogTemplate")
@Api(tags = {"栏目内容关联接口"})
public class CatalogTemplateController extends BaseController {
    @Autowired
    CatalogTemplateService catalogTemplateService;

    /**
     * 单个保存或者更新栏目内容关联
     *
     * @param catalogTemplate
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新栏目内容关联", notes="根据栏目内容关联对象保存或者更新栏目内容关联信息")
    @ApiImplicitParam(name = "catalogTemplate", value = "栏目内容关联对象", required = true, dataType = "CatalogTemplate", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(CatalogTemplate catalogTemplate) {
        log.info(String.format("保存或者更新栏目内容关联: %s ", JSONUtil.toJsonStr(catalogTemplate)));
        boolean result = catalogTemplateService.saveOrUpdate(catalogTemplate);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新栏目内容关联
     *
     * @param catalogTemplateList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新栏目内容关联", notes="根据栏目内容关联对象集合批量保存或者更新栏目内容关联信息")
    @ApiImplicitParam(name = "catalogTemplateList", value = "栏目内容关联对象集合", required = true, allowMultiple = true, dataType = "CatalogTemplate", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<CatalogTemplate> catalogTemplateList) {
        log.info(String.format("批量保存或者更新栏目内容关联: %s ", JSONUtil.toJsonStr(catalogTemplateList)));
        boolean result = catalogTemplateService.saveOrUpdateBatch(catalogTemplateList);
        return RestResult.ok(result);
    }

    /**
     * 根据CatalogTemplate对象属性逻辑删除栏目内容关联
     *
     * @param catalogTemplate
     * @return
     */
    @PostMapping("/removeByCatalogTemplate")
    @ApiOperation(value="根据CatalogTemplate对象属性逻辑删除栏目内容关联", notes="根据栏目内容关联对象逻辑删除栏目内容关联信息")
    @ApiImplicitParam(name = "catalogTemplate", value = "栏目内容关联对象", required = true, dataType = "CatalogTemplate", paramType = "query")
    public RestResult<Boolean> removeByCatalogTemplate(CatalogTemplate catalogTemplate) {
        log.info(String.format("根据CatalogTemplate对象属性逻辑删除栏目内容关联: %s ", catalogTemplate));
        boolean result = catalogTemplateService.removeByBean(catalogTemplate);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除栏目内容关联
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除栏目内容关联", notes="根据栏目内容关联对象ID批量逻辑删除栏目内容关联信息")
    @ApiImplicitParam(name = "ids", value = "栏目内容关联对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除栏目内容关联: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = catalogTemplateService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据CatalogTemplate对象属性获取栏目内容关联
     *
     * @param catalogTemplate
     * @return
     */
    @GetMapping("/getByCatalogTemplate")
    @ApiOperation(value="根据CatalogTemplate对象属性获取栏目内容关联", notes="根据栏目内容关联对象属性获取栏目内容关联信息")
    @ApiImplicitParam(name = "catalogTemplate", value = "栏目内容关联对象", required = false, dataType = "CatalogTemplate", paramType = "query")
    public RestResult<CatalogTemplateVo> getByCatalogTemplate(CatalogTemplate catalogTemplate) {
        catalogTemplate = catalogTemplateService.getByBean(catalogTemplate);
        CatalogTemplateVo catalogTemplateVo = catalogTemplateService.setVoProperties(catalogTemplate);
        log.info(String.format("根据id获取栏目内容关联：%s", JSONUtil.toJsonStr(catalogTemplateVo)));
        return RestResult.ok(catalogTemplateVo);
    }

    /**
     * 根据CatalogTemplate对象属性检索所有栏目内容关联
     *
     * @param catalogTemplate
     * @return
     */
    @GetMapping("/listByCatalogTemplate")
    @ApiOperation(value="根据CatalogTemplate对象属性检索所有栏目内容关联", notes="根据CatalogTemplate对象属性检索所有栏目内容关联信息")
    @ApiImplicitParam(name = "catalogTemplate", value = "栏目内容关联对象", required = false, dataType = "CatalogTemplate", paramType = "query")
    public RestResult<Collection<CatalogTemplateVo>> listByCatalogTemplate(CatalogTemplate catalogTemplate) {
        Collection<CatalogTemplate> catalogTemplates = catalogTemplateService.listByBean(catalogTemplate);
        Collection<CatalogTemplateVo> catalogTemplateVos = catalogTemplateService.setVoProperties(catalogTemplates);
        log.info(String.format("根据CatalogTemplate对象属性检索所有栏目内容关联: %s ",JSONUtil.toJsonStr(catalogTemplateVos)));
        return RestResult.ok(catalogTemplateVos);
    }

    /**
     * 根据CatalogTemplate对象属性分页检索栏目内容关联
     *
     * @param catalogTemplate
     * @return
     */
    @GetMapping("/pageByCatalogTemplate")
    @ApiOperation(value="根据CatalogTemplate对象属性分页检索栏目内容关联", notes="根据CatalogTemplate对象属性分页检索栏目内容关联信息")
    @ApiImplicitParam(name = "catalogTemplate", value = "栏目内容关联对象", required = false, dataType = "CatalogTemplate", paramType = "query")
    public RestResult<IPage<CatalogTemplateVo>> pageByCatalogTemplate(CatalogTemplate catalogTemplate) {
        IPage<CatalogTemplateVo> catalogTemplates = catalogTemplateService.pageByBean(catalogTemplate);
        catalogTemplates.setRecords(catalogTemplateService.setVoProperties(catalogTemplates.getRecords()));
        log.info(String.format("根据CatalogTemplate对象属性分页检索栏目内容关联: %s ",JSONUtil.toJsonStr(catalogTemplates)));
        return RestResult.ok(catalogTemplates);
    }

}
