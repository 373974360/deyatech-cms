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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
