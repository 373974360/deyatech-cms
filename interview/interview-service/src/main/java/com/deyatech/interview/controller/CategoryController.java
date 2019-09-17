package com.deyatech.interview.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.interview.entity.Category;
import com.deyatech.interview.service.CategoryService;
import com.deyatech.interview.vo.CategoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 访谈分类 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-08-26
 */
@Slf4j
@RestController
@RequestMapping("/interview/category")
@Api(tags = {"访谈分类接口"})
public class CategoryController extends BaseController {
    @Autowired
    CategoryService categoryService;

    /**
     * 单个保存或者更新访谈分类
     *
     * @param category
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新访谈分类", notes="根据访谈分类对象保存或者更新访谈分类信息")
    @ApiImplicitParam(name = "category", value = "访谈分类对象", required = true, dataType = "Category", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Category category) {
        log.info(String.format("保存或者更新访谈分类: %s ", JSONUtil.toJsonStr(category)));
        boolean result = categoryService.saveOrUpdate(category);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新访谈分类
     *
     * @param categoryList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新访谈分类", notes="根据访谈分类对象集合批量保存或者更新访谈分类信息")
    @ApiImplicitParam(name = "categoryList", value = "访谈分类对象集合", required = true, allowMultiple = true, dataType = "Category", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Category> categoryList) {
        log.info(String.format("批量保存或者更新访谈分类: %s ", JSONUtil.toJsonStr(categoryList)));
        boolean result = categoryService.saveOrUpdateBatch(categoryList);
        return RestResult.ok(result);
    }

    /**
     * 根据Category对象属性逻辑删除访谈分类
     *
     * @param category
     * @return
     */
    @PostMapping("/removeByCategory")
    @ApiOperation(value="根据Category对象属性逻辑删除访谈分类", notes="根据访谈分类对象逻辑删除访谈分类信息")
    @ApiImplicitParam(name = "category", value = "访谈分类对象", required = true, dataType = "Category", paramType = "query")
    public RestResult<Boolean> removeByCategory(Category category) {
        log.info(String.format("根据Category对象属性逻辑删除访谈分类: %s ", category));
        boolean result = categoryService.removeByBean(category);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除访谈分类
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除访谈分类", notes="根据访谈分类对象ID批量逻辑删除访谈分类信息")
    @ApiImplicitParam(name = "ids", value = "访谈分类对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除访谈分类: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = categoryService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Category对象属性获取访谈分类
     *
     * @param category
     * @return
     */
    @GetMapping("/getByCategory")
    @ApiOperation(value="根据Category对象属性获取访谈分类", notes="根据访谈分类对象属性获取访谈分类信息")
    @ApiImplicitParam(name = "category", value = "访谈分类对象", required = false, dataType = "Category", paramType = "query")
    public RestResult<CategoryVo> getByCategory(Category category) {
        category = categoryService.getByBean(category);
        CategoryVo categoryVo = categoryService.setVoProperties(category);
        log.info(String.format("根据id获取访谈分类：%s", JSONUtil.toJsonStr(categoryVo)));
        return RestResult.ok(categoryVo);
    }

    /**
     * 根据Category对象属性检索所有访谈分类
     *
     * @param category
     * @return
     */
    @GetMapping("/listByCategory")
    @ApiOperation(value="根据Category对象属性检索所有访谈分类", notes="根据Category对象属性检索所有访谈分类信息")
    @ApiImplicitParam(name = "category", value = "访谈分类对象", required = false, dataType = "Category", paramType = "query")
    public RestResult<Collection<CategoryVo>> listByCategory(Category category) {
        Collection<Category> categorys = categoryService.listByBean(category);
        Collection<CategoryVo> categoryVos = categoryService.setVoProperties(categorys);
        log.info(String.format("根据Category对象属性检索所有访谈分类: %s ",JSONUtil.toJsonStr(categoryVos)));
        return RestResult.ok(categoryVos);
    }

    /**
     * 根据Category对象属性分页检索访谈分类
     *
     * @param category
     * @return
     */
    @GetMapping("/pageByCategory")
    @ApiOperation(value="根据Category对象属性分页检索访谈分类", notes="根据Category对象属性分页检索访谈分类信息")
    @ApiImplicitParam(name = "category", value = "访谈分类对象", required = false, dataType = "Category", paramType = "query")
    public RestResult<IPage<CategoryVo>> pageByCategory(Category category) {
        IPage<CategoryVo> categorys = categoryService.pageByBean(category);
        categorys.setRecords(categoryService.setVoProperties(categorys.getRecords()));
        log.info(String.format("根据Category对象属性分页检索访谈分类: %s ",JSONUtil.toJsonStr(categorys)));
        return RestResult.ok(categorys);
    }

    /**
     * 根据Category对象属性分页检索访谈分类
     *
     * @param category
     * @return
     */
    @GetMapping("/pageByNameAndSiteId")
    @ApiOperation(value="根据Category对象属性分页检索访谈分类", notes="根据Category对象属性分页检索访谈分类信息")
    @ApiImplicitParam(name = "category", value = "访谈分类对象", required = false, dataType = "Category", paramType = "query")
    public RestResult<IPage<CategoryVo>> pageByNameAndSiteId(Category category) {
        IPage<CategoryVo> categorys = categoryService.pageByNameAndSiteId(category);
        categorys.setRecords(categoryService.setVoProperties(categorys.getRecords()));
        log.info(String.format("根据Category对象属性分页检索访谈分类: %s ",JSONUtil.toJsonStr(categorys)));
        return RestResult.ok(categorys);
    }

    /**
     * 根据Category对象属性分页检索访谈分类
     *
     * @param category
     * @return
     */
    @GetMapping("/listByNameAndSiteId")
    @ApiOperation(value="根据Category对象属性分页检索访谈分类", notes="根据Category对象属性分页检索访谈分类信息")
    @ApiImplicitParam(name = "category", value = "访谈分类对象", required = false, dataType = "Category", paramType = "query")
    public RestResult<IPage<CategoryVo>> listByNameAndSiteId(Category category) {
        List<CategoryVo> categorys = categoryService.listByNameAndSiteId(category);
        log.info(String.format("根据Category对象属性分页检索访谈分类: %s ",JSONUtil.toJsonStr(categorys)));
        return RestResult.ok(categorys);
    }
}
