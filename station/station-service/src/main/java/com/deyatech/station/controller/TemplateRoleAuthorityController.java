package com.deyatech.station.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.station.entity.TemplateRoleAuthority;
import com.deyatech.station.vo.TemplateRoleAuthorityVo;
import com.deyatech.station.service.TemplateRoleAuthorityService;
import com.deyatech.common.entity.RestResult;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
import java.util.*;

import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 角色内容权限 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-11-01
 */
@Slf4j
@RestController
@RequestMapping("/station/templateRoleAuthority")
@Api(tags = {"角色内容权限接口"})
public class TemplateRoleAuthorityController extends BaseController {
    @Autowired
    TemplateRoleAuthorityService templateRoleAuthorityService;



    /**
     * 单个保存或者更新角色内容权限
     *
     * @param templateRoleAuthority
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新角色内容权限", notes="根据角色内容权限对象保存或者更新角色内容权限信息")
    @ApiImplicitParam(name = "templateRoleAuthority", value = "角色内容权限对象", required = true, dataType = "TemplateRoleAuthority", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(TemplateRoleAuthority templateRoleAuthority) {
        log.info(String.format("保存或者更新角色内容权限: %s ", JSONUtil.toJsonStr(templateRoleAuthority)));
        boolean result = templateRoleAuthorityService.saveOrUpdate(templateRoleAuthority);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新角色内容权限
     *
     * @param templateRoleAuthorityList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新角色内容权限", notes="根据角色内容权限对象集合批量保存或者更新角色内容权限信息")
    @ApiImplicitParam(name = "templateRoleAuthorityList", value = "角色内容权限对象集合", required = true, allowMultiple = true, dataType = "TemplateRoleAuthority", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<TemplateRoleAuthority> templateRoleAuthorityList) {
        log.info(String.format("批量保存或者更新角色内容权限: %s ", JSONUtil.toJsonStr(templateRoleAuthorityList)));
        boolean result = templateRoleAuthorityService.saveOrUpdateBatch(templateRoleAuthorityList);
        return RestResult.ok(result);
    }

    /**
     * 根据TemplateRoleAuthority对象属性逻辑删除角色内容权限
     *
     * @param templateRoleAuthority
     * @return
     */
    @PostMapping("/removeByTemplateRoleAuthority")
    @ApiOperation(value="根据TemplateRoleAuthority对象属性逻辑删除角色内容权限", notes="根据角色内容权限对象逻辑删除角色内容权限信息")
    @ApiImplicitParam(name = "templateRoleAuthority", value = "角色内容权限对象", required = true, dataType = "TemplateRoleAuthority", paramType = "query")
    public RestResult<Boolean> removeByTemplateRoleAuthority(TemplateRoleAuthority templateRoleAuthority) {
        log.info(String.format("根据TemplateRoleAuthority对象属性逻辑删除角色内容权限: %s ", templateRoleAuthority));
        boolean result = templateRoleAuthorityService.removeByBean(templateRoleAuthority);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除角色内容权限
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除角色内容权限", notes="根据角色内容权限对象ID批量逻辑删除角色内容权限信息")
    @ApiImplicitParam(name = "ids", value = "角色内容权限对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除角色内容权限: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = templateRoleAuthorityService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据TemplateRoleAuthority对象属性获取角色内容权限
     *
     * @param templateRoleAuthority
     * @return
     */
    @GetMapping("/getByTemplateRoleAuthority")
    @ApiOperation(value="根据TemplateRoleAuthority对象属性获取角色内容权限", notes="根据角色内容权限对象属性获取角色内容权限信息")
    @ApiImplicitParam(name = "templateRoleAuthority", value = "角色内容权限对象", required = false, dataType = "TemplateRoleAuthority", paramType = "query")
    public RestResult<TemplateRoleAuthorityVo> getByTemplateRoleAuthority(TemplateRoleAuthority templateRoleAuthority) {
        templateRoleAuthority = templateRoleAuthorityService.getByBean(templateRoleAuthority);
        TemplateRoleAuthorityVo templateRoleAuthorityVo = templateRoleAuthorityService.setVoProperties(templateRoleAuthority);
        log.info(String.format("根据id获取角色内容权限：%s", JSONUtil.toJsonStr(templateRoleAuthorityVo)));
        return RestResult.ok(templateRoleAuthorityVo);
    }

    /**
     * 根据TemplateRoleAuthority对象属性检索所有角色内容权限
     *
     * @param templateRoleAuthority
     * @return
     */
    @GetMapping("/listByTemplateRoleAuthority")
    @ApiOperation(value="根据TemplateRoleAuthority对象属性检索所有角色内容权限", notes="根据TemplateRoleAuthority对象属性检索所有角色内容权限信息")
    @ApiImplicitParam(name = "templateRoleAuthority", value = "角色内容权限对象", required = false, dataType = "TemplateRoleAuthority", paramType = "query")
    public RestResult<Collection<TemplateRoleAuthorityVo>> listByTemplateRoleAuthority(TemplateRoleAuthority templateRoleAuthority) {
        Collection<TemplateRoleAuthority> templateRoleAuthoritys = templateRoleAuthorityService.listByBean(templateRoleAuthority);
        Collection<TemplateRoleAuthorityVo> templateRoleAuthorityVos = templateRoleAuthorityService.setVoProperties(templateRoleAuthoritys);
        log.info(String.format("根据TemplateRoleAuthority对象属性检索所有角色内容权限: %s ",JSONUtil.toJsonStr(templateRoleAuthorityVos)));
        return RestResult.ok(templateRoleAuthorityVos);
    }

    /**
     * 根据TemplateRoleAuthority对象属性分页检索角色内容权限
     *
     * @param templateRoleAuthority
     * @return
     */
    @GetMapping("/pageByTemplateRoleAuthority")
    @ApiOperation(value="根据TemplateRoleAuthority对象属性分页检索角色内容权限", notes="根据TemplateRoleAuthority对象属性分页检索角色内容权限信息")
    @ApiImplicitParam(name = "templateRoleAuthority", value = "角色内容权限对象", required = false, dataType = "TemplateRoleAuthority", paramType = "query")
    public RestResult<IPage<TemplateRoleAuthorityVo>> pageByTemplateRoleAuthority(TemplateRoleAuthority templateRoleAuthority) {
        IPage<TemplateRoleAuthorityVo> templateRoleAuthoritys = templateRoleAuthorityService.pageByBean(templateRoleAuthority);
        templateRoleAuthoritys.setRecords(templateRoleAuthorityService.setVoProperties(templateRoleAuthoritys.getRecords()));
        log.info(String.format("根据TemplateRoleAuthority对象属性分页检索角色内容权限: %s ",JSONUtil.toJsonStr(templateRoleAuthoritys)));
        return RestResult.ok(templateRoleAuthoritys);
    }

    /**
     * 获取角色权限
     *
     * @param roleId
     * @return
     */
    @GetMapping("/getRoleAuthority")
    @ApiOperation(value="获取角色权限", notes="获取角色权限")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "roleId", paramType = "query")
    public RestResult<String> getRoleAuthority(String roleId) {
        TemplateRoleAuthority templateRoleAuthority = new TemplateRoleAuthority();
        templateRoleAuthority.setRoleId(roleId);
        Collection<TemplateRoleAuthority> templateRoleAuthoritys = templateRoleAuthorityService.listByBean(templateRoleAuthority);
        String authority = "";
        if (CollectionUtil.isNotEmpty(templateRoleAuthoritys)) {
            Iterator<TemplateRoleAuthority> iterator = templateRoleAuthoritys.iterator();
            authority = iterator.next().getAuthority();
        }
        log.info(String.format("获取角色权限: roleId = %s ",roleId));
        return RestResult.ok(authority);
    }

    /**
     * 设置角色权限
     *
     * @param roleId
     * @return
     */
    @GetMapping("/setRoleAuthority")
    @ApiOperation(value="设置角色权限", notes="设置角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "roleId", paramType = "query"),
            @ApiImplicitParam(name = "authority", value = "权限", required = true, dataType = "authority", paramType = "query")
    })
    public RestResult<String> setRoleAuthority(String roleId, String authority) {
        // 删除原来的
        TemplateRoleAuthority templateRoleAuthority = new TemplateRoleAuthority();
        templateRoleAuthority.setRoleId(roleId);
        templateRoleAuthorityService.removeByBean(templateRoleAuthority);
        // 添加新的
        templateRoleAuthority.setAuthority(authority);
        if (StrUtil.isNotEmpty(authority)) {
            templateRoleAuthorityService.saveOrUpdate(templateRoleAuthority);
        }
        log.info(String.format("设置角色权限: roleId = %s, authority = %s",roleId, authority));
        return RestResult.ok(true);
    }

    /**
     * 获取角色页面上的数量统计结果
     *
     * @param roleIds
     * @return
     */
    @PostMapping("/getRoleViewCount")
    @ApiOperation(value="获取角色页面上的数量统计结果", notes="获取角色页面上的数量统计结果")
    @ApiImplicitParam(name = "roleIds", value = "角色ID", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult getRoleViewCount(@RequestParam("roleIds[]") List<String> roleIds) {
        Map<String, String> map = MapUtil.newHashMap();
        if (CollectionUtil.isNotEmpty(roleIds)) {
            Map<String, String> stationMap = templateRoleAuthorityService.getStationCount(roleIds);
            Map<String, String> catalogMap = templateRoleAuthorityService.getCatalogCount(roleIds);
            Map<String, String> contentMap = templateRoleAuthorityService.getContentCount(roleIds);
            roleIds.stream().forEach(id -> {
                StringBuilder count = new StringBuilder();
                count.append(stationMap.get(id));
                count.append("_");
                count.append(catalogMap.get(id));
                count.append("_");
                count.append(contentMap.get(id));
                map.put(id, count.toString());
            });
        }
        return RestResult.ok(map);
    }
}
