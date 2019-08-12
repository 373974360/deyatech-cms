package com.deyatech.monitor.contorller;

import com.deyatech.monitor.entity.Site;
import com.deyatech.monitor.service.GroupSiteService;
import com.deyatech.monitor.service.SiteManagerService;
import com.deyatech.monitor.vo.SiteVo;
import com.deyatech.monitor.service.SiteService;
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
 * 监控配置表 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-07-29
 */
@Slf4j
@RestController
@RequestMapping("/monitor/site")
@Api(tags = {"监控配置表接口"})
public class SiteController extends BaseController {
    @Autowired
    SiteService siteService;

    @Autowired
    SiteManagerService siteManagerService;
    @Autowired
    GroupSiteService groupSiteService;

    /**
     * 单个保存或者更新监控配置表
     *
     * @param site
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新监控配置表", notes="根据监控配置表对象保存或者更新监控配置表信息")
    @ApiImplicitParam(name = "site", value = "监控配置表对象", required = true, dataType = "Site", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Site site) {
        log.info(String.format("保存或者更新监控配置表: %s ", JSONUtil.toJsonStr(site)));
        boolean result = siteService.saveOrUpdate(site);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新监控配置表
     *
     * @param siteList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新监控配置表", notes="根据监控配置表对象集合批量保存或者更新监控配置表信息")
    @ApiImplicitParam(name = "siteList", value = "监控配置表对象集合", required = true, allowMultiple = true, dataType = "Site", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Site> siteList) {
        log.info(String.format("批量保存或者更新监控配置表: %s ", JSONUtil.toJsonStr(siteList)));
        boolean result = siteService.saveOrUpdateBatch(siteList);
        return RestResult.ok(result);
    }

    /**
     * 根据Site对象属性逻辑删除监控配置表
     *
     * @param site
     * @return
     */
    @PostMapping("/removeBySite")
    @ApiOperation(value="根据Site对象属性逻辑删除监控配置表", notes="根据监控配置表对象逻辑删除监控配置表信息")
    @ApiImplicitParam(name = "site", value = "监控配置表对象", required = true, dataType = "Site", paramType = "query")
    public RestResult<Boolean> removeBySite(Site site) {
        log.info(String.format("根据Site对象属性逻辑删除监控配置表: %s ", site));
        boolean result = siteService.removeByBean(site);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除监控配置表
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除监控配置表", notes="根据监控配置表对象ID批量逻辑删除监控配置表信息")
    @ApiImplicitParam(name = "ids", value = "监控配置表对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除监控配置表: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = siteService.removeByIds(ids);
        siteManagerService.deleteSiteManagerBySiteId(ids);
        groupSiteService.deleteGroupSiteBySiteId(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Site对象属性获取监控配置表
     *
     * @param site
     * @return
     */
    @GetMapping("/getBySite")
    @ApiOperation(value="根据Site对象属性获取监控配置表", notes="根据监控配置表对象属性获取监控配置表信息")
    @ApiImplicitParam(name = "site", value = "监控配置表对象", required = false, dataType = "Site", paramType = "query")
    public RestResult<SiteVo> getBySite(Site site) {
        site = siteService.getByBean(site);
        SiteVo siteVo = siteService.setVoProperties(site);
        log.info(String.format("根据id获取监控配置表：%s", JSONUtil.toJsonStr(siteVo)));
        return RestResult.ok(siteVo);
    }

    /**
     * 根据Site对象属性检索所有监控配置表
     *
     * @param site
     * @return
     */
    @GetMapping("/listBySite")
    @ApiOperation(value="根据Site对象属性检索所有监控配置表", notes="根据Site对象属性检索所有监控配置表信息")
    @ApiImplicitParam(name = "site", value = "监控配置表对象", required = false, dataType = "Site", paramType = "query")
    public RestResult<Collection<SiteVo>> listBySite(Site site) {
        Collection<Site> sites = siteService.listByBean(site);
        Collection<SiteVo> siteVos = siteService.setVoProperties(sites);
        log.info(String.format("根据Site对象属性检索所有监控配置表: %s ",JSONUtil.toJsonStr(siteVos)));
        return RestResult.ok(siteVos);
    }

    /**
     * 根据Site对象属性分页检索监控配置表
     *
     * @param site
     * @return
     */
    @GetMapping("/pageBySite")
    @ApiOperation(value="根据Site对象属性分页检索监控配置表", notes="根据Site对象属性分页检索监控配置表信息")
    @ApiImplicitParam(name = "site", value = "监控配置表对象", required = false, dataType = "Site", paramType = "query")
    public RestResult<IPage<SiteVo>> pageBySite(Site site) {
        IPage<SiteVo> sites = siteService.pageByBean(site);
        sites.setRecords(siteService.setVoProperties(sites.getRecords()));
        log.info(String.format("根据Site对象属性分页检索监控配置表: %s ",JSONUtil.toJsonStr(sites)));
        return RestResult.ok(sites);
    }

}
