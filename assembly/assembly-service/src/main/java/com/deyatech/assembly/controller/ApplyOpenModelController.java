package com.deyatech.assembly.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.assembly.entity.ApplyOpenModel;
import com.deyatech.assembly.vo.ApplyOpenModelVo;
import com.deyatech.assembly.service.ApplyOpenModelService;
import com.deyatech.common.entity.RestResult;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Struct;
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
 * @since 2019-10-16
 */
@Slf4j
@RestController
@RequestMapping("/assembly/applyOpenModel")
@Api(tags = {"接口"})
public class ApplyOpenModelController extends BaseController {
    @Autowired
    ApplyOpenModelService applyOpenModelService;

    /**
     * 单个保存或者更新
     *
     * @param applyOpenModel
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "applyOpenModel", value = "对象", required = true, dataType = "ApplyOpenModel", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(ApplyOpenModel applyOpenModel) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(applyOpenModel)));
        boolean result = applyOpenModelService.saveOrUpdate(applyOpenModel);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param applyOpenModelList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "applyOpenModelList", value = "对象集合", required = true, allowMultiple = true, dataType = "ApplyOpenModel", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<ApplyOpenModel> applyOpenModelList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(applyOpenModelList)));
        boolean result = applyOpenModelService.saveOrUpdateBatch(applyOpenModelList);
        return RestResult.ok(result);
    }

    /**
     * 根据ApplyOpenModel对象属性逻辑删除
     *
     * @param applyOpenModel
     * @return
     */
    @PostMapping("/removeByApplyOpenModel")
    @ApiOperation(value="根据ApplyOpenModel对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "applyOpenModel", value = "对象", required = true, dataType = "ApplyOpenModel", paramType = "query")
    public RestResult<Boolean> removeByApplyOpenModel(ApplyOpenModel applyOpenModel) {
        log.info(String.format("根据ApplyOpenModel对象属性逻辑删除: %s ", applyOpenModel));
        boolean result = applyOpenModelService.removeByBean(applyOpenModel);
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
        boolean result = applyOpenModelService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据ApplyOpenModel对象属性获取
     *
     * @param applyOpenModel
     * @return
     */
    @GetMapping("/getByApplyOpenModel")
    @ApiOperation(value="根据ApplyOpenModel对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "applyOpenModel", value = "对象", required = false, dataType = "ApplyOpenModel", paramType = "query")
    public RestResult<ApplyOpenModelVo> getByApplyOpenModel(ApplyOpenModel applyOpenModel) {
        applyOpenModel = applyOpenModelService.getByBean(applyOpenModel);
        ApplyOpenModelVo applyOpenModelVo = applyOpenModelService.setVoProperties(applyOpenModel);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(applyOpenModelVo)));
        return RestResult.ok(applyOpenModelVo);
    }

    /**
     * 根据ApplyOpenModel对象属性检索所有
     *
     * @param applyOpenModel
     * @return
     */
    @GetMapping("/listByApplyOpenModel")
    @ApiOperation(value="根据ApplyOpenModel对象属性检索所有", notes="根据ApplyOpenModel对象属性检索所有信息")
    @ApiImplicitParam(name = "applyOpenModel", value = "对象", required = false, dataType = "ApplyOpenModel", paramType = "query")
    public RestResult<Collection<ApplyOpenModelVo>> listByApplyOpenModel(ApplyOpenModel applyOpenModel) {
        Collection<ApplyOpenModel> applyOpenModels = applyOpenModelService.listByBean(applyOpenModel);
        Collection<ApplyOpenModelVo> applyOpenModelVos = applyOpenModelService.setVoProperties(applyOpenModels);
        log.info(String.format("根据ApplyOpenModel对象属性检索所有: %s ",JSONUtil.toJsonStr(applyOpenModelVos)));
        return RestResult.ok(applyOpenModelVos);
    }

    /**
     * 根据ApplyOpenModel对象属性分页检索
     *
     * @param applyOpenModel
     * @return
     */
    @GetMapping("/pageByApplyOpenModel")
    @ApiOperation(value="根据ApplyOpenModel对象属性分页检索", notes="根据ApplyOpenModel对象属性分页检索信息")
    @ApiImplicitParam(name = "applyOpenModel", value = "对象", required = false, dataType = "ApplyOpenModel", paramType = "query")
    public RestResult<IPage<ApplyOpenModelVo>> pageByApplyOpenModel(ApplyOpenModel applyOpenModel) {
        return RestResult.ok(applyOpenModelService.pageByApplyOpenModel(applyOpenModel));
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
    public RestResult<Collection<ApplyOpenModelVo>> listModelByCompetentDeptId(String departmentId) {
        QueryWrapper<ApplyOpenModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeLeft("competent_dept", departmentId)
                .or().like("part_dept",departmentId);
        Collection<ApplyOpenModel> models = applyOpenModelService.list(queryWrapper);
        Collection<ApplyOpenModelVo> modelVos = applyOpenModelService.setVoProperties(models);
        log.info(String.format("检索主管部门的模型: %s ",JSONUtil.toJsonStr(modelVos)));
        return RestResult.ok(modelVos);
    }

}
