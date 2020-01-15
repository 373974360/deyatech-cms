package com.deyatech.resource.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.service.StationGroupUserService;
import com.deyatech.resource.vo.StationGroupUserVo;
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
 * 站点用户关联 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-09-12
 */
@Slf4j
@RestController
@RequestMapping("/resource/stationGroupUser")
@Api(tags = {"站点用户关联接口"})
public class StationGroupUserController extends BaseController {
    @Autowired
    StationGroupUserService stationGroupUserService;

    /**
     * 获取站点用户数据，已选择和未选择
     *
     * @param stationGroupId
     * @return
     */
    @RequestMapping("/getStationGroupUser")
    @ApiOperation(value="获取站点用户，已选择和未选择", notes="获取站点用户，已选择和未选择")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationGroupId", value = "站点ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "departmentId", value = "部门ID", required = true, dataType = "String", paramType = "query")
    })
    public RestResult getStationGroupUser(String stationGroupId, String departmentId) {
        return RestResult.ok(stationGroupUserService.getStationGroupUser(stationGroupId, departmentId));
    }

    /**
     * 设置站点用户
     *
     * @param stationGroupId
     * @param userIds
     * @return
     */
    @RequestMapping("/setStationGroupUsers")
    @ApiOperation(value = "设置站点用户", notes = "设置站点用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationGroupId", value = "站点id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userIds", value = "用户id", required = true, dataType = "String", paramType = "query")
    })
    public RestResult setStationGroupUsers(String stationGroupId, @RequestParam(value = "userIds[]", required = false) List<String> userIds) {
        stationGroupUserService.setStationGroupUsers(stationGroupId, userIds);
        return RestResult.ok();
    }

    /**
     * 翻页检索站点用户列表
     *
     * @param stationGroupUserVo
     * @return
     */
    @RequestMapping("/pageStationGroupUser")
    @ApiOperation(value="翻页检索站点用户列表", notes="翻页检索站点用户列表")
    @ApiImplicitParam(name = "stationGroupUserVo", value = "站点ID", required = true, dataType = "StationGroupUserVo", paramType = "query")
    public RestResult<IPage<StationGroupUserVo>> pageStationGroupUser(StationGroupUserVo stationGroupUserVo) {
        return RestResult.ok(stationGroupUserService.pageStationGroupUser(stationGroupUserVo));
    }
}
