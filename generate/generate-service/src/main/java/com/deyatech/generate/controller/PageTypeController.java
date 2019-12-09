package com.deyatech.generate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.generate.entity.PageType;
import com.deyatech.generate.vo.PageTypeVo;
import com.deyatech.generate.service.PageTypeService;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.entity.CascaderResult;
import com.deyatech.common.utils.CascaderUtil;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
 * @since 2019-12-09
 */
@Slf4j
@RestController
@RequestMapping("/generate/pageType")
@Api(tags = {"接口"})
public class PageTypeController extends BaseController {
    @Autowired
    PageTypeService pageTypeService;

    /**
     * 单个保存或者更新
     *
     * @param pageType
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "PageType", value = "对象", required = true, dataType = "PageType", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(PageType pageType) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(pageType)));
        boolean result = pageTypeService.saveOrUpdate(pageType);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param pageTypeList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "PageTypeList", value = "对象集合", required = true, allowMultiple = true, dataType = "PageType", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<PageType> pageTypeList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(pageTypeList)));
        boolean result = pageTypeService.saveOrUpdateBatch(pageTypeList);
        return RestResult.ok(result);
    }

    /**
     * 根据PageType对象属性逻辑删除
     *
     * @param pageType
     * @return
     */
    @PostMapping("/removeByPageType")
    @ApiOperation(value="根据PageType对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "PageType", value = "对象", required = true, dataType = "PageType", paramType = "query")
    public RestResult<Boolean> removeByPageType(PageType pageType) {
        log.info(String.format("根据PageType对象属性逻辑删除: %s ", pageType));
        boolean result = pageTypeService.removeByBean(pageType);
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
        boolean result = pageTypeService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据PageType对象属性获取
     *
     * @param pageType
     * @return
     */
    @GetMapping("/getByPageType")
    @ApiOperation(value="根据PageType对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "PageType", value = "对象", required = false, dataType = "PageType", paramType = "query")
    public RestResult<PageTypeVo> getByPageType(PageType pageType) {
        pageType = pageTypeService.getByBean(pageType);
        PageTypeVo pageTypeVo = pageTypeService.setVoProperties(pageType);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(pageTypeVo)));
        return RestResult.ok(pageTypeVo);
    }

    /**
     * 根据PageType对象属性检索所有
     *
     * @param pageType
     * @return
     */
    @GetMapping("/listByPageType")
    @ApiOperation(value="根据PageType对象属性检索所有", notes="根据PageType对象属性检索所有信息")
    @ApiImplicitParam(name = "PageType", value = "对象", required = false, dataType = "PageType", paramType = "query")
    public RestResult<Collection<PageTypeVo>> listByPageType(PageType pageType) {
        Collection<PageType> pageTypes = pageTypeService.listByBean(pageType);
        Collection<PageTypeVo> pageTypeVos = pageTypeService.setVoProperties(pageTypes);
        log.info(String.format("根据PageType对象属性检索所有: %s ",JSONUtil.toJsonStr(pageTypeVos)));
        return RestResult.ok(pageTypeVos);
    }

    /**
     * 根据PageType对象属性分页检索
     *
     * @param pageType
     * @return
     */
    @GetMapping("/pageByPageType")
    @ApiOperation(value="根据PageType对象属性分页检索", notes="根据PageType对象属性分页检索信息")
    @ApiImplicitParam(name = "PageType", value = "对象", required = false, dataType = "PageType", paramType = "query")
    public RestResult<IPage<PageTypeVo>> pageByPageType(PageType pageType) {
        IPage<PageTypeVo> pageTypes = pageTypeService.pageByBean(pageType);
        pageTypes.setRecords(pageTypeService.setVoProperties(pageTypes.getRecords()));
        log.info(String.format("根据PageType对象属性分页检索: %s ",JSONUtil.toJsonStr(pageTypes)));
        return RestResult.ok(pageTypes);
    }

    /**
     * 获取的tree对象
     *
     * @param pageType
     * @return
     */
    @GetMapping("/getTree")
    @ApiOperation(value="获取的tree对象", notes="获取的tree对象")
    public RestResult<Collection<PageTypeVo>> getPageTypeTree(PageType pageType) {
        Collection<PageTypeVo> pageTypeTree = pageTypeService.getPageTypeTree(pageType);
        log.info(String.format("获取的tree对象: %s ",JSONUtil.toJsonStr(pageTypeTree)));
        return RestResult.ok(pageTypeTree);
    }

    /**
     * 获取的级联对象
     *
     * @param pageType
     * @return
     */
    @GetMapping("/getCascader")
    @ApiOperation(value="获取的级联对象", notes="获取的级联对象")
    @ApiImplicitParam(name = "PageType", value = "PageType", required = false, dataType = "PageType", paramType = "query")
    public RestResult<List<CascaderResult>> getCascader(PageType pageType) {
        Collection<PageTypeVo> pageTypeVos = pageTypeService.getPageTypeTree(pageType);
        List<CascaderResult> cascaderResults = CascaderUtil.getResult("Id", "Name","TreePosition", pageType.getId(), pageTypeVos);
        log.info(String.format("获取的级联对象: %s ",JSONUtil.toJsonStr(cascaderResults)));
        return RestResult.ok(cascaderResults);
    }


    /**
     * 检查分类下是否存在页面
     *
     * @param id
     * @return
     */
    @GetMapping("/checkPageExist")
    @ApiOperation(value="检查分类下是否存在页面", notes="检查分类下是否存在页面")
    @ApiImplicitParam(name = "id", value = "元数据分类id", required = true, dataType = "String", paramType = "query")
    public RestResult checkPageExist(String id) {
        return RestResult.ok(pageTypeService.checkPageExist(id));
    }

    /**
     * 下一个排序号
     *
     * @return
     */
    @RequestMapping("/getNextSortNo")
    @ApiOperation(value = "下一个排序号", notes = "下一个排序号")
    public RestResult<Integer> getNextSortNo(String id) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        queryWrapper.select("ifnull(max(sort_no), 0) + 1 as sortNo");
        return RestResult.ok(pageTypeService.getMap(queryWrapper).get("sortNo"));
    }
}
