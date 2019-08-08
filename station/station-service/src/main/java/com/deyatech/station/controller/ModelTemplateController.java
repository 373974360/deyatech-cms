package com.deyatech.station.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.station.entity.ModelTemplate;
import com.deyatech.station.vo.ModelTemplateVo;
import com.deyatech.station.service.ModelTemplateService;
import com.deyatech.common.entity.RestResult;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 内容模型模版 前端控制器
 * </p>
 * @author: csm.
 * @since 2019-08-06
 */
@Slf4j
@RestController
@RequestMapping("/station/modelTemplate")
@Api(tags = {"内容模型模版接口"})
public class ModelTemplateController extends BaseController {
    @Autowired
    ModelTemplateService modelTemplateService;

    /**
     * 单个保存或者更新内容模型模版
     *
     * @param modelTemplate
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新内容模型模版", notes="根据内容模型模版对象保存或者更新内容模型模版信息")
    @ApiImplicitParam(name = "modelTemplate", value = "内容模型模版对象", required = true, dataType = "ModelTemplate", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(ModelTemplate modelTemplate) {
        log.info(String.format("保存或者更新内容模型模版: %s ", JSONUtil.toJsonStr(modelTemplate)));
        boolean result = modelTemplateService.saveOrUpdate(modelTemplate);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新内容模型模版
     *
     * @param modelTemplates
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新内容模型模版", notes="根据内容模型模版对象集合批量保存或者更新内容模型模版信息")
    @ApiImplicitParam(name = "modelTemplates", value = "内容模型模版对象集合", required = true, allowMultiple = true, dataType = "String", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(String modelTemplates) {
        log.info(String.format("批量保存或者更新内容模型模版: %s ", JSONUtil.toJsonStr(modelTemplates)));
        List<ModelTemplate> modelTemplateList = JSONUtil.parseArray(modelTemplates).toList(ModelTemplate.class);
        boolean result = modelTemplateService.saveOrUpdateBatch(modelTemplateList);
        return RestResult.ok(result);
    }

    /**
     * 根据ModelTemplate对象属性逻辑删除内容模型模版
     *
     * @param modelTemplate
     * @return
     */
    @PostMapping("/removeByModelTemplate")
    @ApiOperation(value="根据ModelTemplate对象属性逻辑删除内容模型模版", notes="根据内容模型模版对象逻辑删除内容模型模版信息")
    @ApiImplicitParam(name = "modelTemplate", value = "内容模型模版对象", required = true, dataType = "ModelTemplate", paramType = "query")
    public RestResult<Boolean> removeByModelTemplate(ModelTemplate modelTemplate) {
        log.info(String.format("根据ModelTemplate对象属性逻辑删除内容模型模版: %s ", modelTemplate));
        boolean result = modelTemplateService.removeByBean(modelTemplate);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除内容模型模版
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除内容模型模版", notes="根据内容模型模版对象ID批量逻辑删除内容模型模版信息")
    @ApiImplicitParam(name = "ids", value = "内容模型模版对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除内容模型模版: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = modelTemplateService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据ModelTemplate对象属性获取内容模型模版
     *
     * @param modelTemplate
     * @return
     */
    @GetMapping("/getByModelTemplate")
    @ApiOperation(value="根据ModelTemplate对象属性获取内容模型模版", notes="根据内容模型模版对象属性获取内容模型模版信息")
    @ApiImplicitParam(name = "modelTemplate", value = "内容模型模版对象", required = false, dataType = "ModelTemplate", paramType = "query")
    public RestResult<ModelTemplateVo> getByModelTemplate(ModelTemplate modelTemplate) {
        modelTemplate = modelTemplateService.getByBean(modelTemplate);
        ModelTemplateVo modelTemplateVo = modelTemplateService.setVoProperties(modelTemplate);
        log.info(String.format("根据id获取内容模型模版：%s", JSONUtil.toJsonStr(modelTemplateVo)));
        return RestResult.ok(modelTemplateVo);
    }

    /**
     * 根据ModelTemplate对象属性检索所有内容模型模版
     *
     * @param modelTemplate
     * @return
     */
    @GetMapping("/listByModelTemplate")
    @ApiOperation(value="根据ModelTemplate对象属性检索所有内容模型模版", notes="根据ModelTemplate对象属性检索所有内容模型模版信息")
    @ApiImplicitParam(name = "modelTemplate", value = "内容模型模版对象", required = false, dataType = "ModelTemplate", paramType = "query")
    public RestResult<Collection<ModelTemplateVo>> listByModelTemplate(ModelTemplate modelTemplate) {
        Collection<ModelTemplate> modelTemplates = modelTemplateService.listByBean(modelTemplate);
        Collection<ModelTemplateVo> modelTemplateVos = modelTemplateService.setVoProperties(modelTemplates);
        log.info(String.format("根据ModelTemplate对象属性检索所有内容模型模版: %s ",JSONUtil.toJsonStr(modelTemplateVos)));
        return RestResult.ok(modelTemplateVos);
    }

    /**
     * 根据ModelTemplate对象属性分页检索内容模型模版
     *
     * @param modelTemplate
     * @return
     */
    @GetMapping("/pageByModelTemplate")
    @ApiOperation(value="根据ModelTemplate对象属性分页检索内容模型模版", notes="根据ModelTemplate对象属性分页检索内容模型模版信息")
    @ApiImplicitParam(name = "modelTemplate", value = "内容模型模版对象", required = false, dataType = "ModelTemplate", paramType = "query")
    public RestResult<IPage<ModelTemplateVo>> pageByModelTemplate(ModelTemplate modelTemplate) {
        IPage<ModelTemplateVo> modelTemplates = modelTemplateService.pageByModelTemplate(modelTemplate);
        modelTemplates.setRecords(modelTemplateService.setVoProperties(modelTemplates.getRecords()));
        log.info(String.format("根据ModelTemplate对象属性分页检索内容模型模版: %s ",JSONUtil.toJsonStr(modelTemplates)));
        return RestResult.ok(modelTemplates);
    }

    /**
     * 根据ModelTemplate对象属性分页检索内容模型模版，按站点分组
     *
     * @param modelTemplate
     * @return
     */
    @GetMapping("/pageByModelTemplateGroupBySite")
    @ApiOperation(value="根据ModelTemplate对象属性分页检索内容模型模版，按站点分组", notes="根据ModelTemplate对象属性分页检索内容模型模版信息，按站点分组")
    @ApiImplicitParam(name = "modelTemplate", value = "内容模型模版对象", required = false, dataType = "ModelTemplate", paramType = "query")
    public RestResult<IPage<ModelTemplateVo>> pageByModelTemplateGroupBySite(ModelTemplate modelTemplate) {
        IPage<ModelTemplateVo> modelTemplates = modelTemplateService.pageByModelTemplateGroupBySite(modelTemplate);
        modelTemplates.setRecords(modelTemplateService.setVoProperties(modelTemplates.getRecords()));
        log.info(String.format("根据ModelTemplate对象属性分页检索内容模型模版，按站点分组: %s ",JSONUtil.toJsonStr(modelTemplates)));
        return RestResult.ok(modelTemplates);
    }

    /**
     * 检查站点的内容模型是否已配置
     *
     * @param modelTemplate
     * @return
     */
    @GetMapping("/checkSiteContentModelExist")
    @ApiOperation(value="检查站点的内容模型是否已配置", notes="检查站点的内容模型是否已配置")
    @ApiImplicitParam(name = "modelTemplate", value = "内容模型模版对象", required = false, dataType = "ModelTemplate", paramType = "query")
    public RestResult<Boolean> checkSiteContentModelExist(ModelTemplate modelTemplate) {
        log.info(String.format("检查站点的内容模型是否已配置: %s ",JSONUtil.toJsonStr(modelTemplate)));
        Boolean flag = modelTemplateService.checkSiteContentModelExist(modelTemplate);
        return RestResult.ok(flag);
    }

    /**
     * 根据ModelTemplate对象属性逻辑删除内容模型模版
     *
     * @param modelTemplateVo
     * @return
     */
    @PostMapping("/removeByModelTemplateVo")
    @ApiOperation(value="根据ModelTemplate对象属性逻辑删除内容模型模版", notes="根据ModelTemplate对象属性逻辑删除内容模型模版")
    @ApiImplicitParam(name = "modelTemplateVo", value = "内容模型模版对象集合", required = true, allowMultiple = true, dataType = "ModelTemplateVo", paramType = "query")
    public RestResult<Boolean> removeByModelTemplateVo(String modelTemplateVo) {
        log.info(String.format("批量保存或者更新内容模型模版: %s ", JSONUtil.toJsonStr(modelTemplateVo)));
        JSONObject jsonObject = JSONUtil.parseObj(modelTemplateVo);
        List<String> siteIds = jsonObject.get("siteIds", List.class);
        String contentModelId = jsonObject.get("contentModelId", String.class);
        QueryWrapper<ModelTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("site_id", siteIds).eq("content_model_id", contentModelId);
        boolean result = modelTemplateService.remove(queryWrapper);
        return RestResult.ok(result);
    }
}
