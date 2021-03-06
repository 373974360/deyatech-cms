package com.deyatech.appeal.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.admin.entity.Role;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.appeal.entity.Model;
import com.deyatech.appeal.service.RecordService;
import com.deyatech.appeal.vo.ModelVo;
import com.deyatech.appeal.service.ModelService;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.entity.CascaderResult;
import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.feign.ResourceFeign;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
@RequestMapping("/appeal/model")
@Api(tags = {"接口"})
public class ModelController extends BaseController {
    @Autowired
    ModelService modelService;
    @Autowired
    RecordService recordService;
    @Autowired
    AdminFeign adminFeign;
    @Autowired
    ResourceFeign resourceFeign;
    /**
     * 单个保存或者更新
     *
     * @param model
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "model", value = "对象", required = true, dataType = "Model", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Model model) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(model)));
        boolean result = modelService.saveOrUpdate(model);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param modelList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "modelList", value = "对象集合", required = true, allowMultiple = true, dataType = "Model", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Model> modelList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(modelList)));
        boolean result = modelService.saveOrUpdateBatch(modelList);
        return RestResult.ok(result);
    }

    /**
     * 根据Model对象属性逻辑删除
     *
     * @param model
     * @return
     */
    @PostMapping("/removeByModel")
    @ApiOperation(value="根据Model对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "model", value = "对象", required = true, dataType = "Model", paramType = "query")
    public RestResult<Boolean> removeByModel(Model model) {
        log.info(String.format("根据Model对象属性逻辑删除: %s ", model));
        boolean result = modelService.removeByBean(model);
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
        boolean result = modelService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Model对象属性获取
     *
     * @param model
     * @return
     */
    @GetMapping("/getByModel")
    @ApiOperation(value="根据Model对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "model", value = "对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<ModelVo> getByModel(Model model) {
        model = modelService.getByBean(model);
        ModelVo modelVo = modelService.setVoProperties(model);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(modelVo)));
        return RestResult.ok(modelVo);
    }

    /**
     * 根据Model对象属性检索所有
     *
     * @param model
     * @return
     */
    @GetMapping("/listByModel")
    @ApiOperation(value="根据Model对象属性检索所有", notes="根据Model对象属性检索所有信息")
    @ApiImplicitParam(name = "model", value = "对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<Collection<ModelVo>> listByModel(Model model) {
        Collection<Model> models = modelService.listByBean(model);
        Collection<ModelVo> modelVos = modelService.setVoProperties(models);
        log.info(String.format("根据Model对象属性检索所有: %s ",JSONUtil.toJsonStr(modelVos)));
        return RestResult.ok(modelVos);
    }

    /**
     * 根据Model对象属性分页检索
     *
     * @param model
     * @return
     */
    @GetMapping("/pageByModel")
    @ApiOperation(value="根据Model对象属性分页检索", notes="根据Model对象属性分页检索信息")
    @ApiImplicitParam(name = "model", value = "对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<IPage<ModelVo>> pageByModel(Model model) {
        IPage<ModelVo> models = modelService.pageByModel(model);
        List<ModelVo> list = models.getRecords();
        if (CollectionUtil.isNotEmpty(list)) {
            List<RecordVo> usageCounts = recordService.countModel();
            if (CollectionUtil.isNotEmpty(usageCounts)) {
                Map<String, Long> usageCountMap = usageCounts.stream().collect(Collectors.toMap(RecordVo::getModelId, RecordVo::getNumber));
                list.stream().parallel().forEach(m -> m.setUsageCount(Objects.isNull(usageCountMap.get(m.getId())) ? 0 : usageCountMap.get(m.getId())));
            }
        }
        log.info(String.format("根据Model对象属性分页检索: %s ",JSONUtil.toJsonStr(models)));
        return RestResult.ok(models);
    }

    /**
     * 检索主管部门的模型
     *
     * @param departmentId
     * @return
     */
    @GetMapping("/listModelByCompetentDeptId")
    @ApiOperation(value="检索主管部门的模型", notes="检索主管部门的模型")
    @ApiImplicitParam(name = "model", value = "对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<Collection<ModelVo>> listModelByCompetentDeptId(String departmentId) {
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        boolean allModel = false;
        List<Role> roles = adminFeign.getRolesByUserId(UserContextHelper.getUserId()).getData();
        if(CollectionUtil.isNotEmpty(roles)){
            for (Role role:roles){
                //当前用户拥有系统类型的角色
                if(role.getType() == 3){
                    allModel = true;
                }
            }
        }
        //当前用户角色不是系统角色
        if(!allModel){
            queryWrapper.likeLeft("competent_dept", departmentId)
                    .or().like("part_dept",departmentId);
        }
        Collection<Model> models = modelService.list(queryWrapper);
        Collection<ModelVo> modelVos = modelService.setVoProperties(models);
        log.info(String.format("检索主管部门的模型: %s ",JSONUtil.toJsonStr(modelVos)));
        return RestResult.ok(modelVos);
    }

    /**
     * 统计部门的模型件数
     *
     * @param departmentIds
     * @return
     */
    @RequestMapping("/countModelByDepartmentId")
    @ApiOperation(value="统计部门的模型件数", notes="统计部门的模型件数")
    @ApiImplicitParam(name = "departmentId", value = "部门对象", required = false, dataType = "Model", paramType = "query")
    public RestResult<Integer> countModelByDepartmentId(@RequestParam("departmentIds[]") List<String> departmentIds) {
        return RestResult.ok(modelService.countModelByDepartmentId(departmentIds));
    }

    /**
     * 根据站点ID获取组织机构树
     *
     * @param siteId
     * @return
     */
    @GetMapping("/getDepartmentTreeBySiteId")
    @ApiOperation(value="获取系统部门信息的级联对象", notes="获取系统部门信息的级联对象")
    @ApiImplicitParam(name = "siteId", value = "站点ID", required = false, dataType = "Department", paramType = "query")
    public RestResult<List<CascaderResult>> getDepartmentTreeBySiteId(String siteId, Integer layer) {
        StationGroup stationGroup = resourceFeign.getStationGroupById(siteId).getData();
        List<CascaderResult> cascaderResults = adminFeign.getDepartmentTreeByParentId(stationGroup.getDepartmentId(),layer).getData();
        log.info(String.format("获取系统部门信息的级联对象: %s ",JSONUtil.toJsonStr(cascaderResults)));
        return RestResult.ok(cascaderResults);
    }
}
