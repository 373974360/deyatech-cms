package com.deyatech.zsds.controller;

import cn.hutool.core.util.ObjectUtil;
import com.deyatech.zsds.entity.OnlineTraining;
import com.deyatech.zsds.vo.OnlineTrainingVo;
import com.deyatech.zsds.service.OnlineTrainingService;
import com.deyatech.common.entity.RestResult;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 在线培训系统 前端控制器
 * </p>
 * @author: csm
 * @since 2019-11-08
 */
@Slf4j
@RestController
@RequestMapping("/zsds/onlineTraining")
@Api(tags = {"在线培训系统接口"})
public class OnlineTrainingController extends BaseController {
    @Autowired
    OnlineTrainingService onlineTrainingService;

    /**
     * 单个保存或者更新在线培训系统
     *
     * @param onlineTraining
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新在线培训系统", notes="根据在线培训系统对象保存或者更新在线培训系统信息")
    @ApiImplicitParam(name = "onlineTraining", value = "在线培训系统对象", required = true, dataType = "OnlineTraining", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(OnlineTraining onlineTraining) {
        log.info(String.format("保存或者更新在线培训系统: %s ", JSONUtil.toJsonStr(onlineTraining)));
        boolean result = onlineTrainingService.saveOrUpdate(onlineTraining);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新在线培训系统
     *
     * @param onlineTrainingList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新在线培训系统", notes="根据在线培训系统对象集合批量保存或者更新在线培训系统信息")
    @ApiImplicitParam(name = "onlineTrainingList", value = "在线培训系统对象集合", required = true, allowMultiple = true, dataType = "OnlineTraining", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<OnlineTraining> onlineTrainingList) {
        log.info(String.format("批量保存或者更新在线培训系统: %s ", JSONUtil.toJsonStr(onlineTrainingList)));
        boolean result = onlineTrainingService.saveOrUpdateBatch(onlineTrainingList);
        return RestResult.ok(result);
    }

    /**
     * 根据OnlineTraining对象属性逻辑删除在线培训系统
     *
     * @param onlineTraining
     * @return
     */
    @PostMapping("/removeByOnlineTraining")
    @ApiOperation(value="根据OnlineTraining对象属性逻辑删除在线培训系统", notes="根据在线培训系统对象逻辑删除在线培训系统信息")
    @ApiImplicitParam(name = "onlineTraining", value = "在线培训系统对象", required = true, dataType = "OnlineTraining", paramType = "query")
    public RestResult<Boolean> removeByOnlineTraining(OnlineTraining onlineTraining) {
        log.info(String.format("根据OnlineTraining对象属性逻辑删除在线培训系统: %s ", onlineTraining));
        boolean result = onlineTrainingService.removeByBean(onlineTraining);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除在线培训系统
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除在线培训系统", notes="根据在线培训系统对象ID批量逻辑删除在线培训系统信息")
    @ApiImplicitParam(name = "ids", value = "在线培训系统对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除在线培训系统: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = onlineTrainingService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据OnlineTraining对象属性获取在线培训系统
     *
     * @param onlineTraining
     * @return
     */
    @GetMapping("/getByOnlineTraining")
    @ApiOperation(value="根据OnlineTraining对象属性获取在线培训系统", notes="根据在线培训系统对象属性获取在线培训系统信息")
    @ApiImplicitParam(name = "onlineTraining", value = "在线培训系统对象", required = false, dataType = "OnlineTraining", paramType = "query")
    public RestResult<OnlineTrainingVo> getByOnlineTraining(OnlineTraining onlineTraining) {
        onlineTraining = onlineTrainingService.getByBean(onlineTraining);
        OnlineTrainingVo onlineTrainingVo = onlineTrainingService.setVoProperties(onlineTraining);
        log.info(String.format("根据id获取在线培训系统：%s", JSONUtil.toJsonStr(onlineTrainingVo)));
        return RestResult.ok(onlineTrainingVo);
    }

    /**
     * 根据OnlineTraining对象属性检索所有在线培训系统
     *
     * @param onlineTraining
     * @return
     */
    @GetMapping("/listByOnlineTraining")
    @ApiOperation(value="根据OnlineTraining对象属性检索所有在线培训系统", notes="根据OnlineTraining对象属性检索所有在线培训系统信息")
    @ApiImplicitParam(name = "onlineTraining", value = "在线培训系统对象", required = false, dataType = "OnlineTraining", paramType = "query")
    public RestResult<Collection<OnlineTrainingVo>> listByOnlineTraining(OnlineTraining onlineTraining) {
        Collection<OnlineTraining> onlineTrainings = onlineTrainingService.listByBean(onlineTraining);
        Collection<OnlineTrainingVo> onlineTrainingVos = onlineTrainingService.setVoProperties(onlineTrainings);
        log.info(String.format("根据OnlineTraining对象属性检索所有在线培训系统: %s ",JSONUtil.toJsonStr(onlineTrainingVos)));
        return RestResult.ok(onlineTrainingVos);
    }

    /**
     * 根据OnlineTraining对象属性分页检索在线培训系统
     *
     * @param onlineTrainingVo
     * @return
     */
    @GetMapping("/pageByOnlineTraining")
    @ApiOperation(value="根据OnlineTraining对象属性分页检索在线培训系统", notes="根据OnlineTraining对象属性分页检索在线培训系统信息")
    @ApiImplicitParam(name = "onlineTrainingVo", value = "在线培训系统对象", required = false, dataType = "OnlineTrainingVo", paramType = "query")
    public RestResult<IPage<OnlineTrainingVo>> pageByOnlineTraining(OnlineTrainingVo onlineTrainingVo) {
        IPage<OnlineTrainingVo> onlineTrainings = onlineTrainingService.pageByOnlineTrainingVo(onlineTrainingVo);
//        onlineTrainings.setRecords(onlineTrainingService.setVoProperties(onlineTrainings.getRecords()));
        log.info(String.format("根据OnlineTraining对象属性分页检索在线培训系统: %s ",JSONUtil.toJsonStr(onlineTrainings)));
        return RestResult.ok(onlineTrainings);
    }

    /**
     * 上传Excel
     *
     * @param file
     * @return
     */
    @PostMapping("/importExcel")
    @ApiOperation(value="上传Excel", notes="上传Excel")
    @ApiImplicitParam(name = "file", value = "Excel文件", required = true, dataType = "MultipartFile", paramType = "query")
    public RestResult importExcel(MultipartFile file) {
        Map map = onlineTrainingService.importExcel(file);
        return ObjectUtil.isNull(map.get("message")) ? RestResult.ok(map) : RestResult.error("上传失败！" + map.get("message"));
    }

}
