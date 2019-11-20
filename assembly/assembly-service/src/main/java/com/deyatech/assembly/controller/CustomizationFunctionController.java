package com.deyatech.assembly.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.deyatech.assembly.entity.CustomizationTableHead;
import com.deyatech.assembly.service.CustomizationFunctionService;
import com.deyatech.assembly.vo.CustomizationTableHeadVo;
import com.deyatech.assembly.vo.CustomizationTableHeadItemVo;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.CustomizationTypeEnum;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 * @author: lee.
 * @since 2019-10-30
 */
@Slf4j
@RestController
@RequestMapping("/assembly/customizationFunction")
@Api(tags = {"接口"})
public class CustomizationFunctionController extends BaseController {
    @Autowired
    CustomizationFunctionService customizationFunctionService;
    /**
     * 单个保存或者更新
     *
     * @param customizationFunction
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "customizationFunction", value = "对象", required = true, dataType = "CustomizationTableHead", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(CustomizationTableHead customizationFunction) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(customizationFunction)));
        return RestResult.ok(customizationFunctionService.saveOrUpdateData(customizationFunction));
    }

    /**
     * 批量保存或者更新
     *
     * @param customizationFunctions
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "customizationFunctions", value = "对象集合", required = true, allowMultiple = true, dataType = "CustomizationTableHead", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(String customizationFunctions) {
        return RestResult.ok(customizationFunctionService.saveOrUpdateAllData(customizationFunctions));
    }

    /**
     * 获取所有定制功能
     *
     * @return
     */
    @RequestMapping("/getAllCustomizationFunction")
    @ApiOperation(value="获取所有定制功能", notes="获取所有定制功能")
    public RestResult<List<CustomizationTableHeadVo>> getAllCustomizationFunction() {
        return RestResult.ok(customizationFunctionService.getAllCustomizationFunction());
    }

    /**
     * 栏目表头定制功能
     *
     * @return
     */
    @RequestMapping("/getCustomizationFunctionCatalog")
    @ApiOperation(value="获取所有定制功能", notes="获取所有定制功能")
    public RestResult<CustomizationTableHeadVo> getCustomizationFunctionCatalog() {
        return RestResult.ok(customizationFunctionService.getCustomizationFunction(CustomizationTypeEnum.TABLE_HEAD_CATALOG.getValue(), CustomizationTypeEnum.TABLE_HEAD_CATALOG.getCode()));
    }

    /**
     * 内容表头定制功能
     *
     * @return
     */
    @RequestMapping("/getCustomizationFunctionContent")
    @ApiOperation(value="获取所有定制功能", notes="获取所有定制功能")
    public RestResult<CustomizationTableHeadVo> getCustomizationFunctionContent() {
        return RestResult.ok(customizationFunctionService.getCustomizationFunction(CustomizationTypeEnum.TABLE_HEAD_CONTENT.getValue(), CustomizationTypeEnum.TABLE_HEAD_CONTENT.getCode()));
    }

    /**
     * 栏目表头
     *
     * @return
     */
    @RequestMapping("/getTableHeadCatalogData")
    @ApiOperation(value="栏目表头", notes="栏目表头")
    public RestResult<List<CustomizationTableHeadItemVo>> getTableHeadCatalogData () {
        return RestResult.ok(customizationFunctionService.getTableHeadData(CustomizationTypeEnum.TABLE_HEAD_CATALOG.getValue(), CustomizationTypeEnum.TABLE_HEAD_CATALOG.getCode()));
    }

    /**
     * 内容表头
     *
     * @return
     */
    @RequestMapping("/getTableHeadContentData")
    @ApiOperation(value="内容表头", notes="内容表头")
    public RestResult<List<CustomizationTableHeadItemVo>> getTableHeadContentData () {
        return RestResult.ok(customizationFunctionService.getTableHeadData(CustomizationTypeEnum.TABLE_HEAD_CONTENT.getValue(), CustomizationTypeEnum.TABLE_HEAD_CONTENT.getCode()));

    }

    /**
     * 删除所有数据
     *
     * @return
     */
    @RequestMapping("/removeAllData")
    @ApiOperation(value="删除所有", notes="删除所有")
    public RestResult<Boolean> removeAll() {
        customizationFunctionService.removeAllData();
        return RestResult.ok();
    }

    /**
     * 删除分类数据
     *
     * @return
     */
    @RequestMapping("/removeCatalogData")
    @ApiOperation(value="删除所有", notes="删除所有")
    public RestResult<Boolean> removeCatalogData() {
        return RestResult.ok(customizationFunctionService.removeData(CustomizationTypeEnum.TABLE_HEAD_CATALOG.getCode()));
    }

    /**
     * 删除内容数据
     *
     * @return
     */
    @RequestMapping("/removeContentData")
    @ApiOperation(value="删除所有", notes="删除所有")
    public RestResult<Boolean> removeContentData() {
        return RestResult.ok(customizationFunctionService.removeData(CustomizationTypeEnum.TABLE_HEAD_CONTENT.getCode()));
    }
}
