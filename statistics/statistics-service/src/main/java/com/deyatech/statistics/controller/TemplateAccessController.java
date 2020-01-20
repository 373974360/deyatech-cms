package com.deyatech.statistics.controller;

import com.deyatech.statistics.entity.TemplateAccess;
import com.deyatech.statistics.vo.TemplateAccessVo;
import com.deyatech.statistics.service.TemplateAccessService;
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
 * @since 2020-01-19
 */
@Slf4j
@RestController
@RequestMapping("/statistics/templateAccess")
@Api(tags = {"接口"})
public class TemplateAccessController extends BaseController {

    @Autowired
    TemplateAccessService templateAccessService;

    /**
     * 单个保存或者更新
     *
     * @param templateAccess
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "templateAccess", value = "对象", required = true, dataType = "TemplateAccess", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(TemplateAccess templateAccess) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(templateAccess)));
        boolean result = templateAccessService.saveOrUpdate(templateAccess);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param templateAccessList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "templateAccessList", value = "对象集合", required = true, allowMultiple = true, dataType = "TemplateAccess", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<TemplateAccess> templateAccessList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(templateAccessList)));
        boolean result = templateAccessService.saveOrUpdateBatch(templateAccessList);
        return RestResult.ok(result);
    }

    /**
     * 根据TemplateAccess对象属性逻辑删除
     *
     * @param templateAccess
     * @return
     */
    @PostMapping("/removeByTemplateAccess")
    @ApiOperation(value="根据TemplateAccess对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "templateAccess", value = "对象", required = true, dataType = "TemplateAccess", paramType = "query")
    public RestResult<Boolean> removeByTemplateAccess(TemplateAccess templateAccess) {
        log.info(String.format("根据TemplateAccess对象属性逻辑删除: %s ", templateAccess));
        boolean result = templateAccessService.removeByBean(templateAccess);
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
        boolean result = templateAccessService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据TemplateAccess对象属性获取
     *
     * @param templateAccess
     * @return
     */
    @GetMapping("/getByTemplateAccess")
    @ApiOperation(value="根据TemplateAccess对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "templateAccess", value = "对象", required = false, dataType = "TemplateAccess", paramType = "query")
    public RestResult<TemplateAccessVo> getByTemplateAccess(TemplateAccess templateAccess) {
        templateAccess = templateAccessService.getByBean(templateAccess);
        TemplateAccessVo templateAccessVo = templateAccessService.setVoProperties(templateAccess);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(templateAccessVo)));
        return RestResult.ok(templateAccessVo);
    }

    /**
     * 根据TemplateAccess对象属性检索所有
     *
     * @param templateAccess
     * @return
     */
    @GetMapping("/listByTemplateAccess")
    @ApiOperation(value="根据TemplateAccess对象属性检索所有", notes="根据TemplateAccess对象属性检索所有信息")
    @ApiImplicitParam(name = "templateAccess", value = "对象", required = false, dataType = "TemplateAccess", paramType = "query")
    public RestResult<Collection<TemplateAccessVo>> listByTemplateAccess(TemplateAccess templateAccess) {
        Collection<TemplateAccess> templateAccesss = templateAccessService.listByBean(templateAccess);
        Collection<TemplateAccessVo> templateAccessVos = templateAccessService.setVoProperties(templateAccesss);
        log.info(String.format("根据TemplateAccess对象属性检索所有: %s ",JSONUtil.toJsonStr(templateAccessVos)));
        return RestResult.ok(templateAccessVos);
    }

    /**
     * 根据TemplateAccess对象属性分页检索
     *
     * @param templateAccess
     * @return
     */
    @GetMapping("/pageByTemplateAccess")
    @ApiOperation(value="根据TemplateAccess对象属性分页检索", notes="根据TemplateAccess对象属性分页检索信息")
    @ApiImplicitParam(name = "templateAccess", value = "对象", required = false, dataType = "TemplateAccess", paramType = "query")
    public RestResult<IPage<TemplateAccessVo>> pageByTemplateAccess(TemplateAccessVo templateAccess) {
        IPage<TemplateAccessVo> templateAccesss;
        if(templateAccess.getAccessType() == 1){
            templateAccesss = templateAccessService.getAccessCountByCatalog(templateAccess);
        }else{
            templateAccesss = templateAccessService.getAccessCountByInfo(templateAccess);
        }
        log.info(String.format("根据TemplateAccess对象属性分页检索: %s ",JSONUtil.toJsonStr(templateAccesss)));
        return RestResult.ok(templateAccesss);
    }

}
