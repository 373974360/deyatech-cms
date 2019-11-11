package com.deyatech.zsds.controller;

import cn.hutool.core.util.ObjectUtil;
import com.deyatech.zsds.entity.AgriculturalProductsUpDown;
import com.deyatech.zsds.vo.AgriculturalProductsUpDownVo;
import com.deyatech.zsds.service.AgriculturalProductsUpDownService;
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
 * 农产品上行/下行 前端控制器
 * </p>
 * @author: csm
 * @since 2019-11-08
 */
@Slf4j
@RestController
@RequestMapping("/zsds/agriculturalProductsUpDown")
@Api(tags = {"农产品上行/下行接口"})
public class AgriculturalProductsUpDownController extends BaseController {
    @Autowired
    AgriculturalProductsUpDownService agriculturalProductsUpDownService;

    /**
     * 单个保存或者更新农产品上行/下行
     *
     * @param agriculturalProductsUpDown
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新农产品上行/下行", notes="根据农产品上行/下行对象保存或者更新农产品上行/下行信息")
    @ApiImplicitParam(name = "agriculturalProductsUpDown", value = "农产品上行/下行对象", required = true, dataType = "AgriculturalProductsUpDown", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(AgriculturalProductsUpDown agriculturalProductsUpDown) {
        log.info(String.format("保存或者更新农产品上行/下行: %s ", JSONUtil.toJsonStr(agriculturalProductsUpDown)));
        boolean result = agriculturalProductsUpDownService.saveOrUpdate(agriculturalProductsUpDown);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新农产品上行/下行
     *
     * @param agriculturalProductsUpDownList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新农产品上行/下行", notes="根据农产品上行/下行对象集合批量保存或者更新农产品上行/下行信息")
    @ApiImplicitParam(name = "agriculturalProductsUpDownList", value = "农产品上行/下行对象集合", required = true, allowMultiple = true, dataType = "AgriculturalProductsUpDown", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<AgriculturalProductsUpDown> agriculturalProductsUpDownList) {
        log.info(String.format("批量保存或者更新农产品上行/下行: %s ", JSONUtil.toJsonStr(agriculturalProductsUpDownList)));
        boolean result = agriculturalProductsUpDownService.saveOrUpdateBatch(agriculturalProductsUpDownList);
        return RestResult.ok(result);
    }

    /**
     * 根据AgriculturalProductsUpDown对象属性逻辑删除农产品上行/下行
     *
     * @param agriculturalProductsUpDown
     * @return
     */
    @PostMapping("/removeByAgriculturalProductsUpDown")
    @ApiOperation(value="根据AgriculturalProductsUpDown对象属性逻辑删除农产品上行/下行", notes="根据农产品上行/下行对象逻辑删除农产品上行/下行信息")
    @ApiImplicitParam(name = "agriculturalProductsUpDown", value = "农产品上行/下行对象", required = true, dataType = "AgriculturalProductsUpDown", paramType = "query")
    public RestResult<Boolean> removeByAgriculturalProductsUpDown(AgriculturalProductsUpDown agriculturalProductsUpDown) {
        log.info(String.format("根据AgriculturalProductsUpDown对象属性逻辑删除农产品上行/下行: %s ", agriculturalProductsUpDown));
        boolean result = agriculturalProductsUpDownService.removeByBean(agriculturalProductsUpDown);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除农产品上行/下行
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除农产品上行/下行", notes="根据农产品上行/下行对象ID批量逻辑删除农产品上行/下行信息")
    @ApiImplicitParam(name = "ids", value = "农产品上行/下行对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除农产品上行/下行: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = agriculturalProductsUpDownService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据AgriculturalProductsUpDown对象属性获取农产品上行/下行
     *
     * @param agriculturalProductsUpDown
     * @return
     */
    @GetMapping("/getByAgriculturalProductsUpDown")
    @ApiOperation(value="根据AgriculturalProductsUpDown对象属性获取农产品上行/下行", notes="根据农产品上行/下行对象属性获取农产品上行/下行信息")
    @ApiImplicitParam(name = "agriculturalProductsUpDown", value = "农产品上行/下行对象", required = false, dataType = "AgriculturalProductsUpDown", paramType = "query")
    public RestResult<AgriculturalProductsUpDownVo> getByAgriculturalProductsUpDown(AgriculturalProductsUpDown agriculturalProductsUpDown) {
        agriculturalProductsUpDown = agriculturalProductsUpDownService.getByBean(agriculturalProductsUpDown);
        AgriculturalProductsUpDownVo agriculturalProductsUpDownVo = agriculturalProductsUpDownService.setVoProperties(agriculturalProductsUpDown);
        log.info(String.format("根据id获取农产品上行/下行：%s", JSONUtil.toJsonStr(agriculturalProductsUpDownVo)));
        return RestResult.ok(agriculturalProductsUpDownVo);
    }

    /**
     * 根据AgriculturalProductsUpDown对象属性检索所有农产品上行/下行
     *
     * @param agriculturalProductsUpDown
     * @return
     */
    @GetMapping("/listByAgriculturalProductsUpDown")
    @ApiOperation(value="根据AgriculturalProductsUpDown对象属性检索所有农产品上行/下行", notes="根据AgriculturalProductsUpDown对象属性检索所有农产品上行/下行信息")
    @ApiImplicitParam(name = "agriculturalProductsUpDown", value = "农产品上行/下行对象", required = false, dataType = "AgriculturalProductsUpDown", paramType = "query")
    public RestResult<Collection<AgriculturalProductsUpDownVo>> listByAgriculturalProductsUpDown(AgriculturalProductsUpDown agriculturalProductsUpDown) {
        Collection<AgriculturalProductsUpDown> agriculturalProductsUpDowns = agriculturalProductsUpDownService.listByBean(agriculturalProductsUpDown);
        Collection<AgriculturalProductsUpDownVo> agriculturalProductsUpDownVos = agriculturalProductsUpDownService.setVoProperties(agriculturalProductsUpDowns);
        log.info(String.format("根据AgriculturalProductsUpDown对象属性检索所有农产品上行/下行: %s ",JSONUtil.toJsonStr(agriculturalProductsUpDownVos)));
        return RestResult.ok(agriculturalProductsUpDownVos);
    }

    /**
     * 根据AgriculturalProductsUpDown对象属性分页检索农产品上行/下行
     *
     * @param agriculturalProductsUpDownVo
     * @return
     */
    @GetMapping("/pageByAgriculturalProductsUpDown")
    @ApiOperation(value="根据AgriculturalProductsUpDown对象属性分页检索农产品上行/下行", notes="根据AgriculturalProductsUpDown对象属性分页检索农产品上行/下行信息")
    @ApiImplicitParam(name = "agriculturalProductsUpDownVo", value = "农产品上行/下行对象", required = false, dataType = "AgriculturalProductsUpDownVo", paramType = "query")
    public RestResult<IPage<AgriculturalProductsUpDownVo>> pageByAgriculturalProductsUpDown(AgriculturalProductsUpDownVo agriculturalProductsUpDownVo) {
        IPage<AgriculturalProductsUpDownVo> agriculturalProductsUpDowns
                = agriculturalProductsUpDownService.pageByAgriculturalProductsUpDownVo(agriculturalProductsUpDownVo);
//        agriculturalProductsUpDowns.setRecords(agriculturalProductsUpDownService.setVoProperties(agriculturalProductsUpDowns.getRecords()));
        log.info(String.format("根据AgriculturalProductsUpDown对象属性分页检索农产品上行/下行: %s ",JSONUtil.toJsonStr(agriculturalProductsUpDowns)));
        return RestResult.ok(agriculturalProductsUpDowns);
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
        Map map = agriculturalProductsUpDownService.importExcel(file);
        return ObjectUtil.isNull(map.get("message")) ? RestResult.ok(map) : RestResult.error("上传失败！" + map.get("message"));
    }

}
