package com.deyatech.monitor.contorller;

import com.deyatech.monitor.entity.GroupSite;
import com.deyatech.monitor.vo.GroupSiteVo;
import com.deyatech.monitor.service.GroupSiteService;
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
@RequestMapping("/monitor/groupSite")
@Api(tags = {"接口"})
public class GroupSiteController extends BaseController {
    @Autowired
    GroupSiteService groupSiteService;

    /**
     * 单个保存或者更新
     *
     * @param groupSite
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "groupSite", value = "对象", required = true, dataType = "GroupSite", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(GroupSite groupSite) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(groupSite)));
        boolean result = groupSiteService.saveOrUpdate(groupSite);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param groupSiteList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "groupSiteList", value = "对象集合", required = true, allowMultiple = true, dataType = "GroupSite", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<GroupSite> groupSiteList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(groupSiteList)));
        boolean result = groupSiteService.saveOrUpdateBatch(groupSiteList);
        return RestResult.ok(result);
    }

    /**
     * 根据GroupSite对象属性逻辑删除
     *
     * @param groupSite
     * @return
     */
    @PostMapping("/removeByGroupSite")
    @ApiOperation(value="根据GroupSite对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "groupSite", value = "对象", required = true, dataType = "GroupSite", paramType = "query")
    public RestResult<Boolean> removeByGroupSite(GroupSite groupSite) {
        log.info(String.format("根据GroupSite对象属性逻辑删除: %s ", groupSite));
        boolean result = groupSiteService.removeByBean(groupSite);
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
        boolean result = groupSiteService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据GroupSite对象属性获取
     *
     * @param groupSite
     * @return
     */
    @GetMapping("/getByGroupSite")
    @ApiOperation(value="根据GroupSite对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "groupSite", value = "对象", required = false, dataType = "GroupSite", paramType = "query")
    public RestResult<GroupSiteVo> getByGroupSite(GroupSite groupSite) {
        groupSite = groupSiteService.getByBean(groupSite);
        GroupSiteVo groupSiteVo = groupSiteService.setVoProperties(groupSite);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(groupSiteVo)));
        return RestResult.ok(groupSiteVo);
    }

    /**
     * 根据GroupSite对象属性检索所有
     *
     * @param groupSite
     * @return
     */
    @GetMapping("/listByGroupSite")
    @ApiOperation(value="根据GroupSite对象属性检索所有", notes="根据GroupSite对象属性检索所有信息")
    @ApiImplicitParam(name = "groupSite", value = "对象", required = false, dataType = "GroupSite", paramType = "query")
    public RestResult<Collection<GroupSiteVo>> listByGroupSite(GroupSite groupSite) {
        Collection<GroupSite> groupSites = groupSiteService.listByBean(groupSite);
        Collection<GroupSiteVo> groupSiteVos = groupSiteService.setVoProperties(groupSites);
        log.info(String.format("根据GroupSite对象属性检索所有: %s ",JSONUtil.toJsonStr(groupSiteVos)));
        return RestResult.ok(groupSiteVos);
    }

    /**
     * 根据GroupSite对象属性分页检索
     *
     * @param groupSite
     * @return
     */
    @GetMapping("/pageByGroupSite")
    @ApiOperation(value="根据GroupSite对象属性分页检索", notes="根据GroupSite对象属性分页检索信息")
    @ApiImplicitParam(name = "groupSite", value = "对象", required = false, dataType = "GroupSite", paramType = "query")
    public RestResult<IPage<GroupSiteVo>> pageByGroupSite(GroupSite groupSite) {
        IPage<GroupSiteVo> groupSites = groupSiteService.pageByBean(groupSite);
        groupSites.setRecords(groupSiteService.setVoProperties(groupSites.getRecords()));
        log.info(String.format("根据GroupSite对象属性分页检索: %s ",JSONUtil.toJsonStr(groupSites)));
        return RestResult.ok(groupSites);
    }

    /**
     * 关联人员
     *
     * @param groupId
     * @param siteIds
     * @return
     */
    @PostMapping("/setGroupSites")
    @ApiOperation(value = "设置站点人员", notes = "设置站点人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "任务组ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "siteIds", value = "站点ID", required = true, dataType = "String", paramType = "query")
    })
    public RestResult setGroupSites(String groupId, @RequestParam(value = "siteIds[]", required = false) List<String> siteIds) {
        groupSiteService.setGroupSites(groupId, siteIds);
        return RestResult.ok();
    }

}
