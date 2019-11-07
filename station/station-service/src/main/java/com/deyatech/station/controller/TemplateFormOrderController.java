package com.deyatech.station.controller;

import com.deyatech.station.entity.TemplateFormOrder;
import com.deyatech.station.vo.TemplateFormOrderVo;
import com.deyatech.station.service.TemplateFormOrderService;
import com.deyatech.common.entity.RestResult;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 内容表单顺 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-11-04
 */
@Slf4j
@RestController
@RequestMapping("/station/templateFormOrder")
@Api(tags = {"内容表单顺接口"})
public class TemplateFormOrderController extends BaseController {
    @Autowired
    TemplateFormOrderService templateFormOrderService;

    /**
     * 获取表单顺序
     *
     * @param collectionId
     * @return
     */
    @RequestMapping("/getFormOrderByCollectionId")
    public RestResult getFormOrderByCollectionId(String collectionId) {
        return RestResult.ok(templateFormOrderService.getFormOrderByCollectionId(collectionId));
    }

    /**
     * 获取排序数据
     *
     * @param collectionId
     * @return
     */
    @RequestMapping("/getSortDataByCollectionId")
    public RestResult getSortDataByCollectionId(String collectionId) {
        return RestResult.ok(templateFormOrderService.getSortDataByCollectionId(collectionId));
    }

    /**
     * 保存或者更新
     *
     * @param json
     * @return
     */
    @RequestMapping("/saveOrUpdateByJson")
    @ApiOperation(value="保存或者更新", notes="保存或者更新")
    @ApiImplicitParam(name = "json", value = "数据", required = true, dataType = "json", paramType = "query")
    public RestResult<Boolean> saveOrUpdateByJson(String collectionId, String json) {
        return RestResult.ok(templateFormOrderService.saveOrUpdateByJson(collectionId, json));
    }

    /**
     * 获取元数据集列表
     *
     * @param enName
     */
    @RequestMapping("/getCollectionList")
    @ApiOperation(value="获取元数据集列表", notes="获取元数据集列表")
    @ApiImplicitParam(name = "enName", value = "数据", required = true, dataType = "String", paramType = "query")
    public RestResult getCollectionList(String enName) {
       return RestResult.ok(templateFormOrderService.getCollectionList(enName));
    }

    /**
     * 单个保存或者更新内容表单顺
     *
     * @param templateFormOrder
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新内容表单顺", notes="根据内容表单顺对象保存或者更新内容表单顺信息")
    @ApiImplicitParam(name = "templateFormOrder", value = "内容表单顺对象", required = true, dataType = "TemplateFormOrder", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(TemplateFormOrder templateFormOrder) {
        log.info(String.format("保存或者更新内容表单顺: %s ", JSONUtil.toJsonStr(templateFormOrder)));
        boolean result = templateFormOrderService.saveOrUpdate(templateFormOrder);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新内容表单顺
     *
     * @param templateFormOrderList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新内容表单顺", notes="根据内容表单顺对象集合批量保存或者更新内容表单顺信息")
    @ApiImplicitParam(name = "templateFormOrderList", value = "内容表单顺对象集合", required = true, allowMultiple = true, dataType = "TemplateFormOrder", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<TemplateFormOrder> templateFormOrderList) {
        log.info(String.format("批量保存或者更新内容表单顺: %s ", JSONUtil.toJsonStr(templateFormOrderList)));
        boolean result = templateFormOrderService.saveOrUpdateBatch(templateFormOrderList);
        return RestResult.ok(result);
    }

    /**
     * 根据TemplateFormOrder对象属性逻辑删除内容表单顺
     *
     * @param templateFormOrder
     * @return
     */
    @PostMapping("/removeByTemplateFormOrder")
    @ApiOperation(value="根据TemplateFormOrder对象属性逻辑删除内容表单顺", notes="根据内容表单顺对象逻辑删除内容表单顺信息")
    @ApiImplicitParam(name = "templateFormOrder", value = "内容表单顺对象", required = true, dataType = "TemplateFormOrder", paramType = "query")
    public RestResult<Boolean> removeByTemplateFormOrder(TemplateFormOrder templateFormOrder) {
        log.info(String.format("根据TemplateFormOrder对象属性逻辑删除内容表单顺: %s ", templateFormOrder));
        boolean result = templateFormOrderService.removeByBean(templateFormOrder);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除内容表单顺
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除内容表单顺", notes="根据内容表单顺对象ID批量逻辑删除内容表单顺信息")
    @ApiImplicitParam(name = "ids", value = "内容表单顺对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除内容表单顺: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = templateFormOrderService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据TemplateFormOrder对象属性获取内容表单顺
     *
     * @param templateFormOrder
     * @return
     */
    @GetMapping("/getByTemplateFormOrder")
    @ApiOperation(value="根据TemplateFormOrder对象属性获取内容表单顺", notes="根据内容表单顺对象属性获取内容表单顺信息")
    @ApiImplicitParam(name = "templateFormOrder", value = "内容表单顺对象", required = false, dataType = "TemplateFormOrder", paramType = "query")
    public RestResult<TemplateFormOrderVo> getByTemplateFormOrder(TemplateFormOrder templateFormOrder) {
        templateFormOrder = templateFormOrderService.getByBean(templateFormOrder);
        TemplateFormOrderVo templateFormOrderVo = templateFormOrderService.setVoProperties(templateFormOrder);
        log.info(String.format("根据id获取内容表单顺：%s", JSONUtil.toJsonStr(templateFormOrderVo)));
        return RestResult.ok(templateFormOrderVo);
    }

    /**
     * 根据TemplateFormOrder对象属性检索所有内容表单顺
     *
     * @param templateFormOrder
     * @return
     */
    @GetMapping("/listByTemplateFormOrder")
    @ApiOperation(value="根据TemplateFormOrder对象属性检索所有内容表单顺", notes="根据TemplateFormOrder对象属性检索所有内容表单顺信息")
    @ApiImplicitParam(name = "templateFormOrder", value = "内容表单顺对象", required = false, dataType = "TemplateFormOrder", paramType = "query")
    public RestResult<Collection<TemplateFormOrderVo>> listByTemplateFormOrder(TemplateFormOrder templateFormOrder) {
        Collection<TemplateFormOrder> templateFormOrders = templateFormOrderService.listByBean(templateFormOrder);
        Collection<TemplateFormOrderVo> templateFormOrderVos = templateFormOrderService.setVoProperties(templateFormOrders);
        log.info(String.format("根据TemplateFormOrder对象属性检索所有内容表单顺: %s ",JSONUtil.toJsonStr(templateFormOrderVos)));
        return RestResult.ok(templateFormOrderVos);
    }

    /**
     * 根据TemplateFormOrder对象属性分页检索内容表单顺
     *
     * @param templateFormOrder
     * @return
     */
    @GetMapping("/pageByTemplateFormOrder")
    @ApiOperation(value="根据TemplateFormOrder对象属性分页检索内容表单顺", notes="根据TemplateFormOrder对象属性分页检索内容表单顺信息")
    @ApiImplicitParam(name = "templateFormOrder", value = "内容表单顺对象", required = false, dataType = "TemplateFormOrder", paramType = "query")
    public RestResult<IPage<TemplateFormOrderVo>> pageByTemplateFormOrder(TemplateFormOrder templateFormOrder) {
        IPage<TemplateFormOrderVo> templateFormOrders = templateFormOrderService.pageByBean(templateFormOrder);
        templateFormOrders.setRecords(templateFormOrderService.setVoProperties(templateFormOrders.getRecords()));
        log.info(String.format("根据TemplateFormOrder对象属性分页检索内容表单顺: %s ",JSONUtil.toJsonStr(templateFormOrders)));
        return RestResult.ok(templateFormOrders);
    }

}
