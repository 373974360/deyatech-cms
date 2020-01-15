package com.deyatech.station.controller;

import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.service.CatalogUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 栏目用户关联信息 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-10-10
 */
@Slf4j
@RestController
@RequestMapping("/station/catalogUser")
@Api(tags = {"栏目用户关联信息接口"})
public class CatalogUserController extends BaseController {
    @Autowired
    CatalogUserService catalogUserService;

    /**
     * 取得用户栏目
     *
     * @param userIds
     * @return
     */
    @RequestMapping("/getUsersCatalogs")
    @ApiOperation(value="取得用户栏目", notes="取得用户栏目")
    @ApiImplicitParam(name = "userIds", value = "用户id", required = true, dataType = "List", paramType = "query")
    public RestResult getUsersCatalogs(@RequestParam("userIds[]") List<String> userIds) {
        return RestResult.ok(catalogUserService.getUsersCatalogs(userIds));
    }

    /**
     * 设置用户栏目
     *
     * @param userIds
     * @param catalogIds
     * @return
     */
    @RequestMapping("/setUsersCatalogs")
    @ApiOperation(value="设置用户栏目", notes="设置用户栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIds", value = "用户id", required = true, dataType = "List", paramType = "query"),
            @ApiImplicitParam(name = "catalogIds", value = "栏目id", required = true, dataType = "List", paramType = "query")
    })
    public RestResult setUsersCatalogs(@RequestParam("userIds[]") List<String> userIds,
                                       @RequestParam(value = "catalogIds[]", required = false) List<String> catalogIds) {
        catalogUserService.setUsersCatalogs(userIds, catalogIds);
        return RestResult.ok();
    }
}
