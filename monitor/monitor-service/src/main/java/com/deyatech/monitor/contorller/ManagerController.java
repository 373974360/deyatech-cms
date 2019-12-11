package com.deyatech.monitor.contorller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.monitor.entity.Manager;
import com.deyatech.monitor.entity.SiteManager;
import com.deyatech.monitor.service.SiteManagerService;
import com.deyatech.monitor.vo.ManagerVo;
import com.deyatech.monitor.service.ManagerService;
import com.deyatech.common.entity.RestResult;
import com.deyatech.monitor.vo.SiteVo;
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
@RequestMapping("/monitor/manager")
@Api(tags = {"接口"})
public class ManagerController extends BaseController {
    @Autowired
    ManagerService managerService;
    @Autowired
    SiteManagerService siteManagerService;

    /**
     * 单个保存或者更新
     *
     * @param manager
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "manager", value = "对象", required = true, dataType = "Manager", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Manager manager) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(manager)));
        boolean result = managerService.saveOrUpdate(manager);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param managerList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "managerList", value = "对象集合", required = true, allowMultiple = true, dataType = "Manager", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Manager> managerList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(managerList)));
        boolean result = managerService.saveOrUpdateBatch(managerList);
        return RestResult.ok(result);
    }

    /**
     * 根据Manager对象属性逻辑删除
     *
     * @param manager
     * @return
     */
    @PostMapping("/removeByManager")
    @ApiOperation(value="根据Manager对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "manager", value = "对象", required = true, dataType = "Manager", paramType = "query")
    public RestResult<Boolean> removeByManager(Manager manager) {
        log.info(String.format("根据Manager对象属性逻辑删除: %s ", manager));
        boolean result = managerService.removeByBean(manager);
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
        boolean result = managerService.removeByIds(ids);
        siteManagerService.deleteSiteManagerByManagerId(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Manager对象属性获取
     *
     * @param manager
     * @return
     */
    @GetMapping("/getByManager")
    @ApiOperation(value="根据Manager对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "manager", value = "对象", required = false, dataType = "Manager", paramType = "query")
    public RestResult<ManagerVo> getByManager(Manager manager) {
        manager = managerService.getByBean(manager);
        ManagerVo managerVo = managerService.setVoProperties(manager);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(managerVo)));
        return RestResult.ok(managerVo);
    }

    /**
     * 根据Manager对象属性检索所有
     *
     * @param manager
     * @return
     */
    @GetMapping("/listByManager")
    @ApiOperation(value="根据Manager对象属性检索所有", notes="根据Manager对象属性检索所有信息")
    @ApiImplicitParam(name = "manager", value = "对象", required = false, dataType = "Manager", paramType = "query")
    public RestResult<Collection<ManagerVo>> listByManager(Manager manager) {
        Collection<Manager> managers = managerService.listByBean(manager);
        Collection<ManagerVo> managerVos = managerService.setVoProperties(managers);
        log.info(String.format("根据Manager对象属性检索所有: %s ",JSONUtil.toJsonStr(managerVos)));
        return RestResult.ok(managerVos);
    }

    /**
     * 根据Manager对象属性分页检索
     *
     * @param manager
     * @return
     */
    @GetMapping("/pageByManager")
    @ApiOperation(value="根据Manager对象属性分页检索", notes="根据Manager对象属性分页检索信息")
    @ApiImplicitParam(name = "manager", value = "对象", required = false, dataType = "Manager", paramType = "query")
    public RestResult<IPage<ManagerVo>> pageByManager(Manager manager) {
        log.info(String.format("根据Manager对象属性分页检索: %s ",JSONUtil.toJsonStr(manager)));
        return RestResult.ok(managerService.pageByManager(manager));
    }

}
