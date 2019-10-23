package com.deyatech.resource.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.CascaderResult;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.EnableEnum;
import com.deyatech.common.utils.CascaderUtil;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.entity.StationGroupClassification;
import com.deyatech.resource.service.StationGroupService;
import com.deyatech.resource.vo.StationGroupClassificationVo;
import com.deyatech.resource.vo.StationGroupVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 站点 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-08-01
 */
@Slf4j
@RestController
@RequestMapping("/resource/stationGroup")
@Api(tags = {"站点接口"})
public class StationGroupController extends BaseController {
    @Autowired
    StationGroupService stationGroupService;

    /**
     * 获取的级联对象
     *
     * @param stationGroupClassification
     * @return
     */
    @GetMapping("/getClassificationStationCascader")
    @ApiOperation(value="获取的级联对象", notes="获取的级联对象")
    @ApiImplicitParam(name = "stationGroupClassification", value = "stationGroupClassification", required = false, dataType = "StationGroupClassification", paramType = "query")
    public RestResult<List<CascaderResult>> getCascader(String userId, StationGroupClassification stationGroupClassification) {
        Collection<StationGroupClassificationVo> stationGroupClassificationVos = stationGroupService.getClassificationStationTree(userId, stationGroupClassification);
        List<CascaderResult> cascaderResults = CascaderUtil.getResult("Id", "Name","TreePosition", stationGroupClassification.getId(), stationGroupClassificationVos);
        log.info(String.format("获取的级联对象: %s ",JSONUtil.toJsonStr(cascaderResults)));
        return RestResult.ok(cascaderResults);
    }

    /**
     * 单个保存或者更新站点
     *
     * @param stationGroup
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新站点", notes="根据站点对象保存或者更新站点信息")
    @ApiImplicitParam(name = "stationGroup", value = "站点对象", required = true, dataType = "StationGroup", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(StationGroup stationGroup) {
        log.info(String.format("保存或者更新站点: %s ", JSONUtil.toJsonStr(stationGroup)));
        long count = this.stationGroupService.countName(stationGroup.getId(), stationGroup.getName());
        if (count > 0) {
            return RestResult.error("该名称已存在");
        }
        count = this.stationGroupService.countEnglishName(stationGroup.getId(), stationGroup.getEnglishName());
        if (count > 0) {
            return RestResult.error("该英文名称已存在");
        }
        count = this.stationGroupService.countAbbreviation(stationGroup.getId(), stationGroup.getAbbreviation());
        if (count > 0) {
            return RestResult.error("该简称已存在");
        }
        boolean result = stationGroupService.saveOrUpdateAndNginx(stationGroup);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroup对象属性逻辑删除站点
     *
     * @param stationGroup
     * @return
     */
    @PostMapping("/removeByStationGroup")
    @ApiOperation(value="根据StationGroup对象属性逻辑删除站点", notes="根据站点对象逻辑删除站点信息")
    @ApiImplicitParam(name = "stationGroup", value = "站点对象", required = true, dataType = "StationGroup", paramType = "query")
    public RestResult<Boolean> removeByStationGroup(StationGroup stationGroup) {
        log.info(String.format("根据StationGroup对象属性逻辑删除站点: %s ", stationGroup));
        if (stationGroup.getEnable() == EnableEnum.ENABLE.getCode()) {
            return RestResult.error("已启用的站点不允许删除");
        }
        Map<String, StationGroup> maps = new HashMap<>();
        maps.put(stationGroup.getId(), stationGroup);
        List<String> ids = new ArrayList<>();
        ids.add(stationGroup.getId());
        boolean result = stationGroupService.removeStationGroupAndConfig(ids, maps);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除站点
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除站点", notes="根据站点对象ID批量逻辑删除站点信息")
    @ApiImplicitParam(name = "ids", value = "站点对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除站点: %s ", JSONUtil.toJsonStr(ids)));
        Map<String, StationGroup> maps = new HashMap<>();
        for(String id : ids) {
            StationGroup stationGroup = stationGroupService.getById(id);
            maps.put(id, stationGroup);
            if (stationGroup.getEnable() == EnableEnum.ENABLE.getCode()) {
                return RestResult.error("已启用的站点不允许删除");
            }
        }
        boolean result = stationGroupService.removeStationGroupAndConfig(ids, maps);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroup对象属性获取站点
     *
     * @param stationGroup
     * @return
     */
    @GetMapping("/getByStationGroup")
    @ApiOperation(value="根据StationGroup对象属性获取站点", notes="根据站点对象属性获取站点信息")
    @ApiImplicitParam(name = "stationGroup", value = "站点对象", required = false, dataType = "StationGroup", paramType = "query")
    public RestResult<StationGroupVo> getByStationGroup(StationGroup stationGroup) {
        stationGroup = stationGroupService.getByBean(stationGroup);
        StationGroupVo stationGroupVo = stationGroupService.setVoProperties(stationGroup);
        log.info(String.format("根据id获取站点：%s", JSONUtil.toJsonStr(stationGroupVo)));
        return RestResult.ok(stationGroupVo);
    }

    /**
     * 根据StationGroup对象属性分页检索站点
     *
     * @param stationGroupVo
     * @return
     */
    @GetMapping("/pageByStationGroup")
    @ApiOperation(value="根据stationGroupVo对象属性分页检索站点", notes="根据StationGroupVo对象属性分页检索站点信息")
    @ApiImplicitParam(name = "stationGroupVo", value = "站点对象", required = false, dataType = "StationGroupVo", paramType = "query")
    public RestResult<IPage<StationGroupVo>> pageByStationGroup(StationGroupVo stationGroupVo) {
        IPage<StationGroupVo> stationGroups = stationGroupService.pageSelectByStationGroupVo(stationGroupVo);
        stationGroups.setRecords(stationGroupService.setVoProperties(stationGroups.getRecords()));
        log.info(String.format("根据StationGroup对象属性分页检索站点: %s ",JSONUtil.toJsonStr(stationGroups)));
        return RestResult.ok(stationGroups);
    }

    /**
     * 根据StationGroup对象属性检索所有站点
     *
     * @param stationGroupVo
     * @return
     */
    @GetMapping("/listByStationGroup")
    @ApiOperation(value="根据StationGroupVo对象属性检索所有站点", notes="根据StationGroupVo对象属性检索所有站点信息")
    @ApiImplicitParam(name = "stationGroupVo", value = "站点对象", required = false, dataType = "StationGroupVo", paramType = "query")
    public RestResult<Collection<StationGroupVo>> listByStationGroup(StationGroupVo stationGroupVo) {
        Collection<StationGroupVo> stationGroupVos = stationGroupService.listSelectByStationGroupVo(stationGroupVo);
        log.info(String.format("根据StationGroup对象属性检索所有站点: %s ",JSONUtil.toJsonStr(stationGroupVos)));
        return RestResult.ok(stationGroupVos);
    }

    /**
     * 站点名称重名检查
     *
     * @param id
     * @param name
     * @return
     */
    @RequestMapping("/isNameExist")
    @ApiOperation(value="站点名称重名检查", notes="站点名称重名检查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "站点编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "站点名称", required = true, dataType = "String", paramType = "query")
    })
    public RestResult<Boolean> isNameExist(@RequestParam(required = false) String id, String name) {
        log.info(String.format("站点名称重名检查: id = %s, name = %s", id, name));
        long count = this.stationGroupService.countName(id, name);
        if (count > 0) {
            return new RestResult(200, "该名称已存在", true);
        } else {
            return RestResult.ok(false);
        }
    }

    /**
     * 站点英文名称重名检查
     *
     * @param id
     * @param englishName
     * @return
     */
    @RequestMapping("/isEnglishNameExist")
    @ApiOperation(value="站点英文名称重名检查", notes="站点英文名称重名检查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "站点编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "englishName", value = "站点英文名称", required = true, dataType = "String", paramType = "query")
    })
    public RestResult<Boolean> isEnglishNameExist(@RequestParam(required = false) String id, String englishName) {
        log.info(String.format("站点英文名称重名检查: id = %s, englishName = %s", id, englishName));
        long count = this.stationGroupService.countEnglishName(id, englishName);
        if (count > 0) {
            return new RestResult(200, "该英文名称已存在", true);
        } else {
            return RestResult.ok(false);
        }
    }

    /**
     * 站点简称重名检查
     *
     * @param id
     * @param abbreviation
     * @return
     */
    @RequestMapping("/isAbbreviationExist")
    @ApiOperation(value="站点简称重名检查", notes="站点简称重名检查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "站点编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "abbreviation", value = "站点简称", required = true, dataType = "String", paramType = "query")
    })
    public RestResult<Boolean> isAbbreviationExist(@RequestParam(required = false) String id, String abbreviation) {
        log.info(String.format("站点简称重名检查: id = %s, englishName = %s", id, abbreviation));
        long count = this.stationGroupService.countAbbreviation(id, abbreviation);
        if (count > 0) {
            return new RestResult(200, "该简称已存在", true);
        } else {
            return RestResult.ok(false);
        }
    }

    /**
     * 启用或停用站点
     *
     * @param id
     * @param flag
     * @return
     */
    @RequestMapping("/runOrStopStationById")
    @ApiOperation(value="启用或停用站点", notes="启用或停用站点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "站点编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "flag", value = "启用或停用标记", required = true, dataType = "String", paramType = "query"),
    })
    public RestResult<Boolean> runOrStopStationById(String id, String flag) {
        log.info(String.format("启用或停用站点 id = %s, flag = %s", id , flag));
        return RestResult.ok(this.stationGroupService.runOrStopStationById(id, flag));
    }
}
