package com.deyatech.station.controller;

import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.service.TemplateUserAuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 内容模板用户权限信息 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-10-11
 */
@Slf4j
@RestController
@RequestMapping("/station/templateUserAuthority")
@Api(tags = {"内容模板用户权限信息接口"})
public class TemplateUserAuthorityController extends BaseController {
    @Autowired
    TemplateUserAuthorityService templateUserAuthorityService;

    /**
     * 获取用户权限
     *
     * @param userIds
     * @return
     */
    @RequestMapping("/getUsersAuthority")
    @ApiOperation(value="获取用户权限", notes="获取用户权限")
    @ApiImplicitParam(name = "userIds", value = "用户id", required = true, dataType = "List", paramType = "query")
    public RestResult<String> getUsersAuthority(@RequestParam("userIds[]") List<String> userIds) {
        return RestResult.ok(templateUserAuthorityService.getUsersAuthority(userIds));
    }

    /**
     * 设置用户权限
     *
     * @param userIds
     * @return
     */
    @RequestMapping("/setUsersAuthority")
    @ApiOperation(value="设置用户权限", notes="设置用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIds", value = "用户id", required = true, dataType = "List", paramType = "query"),
            @ApiImplicitParam(name = "authority", value = "权限", required = true, dataType = "authority", paramType = "query")
    })
    public RestResult setUsersAuthority(@RequestParam("userIds[]") List<String> userIds,
                                        @RequestParam(value = "authority" ,required = false) String authority) {
        templateUserAuthorityService.setUsersAuthority(userIds, authority);
        return RestResult.ok(true);
    }
}
