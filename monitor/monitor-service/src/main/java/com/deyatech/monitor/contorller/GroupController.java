package com.deyatech.monitor.contorller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.deyatech.monitor.entity.Group;
import com.deyatech.monitor.service.GroupSiteService;
import com.deyatech.monitor.vo.GroupVo;
import com.deyatech.monitor.service.GroupService;
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
 *  前端控制器
 * </p>
 * @author: lee.
 * @since 2019-07-29
 */
@Slf4j
@RestController
@RequestMapping("/monitor/group")
@Api(tags = {"接口"})
public class GroupController extends BaseController {
    @Autowired
    GroupService groupService;
    @Autowired
    GroupSiteService groupSiteService;

    /**
     * 单个保存或者更新
     *
     * @param group
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "group", value = "对象", required = true, dataType = "Group", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Group group) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(group)));
        boolean result = groupService.saveOrUpdate(group);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param groupList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "groupList", value = "对象集合", required = true, allowMultiple = true, dataType = "Group", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Group> groupList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(groupList)));
        boolean result = groupService.saveOrUpdateBatch(groupList);
        return RestResult.ok(result);
    }

    /**
     * 根据Group对象属性逻辑删除
     *
     * @param group
     * @return
     */
    @PostMapping("/removeByGroup")
    @ApiOperation(value="根据Group对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "group", value = "对象", required = true, dataType = "Group", paramType = "query")
    public RestResult<Boolean> removeByGroup(Group group) {
        log.info(String.format("根据Group对象属性逻辑删除: %s ", group));
        boolean result = groupService.removeByBean(group);
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
        boolean result = groupService.removeByIds(ids);
        groupSiteService.deleteGroupSiteByGroupId(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Group对象属性获取
     *
     * @param group
     * @return
     */
    @GetMapping("/getByGroup")
    @ApiOperation(value="根据Group对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "group", value = "对象", required = false, dataType = "Group", paramType = "query")
    public RestResult<GroupVo> getByGroup(Group group) {
        group = groupService.getByBean(group);
        GroupVo groupVo = groupService.setVoProperties(group);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(groupVo)));
        return RestResult.ok(groupVo);
    }

    /**
     * 根据Group对象属性检索所有
     *
     * @param group
     * @return
     */
    @GetMapping("/listByGroup")
    @ApiOperation(value="根据Group对象属性检索所有", notes="根据Group对象属性检索所有信息")
    @ApiImplicitParam(name = "group", value = "对象", required = false, dataType = "Group", paramType = "query")
    public RestResult<Collection<GroupVo>> listByGroup(Group group) {
        Collection<Group> groups = groupService.listByBean(group);
        Collection<GroupVo> groupVos = groupService.setVoProperties(groups);
        log.info(String.format("根据Group对象属性检索所有: %s ",JSONUtil.toJsonStr(groupVos)));
        return RestResult.ok(groupVos);
    }

    /**
     * 根据Group对象属性分页检索
     *
     * @param group
     * @return
     */
    @GetMapping("/pageByGroup")
    @ApiOperation(value="根据Group对象属性分页检索", notes="根据Group对象属性分页检索信息")
    @ApiImplicitParam(name = "group", value = "对象", required = false, dataType = "Group", paramType = "query")
    public RestResult<IPage<GroupVo>> pageByGroup(Group group) {
        log.info(String.format("根据Group对象属性分页检索: %s ",JSONUtil.toJsonStr(group)));
        return RestResult.ok(groupService.pageByGroup(group));
    }

}
