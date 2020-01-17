package com.deyatech.apply.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.admin.entity.Role;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.apply.entity.OpenModel;
import com.deyatech.apply.service.OpenRecordService;
import com.deyatech.apply.service.OpenModelService;
import com.deyatech.apply.vo.OpenModelVo;
import com.deyatech.apply.vo.OpenRecordVo;
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
 * @since 2019-10-16
 */
@Slf4j
@RestController
@RequestMapping("/apply/openModel")
@Api(tags = {"接口"})
public class OpenModelController extends BaseController {
    @Autowired
    OpenModelService openModelService;
    @Autowired
    OpenRecordService openRecordService;
    @Autowired
    AdminFeign adminFeign;
    @Autowired
    ResourceFeign resourceFeign;

    /**
     * 单个保存或者更新
     *
     * @param openModel
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "openModel", value = "对象", required = true, dataType = "OpenModel", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(OpenModel openModel) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(openModel)));
        boolean result = openModelService.saveOrUpdate(openModel);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param openModelList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "openModelList", value = "对象集合", required = true, allowMultiple = true, dataType = "OpenModel", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<OpenModel> openModelList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(openModelList)));
        boolean result = openModelService.saveOrUpdateBatch(openModelList);
        return RestResult.ok(result);
    }

    /**
     * 根据openModel对象属性逻辑删除
     *
     * @param openModel
     * @return
     */
    @PostMapping("/removeByOpenModel")
    @ApiOperation(value="根据openModel对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "openModel", value = "对象", required = true, dataType = "OpenModel", paramType = "query")
    public RestResult<Boolean> removeByOpenModel(OpenModel openModel) {
        log.info(String.format("根据openModel对象属性逻辑删除: %s ", openModel));
        boolean result = openModelService.removeByBean(openModel);
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
        boolean result = openModelService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据openModel对象属性获取
     *
     * @param openModel
     * @return
     */
    @GetMapping("/getByOpenModel")
    @ApiOperation(value="根据OpenModel对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "openModel", value = "对象", required = false, dataType = "OpenModel", paramType = "query")
    public RestResult<OpenModelVo> getByOpenModel(OpenModel openModel) {
        openModel = openModelService.getByBean(openModel);
        OpenModelVo openModelVo = openModelService.setVoProperties(openModel);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(openModelVo)));
        return RestResult.ok(openModelVo);
    }

    /**
     * 根据openModel对象属性检索所有
     *
     * @param openModel
     * @return
     */
    @GetMapping("/listByOpenModel")
    @ApiOperation(value="根据openModel对象属性检索所有", notes="根据openModel对象属性检索所有信息")
    @ApiImplicitParam(name = "openModel", value = "对象", required = false, dataType = "openModel", paramType = "query")
    public RestResult<Collection<OpenModelVo>> listByOpenModel(OpenModel openModel) {
        Collection<OpenModel> openModels = openModelService.listByBean(openModel);
        Collection<OpenModelVo> openModelVos = openModelService.setVoProperties(openModels);
        log.info(String.format("根据openModel对象属性检索所有: %s ",JSONUtil.toJsonStr(openModelVos)));
        return RestResult.ok(openModelVos);
    }

    /**
     * 根据openModel对象属性分页检索
     *
     * @param openModel
     * @return
     */
    @GetMapping("/pageByOpenModel")
    @ApiOperation(value="根据openModel对象属性分页检索", notes="根据openModel对象属性分页检索信息")
    @ApiImplicitParam(name = "openModel", value = "对象", required = false, dataType = "openModel", paramType = "query")
    public RestResult<IPage<OpenModelVo>> pageByOpenModel(OpenModel openModel) {
        IPage<OpenModelVo> page = openModelService.pageByOpenModel(openModel);
        List<OpenModelVo> list = page.getRecords();
        if (CollectionUtil.isNotEmpty(list)) {
            List<OpenRecordVo> usageCounts = openRecordService.countOpenModel();
            if (CollectionUtil.isNotEmpty(usageCounts)) {
                Map<String, Long> usageCountMap = usageCounts.stream().parallel().collect(Collectors.toMap(OpenRecordVo::getModelId, OpenRecordVo::getNumber));
                list.stream().parallel().forEach(m -> m.setUsageCount(Objects.isNull(usageCountMap.get(m.getId())) ? 0 : usageCountMap.get(m.getId())));
            }
        }
        return RestResult.ok(page);
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
    public RestResult<Collection<OpenModelVo>> listModelByCompetentDeptId(String departmentId) {
        QueryWrapper<OpenModel> queryWrapper = new QueryWrapper<>();
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
        Collection<OpenModel> models = openModelService.list(queryWrapper);
        Collection<OpenModelVo> modelVos = openModelService.setVoProperties(models);
        log.info(String.format("检索主管部门的模型: %s ",JSONUtil.toJsonStr(modelVos)));
        return RestResult.ok(modelVos);
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
