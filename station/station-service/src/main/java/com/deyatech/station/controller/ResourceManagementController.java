package com.deyatech.station.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.service.ResourceManagementService;
import com.deyatech.station.vo.ResourceManagementVo;
import com.deyatech.station.vo.TemplateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 * @author: lee.
 * @since 2019-11-13
 */
@Slf4j
@RestController
@RequestMapping("/station/resourceManagement")
@Api(tags = {"接口"})
public class ResourceManagementController extends BaseController {
    @Autowired
    ResourceManagementService resourceManagementService;

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
        int result = resourceManagementService.deleteBytemplateIds(ids);
        return RestResult.ok(result > 0 ? true : false);
    }


    /**
     * 根据ResourceManagement对象属性分页检索
     *
     * @param resourceManagement
     * @return
     */
    @GetMapping("/pageByResourceManagement")
    @ApiOperation(value="根据ResourceManagement对象属性分页检索", notes="根据ResourceManagement对象属性分页检索信息")
    @ApiImplicitParam(name = "resourceManagement", value = "对象", required = false, dataType = "ResourceManagement", paramType = "query")
    public RestResult<IPage<TemplateVo>> pageByResourceManagement(ResourceManagementVo resourceManagement) {
        log.info(String.format("根据ResourceManagement对象属性分页检索: %s ",JSONUtil.toJsonStr(resourceManagement)));
        IPage<TemplateVo> page = resourceManagementService.pageByResourceManagement(resourceManagement);
        return RestResult.ok(page);
    }

}
