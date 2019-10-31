package com.deyatech.resource.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.Constants;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.CascaderResult;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.utils.CascaderUtil;
import com.deyatech.resource.entity.StationGroupClassification;
import com.deyatech.resource.service.StationGroupClassificationService;
import com.deyatech.resource.service.StationGroupService;
import com.deyatech.resource.vo.StationGroupClassificationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        if(StrUtil.isEmpty(stationGroupClassification.getParentId())) {
            stationGroupClassification.setParentId(Constants.ZERO);
        }
        long count = stationGroupService.countStationGroupByClassificationId(stationGroupClassification.getParentId());
        if (count > 0) {
            return RestResult.error("当前分类下已存在站点，不能添加分类");
        }
        count = stationGroupClassificationService.countNameByParentId(
                stationGroupClassification.getId(),
                stationGroupClassification.getParentId(),
                stationGroupClassification.getName());
        if (count > 0) {
            return RestResult.error("当前分类下已存在该名称");
        }
        count = stationGroupClassificationService.countEnglishNameByParentId(
                stationGroupClassification.getId(),
                stationGroupClassification.getParentId(),
                stationGroupClassification.getEnglishName());
        if (count > 0) {
            return RestResult.error("当前分类下已存在该英文名称");
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
        if (StrUtil.isEmpty(stationGroupClassification.getId())) {
            stationGroupClassification = stationGroupClassificationService.getByBean(stationGroupClassification);
        }
        if (StrUtil.isNotEmpty(stationGroupClassification.getId())) {
            List<String> ids = new ArrayList<>();
            ids.add(stationGroupClassification.getId());
            long count = stationGroupService.countStationGroupByClassificationIdList(ids);
            if (count > 0) {
                return RestResult.error("当前分类下已存在站点，不允许删除");
            }
            count = stationGroupClassificationService.countClassificationByParentIdList(ids);
            if (count > 0) {
                return RestResult.error("当前分类下已存在子分类，不允许删除");
            }
        }
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
        long count = stationGroupService.countStationGroupByClassificationIdList(ids);
        if (count > 0) {
            return RestResult.error("当前分类下已存在站点，不允许删除");
        }
        count = stationGroupClassificationService.countClassificationByParentIdList(ids);
        if (count > 0) {
            return RestResult.error("当前分类下已存在子分类，不允许删除");
        }
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

    /**
     * 分类名称重名检查
     *
     * @param id
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping("/isNameExist")
    @ApiOperation(value="分类名称重名检查", notes="分类名称重名检查")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "id", value = "分类编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "parentId", value = "分类编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "分类名称", required = true, dataType = "String", paramType = "query")
    })
    public RestResult<Boolean> isNameExist(String id, String parentId, String name) {
        log.info(String.format("分类名称重名检查: id = %s, parentId = %s, name = %s",id, parentId, name));
        long count = stationGroupClassificationService.countNameByParentId(id, parentId, name);
        if (count > 0) {
            return new RestResult(200, "当前分类下已存在该名称", true);
        } else {
            return RestResult.ok(false);
        }
    }

    /**
     * 分类英文名称重名检查
     *
     * @param id
     * @param parentId
     * @param englishName
     * @return
     */
    @RequestMapping("/isEnglishNameExist")
    @ApiOperation(value="分类英文名称重名检查", notes="分类英文名称重名检查")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "id", value = "分类编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "parentId", value = "父分类编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "分类英文名称", required = true, dataType = "String", paramType = "query")
    })
    public RestResult<Boolean> isEnglishNameExist(String id, String parentId, String englishName) {
        log.info(String.format("分类英文名称重名检查: id = %s, parentId = %s, englishName = %s", id, parentId, englishName));
        long count = stationGroupClassificationService.countEnglishNameByParentId(id, parentId, englishName);
        if (count > 0) {
            return new RestResult(200, "当前分类下已存在该英文名称", true);
        } else {
            return RestResult.ok(false);
        }
    }

    /**
     * 检查分类下有无站点或分类
     *
     * @param ids
     * @return
     */
    @PostMapping("/hasStationOrClassification")
    @ApiOperation(value="检查分类下有无站点或分类", notes="检查分类下有无站点或分类")
    @ApiImplicitParam(name = "ids", value = "对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> hasStationOrClassification(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("检查分类下有无站点或分类: %s ", JSONUtil.toJsonStr(ids)));
        long count = stationGroupService.countStationGroupByClassificationIdList(ids);
        if (count > 0) {
            return new RestResult(200, "当前分类下已存在站点", true);
        }
        count = stationGroupClassificationService.countClassificationByParentIdList(ids);
        if (count > 0) {
            return new RestResult(200, "当前分类下已存在子分类", true);
        }
        return RestResult.ok(false);
    }

    /**
     * 检查分类下有无站点
     *
     * @param id
     * @return
     */
    @RequestMapping("/hasStation")
    @ApiOperation(value="检查分类下有无站点", notes="检查分类下有无站点")
    @ApiImplicitParam(name = "id", value = "分类编号", required = true, dataType = "String", paramType = "query")
    public RestResult<Boolean> hasStation(String id) {
        log.info(String.format("检查分类下有无站点: id = %s",id));
        long count = stationGroupService.countStationGroupByClassificationId(id);
        return RestResult.ok(count > 0 ? true : false);
    }

    /**
     * 下一个排序号
     *
     * @return
     */
    @RequestMapping("/getNextSortNo")
    @ApiOperation(value = "下一个排序号", notes = "下一个排序号")
    public RestResult<Integer> getNextSortNo() {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("ifnull(max(sort_no), 0) + 1 as sortNo");
        return RestResult.ok(stationGroupClassificationService.getMap(queryWrapper).get("sortNo"));
    }
}
