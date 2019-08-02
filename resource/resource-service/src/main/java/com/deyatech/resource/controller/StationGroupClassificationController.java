package com.deyatech.resource.controller;

import cn.hutool.core.util.StrUtil;
import com.deyatech.common.Constants;
import com.deyatech.resource.entity.StationGroupClassification;
import com.deyatech.resource.service.StationGroupService;
import com.deyatech.resource.vo.StationGroupClassificationVo;
import com.deyatech.resource.service.StationGroupClassificationService;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.entity.CascaderResult;
import com.deyatech.common.utils.CascaderUtil;
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
 * @since 2019-08-01
 */
@Slf4j
@RestController
@RequestMapping("/resource/stationGroupClassification")
@Api(tags = {"接口"})
public class StationGroupClassificationController extends BaseController {
    @Autowired
    StationGroupClassificationService stationGroupClassificationService;
    @Autowired
    StationGroupService stationGroupService;
    /**
     * 单个保存或者更新
     *
     * @param stationGroupClassification
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "stationGroupClassification", value = "对象", required = true, dataType = "StationGroupClassification", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(StationGroupClassification stationGroupClassification) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(stationGroupClassification)));
        // 新添加
        if (!Constants.ZERO.equals(stationGroupClassification.getParentId()) && StrUtil.isNotEmpty(stationGroupClassification.getParentId())) {
            long count = stationGroupService.countStationGroupByClassificationId(stationGroupClassification.getParentId());
            if (count > 0) {
                return RestResult.error("当前分类下已存在站群，不允许添加分类");
            }
        }
        boolean result = stationGroupClassificationService.saveOrUpdate(stationGroupClassification);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param stationGroupClassificationList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "stationGroupClassificationList", value = "对象集合", required = true, allowMultiple = true, dataType = "StationGroupClassification", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<StationGroupClassification> stationGroupClassificationList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(stationGroupClassificationList)));
        boolean result = stationGroupClassificationService.saveOrUpdateBatch(stationGroupClassificationList);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroupClassification对象属性逻辑删除
     *
     * @param stationGroupClassification
     * @return
     */
    @PostMapping("/removeByStationGroupClassification")
    @ApiOperation(value="根据StationGroupClassification对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "stationGroupClassification", value = "对象", required = true, dataType = "StationGroupClassification", paramType = "query")
    public RestResult<Boolean> removeByStationGroupClassification(StationGroupClassification stationGroupClassification) {
        log.info(String.format("根据StationGroupClassification对象属性逻辑删除: %s ", stationGroupClassification));
        boolean result = stationGroupClassificationService.removeByBean(stationGroupClassification);
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
        boolean result = stationGroupClassificationService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroupClassification对象属性获取
     *
     * @param stationGroupClassification
     * @return
     */
    @GetMapping("/getByStationGroupClassification")
    @ApiOperation(value="根据StationGroupClassification对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "stationGroupClassification", value = "对象", required = false, dataType = "StationGroupClassification", paramType = "query")
    public RestResult<StationGroupClassificationVo> getByStationGroupClassification(StationGroupClassification stationGroupClassification) {
        stationGroupClassification = stationGroupClassificationService.getByBean(stationGroupClassification);
        StationGroupClassificationVo stationGroupClassificationVo = stationGroupClassificationService.setVoProperties(stationGroupClassification);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(stationGroupClassificationVo)));
        return RestResult.ok(stationGroupClassificationVo);
    }

    /**
     * 根据StationGroupClassification对象属性检索所有
     *
     * @param stationGroupClassification
     * @return
     */
    @GetMapping("/listByStationGroupClassification")
    @ApiOperation(value="根据StationGroupClassification对象属性检索所有", notes="根据StationGroupClassification对象属性检索所有信息")
    @ApiImplicitParam(name = "stationGroupClassification", value = "对象", required = false, dataType = "StationGroupClassification", paramType = "query")
    public RestResult<Collection<StationGroupClassificationVo>> listByStationGroupClassification(StationGroupClassification stationGroupClassification) {
        Collection<StationGroupClassification> stationGroupClassifications = stationGroupClassificationService.listByBean(stationGroupClassification);
        Collection<StationGroupClassificationVo> stationGroupClassificationVos = stationGroupClassificationService.setVoProperties(stationGroupClassifications);
        log.info(String.format("根据StationGroupClassification对象属性检索所有: %s ",JSONUtil.toJsonStr(stationGroupClassificationVos)));
        return RestResult.ok(stationGroupClassificationVos);
    }

    /**
     * 根据StationGroupClassification对象属性分页检索
     *
     * @param stationGroupClassification
     * @return
     */
    @GetMapping("/pageByStationGroupClassification")
    @ApiOperation(value="根据StationGroupClassification对象属性分页检索", notes="根据StationGroupClassification对象属性分页检索信息")
    @ApiImplicitParam(name = "stationGroupClassification", value = "对象", required = false, dataType = "StationGroupClassification", paramType = "query")
    public RestResult<IPage<StationGroupClassificationVo>> pageByStationGroupClassification(StationGroupClassification stationGroupClassification) {
        IPage<StationGroupClassificationVo> stationGroupClassifications = stationGroupClassificationService.pageByBean(stationGroupClassification);
        stationGroupClassifications.setRecords(stationGroupClassificationService.setVoProperties(stationGroupClassifications.getRecords()));
        log.info(String.format("根据StationGroupClassification对象属性分页检索: %s ",JSONUtil.toJsonStr(stationGroupClassifications)));
        return RestResult.ok(stationGroupClassifications);
    }

    /**
     * 获取的tree对象
     *
     * @param stationGroupClassification
     * @return
     */
    @GetMapping("/getTree")
    @ApiOperation(value="获取的tree对象", notes="获取的tree对象")
    public RestResult<Collection<StationGroupClassificationVo>> getStationGroupClassificationTree(StationGroupClassification stationGroupClassification) {
        Collection<StationGroupClassificationVo> stationGroupClassificationTree = stationGroupClassificationService.getStationGroupClassificationTree(stationGroupClassification);
        log.info(String.format("获取的tree对象: %s ",JSONUtil.toJsonStr(stationGroupClassificationTree)));
        return RestResult.ok(stationGroupClassificationTree);
    }

    /**
     * 获取的级联对象
     *
     * @param stationGroupClassification
     * @return
     */
    @GetMapping("/getCascader")
    @ApiOperation(value="获取的级联对象", notes="获取的级联对象")
    @ApiImplicitParam(name = "stationGroupClassification", value = "stationGroupClassification", required = false, dataType = "StationGroupClassification", paramType = "query")
    public RestResult<List<CascaderResult>> getCascader(StationGroupClassification stationGroupClassification) {
        Collection<StationGroupClassificationVo> stationGroupClassificationVos = stationGroupClassificationService.getStationGroupClassificationTree(stationGroupClassification);
        List<CascaderResult> cascaderResults = CascaderUtil.getResult("Id", "Name","TreePosition", stationGroupClassification.getId(), stationGroupClassificationVos);
        log.info(String.format("获取的级联对象: %s ",JSONUtil.toJsonStr(cascaderResults)));
        return RestResult.ok(cascaderResults);
    }
}
