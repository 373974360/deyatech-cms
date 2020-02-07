package com.deyatech.resource.controller;

import com.deyatech.resource.entity.StationGroupDepartmentAdmin;
import com.deyatech.resource.vo.StationGroupDepartmentAdminVo;
import com.deyatech.resource.service.StationGroupDepartmentAdminService;
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
 * 站部门管理员 前端控制器
 * </p>
 * @author: ycx
 * @since 2020-02-06
 */
@Slf4j
@RestController
@RequestMapping("/resource/stationGroupDepartmentAdmin")
@Api(tags = {"站部门管理员接口"})
public class StationGroupDepartmentAdminController extends BaseController {
    @Autowired
    StationGroupDepartmentAdminService stationGroupDepartmentAdminService;

    /**
     * 单个保存或者更新站部门管理员
     *
     * @param stationGroupDepartmentAdmin
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新站部门管理员", notes="根据站部门管理员对象保存或者更新站部门管理员信息")
    @ApiImplicitParam(name = "stationGroupDepartmentAdmin", value = "站部门管理员对象", required = true, dataType = "StationGroupDepartmentAdmin", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(StationGroupDepartmentAdmin stationGroupDepartmentAdmin) {
        log.info(String.format("保存或者更新站部门管理员: %s ", JSONUtil.toJsonStr(stationGroupDepartmentAdmin)));
        boolean result = stationGroupDepartmentAdminService.saveOrUpdate(stationGroupDepartmentAdmin);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新站部门管理员
     *
     * @param stationGroupDepartmentAdminList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新站部门管理员", notes="根据站部门管理员对象集合批量保存或者更新站部门管理员信息")
    @ApiImplicitParam(name = "stationGroupDepartmentAdminList", value = "站部门管理员对象集合", required = true, allowMultiple = true, dataType = "StationGroupDepartmentAdmin", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<StationGroupDepartmentAdmin> stationGroupDepartmentAdminList) {
        log.info(String.format("批量保存或者更新站部门管理员: %s ", JSONUtil.toJsonStr(stationGroupDepartmentAdminList)));
        boolean result = stationGroupDepartmentAdminService.saveOrUpdateBatch(stationGroupDepartmentAdminList);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroupDepartmentAdmin对象属性逻辑删除站部门管理员
     *
     * @param stationGroupDepartmentAdmin
     * @return
     */
    @PostMapping("/removeByStationGroupDepartmentAdmin")
    @ApiOperation(value="根据StationGroupDepartmentAdmin对象属性逻辑删除站部门管理员", notes="根据站部门管理员对象逻辑删除站部门管理员信息")
    @ApiImplicitParam(name = "stationGroupDepartmentAdmin", value = "站部门管理员对象", required = true, dataType = "StationGroupDepartmentAdmin", paramType = "query")
    public RestResult<Boolean> removeByStationGroupDepartmentAdmin(StationGroupDepartmentAdmin stationGroupDepartmentAdmin) {
        log.info(String.format("根据StationGroupDepartmentAdmin对象属性逻辑删除站部门管理员: %s ", stationGroupDepartmentAdmin));
        boolean result = stationGroupDepartmentAdminService.removeByBean(stationGroupDepartmentAdmin);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除站部门管理员
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除站部门管理员", notes="根据站部门管理员对象ID批量逻辑删除站部门管理员信息")
    @ApiImplicitParam(name = "ids", value = "站部门管理员对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除站部门管理员: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = stationGroupDepartmentAdminService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroupDepartmentAdmin对象属性获取站部门管理员
     *
     * @param stationGroupDepartmentAdmin
     * @return
     */
    @GetMapping("/getByStationGroupDepartmentAdmin")
    @ApiOperation(value="根据StationGroupDepartmentAdmin对象属性获取站部门管理员", notes="根据站部门管理员对象属性获取站部门管理员信息")
    @ApiImplicitParam(name = "stationGroupDepartmentAdmin", value = "站部门管理员对象", required = false, dataType = "StationGroupDepartmentAdmin", paramType = "query")
    public RestResult<StationGroupDepartmentAdminVo> getByStationGroupDepartmentAdmin(StationGroupDepartmentAdmin stationGroupDepartmentAdmin) {
        stationGroupDepartmentAdmin = stationGroupDepartmentAdminService.getByBean(stationGroupDepartmentAdmin);
        StationGroupDepartmentAdminVo stationGroupDepartmentAdminVo = stationGroupDepartmentAdminService.setVoProperties(stationGroupDepartmentAdmin);
        log.info(String.format("根据id获取站部门管理员：%s", JSONUtil.toJsonStr(stationGroupDepartmentAdminVo)));
        return RestResult.ok(stationGroupDepartmentAdminVo);
    }

    /**
     * 根据StationGroupDepartmentAdmin对象属性检索所有站部门管理员
     *
     * @param stationGroupDepartmentAdmin
     * @return
     */
    @GetMapping("/listByStationGroupDepartmentAdmin")
    @ApiOperation(value="根据StationGroupDepartmentAdmin对象属性检索所有站部门管理员", notes="根据StationGroupDepartmentAdmin对象属性检索所有站部门管理员信息")
    @ApiImplicitParam(name = "stationGroupDepartmentAdmin", value = "站部门管理员对象", required = false, dataType = "StationGroupDepartmentAdmin", paramType = "query")
    public RestResult<Collection<StationGroupDepartmentAdminVo>> listByStationGroupDepartmentAdmin(StationGroupDepartmentAdmin stationGroupDepartmentAdmin) {
        Collection<StationGroupDepartmentAdmin> stationGroupDepartmentAdmins = stationGroupDepartmentAdminService.listByBean(stationGroupDepartmentAdmin);
        Collection<StationGroupDepartmentAdminVo> stationGroupDepartmentAdminVos = stationGroupDepartmentAdminService.setVoProperties(stationGroupDepartmentAdmins);
        log.info(String.format("根据StationGroupDepartmentAdmin对象属性检索所有站部门管理员: %s ",JSONUtil.toJsonStr(stationGroupDepartmentAdminVos)));
        return RestResult.ok(stationGroupDepartmentAdminVos);
    }

    /**
     * 根据StationGroupDepartmentAdmin对象属性分页检索站部门管理员
     *
     * @param stationGroupDepartmentAdmin
     * @return
     */
    @GetMapping("/pageByStationGroupDepartmentAdmin")
    @ApiOperation(value="根据StationGroupDepartmentAdmin对象属性分页检索站部门管理员", notes="根据StationGroupDepartmentAdmin对象属性分页检索站部门管理员信息")
    @ApiImplicitParam(name = "stationGroupDepartmentAdmin", value = "站部门管理员对象", required = false, dataType = "StationGroupDepartmentAdmin", paramType = "query")
    public RestResult<IPage<StationGroupDepartmentAdminVo>> pageByStationGroupDepartmentAdmin(StationGroupDepartmentAdmin stationGroupDepartmentAdmin) {
        IPage<StationGroupDepartmentAdminVo> stationGroupDepartmentAdmins = stationGroupDepartmentAdminService.pageByBean(stationGroupDepartmentAdmin);
        stationGroupDepartmentAdmins.setRecords(stationGroupDepartmentAdminService.setVoProperties(stationGroupDepartmentAdmins.getRecords()));
        log.info(String.format("根据StationGroupDepartmentAdmin对象属性分页检索站部门管理员: %s ",JSONUtil.toJsonStr(stationGroupDepartmentAdmins)));
        return RestResult.ok(stationGroupDepartmentAdmins);
    }

}
