package com.deyatech.station.controller;

import com.deyatech.station.entity.CatalogUser;
import com.deyatech.station.vo.CatalogUserVo;
import com.deyatech.station.service.CatalogUserService;
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
     * 单个保存或者更新栏目用户关联信息
     *
     * @param catalogUser
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新栏目用户关联信息", notes="根据栏目用户关联信息对象保存或者更新栏目用户关联信息信息")
    @ApiImplicitParam(name = "catalogUser", value = "栏目用户关联信息对象", required = true, dataType = "CatalogUser", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(CatalogUser catalogUser) {
        log.info(String.format("保存或者更新栏目用户关联信息: %s ", JSONUtil.toJsonStr(catalogUser)));
        boolean result = catalogUserService.saveOrUpdate(catalogUser);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新栏目用户关联信息
     *
     * @param catalogUserList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新栏目用户关联信息", notes="根据栏目用户关联信息对象集合批量保存或者更新栏目用户关联信息信息")
    @ApiImplicitParam(name = "catalogUserList", value = "栏目用户关联信息对象集合", required = true, allowMultiple = true, dataType = "CatalogUser", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<CatalogUser> catalogUserList) {
        log.info(String.format("批量保存或者更新栏目用户关联信息: %s ", JSONUtil.toJsonStr(catalogUserList)));
        boolean result = catalogUserService.saveOrUpdateBatch(catalogUserList);
        return RestResult.ok(result);
    }

    /**
     * 根据CatalogUser对象属性逻辑删除栏目用户关联信息
     *
     * @param catalogUser
     * @return
     */
    @PostMapping("/removeByCatalogUser")
    @ApiOperation(value="根据CatalogUser对象属性逻辑删除栏目用户关联信息", notes="根据栏目用户关联信息对象逻辑删除栏目用户关联信息信息")
    @ApiImplicitParam(name = "catalogUser", value = "栏目用户关联信息对象", required = true, dataType = "CatalogUser", paramType = "query")
    public RestResult<Boolean> removeByCatalogUser(CatalogUser catalogUser) {
        log.info(String.format("根据CatalogUser对象属性逻辑删除栏目用户关联信息: %s ", catalogUser));
        boolean result = catalogUserService.removeByBean(catalogUser);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除栏目用户关联信息
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除栏目用户关联信息", notes="根据栏目用户关联信息对象ID批量逻辑删除栏目用户关联信息信息")
    @ApiImplicitParam(name = "ids", value = "栏目用户关联信息对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除栏目用户关联信息: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = catalogUserService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据CatalogUser对象属性获取栏目用户关联信息
     *
     * @param catalogUser
     * @return
     */
    @GetMapping("/getByCatalogUser")
    @ApiOperation(value="根据CatalogUser对象属性获取栏目用户关联信息", notes="根据栏目用户关联信息对象属性获取栏目用户关联信息信息")
    @ApiImplicitParam(name = "catalogUser", value = "栏目用户关联信息对象", required = false, dataType = "CatalogUser", paramType = "query")
    public RestResult<CatalogUserVo> getByCatalogUser(CatalogUser catalogUser) {
        catalogUser = catalogUserService.getByBean(catalogUser);
        CatalogUserVo catalogUserVo = catalogUserService.setVoProperties(catalogUser);
        log.info(String.format("根据id获取栏目用户关联信息：%s", JSONUtil.toJsonStr(catalogUserVo)));
        return RestResult.ok(catalogUserVo);
    }

    /**
     * 根据CatalogUser对象属性检索所有栏目用户关联信息
     *
     * @param catalogUser
     * @return
     */
    @GetMapping("/listByCatalogUser")
    @ApiOperation(value="根据CatalogUser对象属性检索所有栏目用户关联信息", notes="根据CatalogUser对象属性检索所有栏目用户关联信息信息")
    @ApiImplicitParam(name = "catalogUser", value = "栏目用户关联信息对象", required = false, dataType = "CatalogUser", paramType = "query")
    public RestResult<Collection<CatalogUserVo>> listByCatalogUser(CatalogUser catalogUser) {
        Collection<CatalogUser> catalogUsers = catalogUserService.listByBean(catalogUser);
        Collection<CatalogUserVo> catalogUserVos = catalogUserService.setVoProperties(catalogUsers);
        log.info(String.format("根据CatalogUser对象属性检索所有栏目用户关联信息: %s ",JSONUtil.toJsonStr(catalogUserVos)));
        return RestResult.ok(catalogUserVos);
    }

    /**
     * 根据CatalogUser对象属性分页检索栏目用户关联信息
     *
     * @param catalogUser
     * @return
     */
    @GetMapping("/pageByCatalogUser")
    @ApiOperation(value="根据CatalogUser对象属性分页检索栏目用户关联信息", notes="根据CatalogUser对象属性分页检索栏目用户关联信息信息")
    @ApiImplicitParam(name = "catalogUser", value = "栏目用户关联信息对象", required = false, dataType = "CatalogUser", paramType = "query")
    public RestResult<IPage<CatalogUserVo>> pageByCatalogUser(CatalogUser catalogUser) {
        IPage<CatalogUserVo> catalogUsers = catalogUserService.pageByBean(catalogUser);
        catalogUsers.setRecords(catalogUserService.setVoProperties(catalogUsers.getRecords()));
        log.info(String.format("根据CatalogUser对象属性分页检索栏目用户关联信息: %s ",JSONUtil.toJsonStr(catalogUsers)));
        return RestResult.ok(catalogUsers);
    }

    /**
     * 设置用户栏目
     *
     * @param userId
     * @param catalogIds
     * @return
     */
    @PostMapping("/setUserCatalogs")
    @ApiOperation(value="设置用户栏目", notes="设置用户栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "catalogIds", value = "栏目id", required = true, dataType = "String", paramType = "query")
    })
    public RestResult setUserCatalogs(String userId, @RequestParam(value = "catalogIds[]", required = false) List<String> catalogIds) {
        catalogUserService.setUserCatalogs(userId, catalogIds);
        return RestResult.ok();
    }
}
