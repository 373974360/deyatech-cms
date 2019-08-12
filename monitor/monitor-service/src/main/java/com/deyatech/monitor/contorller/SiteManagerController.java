package com.deyatech.monitor.contorller;

import com.deyatech.monitor.entity.SiteManager;
import com.deyatech.monitor.vo.SiteManagerVo;
import com.deyatech.monitor.service.SiteManagerService;
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
 *  前端控制器
 * </p>
 * @author: lee.
 * @since 2019-07-29
 */
@Slf4j
@RestController
@RequestMapping("/monitor/siteManager")
@Api(tags = {"接口"})
public class SiteManagerController extends BaseController {
    @Autowired
    SiteManagerService siteManagerService;

    /**
     * 单个保存或者更新
     *
     * @param siteManager
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "siteManager", value = "对象", required = true, dataType = "SiteManager", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(SiteManager siteManager) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(siteManager)));
        boolean result = siteManagerService.saveOrUpdate(siteManager);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param siteManagerList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "siteManagerList", value = "对象集合", required = true, allowMultiple = true, dataType = "SiteManager", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<SiteManager> siteManagerList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(siteManagerList)));
        boolean result = siteManagerService.saveOrUpdateBatch(siteManagerList);
        return RestResult.ok(result);
    }

    /**
     * 根据SiteManager对象属性逻辑删除
     *
     * @param siteManager
     * @return
     */
    @PostMapping("/removeBySiteManager")
    @ApiOperation(value="根据SiteManager对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "siteManager", value = "对象", required = true, dataType = "SiteManager", paramType = "query")
    public RestResult<Boolean> removeBySiteManager(SiteManager siteManager) {
        log.info(String.format("根据SiteManager对象属性逻辑删除: %s ", siteManager));
        boolean result = siteManagerService.removeByBean(siteManager);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除", notes="根据对象ID批量逻辑删除信息")
    @ApiImplicitParam(name = "ids", value = "对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = siteManagerService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据SiteManager对象属性获取
     *
     * @param siteManager
     * @return
     */
    @GetMapping("/getBySiteManager")
    @ApiOperation(value="根据SiteManager对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "siteManager", value = "对象", required = false, dataType = "SiteManager", paramType = "query")
    public RestResult<SiteManagerVo> getBySiteManager(SiteManager siteManager) {
        siteManager = siteManagerService.getByBean(siteManager);
        SiteManagerVo siteManagerVo = siteManagerService.setVoProperties(siteManager);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(siteManagerVo)));
        return RestResult.ok(siteManagerVo);
    }

    /**
     * 根据SiteManager对象属性检索所有
     *
     * @param siteManager
     * @return
     */
    @GetMapping("/listBySiteManager")
    @ApiOperation(value="根据SiteManager对象属性检索所有", notes="根据SiteManager对象属性检索所有信息")
    @ApiImplicitParam(name = "siteManager", value = "对象", required = false, dataType = "SiteManager", paramType = "query")
    public RestResult<Collection<SiteManagerVo>> listBySiteManager(SiteManager siteManager) {
        Collection<SiteManager> siteManagers = siteManagerService.listByBean(siteManager);
        Collection<SiteManagerVo> siteManagerVos = siteManagerService.setVoProperties(siteManagers);
        log.info(String.format("根据SiteManager对象属性检索所有: %s ",JSONUtil.toJsonStr(siteManagerVos)));
        return RestResult.ok(siteManagerVos);
    }

    /**
     * 根据SiteManager对象属性分页检索
     *
     * @param siteManager
     * @return
     */
    @GetMapping("/pageBySiteManager")
    @ApiOperation(value="根据SiteManager对象属性分页检索", notes="根据SiteManager对象属性分页检索信息")
    @ApiImplicitParam(name = "siteManager", value = "对象", required = false, dataType = "SiteManager", paramType = "query")
    public RestResult<IPage<SiteManagerVo>> pageBySiteManager(SiteManager siteManager) {
        IPage<SiteManagerVo> siteManagers = siteManagerService.pageByBean(siteManager);
        siteManagers.setRecords(siteManagerService.setVoProperties(siteManagers.getRecords()));
        log.info(String.format("根据SiteManager对象属性分页检索: %s ",JSONUtil.toJsonStr(siteManagers)));
        return RestResult.ok(siteManagers);
    }

    /**
     * 关联人员
     *
     * @param siteId
     * @param userIds
     * @return
     */
    @PostMapping("/setSiteUsers")
    @ApiOperation(value = "设置站点人员", notes = "设置站点人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteId", value = "站点ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userIds", value = "用户id", required = true, dataType = "String", paramType = "query")
    })
    public RestResult setSiteUsers(String siteId, @RequestParam(value = "userIds[]", required = false) List<String> userIds) {
        siteManagerService.setSiteUsers(siteId, userIds);
        return RestResult.ok();
    }

    /**
     * 关联站点
     *
     * @param siteId
     * @param userIds
     * @return
     */
    @PostMapping("/setUserSites")
    @ApiOperation(value = "设置站点人员", notes = "设置站点人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteId", value = "站点ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userIds", value = "用户id", required = true, dataType = "String", paramType = "query")
    })
    public RestResult setUserSites(String userId, @RequestParam(value = "siteIds[]", required = false) List<String> siteIds) {
        siteManagerService.setUserSites(userId, siteIds);
        return RestResult.ok();
    }

}
