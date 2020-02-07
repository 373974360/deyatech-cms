package com.deyatech.statistics.controller;

import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.statistics.service.DepartmentDataService;
import com.deyatech.statistics.vo.DepartmentUserDataQueryVo;
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
 * @since 2020-02-05
 */
@Slf4j
@RestController
@RequestMapping("/statistics/departmentData")
public class DepartmentDataController extends BaseController {
    @Autowired
    DepartmentDataService departmentDataService;

    /**
     * 检索部门统计数据
     *
     * @param queryVo
     * @return
     */
    @RequestMapping("/getDepartmentData")
    @ApiOperation(value="检索部门统计数据", notes="检索部门统计数据")
    @ApiImplicitParam(name = "queryVo", value = "对象", required = true, dataType = "DepartmentDataQueryVo", paramType = "query")
    public RestResult getDepartmentData(DepartmentUserDataQueryVo queryVo) throws Exception {
        return RestResult.ok(departmentDataService.getDepartmentData(queryVo));
    }

    /**
     * 检索部门栏目统计数据
     *
     * @param queryVo
     * @return
     */
    @RequestMapping("/getDepartmentCatalogData")
    @ApiOperation(value="检索部门栏目统计数据", notes="检索部门栏目统计数据")
    @ApiImplicitParam(name = "queryVo", value = "对象", required = true, dataType = "DepartmentDataQueryVo", paramType = "query")
    public RestResult getDepartmentCatalogData(DepartmentUserDataQueryVo queryVo) throws Exception {
        return RestResult.ok(departmentDataService.getDepartmentCatalogData(queryVo));
    }

    /**
     * 检索部门栏目内容数据
     *
     * @param queryVo
     * @return
     */
    @RequestMapping("/getDepartmentCatalogTemplateData")
    @ApiOperation(value="检索部门栏目统计数据", notes="检索部门栏目统计数据")
    @ApiImplicitParam(name = "queryVo", value = "对象", required = true, dataType = "DepartmentDataQueryVo", paramType = "query")
    public RestResult getDepartmentCatalogTemplateData(DepartmentUserDataQueryVo queryVo) throws Exception {
        return RestResult.ok(departmentDataService.getDepartmentCatalogTemplateData(queryVo));
    }
}
