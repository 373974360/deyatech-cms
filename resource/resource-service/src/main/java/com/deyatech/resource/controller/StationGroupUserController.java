package com.deyatech.resource.controller;

import com.deyatech.resource.entity.StationGroupUser;
import com.deyatech.resource.vo.StationGroupUserVo;
import com.deyatech.resource.service.StationGroupUserService;
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
 * 站群用户关联 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-09-12
 */
@Slf4j
@RestController
@RequestMapping("/resource/stationGroupUser")
@Api(tags = {"站群用户关联接口"})
public class StationGroupUserController extends BaseController {
    @Autowired
    StationGroupUserService stationGroupUserService;

    /**
     * 单个保存或者更新站群用户关联
     *
     * @param stationGroupUser
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新站群用户关联", notes="根据站群用户关联对象保存或者更新站群用户关联信息")
    @ApiImplicitParam(name = "stationGroupUser", value = "站群用户关联对象", required = true, dataType = "StationGroupUser", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(StationGroupUser stationGroupUser) {
        log.info(String.format("保存或者更新站群用户关联: %s ", JSONUtil.toJsonStr(stationGroupUser)));
        boolean result = stationGroupUserService.saveOrUpdate(stationGroupUser);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新站群用户关联
     *
     * @param stationGroupUserList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新站群用户关联", notes="根据站群用户关联对象集合批量保存或者更新站群用户关联信息")
    @ApiImplicitParam(name = "stationGroupUserList", value = "站群用户关联对象集合", required = true, allowMultiple = true, dataType = "StationGroupUser", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<StationGroupUser> stationGroupUserList) {
        log.info(String.format("批量保存或者更新站群用户关联: %s ", JSONUtil.toJsonStr(stationGroupUserList)));
        boolean result = stationGroupUserService.saveOrUpdateBatch(stationGroupUserList);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroupUser对象属性逻辑删除站群用户关联
     *
     * @param stationGroupUser
     * @return
     */
    @PostMapping("/removeByStationGroupUser")
    @ApiOperation(value="根据StationGroupUser对象属性逻辑删除站群用户关联", notes="根据站群用户关联对象逻辑删除站群用户关联信息")
    @ApiImplicitParam(name = "stationGroupUser", value = "站群用户关联对象", required = true, dataType = "StationGroupUser", paramType = "query")
    public RestResult<Boolean> removeByStationGroupUser(StationGroupUser stationGroupUser) {
        log.info(String.format("根据StationGroupUser对象属性逻辑删除站群用户关联: %s ", stationGroupUser));
        boolean result = stationGroupUserService.removeByBean(stationGroupUser);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除站群用户关联
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除站群用户关联", notes="根据站群用户关联对象ID批量逻辑删除站群用户关联信息")
    @ApiImplicitParam(name = "ids", value = "站群用户关联对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除站群用户关联: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = stationGroupUserService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据StationGroupUser对象属性获取站群用户关联
     *
     * @param stationGroupUser
     * @return
     */
    @GetMapping("/getByStationGroupUser")
    @ApiOperation(value="根据StationGroupUser对象属性获取站群用户关联", notes="根据站群用户关联对象属性获取站群用户关联信息")
    @ApiImplicitParam(name = "stationGroupUser", value = "站群用户关联对象", required = false, dataType = "StationGroupUser", paramType = "query")
    public RestResult<StationGroupUserVo> getByStationGroupUser(StationGroupUser stationGroupUser) {
        stationGroupUser = stationGroupUserService.getByBean(stationGroupUser);
        StationGroupUserVo stationGroupUserVo = stationGroupUserService.setVoProperties(stationGroupUser);
        log.info(String.format("根据id获取站群用户关联：%s", JSONUtil.toJsonStr(stationGroupUserVo)));
        return RestResult.ok(stationGroupUserVo);
    }

    /**
     * 根据StationGroupUser对象属性检索所有站群用户关联
     *
     * @param stationGroupUser
     * @return
     */
    @GetMapping("/listByStationGroupUser")
    @ApiOperation(value="根据StationGroupUser对象属性检索所有站群用户关联", notes="根据StationGroupUser对象属性检索所有站群用户关联信息")
    @ApiImplicitParam(name = "stationGroupUser", value = "站群用户关联对象", required = false, dataType = "StationGroupUser", paramType = "query")
    public RestResult<Collection<StationGroupUserVo>> listByStationGroupUser(StationGroupUser stationGroupUser) {
        Collection<StationGroupUser> stationGroupUsers = stationGroupUserService.listByBean(stationGroupUser);
        Collection<StationGroupUserVo> stationGroupUserVos = stationGroupUserService.setVoProperties(stationGroupUsers);
        log.info(String.format("根据StationGroupUser对象属性检索所有站群用户关联: %s ",JSONUtil.toJsonStr(stationGroupUserVos)));
        return RestResult.ok(stationGroupUserVos);
    }

    /**
     * 根据StationGroupUser对象属性分页检索站群用户关联
     *
     * @param stationGroupUser
     * @return
     */
    @GetMapping("/pageByStationGroupUser")
    @ApiOperation(value="根据StationGroupUser对象属性分页检索站群用户关联", notes="根据StationGroupUser对象属性分页检索站群用户关联信息")
    @ApiImplicitParam(name = "stationGroupUser", value = "站群用户关联对象", required = false, dataType = "StationGroupUser", paramType = "query")
    public RestResult<IPage<StationGroupUserVo>> pageByStationGroupUser(StationGroupUser stationGroupUser) {
        IPage<StationGroupUserVo> stationGroupUsers = stationGroupUserService.pageByBean(stationGroupUser);
        stationGroupUsers.setRecords(stationGroupUserService.setVoProperties(stationGroupUsers.getRecords()));
        log.info(String.format("根据StationGroupUser对象属性分页检索站群用户关联: %s ",JSONUtil.toJsonStr(stationGroupUsers)));
        return RestResult.ok(stationGroupUsers);
    }

}
