package com.deyatech.station.controller;

import com.deyatech.station.entity.CatalogAggregation;
import com.deyatech.station.vo.CatalogAggregationVo;
import com.deyatech.station.service.CatalogAggregationService;
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
 * 聚合栏目 前端控制器
 * </p>
 * @author: csm.
 * @since 2019-09-11
 */
@Slf4j
@RestController
@RequestMapping("/station/catalogAggregation")
@Api(tags = {"聚合栏目接口"})
public class CatalogAggregationController extends BaseController {
    @Autowired
    CatalogAggregationService catalogAggregationService;

    /**
     * 单个保存或者更新聚合栏目
     *
     * @param catalogAggregation
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新聚合栏目", notes="根据聚合栏目对象保存或者更新聚合栏目信息")
    @ApiImplicitParam(name = "catalogAggregation", value = "聚合栏目对象", required = true, dataType = "CatalogAggregation", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(CatalogAggregation catalogAggregation) {
        log.info(String.format("保存或者更新聚合栏目: %s ", JSONUtil.toJsonStr(catalogAggregation)));
        boolean result = catalogAggregationService.saveOrUpdate(catalogAggregation);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新聚合栏目
     *
     * @param catalogAggregationList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新聚合栏目", notes="根据聚合栏目对象集合批量保存或者更新聚合栏目信息")
    @ApiImplicitParam(name = "catalogAggregationList", value = "聚合栏目对象集合", required = true, allowMultiple = true, dataType = "CatalogAggregation", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<CatalogAggregation> catalogAggregationList) {
        log.info(String.format("批量保存或者更新聚合栏目: %s ", JSONUtil.toJsonStr(catalogAggregationList)));
        boolean result = catalogAggregationService.saveOrUpdateBatch(catalogAggregationList);
        return RestResult.ok(result);
    }

    /**
     * 根据CatalogAggregation对象属性逻辑删除聚合栏目
     *
     * @param catalogAggregation
     * @return
     */
    @PostMapping("/removeByCatalogAggregation")
    @ApiOperation(value="根据CatalogAggregation对象属性逻辑删除聚合栏目", notes="根据聚合栏目对象逻辑删除聚合栏目信息")
    @ApiImplicitParam(name = "catalogAggregation", value = "聚合栏目对象", required = true, dataType = "CatalogAggregation", paramType = "query")
    public RestResult<Boolean> removeByCatalogAggregation(CatalogAggregation catalogAggregation) {
        log.info(String.format("根据CatalogAggregation对象属性逻辑删除聚合栏目: %s ", catalogAggregation));
        boolean result = catalogAggregationService.removeByBean(catalogAggregation);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除聚合栏目
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除聚合栏目", notes="根据聚合栏目对象ID批量逻辑删除聚合栏目信息")
    @ApiImplicitParam(name = "ids", value = "聚合栏目对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除聚合栏目: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = catalogAggregationService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据CatalogAggregation对象属性获取聚合栏目
     *
     * @param catalogAggregation
     * @return
     */
    @GetMapping("/getByCatalogAggregation")
    @ApiOperation(value="根据CatalogAggregation对象属性获取聚合栏目", notes="根据聚合栏目对象属性获取聚合栏目信息")
    @ApiImplicitParam(name = "catalogAggregation", value = "聚合栏目对象", required = false, dataType = "CatalogAggregation", paramType = "query")
    public RestResult<CatalogAggregationVo> getByCatalogAggregation(CatalogAggregation catalogAggregation) {
        catalogAggregation = catalogAggregationService.getByBean(catalogAggregation);
        CatalogAggregationVo catalogAggregationVo = catalogAggregationService.setVoProperties(catalogAggregation);
        log.info(String.format("根据id获取聚合栏目：%s", JSONUtil.toJsonStr(catalogAggregationVo)));
        return RestResult.ok(catalogAggregationVo);
    }

    /**
     * 根据CatalogAggregation对象属性检索所有聚合栏目
     *
     * @param catalogAggregation
     * @return
     */
    @GetMapping("/listByCatalogAggregation")
    @ApiOperation(value="根据CatalogAggregation对象属性检索所有聚合栏目", notes="根据CatalogAggregation对象属性检索所有聚合栏目信息")
    @ApiImplicitParam(name = "catalogAggregation", value = "聚合栏目对象", required = false, dataType = "CatalogAggregation", paramType = "query")
    public RestResult<Collection<CatalogAggregationVo>> listByCatalogAggregation(CatalogAggregation catalogAggregation) {
        Collection<CatalogAggregation> catalogAggregations = catalogAggregationService.listByBean(catalogAggregation);
        Collection<CatalogAggregationVo> catalogAggregationVos = catalogAggregationService.setVoProperties(catalogAggregations);
        log.info(String.format("根据CatalogAggregation对象属性检索所有聚合栏目: %s ",JSONUtil.toJsonStr(catalogAggregationVos)));
        return RestResult.ok(catalogAggregationVos);
    }

    /**
     * 根据CatalogAggregation对象属性分页检索聚合栏目
     *
     * @param catalogAggregation
     * @return
     */
    @GetMapping("/pageByCatalogAggregation")
    @ApiOperation(value="根据CatalogAggregation对象属性分页检索聚合栏目", notes="根据CatalogAggregation对象属性分页检索聚合栏目信息")
    @ApiImplicitParam(name = "catalogAggregation", value = "聚合栏目对象", required = false, dataType = "CatalogAggregation", paramType = "query")
    public RestResult<IPage<CatalogAggregationVo>> pageByCatalogAggregation(CatalogAggregation catalogAggregation) {
        IPage<CatalogAggregationVo> catalogAggregations = catalogAggregationService.pageByBean(catalogAggregation);
        catalogAggregations.setRecords(catalogAggregationService.setVoProperties(catalogAggregations.getRecords()));
        log.info(String.format("根据CatalogAggregation对象属性分页检索聚合栏目: %s ",JSONUtil.toJsonStr(catalogAggregations)));
        return RestResult.ok(catalogAggregations);
    }

}
