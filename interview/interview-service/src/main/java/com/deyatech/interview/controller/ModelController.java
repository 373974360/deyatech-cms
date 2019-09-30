package com.deyatech.interview.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.interview.entity.Model;
import com.deyatech.interview.service.ModelService;
import com.deyatech.interview.vo.LiveImageVo;
import com.deyatech.interview.vo.LiveMessageVo;
import com.deyatech.interview.vo.ModelVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 访谈模型 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-08-26
 */
@Slf4j
@RestController
@RequestMapping("/interview/model")
@Api(tags = {"访谈模型接口"})
public class ModelController extends BaseController {
    @Autowired
    ModelService modelService;

    /**
     * 单个保存或者更新访谈模型
     *
     * @param model
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新访谈模型", notes="根据访谈模型对象保存或者更新访谈模型信息")
    @ApiImplicitParam(name = "model", value = "访谈模型对象", required = true, dataType = "Model", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Model model) {
        log.info(String.format("保存或者更新访谈模型: %s ", JSONUtil.toJsonStr(model)));
        boolean result = modelService.saveOrUpdate(model);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新访谈模型
     *
     * @param modelList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新访谈模型", notes="根据访谈模型对象集合批量保存或者更新访谈模型信息")
    @ApiImplicitParam(name = "modelList", value = "访谈模型对象集合", required = true, allowMultiple = true, dataType = "Model", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Model> modelList) {
        log.info(String.format("批量保存或者更新访谈模型: %s ", JSONUtil.toJsonStr(modelList)));
        boolean result = modelService.saveOrUpdateBatch(modelList);
        return RestResult.ok(result);
    }

    /**
     * 根据Model对象属性逻辑删除访谈模型
     *
     * @param model
     * @return
     */
    @PostMapping("/removeByModel")
    @ApiOperation(value="根据Model对象属性逻辑删除访谈模型", notes="根据访谈模型对象逻辑删除访谈模型信息")
    @ApiImplicitParam(name = "model", value = "访谈模型对象", required = true, dataType = "Model", paramType = "query")
    public RestResult<Boolean> removeByModel(Model model) {
        log.info(String.format("根据Model对象属性逻辑删除访谈模型: %s ", model));
        boolean result = modelService.removeByBean(model);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除访谈模型
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除访谈模型", notes="根据访谈模型对象ID批量逻辑删除访谈模型信息")
    @ApiImplicitParam(name = "ids", value = "访谈模型对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除访谈模型: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = modelService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Model对象属性获取访谈模型
     *
     * @param model
     * @return
     */
    @GetMapping("/getByModel")
    @ApiOperation(value="根据Model对象属性获取访谈模型", notes="根据访谈模型对象属性获取访谈模型信息")
    @ApiImplicitParam(name = "model", value = "访谈模型对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<ModelVo> getByModel(Model model) {
        model = modelService.getByBean(model);
        ModelVo modelVo = modelService.setVoProperties(model);
        log.info(String.format("根据id获取访谈模型：%s", JSONUtil.toJsonStr(modelVo)));
        return RestResult.ok(modelVo);
    }

    /**
     * 根据Model对象属性检索所有访谈模型
     *
     * @param model
     * @return
     */
    @GetMapping("/listByModel")
    @ApiOperation(value="根据Model对象属性检索所有访谈模型", notes="根据Model对象属性检索所有访谈模型信息")
    @ApiImplicitParam(name = "model", value = "访谈模型对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<Collection<ModelVo>> listByModel(Model model) {
        Collection<Model> models = modelService.listByBean(model);
        Collection<ModelVo> modelVos = modelService.setVoProperties(models);
        log.info(String.format("根据Model对象属性检索所有访谈模型: %s ",JSONUtil.toJsonStr(modelVos)));
        return RestResult.ok(modelVos);
    }

    /**
     * 根据Model对象属性分页检索访谈模型
     *
     * @param model
     * @return
     */
    @GetMapping("/pageByModel")
    @ApiOperation(value="根据Model对象属性分页检索访谈模型", notes="根据Model对象属性分页检索访谈模型信息")
    @ApiImplicitParam(name = "model", value = "访谈模型对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<IPage<ModelVo>> pageByModel(Model model) {
        IPage<ModelVo> models = modelService.pageByBean(model);
        models.setRecords(modelService.setVoProperties(models.getRecords()));
        log.info(String.format("根据Model对象属性分页检索访谈模型: %s ",JSONUtil.toJsonStr(models)));
        return RestResult.ok(models);
    }

    /**
     * 根据Model对象属性分页检索访谈模型
     *
     * @param model
     * @return
     */
    @GetMapping("/pageByCategoryAndName")
    @ApiOperation(value="根据Model对象属性分页检索访谈模型", notes="根据Model对象属性分页检索访谈模型信息")
    @ApiImplicitParam(name = "model", value = "访谈模型对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<IPage<ModelVo>> pageByCategoryAndName(Model model) {
        IPage<ModelVo> models = modelService.pageByCategoryAndName(model);
        models.setRecords(modelService.setVoProperties(models.getRecords()));
        log.info(String.format("根据Model对象属性分页检索访谈模型: %s ",JSONUtil.toJsonStr(models)));
        return RestResult.ok(models);
    }

    /**
     * 追加直播消息
     *
     * @param liveMessage
     * @return
     */
    @RequestMapping("/operateLiveMessage")
    @ApiOperation(value="追加直播消息", notes="追加直播消息")
    @ApiImplicitParam(name = "liveMessage", value = "消息对象", required = true, dataType = "LiveMessageVo", paramType = "query")
    public RestResult operateLiveMessage(LiveMessageVo liveMessage) {
        return RestResult.ok(modelService.operateLiveMessage(liveMessage));
    }

    /**
     * 追加直播图片
     *
     * @param liveImage
     * @return
     */
    @RequestMapping("/operateLiveImage")
    @ApiOperation(value="追加直播图片", notes="追加直播图片")
    @ApiImplicitParam(name = "liveImage", value = "消息对象", required = true, dataType = "LiveImageVo", paramType = "query")
    public RestResult operateLiveImage(LiveImageVo liveImage) {
        return RestResult.ok(modelService.operateLiveImage(liveImage));
    }
}
