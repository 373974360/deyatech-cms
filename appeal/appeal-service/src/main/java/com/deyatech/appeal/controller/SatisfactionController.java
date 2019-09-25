package com.deyatech.appeal.controller;

import com.deyatech.appeal.entity.Satisfaction;
import com.deyatech.appeal.vo.SatisfactionVo;
import com.deyatech.appeal.service.SatisfactionService;
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
 * @since 2019-09-24
 */
@Slf4j
@RestController
@RequestMapping("/appeal/satisfaction")
@Api(tags = {"接口"})
public class SatisfactionController extends BaseController {
    @Autowired
    SatisfactionService satisfactionService;

    /**
     * 单个保存或者更新
     *
     * @param satisfaction
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "satisfaction", value = "对象", required = true, dataType = "Satisfaction", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Satisfaction satisfaction) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(satisfaction)));
        boolean result = satisfactionService.saveOrUpdate(satisfaction);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param satisfactionList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "satisfactionList", value = "对象集合", required = true, allowMultiple = true, dataType = "Satisfaction", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Satisfaction> satisfactionList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(satisfactionList)));
        boolean result = satisfactionService.saveOrUpdateBatch(satisfactionList);
        return RestResult.ok(result);
    }

    /**
     * 根据Satisfaction对象属性逻辑删除
     *
     * @param satisfaction
     * @return
     */
    @PostMapping("/removeBySatisfaction")
    @ApiOperation(value="根据Satisfaction对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "satisfaction", value = "对象", required = true, dataType = "Satisfaction", paramType = "query")
    public RestResult<Boolean> removeBySatisfaction(Satisfaction satisfaction) {
        log.info(String.format("根据Satisfaction对象属性逻辑删除: %s ", satisfaction));
        boolean result = satisfactionService.removeByBean(satisfaction);
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
        boolean result = satisfactionService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Satisfaction对象属性获取
     *
     * @param satisfaction
     * @return
     */
    @GetMapping("/getBySatisfaction")
    @ApiOperation(value="根据Satisfaction对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "satisfaction", value = "对象", required = false, dataType = "Satisfaction", paramType = "query")
    public RestResult<SatisfactionVo> getBySatisfaction(Satisfaction satisfaction) {
        satisfaction = satisfactionService.getByBean(satisfaction);
        SatisfactionVo satisfactionVo = satisfactionService.setVoProperties(satisfaction);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(satisfactionVo)));
        return RestResult.ok(satisfactionVo);
    }

    /**
     * 根据Satisfaction对象属性检索所有
     *
     * @param satisfaction
     * @return
     */
    @GetMapping("/listBySatisfaction")
    @ApiOperation(value="根据Satisfaction对象属性检索所有", notes="根据Satisfaction对象属性检索所有信息")
    @ApiImplicitParam(name = "satisfaction", value = "对象", required = false, dataType = "Satisfaction", paramType = "query")
    public RestResult<Collection<SatisfactionVo>> listBySatisfaction(Satisfaction satisfaction) {
        Collection<Satisfaction> satisfactions = satisfactionService.listByBean(satisfaction);
        Collection<SatisfactionVo> satisfactionVos = satisfactionService.setVoProperties(satisfactions);
        log.info(String.format("根据Satisfaction对象属性检索所有: %s ",JSONUtil.toJsonStr(satisfactionVos)));
        return RestResult.ok(satisfactionVos);
    }

    /**
     * 根据Satisfaction对象属性分页检索
     *
     * @param satisfaction
     * @return
     */
    @GetMapping("/pageBySatisfaction")
    @ApiOperation(value="根据Satisfaction对象属性分页检索", notes="根据Satisfaction对象属性分页检索信息")
    @ApiImplicitParam(name = "satisfaction", value = "对象", required = false, dataType = "Satisfaction", paramType = "query")
    public RestResult<IPage<SatisfactionVo>> pageBySatisfaction(Satisfaction satisfaction) {
        IPage<SatisfactionVo> satisfactions = satisfactionService.pageByBean(satisfaction);
        satisfactions.setRecords(satisfactionService.setVoProperties(satisfactions.getRecords()));
        log.info(String.format("根据Satisfaction对象属性分页检索: %s ",JSONUtil.toJsonStr(satisfactions)));
        return RestResult.ok(satisfactions);
    }

}
