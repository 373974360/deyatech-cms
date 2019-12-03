package com.deyatech.station.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.entity.CatalogRole;
import com.deyatech.station.service.CatalogRoleService;
import com.deyatech.station.vo.CatalogRoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色栏目关联 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-11-01
 */
@Slf4j
@RestController
@RequestMapping("/station/catalogRole")
@Api(tags = {"角色栏目关联接口"})
public class CatalogRoleController extends BaseController {
    @Autowired
    CatalogRoleService catalogRoleService;

    /**
     * 单个保存或者更新角色栏目关联
     *
     * @param catalogRole
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新角色栏目关联", notes="根据角色栏目关联对象保存或者更新角色栏目关联信息")
    @ApiImplicitParam(name = "catalogRole", value = "角色栏目关联对象", required = true, dataType = "CatalogRole", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(CatalogRole catalogRole) {
        log.info(String.format("保存或者更新角色栏目关联: %s ", JSONUtil.toJsonStr(catalogRole)));
        boolean result = catalogRoleService.saveOrUpdate(catalogRole);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新角色栏目关联
     *
     * @param catalogRoleList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新角色栏目关联", notes="根据角色栏目关联对象集合批量保存或者更新角色栏目关联信息")
    @ApiImplicitParam(name = "catalogRoleList", value = "角色栏目关联对象集合", required = true, allowMultiple = true, dataType = "CatalogRole", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<CatalogRole> catalogRoleList) {
        log.info(String.format("批量保存或者更新角色栏目关联: %s ", JSONUtil.toJsonStr(catalogRoleList)));
        boolean result = catalogRoleService.saveOrUpdateBatch(catalogRoleList);
        return RestResult.ok(result);
    }

    /**
     * 根据CatalogRole对象属性逻辑删除角色栏目关联
     *
     * @param catalogRole
     * @return
     */
    @PostMapping("/removeByCatalogRole")
    @ApiOperation(value="根据CatalogRole对象属性逻辑删除角色栏目关联", notes="根据角色栏目关联对象逻辑删除角色栏目关联信息")
    @ApiImplicitParam(name = "catalogRole", value = "角色栏目关联对象", required = true, dataType = "CatalogRole", paramType = "query")
    public RestResult<Boolean> removeByCatalogRole(CatalogRole catalogRole) {
        log.info(String.format("根据CatalogRole对象属性逻辑删除角色栏目关联: %s ", catalogRole));
        boolean result = catalogRoleService.removeByBean(catalogRole);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除角色栏目关联
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除角色栏目关联", notes="根据角色栏目关联对象ID批量逻辑删除角色栏目关联信息")
    @ApiImplicitParam(name = "ids", value = "角色栏目关联对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除角色栏目关联: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = catalogRoleService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据角色ID批量删除角色栏目关联
     *
     * @param roleIds
     * @return
     */
    @PostMapping("/removeByRoleIds")
    @ApiOperation(value="根据角色ID批量删除角色栏目关联", notes="根据角色ID批量删除角色栏目关联")
    @ApiImplicitParam(name = "roleIds", value = "角色ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByRoleIds(@RequestParam("roleIds[]") List<String> roleIds) {
        log.info(String.format("根据角色ID批量删除角色栏目关联: %s ", JSONUtil.toJsonStr(roleIds)));
        if (CollectionUtil.isEmpty(roleIds)) {
            return RestResult.ok(false);
        }
        QueryWrapper<CatalogRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds);
        boolean result = catalogRoleService.remove(queryWrapper);
        return RestResult.ok(result);
    }

    /**
     * 根据CatalogRole对象属性获取角色栏目关联
     *
     * @param catalogRole
     * @return
     */
    @GetMapping("/getByCatalogRole")
    @ApiOperation(value="根据CatalogRole对象属性获取角色栏目关联", notes="根据角色栏目关联对象属性获取角色栏目关联信息")
    @ApiImplicitParam(name = "catalogRole", value = "角色栏目关联对象", required = false, dataType = "CatalogRole", paramType = "query")
    public RestResult<CatalogRoleVo> getByCatalogRole(CatalogRole catalogRole) {
        catalogRole = catalogRoleService.getByBean(catalogRole);
        CatalogRoleVo catalogRoleVo = catalogRoleService.setVoProperties(catalogRole);
        log.info(String.format("根据id获取角色栏目关联：%s", JSONUtil.toJsonStr(catalogRoleVo)));
        return RestResult.ok(catalogRoleVo);
    }

    /**
     * 根据CatalogRole对象属性检索所有角色栏目关联
     *
     * @param catalogRole
     * @return
     */
    @GetMapping("/listByCatalogRole")
    @ApiOperation(value="根据CatalogRole对象属性检索所有角色栏目关联", notes="根据CatalogRole对象属性检索所有角色栏目关联信息")
    @ApiImplicitParam(name = "catalogRole", value = "角色栏目关联对象", required = false, dataType = "CatalogRole", paramType = "query")
    public RestResult<Collection<CatalogRoleVo>> listByCatalogRole(CatalogRole catalogRole) {
        Collection<CatalogRole> catalogRoles = catalogRoleService.listByBean(catalogRole);
        Collection<CatalogRoleVo> catalogRoleVos = catalogRoleService.setVoProperties(catalogRoles);
        log.info(String.format("根据CatalogRole对象属性检索所有角色栏目关联: %s ",JSONUtil.toJsonStr(catalogRoleVos)));
        return RestResult.ok(catalogRoleVos);
    }

    /**
     * 根据CatalogRole对象属性分页检索角色栏目关联
     *
     * @param catalogRole
     * @return
     */
    @GetMapping("/pageByCatalogRole")
    @ApiOperation(value="根据CatalogRole对象属性分页检索角色栏目关联", notes="根据CatalogRole对象属性分页检索角色栏目关联信息")
    @ApiImplicitParam(name = "catalogRole", value = "角色栏目关联对象", required = false, dataType = "CatalogRole", paramType = "query")
    public RestResult<IPage<CatalogRoleVo>> pageByCatalogRole(CatalogRole catalogRole) {
        IPage<CatalogRoleVo> catalogRoles = catalogRoleService.pageByBean(catalogRole);
        catalogRoles.setRecords(catalogRoleService.setVoProperties(catalogRoles.getRecords()));
        log.info(String.format("根据CatalogRole对象属性分页检索角色栏目关联: %s ",JSONUtil.toJsonStr(catalogRoles)));
        return RestResult.ok(catalogRoles);
    }

    /**
     * 设置角色栏目
     *
     * @param roleId
     * @param catalogIds
     * @return
     */
    @PostMapping("/setRoleCatalogs")
    @ApiOperation(value="设置角色栏目", notes="设置角色栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "catalogIds", value = "栏目id", required = true, dataType = "String", paramType = "query")
    })
    public RestResult setRoleCatalogs(String roleId, @RequestParam(value = "catalogIds[]", required = false) List<String> catalogIds, String siteId) {
        catalogRoleService.setRoleCatalogs(roleId, catalogIds, siteId);
        return RestResult.ok();
    }
}
