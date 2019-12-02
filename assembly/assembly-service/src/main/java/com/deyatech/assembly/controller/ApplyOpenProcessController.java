package com.deyatech.assembly.controller;

import com.deyatech.assembly.entity.ApplyOpenProcess;
import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.vo.ApplyOpenProcessVo;
import com.deyatech.assembly.service.ApplyOpenProcessService;
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
 * @since 2019-11-30
 */
@Slf4j
@RestController
@RequestMapping("/assembly/applyOpenProcess")
@Api(tags = {"接口"})
public class ApplyOpenProcessController extends BaseController {
    @Autowired
    ApplyOpenProcessService applyOpenProcessService;

    /**
     * 单个保存或者更新
     *
     * @param applyOpenProcess
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "applyOpenProcess", value = "对象", required = true, dataType = "ApplyOpenProcess", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(ApplyOpenProcess applyOpenProcess, ApplyOpenRecord applyOpenRecord) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(applyOpenProcess)));
        applyOpenProcessService.doProcess(applyOpenProcess,applyOpenRecord);
        return RestResult.ok();
    }

    /**
     * 批量保存或者更新
     *
     * @param applyOpenProcessList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "applyOpenProcessList", value = "对象集合", required = true, allowMultiple = true, dataType = "ApplyOpenProcess", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<ApplyOpenProcess> applyOpenProcessList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(applyOpenProcessList)));
        boolean result = applyOpenProcessService.saveOrUpdateBatch(applyOpenProcessList);
        return RestResult.ok(result);
    }

    /**
     * 根据ApplyOpenProcess对象属性逻辑删除
     *
     * @param applyOpenProcess
     * @return
     */
    @PostMapping("/removeByApplyOpenProcess")
    @ApiOperation(value="根据ApplyOpenProcess对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "applyOpenProcess", value = "对象", required = true, dataType = "ApplyOpenProcess", paramType = "query")
    public RestResult<Boolean> removeByApplyOpenProcess(ApplyOpenProcess applyOpenProcess) {
        log.info(String.format("根据ApplyOpenProcess对象属性逻辑删除: %s ", applyOpenProcess));
        boolean result = applyOpenProcessService.removeByBean(applyOpenProcess);
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
        boolean result = applyOpenProcessService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据ApplyOpenProcess对象属性获取
     *
     * @param applyOpenProcess
     * @return
     */
    @GetMapping("/getByApplyOpenProcess")
    @ApiOperation(value="根据ApplyOpenProcess对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "applyOpenProcess", value = "对象", required = false, dataType = "ApplyOpenProcess", paramType = "query")
    public RestResult<ApplyOpenProcessVo> getByApplyOpenProcess(ApplyOpenProcess applyOpenProcess) {
        applyOpenProcess = applyOpenProcessService.getByBean(applyOpenProcess);
        ApplyOpenProcessVo applyOpenProcessVo = applyOpenProcessService.setVoProperties(applyOpenProcess);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(applyOpenProcessVo)));
        return RestResult.ok(applyOpenProcessVo);
    }

    /**
     * 根据ApplyOpenProcess对象属性检索所有
     *
     * @param applyOpenProcess
     * @return
     */
    @GetMapping("/listByApplyOpenProcess")
    @ApiOperation(value="根据ApplyOpenProcess对象属性检索所有", notes="根据ApplyOpenProcess对象属性检索所有信息")
    @ApiImplicitParam(name = "applyOpenProcess", value = "对象", required = false, dataType = "ApplyOpenProcess", paramType = "query")
    public RestResult<Collection<ApplyOpenProcessVo>> listByApplyOpenProcess(ApplyOpenProcess applyOpenProcess) {
        applyOpenProcess.setSortSql("create_time asc");
        Collection<ApplyOpenProcess> applyOpenProcesss = applyOpenProcessService.listByBean(applyOpenProcess);
        Collection<ApplyOpenProcessVo> applyOpenProcessVos = applyOpenProcessService.setVoProperties(applyOpenProcesss);
        log.info(String.format("根据ApplyOpenProcess对象属性检索所有: %s ",JSONUtil.toJsonStr(applyOpenProcessVos)));
        return RestResult.ok(applyOpenProcessVos);
    }

    /**
     * 根据ApplyOpenProcess对象属性分页检索
     *
     * @param applyOpenProcess
     * @return
     */
    @GetMapping("/pageByApplyOpenProcess")
    @ApiOperation(value="根据ApplyOpenProcess对象属性分页检索", notes="根据ApplyOpenProcess对象属性分页检索信息")
    @ApiImplicitParam(name = "applyOpenProcess", value = "对象", required = false, dataType = "ApplyOpenProcess", paramType = "query")
    public RestResult<IPage<ApplyOpenProcessVo>> pageByApplyOpenProcess(ApplyOpenProcess applyOpenProcess) {
        IPage<ApplyOpenProcessVo> applyOpenProcesss = applyOpenProcessService.pageByBean(applyOpenProcess);
        applyOpenProcesss.setRecords(applyOpenProcessService.setVoProperties(applyOpenProcesss.getRecords()));
        log.info(String.format("根据ApplyOpenProcess对象属性分页检索: %s ",JSONUtil.toJsonStr(applyOpenProcesss)));
        return RestResult.ok(applyOpenProcesss);
    }

}
