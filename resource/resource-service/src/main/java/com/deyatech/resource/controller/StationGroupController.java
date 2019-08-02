package com.deyatech.resource.controller;

import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.vo.StationGroupVo;
import com.deyatech.resource.service.StationGroupService;
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
 * 站群 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-08-01
 */
@Slf4j
@RestController
@RequestMapping("/resource/stationGroup")
@Api(tags = {"站群接口"})
public class StationGroupController extends BaseController {
    @Autowired
    StationGroupService stationGroupService;

    /**
     * 单个保存或者更新站群
     *
     * @param stationGroup
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新站群", notes="根据站群对象保存或者更新站群信息")
    @ApiImplicitParam(name = "stationGroup", value = "站群对象", required = true, dataType = "StationGroup", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(StationGroup stationGroup) {
        log.info(String.format("保存或者更新站群: %s ", JSONUtil.toJsonStr(stationGroup)));
        boolean result = stationGroupService.saveOrUpdate(stationGroup);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新站群
     *
     * @param stationGroupList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新站群", notes="根据站群对象集合批量保存或者更新站群信息")
    @ApiImplicitParam(name = "stationGroupList", value = "站群对象集合", required = true, allowMultiple = true, dataType = "StationGroup", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<StationGroup> stationGroupList) {
        log.info(String.format("批量保存或者更新站群: %s ", JSONUtil.toJsonStr(stationGroupList)));
        boolean result = stationGroupService.saveOrUpdateBatch(stationGroupList);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroup对象属性逻辑删除站群
     *
     * @param stationGroup
     * @return
     */
    @PostMapping("/removeByStationGroup")
    @ApiOperation(value="根据StationGroup对象属性逻辑删除站群", notes="根据站群对象逻辑删除站群信息")
    @ApiImplicitParam(name = "stationGroup", value = "站群对象", required = true, dataType = "StationGroup", paramType = "query")
    public RestResult<Boolean> removeByStationGroup(StationGroup stationGroup) {
        log.info(String.format("根据StationGroup对象属性逻辑删除站群: %s ", stationGroup));
        boolean result = stationGroupService.removeByBean(stationGroup);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除站群
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除站群", notes="根据站群对象ID批量逻辑删除站群信息")
    @ApiImplicitParam(name = "ids", value = "站群对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除站群: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = stationGroupService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroup对象属性获取站群
     *
     * @param stationGroup
     * @return
     */
    @GetMapping("/getByStationGroup")
    @ApiOperation(value="根据StationGroup对象属性获取站群", notes="根据站群对象属性获取站群信息")
    @ApiImplicitParam(name = "stationGroup", value = "站群对象", required = false, dataType = "StationGroup", paramType = "query")
    public RestResult<StationGroupVo> getByStationGroup(StationGroup stationGroup) {
        stationGroup = stationGroupService.getByBean(stationGroup);
        StationGroupVo stationGroupVo = stationGroupService.setVoProperties(stationGroup);
        log.info(String.format("根据id获取站群：%s", JSONUtil.toJsonStr(stationGroupVo)));
        return RestResult.ok(stationGroupVo);
    }

    /**
     * 根据StationGroup对象属性检索所有站群
     *
     * @param stationGroup
     * @return
     */
    @GetMapping("/listByStationGroup")
    @ApiOperation(value="根据StationGroup对象属性检索所有站群", notes="根据StationGroup对象属性检索所有站群信息")
    @ApiImplicitParam(name = "stationGroup", value = "站群对象", required = false, dataType = "StationGroup", paramType = "query")
    public RestResult<Collection<StationGroupVo>> listByStationGroup(StationGroup stationGroup) {
        Collection<StationGroup> stationGroups = stationGroupService.listByBean(stationGroup);
        Collection<StationGroupVo> stationGroupVos = stationGroupService.setVoProperties(stationGroups);
        log.info(String.format("根据StationGroup对象属性检索所有站群: %s ",JSONUtil.toJsonStr(stationGroupVos)));
        return RestResult.ok(stationGroupVos);
    }

    /**
     * 根据StationGroup对象属性分页检索站群
     *
     * @param stationGroup
     * @return
     */
    @GetMapping("/pageByStationGroup")
    @ApiOperation(value="根据StationGroup对象属性分页检索站群", notes="根据StationGroup对象属性分页检索站群信息")
    @ApiImplicitParam(name = "stationGroup", value = "站群对象", required = false, dataType = "StationGroup", paramType = "query")
    public RestResult<IPage<StationGroupVo>> pageByStationGroup(StationGroup stationGroup) {
        IPage<StationGroupVo> stationGroups = stationGroupService.pageByBean(stationGroup);
        stationGroups.setRecords(stationGroupService.setVoProperties(stationGroups.getRecords()));
        log.info(String.format("根据StationGroup对象属性分页检索站群: %s ",JSONUtil.toJsonStr(stationGroups)));
        return RestResult.ok(stationGroups);
    }

}
