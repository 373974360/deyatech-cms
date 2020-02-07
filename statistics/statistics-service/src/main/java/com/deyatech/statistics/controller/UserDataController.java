package com.deyatech.statistics.controller;

import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.statistics.service.UserDataService;
import com.deyatech.statistics.vo.DepartmentUserDataQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户数据统计 前端控制器
 * </p>
 * @author: ycx
 * @since 2020-01-06
 */
@Slf4j
@RestController
@RequestMapping("/statistics/userData")
@Api(tags = {"用户数据统计"})
public class UserDataController extends BaseController {
    @Autowired
    UserDataService userDataService;

    /**
     * 检索部门用户树统计数据
     *
     * @param queryVo
     * @return
     */
    @RequestMapping("/getDepartmentUserData")
    @ApiOperation(value="检索部门用户树统计数据", notes="检索部门用户树统计数据")
    @ApiImplicitParam(name = "queryVo", value = "对象", required = true, dataType = "DepartmentUserDataQueryVo", paramType = "query")
    public RestResult getDepartmentUserData(DepartmentUserDataQueryVo queryVo) throws Exception {
        return RestResult.ok(userDataService.getDepartmentUserData(queryVo));
    }

    /**
     * 检索用户栏目统计数据
     *
     * @param queryVo
     * @return
     */
    @RequestMapping("/getUserCatalogData")
    @ApiOperation(value="检索用户栏目统计数据", notes="检索用户栏目统计数据")
    @ApiImplicitParam(name = "queryVo", value = "对象", required = true, dataType = "DepartmentUserDataQueryVo", paramType = "query")
    public RestResult getUserCatalogData(DepartmentUserDataQueryVo queryVo) throws Exception {
        return RestResult.ok(userDataService.getUserCatalogData(queryVo));
    }

    /**
     * 检索用户栏目内容数据
     *
     * @param queryVo
     * @return
     */
    @RequestMapping("/getUserCatalogTemplateData")
    @ApiOperation(value="检索用户栏目内容数据", notes="检索用户栏目内容数据")
    @ApiImplicitParam(name = "queryVo", value = "对象", required = true, dataType = "DepartmentDataQueryVo", paramType = "query")
    public RestResult getUserCatalogTemplateData(DepartmentUserDataQueryVo queryVo) {
        return RestResult.ok(userDataService.getUserCatalogTemplateData(queryVo));
    }
}
