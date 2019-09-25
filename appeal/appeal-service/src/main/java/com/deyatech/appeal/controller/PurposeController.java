package com.deyatech.appeal.controller;

import com.deyatech.appeal.entity.Purpose;
import com.deyatech.appeal.vo.PurposeVo;
import com.deyatech.appeal.service.PurposeService;
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
 * @since 2019-09-24
 */
@Slf4j
@RestController
@RequestMapping("/appeal/purpose")
@Api(tags = {"接口"})
public class PurposeController extends BaseController {
    @Autowired
    PurposeService purposeService;

    /**
     * 单个保存或者更新
     *
     * @param purpose
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "purpose", value = "对象", required = true, dataType = "Purpose", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Purpose purpose) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(purpose)));
        boolean result = purposeService.saveOrUpdate(purpose);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param purposeList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "purposeList", value = "对象集合", required = true, allowMultiple = true, dataType = "Purpose", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Purpose> purposeList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(purposeList)));
        boolean result = purposeService.saveOrUpdateBatch(purposeList);
        return RestResult.ok(result);
    }

    /**
     * 根据Purpose对象属性逻辑删除
     *
     * @param purpose
     * @return
     */
    @PostMapping("/removeByPurpose")
    @ApiOperation(value="根据Purpose对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "purpose", value = "对象", required = true, dataType = "Purpose", paramType = "query")
    public RestResult<Boolean> removeByPurpose(Purpose purpose) {
        log.info(String.format("根据Purpose对象属性逻辑删除: %s ", purpose));
        boolean result = purposeService.removeByBean(purpose);
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
        boolean result = purposeService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Purpose对象属性获取
     *
     * @param purpose
     * @return
     */
    @GetMapping("/getByPurpose")
    @ApiOperation(value="根据Purpose对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "purpose", value = "对象", required = false, dataType = "Purpose", paramType = "query")
    public RestResult<PurposeVo> getByPurpose(Purpose purpose) {
        purpose = purposeService.getByBean(purpose);
        PurposeVo purposeVo = purposeService.setVoProperties(purpose);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(purposeVo)));
        return RestResult.ok(purposeVo);
    }

    /**
     * 根据Purpose对象属性检索所有
     *
     * @param purpose
     * @return
     */
    @GetMapping("/listByPurpose")
    @ApiOperation(value="根据Purpose对象属性检索所有", notes="根据Purpose对象属性检索所有信息")
    @ApiImplicitParam(name = "purpose", value = "对象", required = false, dataType = "Purpose", paramType = "query")
    public RestResult<Collection<PurposeVo>> listByPurpose(Purpose purpose) {
        Collection<Purpose> purposes = purposeService.listByBean(purpose);
        Collection<PurposeVo> purposeVos = purposeService.setVoProperties(purposes);
        log.info(String.format("根据Purpose对象属性检索所有: %s ",JSONUtil.toJsonStr(purposeVos)));
        return RestResult.ok(purposeVos);
    }

    /**
     * 根据Purpose对象属性分页检索
     *
     * @param purpose
     * @return
     */
    @GetMapping("/pageByPurpose")
    @ApiOperation(value="根据Purpose对象属性分页检索", notes="根据Purpose对象属性分页检索信息")
    @ApiImplicitParam(name = "purpose", value = "对象", required = false, dataType = "Purpose", paramType = "query")
    public RestResult<IPage<PurposeVo>> pageByPurpose(Purpose purpose) {
        IPage<PurposeVo> purposes = purposeService.pageByBean(purpose);
        purposes.setRecords(purposeService.setVoProperties(purposes.getRecords()));
        log.info(String.format("根据Purpose对象属性分页检索: %s ",JSONUtil.toJsonStr(purposes)));
        return RestResult.ok(purposes);
    }

}
