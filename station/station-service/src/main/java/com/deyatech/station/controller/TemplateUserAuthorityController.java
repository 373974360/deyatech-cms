package com.deyatech.station.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.station.entity.TemplateUserAuthority;
import com.deyatech.station.vo.TemplateUserAuthorityVo;
import com.deyatech.station.service.TemplateUserAuthorityService;
import com.deyatech.common.entity.RestResult;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

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
     * 单个保存或者更新内容模板用户权限信息
     *
     * @param templateUserAuthority
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新内容模板用户权限信息", notes="根据内容模板用户权限信息对象保存或者更新内容模板用户权限信息信息")
    @ApiImplicitParam(name = "templateUserAuthority", value = "内容模板用户权限信息对象", required = true, dataType = "TemplateUserAuthority", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(TemplateUserAuthority templateUserAuthority) {
        log.info(String.format("保存或者更新内容模板用户权限信息: %s ", JSONUtil.toJsonStr(templateUserAuthority)));
        boolean result = templateUserAuthorityService.saveOrUpdate(templateUserAuthority);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新内容模板用户权限信息
     *
     * @param templateUserAuthorityList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新内容模板用户权限信息", notes="根据内容模板用户权限信息对象集合批量保存或者更新内容模板用户权限信息信息")
    @ApiImplicitParam(name = "templateUserAuthorityList", value = "内容模板用户权限信息对象集合", required = true, allowMultiple = true, dataType = "TemplateUserAuthority", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<TemplateUserAuthority> templateUserAuthorityList) {
        log.info(String.format("批量保存或者更新内容模板用户权限信息: %s ", JSONUtil.toJsonStr(templateUserAuthorityList)));
        boolean result = templateUserAuthorityService.saveOrUpdateBatch(templateUserAuthorityList);
        return RestResult.ok(result);
    }

    /**
     * 根据TemplateUserAuthority对象属性逻辑删除内容模板用户权限信息
     *
     * @param templateUserAuthority
     * @return
     */
    @PostMapping("/removeByTemplateUserAuthority")
    @ApiOperation(value="根据TemplateUserAuthority对象属性逻辑删除内容模板用户权限信息", notes="根据内容模板用户权限信息对象逻辑删除内容模板用户权限信息信息")
    @ApiImplicitParam(name = "templateUserAuthority", value = "内容模板用户权限信息对象", required = true, dataType = "TemplateUserAuthority", paramType = "query")
    public RestResult<Boolean> removeByTemplateUserAuthority(TemplateUserAuthority templateUserAuthority) {
        log.info(String.format("根据TemplateUserAuthority对象属性逻辑删除内容模板用户权限信息: %s ", templateUserAuthority));
        boolean result = templateUserAuthorityService.removeByBean(templateUserAuthority);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除内容模板用户权限信息
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除内容模板用户权限信息", notes="根据内容模板用户权限信息对象ID批量逻辑删除内容模板用户权限信息信息")
    @ApiImplicitParam(name = "ids", value = "内容模板用户权限信息对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除内容模板用户权限信息: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = templateUserAuthorityService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据TemplateUserAuthority对象属性获取内容模板用户权限信息
     *
     * @param templateUserAuthority
     * @return
     */
    @GetMapping("/getByTemplateUserAuthority")
    @ApiOperation(value="根据TemplateUserAuthority对象属性获取内容模板用户权限信息", notes="根据内容模板用户权限信息对象属性获取内容模板用户权限信息信息")
    @ApiImplicitParam(name = "templateUserAuthority", value = "内容模板用户权限信息对象", required = false, dataType = "TemplateUserAuthority", paramType = "query")
    public RestResult<TemplateUserAuthorityVo> getByTemplateUserAuthority(TemplateUserAuthority templateUserAuthority) {
        templateUserAuthority = templateUserAuthorityService.getByBean(templateUserAuthority);
        TemplateUserAuthorityVo templateUserAuthorityVo = templateUserAuthorityService.setVoProperties(templateUserAuthority);
        log.info(String.format("根据id获取内容模板用户权限信息：%s", JSONUtil.toJsonStr(templateUserAuthorityVo)));
        return RestResult.ok(templateUserAuthorityVo);
    }

    /**
     * 根据TemplateUserAuthority对象属性检索所有内容模板用户权限信息
     *
     * @param templateUserAuthority
     * @return
     */
    @GetMapping("/listByTemplateUserAuthority")
    @ApiOperation(value="根据TemplateUserAuthority对象属性检索所有内容模板用户权限信息", notes="根据TemplateUserAuthority对象属性检索所有内容模板用户权限信息信息")
    @ApiImplicitParam(name = "templateUserAuthority", value = "内容模板用户权限信息对象", required = false, dataType = "TemplateUserAuthority", paramType = "query")
    public RestResult<Collection<TemplateUserAuthorityVo>> listByTemplateUserAuthority(TemplateUserAuthority templateUserAuthority) {
        Collection<TemplateUserAuthority> templateUserAuthoritys = templateUserAuthorityService.listByBean(templateUserAuthority);
        Collection<TemplateUserAuthorityVo> templateUserAuthorityVos = templateUserAuthorityService.setVoProperties(templateUserAuthoritys);
        log.info(String.format("根据TemplateUserAuthority对象属性检索所有内容模板用户权限信息: %s ",JSONUtil.toJsonStr(templateUserAuthorityVos)));
        return RestResult.ok(templateUserAuthorityVos);
    }

    /**
     * 根据TemplateUserAuthority对象属性分页检索内容模板用户权限信息
     *
     * @param templateUserAuthority
     * @return
     */
    @GetMapping("/pageByTemplateUserAuthority")
    @ApiOperation(value="根据TemplateUserAuthority对象属性分页检索内容模板用户权限信息", notes="根据TemplateUserAuthority对象属性分页检索内容模板用户权限信息信息")
    @ApiImplicitParam(name = "templateUserAuthority", value = "内容模板用户权限信息对象", required = false, dataType = "TemplateUserAuthority", paramType = "query")
    public RestResult<IPage<TemplateUserAuthorityVo>> pageByTemplateUserAuthority(TemplateUserAuthority templateUserAuthority) {
        IPage<TemplateUserAuthorityVo> templateUserAuthoritys = templateUserAuthorityService.pageByBean(templateUserAuthority);
        templateUserAuthoritys.setRecords(templateUserAuthorityService.setVoProperties(templateUserAuthoritys.getRecords()));
        log.info(String.format("根据TemplateUserAuthority对象属性分页检索内容模板用户权限信息: %s ",JSONUtil.toJsonStr(templateUserAuthoritys)));
        return RestResult.ok(templateUserAuthoritys);
    }

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    @GetMapping("/getUserAuthority")
    @ApiOperation(value="获取用户权限", notes="获取用户权限")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "userId", paramType = "query")
    public RestResult<String> getUserAuthority(String userId) {
        TemplateUserAuthority templateUserAuthority = new TemplateUserAuthority();
        templateUserAuthority.setUserId(userId);
        Collection<TemplateUserAuthority> templateUserAuthoritys = templateUserAuthorityService.listByBean(templateUserAuthority);
        String authority = "";
        if (CollectionUtil.isNotEmpty(templateUserAuthoritys)) {
            Iterator<TemplateUserAuthority> iterator = templateUserAuthoritys.iterator();
            authority = iterator.next().getAuthority();
        }
        log.info(String.format("获取用户权限: userId = %s ",userId));
        return RestResult.ok(authority);
    }

    /**
     * 设置用户权限
     *
     * @param userId
     * @return
     */
    @GetMapping("/setUserAuthority")
    @ApiOperation(value="设置用户权限", notes="设置用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "userId", paramType = "query"),
            @ApiImplicitParam(name = "authority", value = "权限", required = true, dataType = "authority", paramType = "query")
    })
    public RestResult<String> setUserAuthority(String userId, String authority) {
        // 删除原来的
        TemplateUserAuthority templateUserAuthority = new TemplateUserAuthority();
        templateUserAuthority.setUserId(userId);
        templateUserAuthorityService.removeByBean(templateUserAuthority);
        // 添加新的
        templateUserAuthority.setAuthority(authority);
        if (StrUtil.isNotEmpty(authority)) {
            templateUserAuthorityService.saveOrUpdate(templateUserAuthority);
        }
        log.info(String.format("设置用户权限: userId = %s, authority = %s",userId, authority));
        return RestResult.ok(true);
    }
}
