package com.deyatech.resource.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.resource.entity.Domain;
import com.deyatech.resource.service.DomainService;
import com.deyatech.resource.vo.DomainVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        // 检查域名是否存在
        long count = this.domainService.countNameByStationGroupId(domain.getId(), domain.getStationGroupId(), domain.getName());
        if (count > 0) {
            return new RestResult(200, "当前站群下已存在该域名", true);
        } else {
            boolean result = domainService.saveOrUpdateAndNginx(domain);
            return RestResult.ok(result);
        }
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
        domain = domainService.getById(domain.getId());
        // 主域名
        if (YesNoEnum.YES.getCode().toString().equals(domain.getSign())) {
            return RestResult.error("主域名不允许删除");
        }
        List<String> ids = new ArrayList<>();
        ids.add(domain.getId());
        Map<String, Domain> maps = new HashMap<>();
        maps.put(domain.getId(), domain);
        boolean result = domainService.removeDomainsAndConfig(ids, maps);
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
        Map<String, Domain> maps = new HashMap<>();
        for(String id : ids) {
            Domain domain = domainService.getById(id);
            maps.put(id, domain);
            // 主域名
            if (YesNoEnum.YES.getCode().toString().equals(domain.getSign())) {
                return RestResult.error("主域名不允许删除");
            }
        }
        boolean result = domainService.removeDomainsAndConfig(ids, maps);
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
            @ApiImplicitParam(name = "stationGroupId", value = "站群编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "站群名称", required = true, dataType = "String", paramType = "query")
    })
    public RestResult<Boolean> isNameExist(@RequestParam(required = false) String id, String stationGroupId, String name) {
        log.info(String.format("域名重名检查: id = %s, stationGroupId = %s, name = %s", id, stationGroupId, name));
        long count = this.domainService.countNameByStationGroupId(id, stationGroupId, name);
        if (count > 0) {
            return new RestResult(200, "当前站群下已存在该域名", true);
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
            @ApiImplicitParam(name = "id", value = "站群编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "flag", value = "运行或停止标记", required = true, dataType = "String", paramType = "query"),
    })
    public RestResult<Boolean> runOrStopDomainById(String id, String flag) {
        log.info(String.format("运行或停止站群 id = %s, flag = %s", id , flag));
        Domain domain = domainService.getById(id);
        // 主域名不能停用
        if ("stop".equals(flag) && YesNoEnum.YES.getCode().toString().equals(domain.getSign())) {
            return RestResult.error("主域名不允许停用");
        }
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
            @ApiImplicitParam(name = "id", value = "站群编号", required = true, dataType = "String", paramType = "query"),
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

    /**
     * 获取nginx端口
     *
     * @return
     */
    @RequestMapping("/getNginxPort")
    @ApiOperation(value="获取nginx端口", notes="获取nginx端口")
    public RestResult getNginxPort() {
        return RestResult.ok(domainService.getNginxPort());
    }
}
