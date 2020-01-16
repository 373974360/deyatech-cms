package com.deyatech.apply.controller;

import com.deyatech.apply.entity.OpenReplyTemplate;
import com.deyatech.apply.vo.OpenReplyTemplateVo;
import com.deyatech.apply.service.OpenReplyTemplateService;
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
 *  前端控制器
 * </p>
 * @author: lee.
 * @since 2020-01-16
 */
@Slf4j
@RestController
@RequestMapping("/apply/openReplyTemplate")
@Api(tags = {"接口"})
public class OpenReplyTemplateController extends BaseController {
    @Autowired
    OpenReplyTemplateService openReplyTemplateService;

    /**
     * 单个保存或者更新
     *
     * @param openReplyTemplate
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "openReplyTemplate", value = "对象", required = true, dataType = "OpenReplyTemplate", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(OpenReplyTemplate openReplyTemplate) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(openReplyTemplate)));
        boolean result = openReplyTemplateService.saveOrUpdate(openReplyTemplate);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param openReplyTemplateList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "openReplyTemplateList", value = "对象集合", required = true, allowMultiple = true, dataType = "OpenReplyTemplate", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<OpenReplyTemplate> openReplyTemplateList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(openReplyTemplateList)));
        boolean result = openReplyTemplateService.saveOrUpdateBatch(openReplyTemplateList);
        return RestResult.ok(result);
    }

    /**
     * 根据OpenReplyTemplate对象属性逻辑删除
     *
     * @param openReplyTemplate
     * @return
     */
    @PostMapping("/removeByOpenReplyTemplate")
    @ApiOperation(value="根据OpenReplyTemplate对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "openReplyTemplate", value = "对象", required = true, dataType = "OpenReplyTemplate", paramType = "query")
    public RestResult<Boolean> removeByOpenReplyTemplate(OpenReplyTemplate openReplyTemplate) {
        log.info(String.format("根据OpenReplyTemplate对象属性逻辑删除: %s ", openReplyTemplate));
        boolean result = openReplyTemplateService.removeByBean(openReplyTemplate);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除", notes="根据对象ID批量逻辑删除信息")
    @ApiImplicitParam(name = "ids", value = "对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = openReplyTemplateService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据OpenReplyTemplate对象属性获取
     *
     * @param openReplyTemplate
     * @return
     */
    @GetMapping("/getByOpenReplyTemplate")
    @ApiOperation(value="根据OpenReplyTemplate对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "openReplyTemplate", value = "对象", required = false, dataType = "OpenReplyTemplate", paramType = "query")
    public RestResult<OpenReplyTemplateVo> getByOpenReplyTemplate(OpenReplyTemplate openReplyTemplate) {
        openReplyTemplate = openReplyTemplateService.getByBean(openReplyTemplate);
        OpenReplyTemplateVo openReplyTemplateVo = openReplyTemplateService.setVoProperties(openReplyTemplate);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(openReplyTemplateVo)));
        return RestResult.ok(openReplyTemplateVo);
    }

    /**
     * 根据OpenReplyTemplate对象属性检索所有
     *
     * @param openReplyTemplate
     * @return
     */
    @GetMapping("/listByOpenReplyTemplate")
    @ApiOperation(value="根据OpenReplyTemplate对象属性检索所有", notes="根据OpenReplyTemplate对象属性检索所有信息")
    @ApiImplicitParam(name = "openReplyTemplate", value = "对象", required = false, dataType = "OpenReplyTemplate", paramType = "query")
    public RestResult<Collection<OpenReplyTemplateVo>> listByOpenReplyTemplate(OpenReplyTemplate openReplyTemplate) {
        Collection<OpenReplyTemplate> openReplyTemplates = openReplyTemplateService.listByBean(openReplyTemplate);
        Collection<OpenReplyTemplateVo> openReplyTemplateVos = openReplyTemplateService.setVoProperties(openReplyTemplates);
        log.info(String.format("根据OpenReplyTemplate对象属性检索所有: %s ",JSONUtil.toJsonStr(openReplyTemplateVos)));
        return RestResult.ok(openReplyTemplateVos);
    }

    /**
     * 根据OpenReplyTemplate对象属性分页检索
     *
     * @param openReplyTemplate
     * @return
     */
    @GetMapping("/pageByOpenReplyTemplate")
    @ApiOperation(value="根据OpenReplyTemplate对象属性分页检索", notes="根据OpenReplyTemplate对象属性分页检索信息")
    @ApiImplicitParam(name = "openReplyTemplate", value = "对象", required = false, dataType = "OpenReplyTemplate", paramType = "query")
    public RestResult<IPage<OpenReplyTemplateVo>> pageByOpenReplyTemplate(OpenReplyTemplate openReplyTemplate) {
        IPage<OpenReplyTemplateVo> openReplyTemplates = openReplyTemplateService.pageByBean(openReplyTemplate);
        openReplyTemplates.setRecords(openReplyTemplateService.setVoProperties(openReplyTemplates.getRecords()));
        log.info(String.format("根据OpenReplyTemplate对象属性分页检索: %s ",JSONUtil.toJsonStr(openReplyTemplates)));
        return RestResult.ok(openReplyTemplates);
    }

}
