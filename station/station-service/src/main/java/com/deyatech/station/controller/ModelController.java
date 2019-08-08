package com.deyatech.station.controller;

import com.deyatech.station.entity.Model;
import com.deyatech.station.vo.ModelVo;
import com.deyatech.station.service.ModelService;
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
 * 内容模型 前端控制器
 * </p>
 * @author: csm.
 * @since 2019-08-06
 */
@Slf4j
@RestController
@RequestMapping("/station/model")
@Api(tags = {"内容模型接口"})
public class ModelController extends BaseController {
    @Autowired
    ModelService modelService;

    /**
     * 单个保存或者更新内容模型
     *
     * @param model
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新内容模型", notes="根据内容模型对象保存或者更新内容模型信息")
    @ApiImplicitParam(name = "model", value = "内容模型对象", required = true, dataType = "Model", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Model model) {
        log.info(String.format("保存或者更新内容模型: %s ", JSONUtil.toJsonStr(model)));
        boolean result = modelService.saveOrUpdate(model);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新内容模型
     *
     * @param modelList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新内容模型", notes="根据内容模型对象集合批量保存或者更新内容模型信息")
    @ApiImplicitParam(name = "modelList", value = "内容模型对象集合", required = true, allowMultiple = true, dataType = "Model", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Model> modelList) {
        log.info(String.format("批量保存或者更新内容模型: %s ", JSONUtil.toJsonStr(modelList)));
        boolean result = modelService.saveOrUpdateBatch(modelList);
        return RestResult.ok(result);
    }

    /**
     * 根据Model对象属性逻辑删除内容模型
     *
     * @param model
     * @return
     */
    @PostMapping("/removeByModel")
    @ApiOperation(value="根据Model对象属性逻辑删除内容模型", notes="根据内容模型对象逻辑删除内容模型信息")
    @ApiImplicitParam(name = "model", value = "内容模型对象", required = true, dataType = "Model", paramType = "query")
    public RestResult<Boolean> removeByModel(Model model) {
        log.info(String.format("根据Model对象属性逻辑删除内容模型: %s ", model));
        boolean result = modelService.removeByBean(model);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除内容模型
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除内容模型", notes="根据内容模型对象ID批量逻辑删除内容模型信息")
    @ApiImplicitParam(name = "ids", value = "内容模型对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除内容模型: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = modelService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Model对象属性获取内容模型
     *
     * @param model
     * @return
     */
    @GetMapping("/getByModel")
    @ApiOperation(value="根据Model对象属性获取内容模型", notes="根据内容模型对象属性获取内容模型信息")
    @ApiImplicitParam(name = "model", value = "内容模型对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<ModelVo> getByModel(Model model) {
        model = modelService.getByBean(model);
        ModelVo modelVo = modelService.setVoProperties(model);
        log.info(String.format("根据id获取内容模型：%s", JSONUtil.toJsonStr(modelVo)));
        return RestResult.ok(modelVo);
    }

    /**
     * 根据Model对象属性检索所有内容模型
     *
     * @param model
     * @return
     */
    @GetMapping("/listByModel")
    @ApiOperation(value="根据Model对象属性检索所有内容模型", notes="根据Model对象属性检索所有内容模型信息")
    @ApiImplicitParam(name = "model", value = "内容模型对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<Collection<ModelVo>> listByModel(Model model) {
        Collection<Model> models = modelService.listByBean(model);
        Collection<ModelVo> modelVos = modelService.setVoProperties(models);
        log.info(String.format("根据Model对象属性检索所有内容模型: %s ",JSONUtil.toJsonStr(modelVos)));
        return RestResult.ok(modelVos);
    }

    /**
     * 根据Model对象属性分页检索内容模型
     *
     * @param model
     * @return
     */
    @GetMapping("/pageByModel")
    @ApiOperation(value="根据Model对象属性分页检索内容模型", notes="根据Model对象属性分页检索内容模型信息")
    @ApiImplicitParam(name = "model", value = "内容模型对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<IPage<ModelVo>> pageByModel(Model model) {
        IPage<ModelVo> models = modelService.pageByBean(model);
        models.setRecords(modelService.setVoProperties(models.getRecords()));
        log.info(String.format("根据Model对象属性分页检索内容模型: %s ",JSONUtil.toJsonStr(models)));
        return RestResult.ok(models);
    }

}
