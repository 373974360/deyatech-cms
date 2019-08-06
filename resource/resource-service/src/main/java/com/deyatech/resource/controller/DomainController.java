package com.deyatech.resource.controller;

import com.deyatech.resource.entity.Domain;
import com.deyatech.resource.vo.DomainVo;
import com.deyatech.resource.service.DomainService;
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
 * @since 2019-08-01
 */
@Slf4j
@RestController
@RequestMapping("/resource/domain")
@Api(tags = {"接口"})
public class DomainController extends BaseController {
    @Autowired
    DomainService domainService;

    /**
     * 单个保存或者更新
     *
     * @param domain
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "domain", value = "对象", required = true, dataType = "Domain", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Domain domain) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(domain)));
        boolean result = domainService.saveOrUpdate(domain);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param domainList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "domainList", value = "对象集合", required = true, allowMultiple = true, dataType = "Domain", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Domain> domainList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(domainList)));
        boolean result = domainService.saveOrUpdateBatch(domainList);
        return RestResult.ok(result);
    }

    /**
     * 根据Domain对象属性逻辑删除
     *
     * @param domain
     * @return
     */
    @PostMapping("/removeByDomain")
    @ApiOperation(value="根据Domain对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "domain", value = "对象", required = true, dataType = "Domain", paramType = "query")
    public RestResult<Boolean> removeByDomain(Domain domain) {
        log.info(String.format("根据Domain对象属性逻辑删除: %s ", domain));
        boolean result = domainService.removeByBean(domain);
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
        boolean result = domainService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Domain对象属性获取
     *
     * @param domain
     * @return
     */
    @GetMapping("/getByDomain")
    @ApiOperation(value="根据Domain对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "domain", value = "对象", required = false, dataType = "Domain", paramType = "query")
    public RestResult<DomainVo> getByDomain(Domain domain) {
        domain = domainService.getByBean(domain);
        DomainVo domainVo = domainService.setVoProperties(domain);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(domainVo)));
        return RestResult.ok(domainVo);
    }

    /**
     * 根据Domain对象属性检索所有
     *
     * @param domain
     * @return
     */
    @GetMapping("/listByDomain")
    @ApiOperation(value="根据Domain对象属性检索所有", notes="根据Domain对象属性检索所有信息")
    @ApiImplicitParam(name = "domain", value = "对象", required = false, dataType = "Domain", paramType = "query")
    public RestResult<Collection<DomainVo>> listByDomain(Domain domain) {
        Collection<Domain> domains = domainService.listByBean(domain);
        Collection<DomainVo> domainVos = domainService.setVoProperties(domains);
        log.info(String.format("根据Domain对象属性检索所有: %s ",JSONUtil.toJsonStr(domainVos)));
        return RestResult.ok(domainVos);
    }

    /**
     * 根据Domain对象属性分页检索
     *
     * @param domainVo
     * @return
     */
    @GetMapping("/pageByDomain")
    @ApiOperation(value="根据Domain对象属性分页检索", notes="根据Domain对象属性分页检索信息")
    @ApiImplicitParam(name = "domain", value = "对象", required = false, dataType = "Domain", paramType = "query")
    public RestResult<IPage<DomainVo>> pageByDomain(DomainVo domainVo) {
        IPage<DomainVo> domains = domainService.pageSelectByDomainVo(domainVo);
        log.info(String.format("根据Domain对象属性分页检索: %s ",JSONUtil.toJsonStr(domains)));
        return RestResult.ok(domains);
    }

    /**
     * 域名重名检查
     *
     * @param id
     * @param stationGroupId
     * @param name
     * @return
     */
    @RequestMapping("/isNameExist")
    @ApiOperation(value="域名重名检查", notes="域名重名检查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "域名编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "stationGroupId", value = "网站编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "网站名称", required = true, dataType = "String", paramType = "query")
    })
    public RestResult<Boolean> isNameExist(@RequestParam(required = false) String id, String stationGroupId, String name) {
        log.info(String.format("域名重名检查: id = %s, stationGroupId = %s, name = %s", id, stationGroupId, name));
        long count = this.domainService.countNameByStationGroupId(id, stationGroupId, name);
        if (count > 0) {
            return new RestResult(200, "当前网站下已存在该域名", true);
        } else {
            return RestResult.ok(false);
        }
    }

    /**
     * 启用或停用域名
     *
     * @param id
     * @param flag
     * @return
     */
    @RequestMapping("/runOrStopDomainById")
    @ApiOperation(value="启用或停用域名", notes="启用或停用域名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "网站编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "flag", value = "运行或停止标记", required = true, dataType = "String", paramType = "query"),
    })
    public RestResult<Boolean> runOrStopDomainById(String id, String flag) {
        log.info(String.format("运行或停止网站 id = %s, flag = %s", id , flag));
        long count = this.domainService.runOrStopStationById(id, flag);
        if (count > 0) {
            return RestResult.ok(true);
        } else {
            return RestResult.ok(false);
        }
    }

    /**
     * 更新主域名
     *
     * @param id
     * @param stationGroupId
     * @return
     */
    @RequestMapping("/updateSignByIdAndStationGroupId")
    @ApiOperation(value="更新主域名", notes="更新主域名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "网站编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "flag", value = "运行或停止标记", required = true, dataType = "String", paramType = "query"),
    })
    public RestResult<Boolean> updateSignByIdAndStationGroupId(String id, String stationGroupId) {
        log.info(String.format("更新主域名 id = %s, stationGroupId = %s", id , stationGroupId));
        long count = this.domainService.updateSignByIdAndStationGroupId(id, stationGroupId);
        if (count > 0) {
            return RestResult.ok(true);
        } else {
            return RestResult.ok(false);
        }
    }
}
