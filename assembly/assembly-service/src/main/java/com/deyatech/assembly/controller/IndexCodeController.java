package com.deyatech.assembly.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.assembly.entity.IndexCode;
import com.deyatech.assembly.service.IndexCodeService;
import com.deyatech.assembly.vo.IndexCodeVo;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 索引编码规则 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-10-18
 */
@Slf4j
@RestController
@RequestMapping("/assembly/indexCode")
@Api(tags = {"索引编码规则接口"})
public class IndexCodeController extends BaseController {
    @Autowired
    IndexCodeService indexCodeService;


    /**
     * 索引码重置
     *
     * @param siteId
     * @return
     */
    @RequestMapping("/reset")
    @ApiOperation(value="索引码重置", notes="索引码重置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteId", value = "站点编码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "开始日期", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "end", value = "结束日期", required = true, dataType = "String", paramType = "query")
    })
    public RestResult reset(String siteId, String start, String end) {
        if (StrUtil.isEmpty(siteId)) {
            return RestResult.error("没有站点编码");
        }
        if (StrUtil.isEmpty(start)) {
            return RestResult.error("没有开始日期");
        }
        if (StrUtil.isEmpty(end)) {
            return RestResult.error("没有结束日期");
        }
        return RestResult.ok(indexCodeService.reset(siteId,start,end));
    }

    /**
     * 获取站点索引编码规则
     *
     * @param siteId
     * @return
     */
    @RequestMapping("/getIndexCodeBySiteId")
    @ApiOperation(value="获取站点索引编码规则", notes="获取站点索引编码规则")
    @ApiImplicitParam(name = "siteId", value = "站点编码", required = true, dataType = "String", paramType = "query")
    public RestResult getIndexCodeBySiteId(String siteId) {
        if (StrUtil.isEmpty(siteId)) {
            return RestResult.error("站点编码不存在");
        }
        QueryWrapper<IndexCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site_id", siteId);
        return RestResult.ok(indexCodeService.setVoProperties(indexCodeService.getOne(queryWrapper)));
    }

    /**
     * 单个保存或者更新索引编码规则
     *
     * @param indexCode
     * @return
     */
    @RequestMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新索引编码规则", notes="根据索引编码规则对象保存或者更新索引编码规则信息")
    @ApiImplicitParam(name = "indexCode", value = "索引编码规则对象", required = true, dataType = "IndexCode", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(IndexCode indexCode) {
        log.info(String.format("保存或者更新索引编码规则: %s ", JSONUtil.toJsonStr(indexCode)));
        if (StrUtil.isNotEmpty(indexCode.getId())) {
            IndexCode tmp = indexCodeService.getById(indexCode.getId());
            if (Objects.nonNull(tmp)) {
                indexCode.setVersion(tmp.getVersion());
            }
        }
        boolean result = indexCodeService.saveOrUpdate(indexCode);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新索引编码规则
     *
     * @param indexCodeList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新索引编码规则", notes="根据索引编码规则对象集合批量保存或者更新索引编码规则信息")
    @ApiImplicitParam(name = "indexCodeList", value = "索引编码规则对象集合", required = true, allowMultiple = true, dataType = "IndexCode", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<IndexCode> indexCodeList) {
        log.info(String.format("批量保存或者更新索引编码规则: %s ", JSONUtil.toJsonStr(indexCodeList)));
        boolean result = indexCodeService.saveOrUpdateBatch(indexCodeList);
        return RestResult.ok(result);
    }

    /**
     * 根据IndexCode对象属性逻辑删除索引编码规则
     *
     * @param indexCode
     * @return
     */
    @PostMapping("/removeByIndexCode")
    @ApiOperation(value="根据IndexCode对象属性逻辑删除索引编码规则", notes="根据索引编码规则对象逻辑删除索引编码规则信息")
    @ApiImplicitParam(name = "indexCode", value = "索引编码规则对象", required = true, dataType = "IndexCode", paramType = "query")
    public RestResult<Boolean> removeByIndexCode(IndexCode indexCode) {
        log.info(String.format("根据IndexCode对象属性逻辑删除索引编码规则: %s ", indexCode));
        boolean result = indexCodeService.removeByBean(indexCode);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除索引编码规则
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除索引编码规则", notes="根据索引编码规则对象ID批量逻辑删除索引编码规则信息")
    @ApiImplicitParam(name = "ids", value = "索引编码规则对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除索引编码规则: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = indexCodeService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据IndexCode对象属性获取索引编码规则
     *
     * @param indexCode
     * @return
     */
    @GetMapping("/getByIndexCode")
    @ApiOperation(value="根据IndexCode对象属性获取索引编码规则", notes="根据索引编码规则对象属性获取索引编码规则信息")
    @ApiImplicitParam(name = "indexCode", value = "索引编码规则对象", required = false, dataType = "IndexCode", paramType = "query")
    public RestResult<IndexCodeVo> getByIndexCode(IndexCode indexCode) {
        indexCode = indexCodeService.getByBean(indexCode);
        IndexCodeVo indexCodeVo = indexCodeService.setVoProperties(indexCode);
        log.info(String.format("根据id获取索引编码规则：%s", JSONUtil.toJsonStr(indexCodeVo)));
        return RestResult.ok(indexCodeVo);
    }

    /**
     * 根据IndexCode对象属性检索所有索引编码规则
     *
     * @param indexCode
     * @return
     */
    @GetMapping("/listByIndexCode")
    @ApiOperation(value="根据IndexCode对象属性检索所有索引编码规则", notes="根据IndexCode对象属性检索所有索引编码规则信息")
    @ApiImplicitParam(name = "indexCode", value = "索引编码规则对象", required = false, dataType = "IndexCode", paramType = "query")
    public RestResult<Collection<IndexCodeVo>> listByIndexCode(IndexCode indexCode) {
        Collection<IndexCode> indexCodes = indexCodeService.listByBean(indexCode);
        Collection<IndexCodeVo> indexCodeVos = indexCodeService.setVoProperties(indexCodes);
        log.info(String.format("根据IndexCode对象属性检索所有索引编码规则: %s ",JSONUtil.toJsonStr(indexCodeVos)));
        return RestResult.ok(indexCodeVos);
    }

    /**
     * 根据IndexCode对象属性分页检索索引编码规则
     *
     * @param indexCode
     * @return
     */
    @GetMapping("/pageByIndexCode")
    @ApiOperation(value="根据IndexCode对象属性分页检索索引编码规则", notes="根据IndexCode对象属性分页检索索引编码规则信息")
    @ApiImplicitParam(name = "indexCode", value = "索引编码规则对象", required = false, dataType = "IndexCode", paramType = "query")
    public RestResult<IPage<IndexCodeVo>> pageByIndexCode(IndexCode indexCode) {
        IPage<IndexCodeVo> indexCodes = indexCodeService.pageByBean(indexCode);
        indexCodes.setRecords(indexCodeService.setVoProperties(indexCodes.getRecords()));
        log.info(String.format("根据IndexCode对象属性分页检索索引编码规则: %s ",JSONUtil.toJsonStr(indexCodes)));
        return RestResult.ok(indexCodes);
    }

}
