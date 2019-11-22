package com.deyatech.appeal.controller;

import com.deyatech.appeal.entity.RecordSatisfaction;
import com.deyatech.appeal.vo.RecordSatisfactionVo;
import com.deyatech.appeal.service.RecordSatisfactionService;
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
 * @since 2019-11-21
 */
@Slf4j
@RestController
@RequestMapping("/appeal/recordSatisfaction")
@Api(tags = {"接口"})
public class RecordSatisfactionController extends BaseController {
    @Autowired
    RecordSatisfactionService recordSatisfactionService;

    /**
     * 单个保存或者更新
     *
     * @param recordSatisfaction
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "recordSatisfaction", value = "对象", required = true, dataType = "RecordSatisfaction", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(RecordSatisfaction recordSatisfaction) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(recordSatisfaction)));
        boolean result = recordSatisfactionService.saveOrUpdate(recordSatisfaction);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param recordSatisfactionList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "recordSatisfactionList", value = "对象集合", required = true, allowMultiple = true, dataType = "RecordSatisfaction", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<RecordSatisfaction> recordSatisfactionList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(recordSatisfactionList)));
        boolean result = recordSatisfactionService.saveOrUpdateBatch(recordSatisfactionList);
        return RestResult.ok(result);
    }

    /**
     * 根据RecordSatisfaction对象属性逻辑删除
     *
     * @param recordSatisfaction
     * @return
     */
    @PostMapping("/removeByRecordSatisfaction")
    @ApiOperation(value="根据RecordSatisfaction对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "recordSatisfaction", value = "对象", required = true, dataType = "RecordSatisfaction", paramType = "query")
    public RestResult<Boolean> removeByRecordSatisfaction(RecordSatisfaction recordSatisfaction) {
        log.info(String.format("根据RecordSatisfaction对象属性逻辑删除: %s ", recordSatisfaction));
        boolean result = recordSatisfactionService.removeByBean(recordSatisfaction);
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
        boolean result = recordSatisfactionService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据RecordSatisfaction对象属性获取
     *
     * @param recordSatisfaction
     * @return
     */
    @GetMapping("/getByRecordSatisfaction")
    @ApiOperation(value="根据RecordSatisfaction对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "recordSatisfaction", value = "对象", required = false, dataType = "RecordSatisfaction", paramType = "query")
    public RestResult<RecordSatisfactionVo> getByRecordSatisfaction(RecordSatisfaction recordSatisfaction) {
        recordSatisfaction = recordSatisfactionService.getByBean(recordSatisfaction);
        RecordSatisfactionVo recordSatisfactionVo = recordSatisfactionService.setVoProperties(recordSatisfaction);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(recordSatisfactionVo)));
        return RestResult.ok(recordSatisfactionVo);
    }

    /**
     * 根据RecordSatisfaction对象属性检索所有
     *
     * @param recordSatisfaction
     * @return
     */
    @GetMapping("/listByRecordSatisfaction")
    @ApiOperation(value="根据RecordSatisfaction对象属性检索所有", notes="根据RecordSatisfaction对象属性检索所有信息")
    @ApiImplicitParam(name = "recordSatisfaction", value = "对象", required = false, dataType = "RecordSatisfaction", paramType = "query")
    public RestResult<Collection<RecordSatisfactionVo>> listByRecordSatisfaction(RecordSatisfaction recordSatisfaction) {
        Collection<RecordSatisfaction> recordSatisfactions = recordSatisfactionService.listByBean(recordSatisfaction);
        Collection<RecordSatisfactionVo> recordSatisfactionVos = recordSatisfactionService.setVoProperties(recordSatisfactions);
        log.info(String.format("根据RecordSatisfaction对象属性检索所有: %s ",JSONUtil.toJsonStr(recordSatisfactionVos)));
        return RestResult.ok(recordSatisfactionVos);
    }

    /**
     * 根据RecordSatisfaction对象属性分页检索
     *
     * @param recordSatisfaction
     * @return
     */
    @GetMapping("/pageByRecordSatisfaction")
    @ApiOperation(value="根据RecordSatisfaction对象属性分页检索", notes="根据RecordSatisfaction对象属性分页检索信息")
    @ApiImplicitParam(name = "recordSatisfaction", value = "对象", required = false, dataType = "RecordSatisfaction", paramType = "query")
    public RestResult<IPage<RecordSatisfactionVo>> pageByRecordSatisfaction(RecordSatisfaction recordSatisfaction) {
        IPage<RecordSatisfactionVo> recordSatisfactions = recordSatisfactionService.pageByBean(recordSatisfaction);
        recordSatisfactions.setRecords(recordSatisfactionService.setVoProperties(recordSatisfactions.getRecords()));
        log.info(String.format("根据RecordSatisfaction对象属性分页检索: %s ",JSONUtil.toJsonStr(recordSatisfactions)));
        return RestResult.ok(recordSatisfactions);
    }

}
