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
    static ObjectMapper mapper = new ObjectMapper();
//
//    /**
//     * 根据CustomizationFunction对象属性逻辑删除
//     *
//     * @param customizationFunction
//     * @return
//     */
//    @PostMapping("/removeByCustomizationFunction")
//    @ApiOperation(value="根据CustomizationFunction对象属性逻辑删除", notes="根据对象逻辑删除信息")
//    @ApiImplicitParam(name = "customizationFunction", value = "对象", required = true, dataType = "CustomizationTableHead", paramType = "query")
//    public RestResult<Boolean> removeByCustomizationFunction(CustomizationTableHead customizationFunction) {
//        log.info(String.format("根据CustomizationFunction对象属性逻辑删除: %s ", customizationFunction));
//        boolean result = customizationFunctionService.removeByBean(customizationFunction);
//        return RestResult.ok(result);
//    }
//
//
//    /**
//     * 根据ID批量逻辑删除
//     *
//     * @param ids
//     * @return
//     */
//    @PostMapping("/removeByIds")
//    @ApiOperation(value="根据ID批量逻辑删除", notes="根据对象ID批量逻辑删除信息")
//    @ApiImplicitParam(name = "ids", value = "对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
//    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
//        log.info(String.format("根据id批量删除: %s ", JSONUtil.toJsonStr(ids)));
//        boolean result = customizationFunctionService.removeByIds(ids);
//        return RestResult.ok(result);
//    }
//
//    /**
//     * 根据CustomizationFunction对象属性获取
//     *
//     * @param customizationFunction
//     * @return
//     */
//    @GetMapping("/getByCustomizationFunction")
//    @ApiOperation(value="根据CustomizationFunction对象属性获取", notes="根据对象属性获取信息")
//    @ApiImplicitParam(name = "customizationFunction", value = "对象", required = false, dataType = "CustomizationTableHead", paramType = "query")
//    public RestResult<CustomizationTableHeadVo> getByCustomizationFunction(CustomizationTableHead customizationFunction) {
//        customizationFunction = customizationFunctionService.getByBean(customizationFunction);
//        CustomizationTableHeadVo customizationFunctionVo = customizationFunctionService.setVoProperties(customizationFunction);
//        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(customizationFunctionVo)));
//        return RestResult.ok(customizationFunctionVo);
//    }
//
//    /**
//     * 根据CustomizationFunction对象属性检索所有
//     *
//     * @param customizationFunction
//     * @return
//     */
//    @GetMapping("/listByCustomizationFunction")
//    @ApiOperation(value="根据CustomizationFunction对象属性检索所有", notes="根据CustomizationFunction对象属性检索所有信息")
//    @ApiImplicitParam(name = "customizationFunction", value = "对象", required = false, dataType = "CustomizationTableHead", paramType = "query")
//    public RestResult<Collection<CustomizationTableHeadVo>> listByCustomizationFunction(CustomizationTableHead customizationFunction) {
//        Collection<CustomizationTableHead> customizationFunctions = customizationFunctionService.listByBean(customizationFunction);
//        Collection<CustomizationTableHeadVo> customizationFunctionVos = customizationFunctionService.setVoProperties(customizationFunctions);
//        log.info(String.format("根据CustomizationFunction对象属性检索所有: %s ",JSONUtil.toJsonStr(customizationFunctionVos)));
//        return RestResult.ok(customizationFunctionVos);
//    }
//
//    /**
//     * 根据CustomizationFunction对象属性分页检索
//     *
//     * @param customizationFunction
//     * @return
//     */
//    @GetMapping("/pageByCustomizationFunction")
//    @ApiOperation(value="根据CustomizationFunction对象属性分页检索", notes="根据CustomizationFunction对象属性分页检索信息")
//    @ApiImplicitParam(name = "customizationFunction", value = "对象", required = false, dataType = "CustomizationTableHead", paramType = "query")
//    public RestResult<IPage<CustomizationTableHeadVo>> pageByCustomizationFunction(CustomizationTableHead customizationFunction) {
//        IPage<CustomizationTableHeadVo> customizationFunctions = customizationFunctionService.pageByBean(customizationFunction);
//        customizationFunctions.setRecords(customizationFunctionService.setVoProperties(customizationFunctions.getRecords()));
//        log.info(String.format("根据CustomizationFunction对象属性分页检索: %s ",JSONUtil.toJsonStr(customizationFunctions)));
//        return RestResult.ok(customizationFunctions);
//    }



    //    /**
//     * 获取定制功能
//     *
//     * @return
//     */
//    @RequestMapping("/getCustomizationFunction")
//    @ApiOperation(value="获取定制功能", notes="获取定制功能")
//    @ApiImplicitParam(name = "type", value = "定制功能类型", required = true, dataType = "String", paramType = "query")
//    public RestResult getCustomizationFunction(String type) {
//        QueryWrapper<CustomizationTableHead> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", UserContextHelper.getUserId());
//        queryWrapper.eq("type_", type);
//        CustomizationTableHead customizationFunction = customizationFunctionService.getOne(queryWrapper);
//        return RestResult.ok(customizationFunctionService.setVoProperties(customizationFunction));
//    }

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
        boolean result = customizationFunctionService.saveOrUpdate(customizationFunction);
        return RestResult.ok(result);
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
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, CustomizationTableHead.class);
        List<CustomizationTableHead> customizationFunctionList;
        try {
            customizationFunctionList = mapper.readValue(customizationFunctions, javaType);
        } catch (IOException e) {
            return RestResult.error("数据转换错误");
        }
        if (CollectionUtil.isNotEmpty(customizationFunctionList)) {
            String userId = UserContextHelper.getUserId();
            for (CustomizationTableHead cf : customizationFunctionList) {
                cf.setUserId(userId);
            }
        }
        boolean result = customizationFunctionService.saveOrUpdateBatch(customizationFunctionList);
        return RestResult.ok(result);
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
}
