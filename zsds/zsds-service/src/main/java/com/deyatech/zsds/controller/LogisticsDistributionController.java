package com.deyatech.zsds.controller;

import cn.hutool.core.util.ObjectUtil;
import com.deyatech.zsds.entity.LogisticsDistribution;
import com.deyatech.zsds.vo.LogisticsDistributionVo;
import com.deyatech.zsds.service.LogisticsDistributionService;
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
 * 物流配送体系 前端控制器
 * </p>
 * @author: csm
 * @since 2019-11-08
 */
@Slf4j
@RestController
@RequestMapping("/zsds/logisticsDistribution")
@Api(tags = {"物流配送体系接口"})
public class LogisticsDistributionController extends BaseController {
    @Autowired
    LogisticsDistributionService logisticsDistributionService;

    /**
     * 单个保存或者更新物流配送体系
     *
     * @param logisticsDistribution
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新物流配送体系", notes="根据物流配送体系对象保存或者更新物流配送体系信息")
    @ApiImplicitParam(name = "logisticsDistribution", value = "物流配送体系对象", required = true, dataType = "LogisticsDistribution", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(LogisticsDistribution logisticsDistribution) {
        log.info(String.format("保存或者更新物流配送体系: %s ", JSONUtil.toJsonStr(logisticsDistribution)));
        boolean result = logisticsDistributionService.saveOrUpdate(logisticsDistribution);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新物流配送体系
     *
     * @param logisticsDistributionList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新物流配送体系", notes="根据物流配送体系对象集合批量保存或者更新物流配送体系信息")
    @ApiImplicitParam(name = "logisticsDistributionList", value = "物流配送体系对象集合", required = true, allowMultiple = true, dataType = "LogisticsDistribution", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<LogisticsDistribution> logisticsDistributionList) {
        log.info(String.format("批量保存或者更新物流配送体系: %s ", JSONUtil.toJsonStr(logisticsDistributionList)));
        boolean result = logisticsDistributionService.saveOrUpdateBatch(logisticsDistributionList);
        return RestResult.ok(result);
    }

    /**
     * 根据LogisticsDistribution对象属性逻辑删除物流配送体系
     *
     * @param logisticsDistribution
     * @return
     */
    @PostMapping("/removeByLogisticsDistribution")
    @ApiOperation(value="根据LogisticsDistribution对象属性逻辑删除物流配送体系", notes="根据物流配送体系对象逻辑删除物流配送体系信息")
    @ApiImplicitParam(name = "logisticsDistribution", value = "物流配送体系对象", required = true, dataType = "LogisticsDistribution", paramType = "query")
    public RestResult<Boolean> removeByLogisticsDistribution(LogisticsDistribution logisticsDistribution) {
        log.info(String.format("根据LogisticsDistribution对象属性逻辑删除物流配送体系: %s ", logisticsDistribution));
        boolean result = logisticsDistributionService.removeByBean(logisticsDistribution);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除物流配送体系
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除物流配送体系", notes="根据物流配送体系对象ID批量逻辑删除物流配送体系信息")
    @ApiImplicitParam(name = "ids", value = "物流配送体系对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除物流配送体系: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = logisticsDistributionService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据LogisticsDistribution对象属性获取物流配送体系
     *
     * @param logisticsDistribution
     * @return
     */
    @GetMapping("/getByLogisticsDistribution")
    @ApiOperation(value="根据LogisticsDistribution对象属性获取物流配送体系", notes="根据物流配送体系对象属性获取物流配送体系信息")
    @ApiImplicitParam(name = "logisticsDistribution", value = "物流配送体系对象", required = false, dataType = "LogisticsDistribution", paramType = "query")
    public RestResult<LogisticsDistributionVo> getByLogisticsDistribution(LogisticsDistribution logisticsDistribution) {
        logisticsDistribution = logisticsDistributionService.getByBean(logisticsDistribution);
        LogisticsDistributionVo logisticsDistributionVo = logisticsDistributionService.setVoProperties(logisticsDistribution);
        log.info(String.format("根据id获取物流配送体系：%s", JSONUtil.toJsonStr(logisticsDistributionVo)));
        return RestResult.ok(logisticsDistributionVo);
    }

    /**
     * 根据LogisticsDistribution对象属性检索所有物流配送体系
     *
     * @param logisticsDistribution
     * @return
     */
    @GetMapping("/listByLogisticsDistribution")
    @ApiOperation(value="根据LogisticsDistribution对象属性检索所有物流配送体系", notes="根据LogisticsDistribution对象属性检索所有物流配送体系信息")
    @ApiImplicitParam(name = "logisticsDistribution", value = "物流配送体系对象", required = false, dataType = "LogisticsDistribution", paramType = "query")
    public RestResult<Collection<LogisticsDistributionVo>> listByLogisticsDistribution(LogisticsDistribution logisticsDistribution) {
        Collection<LogisticsDistribution> logisticsDistributions = logisticsDistributionService.listByBean(logisticsDistribution);
        Collection<LogisticsDistributionVo> logisticsDistributionVos = logisticsDistributionService.setVoProperties(logisticsDistributions);
        log.info(String.format("根据LogisticsDistribution对象属性检索所有物流配送体系: %s ",JSONUtil.toJsonStr(logisticsDistributionVos)));
        return RestResult.ok(logisticsDistributionVos);
    }

    /**
     * 根据LogisticsDistribution对象属性分页检索物流配送体系
     *
     * @param logisticsDistributionVo
     * @return
     */
    @GetMapping("/pageByLogisticsDistribution")
    @ApiOperation(value="根据LogisticsDistribution对象属性分页检索物流配送体系", notes="根据LogisticsDistribution对象属性分页检索物流配送体系信息")
    @ApiImplicitParam(name = "logisticsDistributionVo", value = "物流配送体系对象", required = false, dataType = "LogisticsDistributionVo", paramType = "query")
    public RestResult<IPage<LogisticsDistributionVo>> pageByLogisticsDistribution(LogisticsDistributionVo logisticsDistributionVo) {
        IPage<LogisticsDistributionVo> logisticsDistributions = logisticsDistributionService.pageByLogisticsDistributionVo(logisticsDistributionVo);
//        logisticsDistributions.setRecords(logisticsDistributionService.setVoProperties(logisticsDistributions.getRecords()));
        log.info(String.format("根据LogisticsDistribution对象属性分页检索物流配送体系: %s ",JSONUtil.toJsonStr(logisticsDistributions)));
        return RestResult.ok(logisticsDistributions);
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
        Map map = logisticsDistributionService.importExcel(file);
        return ObjectUtil.isNull(map.get("message")) ? RestResult.ok(map) : RestResult.error("上传失败！" + map.get("message"));
    }

}
