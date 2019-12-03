package com.deyatech.resource.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.resource.entity.StationGroupRole;
import com.deyatech.resource.vo.StationGroupRoleVo;
import com.deyatech.resource.service.StationGroupRoleService;
import com.deyatech.common.entity.RestResult;
import io.swagger.annotations.ApiImplicitParams;
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
 * 站点角色关联 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-10-31
 */
@Slf4j
@RestController
@RequestMapping("/resource/stationGroupRole")
@Api(tags = {"站点角色关联接口"})
public class StationGroupRoleController extends BaseController {
    @Autowired
    StationGroupRoleService stationGroupRoleService;

    /**
     * 单个保存或者更新站点角色关联
     *
     * @param stationGroupRole
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新站点角色关联", notes="根据站点角色关联对象保存或者更新站点角色关联信息")
    @ApiImplicitParam(name = "stationGroupRole", value = "站点角色关联对象", required = true, dataType = "StationGroupRole", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(StationGroupRole stationGroupRole) {
        log.info(String.format("保存或者更新站点角色关联: %s ", JSONUtil.toJsonStr(stationGroupRole)));
        boolean result = stationGroupRoleService.saveOrUpdate(stationGroupRole);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新站点角色关联
     *
     * @param stationGroupRoleList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新站点角色关联", notes="根据站点角色关联对象集合批量保存或者更新站点角色关联信息")
    @ApiImplicitParam(name = "stationGroupRoleList", value = "站点角色关联对象集合", required = true, allowMultiple = true, dataType = "StationGroupRole", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<StationGroupRole> stationGroupRoleList) {
        log.info(String.format("批量保存或者更新站点角色关联: %s ", JSONUtil.toJsonStr(stationGroupRoleList)));
        boolean result = stationGroupRoleService.saveOrUpdateBatch(stationGroupRoleList);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroupRole对象属性逻辑删除站点角色关联
     *
     * @param stationGroupRole
     * @return
     */
    @PostMapping("/removeByStationGroupRole")
    @ApiOperation(value="根据StationGroupRole对象属性逻辑删除站点角色关联", notes="根据站点角色关联对象逻辑删除站点角色关联信息")
    @ApiImplicitParam(name = "stationGroupRole", value = "站点角色关联对象", required = true, dataType = "StationGroupRole", paramType = "query")
    public RestResult<Boolean> removeByStationGroupRole(StationGroupRole stationGroupRole) {
        log.info(String.format("根据StationGroupRole对象属性逻辑删除站点角色关联: %s ", stationGroupRole));
        boolean result = stationGroupRoleService.removeByBean(stationGroupRole);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除站点角色关联
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除站点角色关联", notes="根据站点角色关联对象ID批量逻辑删除站点角色关联信息")
    @ApiImplicitParam(name = "ids", value = "站点角色关联对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除站点角色关联: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = stationGroupRoleService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据角色ID批量删除站点角色关联
     *
     * @param roleIds
     * @return
     */
    @PostMapping("/removeByRoleIds")
    @ApiOperation(value="根据角色ID批量删除站点角色关联", notes="根据角色ID批量删除站点角色关联")
    @ApiImplicitParam(name = "roleIds", value = "角色ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByRoleIds(@RequestParam("roleIds[]") List<String> roleIds) {
        log.info(String.format("根据角色ID批量删除站点角色关联: %s ", JSONUtil.toJsonStr(roleIds)));
        if (CollectionUtil.isEmpty(roleIds)) {
            return RestResult.ok(false);
        }
        QueryWrapper<StationGroupRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds);
        boolean result = stationGroupRoleService.remove(queryWrapper);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroupRole对象属性获取站点角色关联
     *
     * @param stationGroupRole
     * @return
     */
    @GetMapping("/getByStationGroupRole")
    @ApiOperation(value="根据StationGroupRole对象属性获取站点角色关联", notes="根据站点角色关联对象属性获取站点角色关联信息")
    @ApiImplicitParam(name = "stationGroupRole", value = "站点角色关联对象", required = false, dataType = "StationGroupRole", paramType = "query")
    public RestResult<StationGroupRoleVo> getByStationGroupRole(StationGroupRole stationGroupRole) {
        stationGroupRole = stationGroupRoleService.getByBean(stationGroupRole);
        StationGroupRoleVo stationGroupRoleVo = stationGroupRoleService.setVoProperties(stationGroupRole);
        log.info(String.format("根据id获取站点角色关联：%s", JSONUtil.toJsonStr(stationGroupRoleVo)));
        return RestResult.ok(stationGroupRoleVo);
    }

    /**
     * 根据StationGroupRole对象属性检索所有站点角色关联
     *
     * @param stationGroupRole
     * @return
     */
    @GetMapping("/listByStationGroupRole")
    @ApiOperation(value="根据StationGroupRole对象属性检索所有站点角色关联", notes="根据StationGroupRole对象属性检索所有站点角色关联信息")
    @ApiImplicitParam(name = "stationGroupRole", value = "站点角色关联对象", required = false, dataType = "StationGroupRole", paramType = "query")
    public RestResult<Collection<StationGroupRoleVo>> listByStationGroupRole(StationGroupRole stationGroupRole) {
        Collection<StationGroupRole> stationGroupRoles = stationGroupRoleService.listByBean(stationGroupRole);
        Collection<StationGroupRoleVo> stationGroupRoleVos = stationGroupRoleService.setVoProperties(stationGroupRoles);
        log.info(String.format("根据StationGroupRole对象属性检索所有站点角色关联: %s ",JSONUtil.toJsonStr(stationGroupRoleVos)));
        return RestResult.ok(stationGroupRoleVos);
    }

    /**
     * 根据StationGroupRole对象属性分页检索站点角色关联
     *
     * @param stationGroupRole
     * @return
     */
    @GetMapping("/pageByStationGroupRole")
    @ApiOperation(value="根据StationGroupRole对象属性分页检索站点角色关联", notes="根据StationGroupRole对象属性分页检索站点角色关联信息")
    @ApiImplicitParam(name = "stationGroupRole", value = "站点角色关联对象", required = false, dataType = "StationGroupRole", paramType = "query")
    public RestResult<IPage<StationGroupRoleVo>> pageByStationGroupRole(StationGroupRole stationGroupRole) {
        IPage<StationGroupRoleVo> stationGroupRoles = stationGroupRoleService.pageByBean(stationGroupRole);
        stationGroupRoles.setRecords(stationGroupRoleService.setVoProperties(stationGroupRoles.getRecords()));
        log.info(String.format("根据StationGroupRole对象属性分页检索站点角色关联: %s ",JSONUtil.toJsonStr(stationGroupRoles)));
        return RestResult.ok(stationGroupRoles);
    }

    /**
     * 设置角色站点
     *
     * @param roleId
     * @param stationGroupIds
     * @return
     */
    @PostMapping("/setRoleStationGroups")
    @ApiOperation(value = "设置角色站点", notes = "设置角色站点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "stationGroupIds", value = "站点id", required = true, dataType = "String", paramType = "query")
    })
    public RestResult setRoleStationGroups(String roleId, @RequestParam(value = "stationGroupIds[]", required = false) List<String> stationGroupIds) {
        stationGroupRoleService.setRoleStationGroups(roleId, stationGroupIds);
        return RestResult.ok();
    }

    /**
     * 根据User对象属性分页检索系统用户信息
     *
     * @param user
     * @return
     */
    @GetMapping("/pageStationAssociationUser")
    @ApiOperation(value="根据User对象属性分页检索系统用户信息", notes="根据User对象属性分页检索系统用户信息信息")
    @ApiImplicitParam(name = "user", value = "系统用户信息对象", required = false, dataType = "User", paramType = "query")
    public RestResult<IPage<UserVo>> pageStationAssociationUser(String siteId, UserVo user) {
        if (StrUtil.isEmpty(siteId)) {
            return RestResult.error("站点ID不存在");
        }
        IPage<UserVo> users = stationGroupRoleService.pageStationAssociationUser(siteId, user);
        log.info(String.format("根据User对象属性分页检索系统用户信息: %s ",JSONUtil.toJsonStr(users)));
        return RestResult.ok(users);
    }
}
