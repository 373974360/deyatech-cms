package com.deyatech.station.controller;

import cn.hutool.core.util.StrUtil;
import com.baidu.aip.nlp.AipNlp;
import com.deyatech.common.enums.ContentStatusEnum;
import com.deyatech.station.config.AipNlpConfig;
import com.deyatech.station.entity.Template;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.station.service.TemplateService;
import com.deyatech.common.entity.RestResult;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 内容模板 前端控制器
 * </p>
 * @author: csm.
 * @since 2019-08-06
 */
@Slf4j
@RestController
@RequestMapping("/station/template")
@Api(tags = {"内容模板接口"})
public class TemplateController extends BaseController {
    @Autowired
    AipNlpConfig aipNlpConfig;
    @Autowired
    TemplateService templateService;

    /**
     * 单个保存或者更新内容模板
     *
     * @param templateVo
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新内容模板", notes="根据内容模板对象保存或者更新内容模板信息")
    @ApiImplicitParam(name = "templateVo", value = "内容模板对象", required = true, dataType = "TemplateVo", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(TemplateVo templateVo) {
        log.info(String.format("保存或者更新内容模板: %s ", JSONUtil.toJsonStr(templateVo)));
        boolean result = templateService.saveOrUpdateTemplateVo(templateVo);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新内容模板
     *
     * @param templateList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新内容模板", notes="根据内容模板对象集合批量保存或者更新内容模板信息")
    @ApiImplicitParam(name = "templateList", value = "内容模板对象集合", required = true, allowMultiple = true, dataType = "Template", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Template> templateList) {
        log.info(String.format("批量保存或者更新内容模板: %s ", JSONUtil.toJsonStr(templateList)));
        boolean result = templateService.saveOrUpdateBatch(templateList);
        return RestResult.ok(result);
    }

    /**
     * 根据Template对象属性逻辑删除内容模板
     *
     * @param template
     * @return
     */
    @PostMapping("/removeByTemplate")
    @ApiOperation(value="根据Template对象属性逻辑删除内容模板", notes="根据内容模板对象逻辑删除内容模板信息")
    @ApiImplicitParam(name = "template", value = "内容模板对象", required = true, dataType = "Template", paramType = "query")
    public RestResult<Boolean> removeByTemplate(Template template) {
        log.info(String.format("根据Template对象属性逻辑删除内容模板: %s ", template));
        boolean result = templateService.removeByBean(template);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除内容模板
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除内容模板", notes="根据内容模板对象ID批量逻辑删除内容模板信息")
    @ApiImplicitParam(name = "ids", value = "内容模板对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(String ids) {
        log.info(String.format("根据id批量删除内容模板: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = templateService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 删除内容到回收站
     *
     * @param ids
     * @return
     */
    @RequestMapping("/recycleByIds")
    @ApiOperation(value="删除内容到回收站", notes="删除内容到回收站")
    @ApiImplicitParam(name = "ids", value = "内容模板对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> recycleByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("删除内容到回收站: %s ", JSONUtil.toJsonStr(ids)));
        int count = templateService.updateStatusByIds(ids, ContentStatusEnum.RECYCLE.getCode());
        return RestResult.ok(count > 0 ? true : false);
    }

    /**
     * 撤销内容
     *
     * @param ids
     * @return
     */
    @RequestMapping("/cancelByIds")
    @ApiOperation(value="撤销内容", notes="撤销内容")
    @ApiImplicitParam(name = "ids", value = "内容模板对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> cancelByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("撤销内容: %s ", JSONUtil.toJsonStr(ids)));
        int count = templateService.updateStatusByIds(ids, ContentStatusEnum.CANCEL.getCode());
        return RestResult.ok(count > 0 ? true : false);
    }

    /**
     * 更新权重
     *
     * @param sortNo
     * @param id
     * @return
     */
    @RequestMapping("/updateSortNoById")
    @ApiOperation(value="更新权重", notes="更新权重")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sortNo", value = "权重", required = true, allowMultiple = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "编号", required = true, allowMultiple = true, dataType = "String", paramType = "query")
    })
    public RestResult<Boolean> updateSortNoById(int sortNo, String id) {
        log.info(String.format("更新权重: sortNo=%s id=%s ", sortNo, id));
        int count = templateService.updateSortNoById(sortNo, id);
        return RestResult.ok(count > 0 ? true : false);
    }

    /**
     * 更新置顶
     *
     * @param flagTop
     * @param id
     * @return
     */
    @RequestMapping("/updateFlagTopById")
    @ApiOperation(value="更新置顶", notes="更新置顶")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flagTop", value = "置顶", required = true, allowMultiple = true, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "编号", required = true, allowMultiple = true, dataType = "String", paramType = "query")
    })
    public RestResult<Boolean> updateFlagTopById(boolean flagTop, String id) {
        log.info(String.format("更新置顶: flagTop=%s id=%s", flagTop, id));
        int count = templateService.updateFlagTopById(flagTop, id);
        return RestResult.ok(count > 0 ? true : false);
    }

    /**
     * 根据Template对象属性获取内容模板
     *
     * @param template
     * @return
     */
    @GetMapping("/getByTemplate")
    @ApiOperation(value="根据Template对象属性获取内容模板", notes="根据内容模板对象属性获取内容模板信息")
    @ApiImplicitParam(name = "template", value = "内容模板对象", required = false, dataType = "Template", paramType = "query")
    public RestResult<TemplateVo> getByTemplate(Template template) {
        template = templateService.getByBean(template);
        TemplateVo templateVo = templateService.setVoProperties(template);
        log.info(String.format("根据id获取内容模板：%s", JSONUtil.toJsonStr(templateVo)));
        return RestResult.ok(templateVo);
    }

    /**
     * 根据Template对象属性检索所有内容模板
     *
     * @param template
     * @return
     */
    @GetMapping("/listByTemplate")
    @ApiOperation(value="根据Template对象属性检索所有内容模板", notes="根据Template对象属性检索所有内容模板信息")
    @ApiImplicitParam(name = "template", value = "内容模板对象", required = false, dataType = "Template", paramType = "query")
    public RestResult<Collection<TemplateVo>> listByTemplate(Template template) {
        Collection<Template> templates = templateService.listByBean(template);
        Collection<TemplateVo> templateVos = templateService.setVoProperties(templates);
        log.info(String.format("根据Template对象属性检索所有内容模板: %s ",JSONUtil.toJsonStr(templateVos)));
        return RestResult.ok(templateVos);
    }

    /**
     * 根据Template对象属性分页检索内容模板
     *
     * @param template
     * @return
     */
    @GetMapping("/pageByTemplate")
    @ApiOperation(value="根据Template对象属性分页检索内容模板", notes="根据Template对象属性分页检索内容模板信息")
    @ApiImplicitParam(name = "template", value = "内容模板对象", required = false, dataType = "Template", paramType = "query")
    public RestResult<IPage<TemplateVo>> pageByTemplate(Template template) {
        IPage<TemplateVo> templates = templateService.pageByTemplate(template);
        templates.setRecords(templateService.setVoProperties(templates.getRecords()));
        log.info(String.format("根据Template对象属性分页检索内容模板: %s ",JSONUtil.toJsonStr(templates)));
        return RestResult.ok(templates);
    }

    /**
     * 判断Template对象标题是否存在
     *
     * @param template
     * @return
     */
    @GetMapping("/checkTitleExist")
    @ApiOperation(value="判断Template对象标题是否存在", notes="判断Template对象标题是否存在")
    @ApiImplicitParam(name = "template", value = "内容模板对象", required = false, dataType = "Template", paramType = "query")
    public RestResult<Boolean> checkTitleExist(Template template) {
        log.info(String.format("判断Template对象标题是否存在: %s ", template));
        boolean result = templateService.checkTitleExist(template);
        return RestResult.ok(result);
    }

    /**
     * 生成静态页
     *
     * @param templateVo
     * @return
     */
    @GetMapping("/genStaticPage")
    @ApiOperation(value="生成静态页", notes="生成静态页")
    @ApiImplicitParam(name = "templateVo", value = "内容模板Vo对象", required = false, dataType = "TemplateVo", paramType = "query")
    public RestResult<Boolean> genStaticPage(TemplateVo templateVo) {
        log.info(String.format("生成静态页: %s ", templateVo));
        boolean result = templateService.genStaticPage(templateVo);
        return RestResult.ok(result);
    }

    /**
     * 生成索引
     *
     * @param templateVo
     * @return
     */
    @GetMapping("/reindex")
    @ApiOperation(value="生成索引", notes="生成索引")
    @ApiImplicitParam(name = "templateVo", value = "内容模板Vo对象", required = false, dataType = "TemplateVo", paramType = "query")
    public RestResult<Boolean> reindex(TemplateVo templateVo) {
        log.info(String.format("生成索引: %s ", templateVo));
        boolean result = templateService.reindex(templateVo);
        return RestResult.ok(result);
    }

    /**
     * 更新内容状态
     *
     * @param template
     * @return
     */
    @PostMapping("/updateContentStatus")
    @ApiOperation(value="更新内容状态", notes="更新内容状态")
    @ApiImplicitParam(name = "template", value = "内容模板对象", required = true, dataType = "Template", paramType = "query")
    public RestResult<Boolean> updateContentStatus(Template template) {
        log.info(String.format("更新内容状态: %s ", JSONUtil.toJsonStr(template)));
        // 内容发布状态：1-草稿，2-已发布
        template.setStatus(2);
        // 发布日期
        template.setResourcePublicationDate(new Date());
        boolean result = templateService.updateById(template);
        return RestResult.ok(result);
    }

    /**
     * 删除索引数据
     *
     * @param template
     * @return
     */
    @GetMapping("/removeIndexData")
    @ApiOperation(value="删除索引数据", notes="删除索引数据")
    @ApiImplicitParam(name = "template", value = "内容模板扩展对象", required = false, dataType = "Template", paramType = "query")
    public RestResult<Boolean> removeIndexData(Template template) {
        log.info(String.format("删除索引数据: %s ", template));
        boolean result = templateService.removeIndexData(template);
        return RestResult.ok(result);
    }

    /**
     * 关键字
     *
     * @param title
     * @param content
     * @return
     */
    @RequestMapping("/keyword")
    @ApiOperation(value="关键字", notes="关键字")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "正文", required = false, dataType = "String", paramType = "query")
    })
    public RestResult keyword(String title, String content) {
        if (StrUtil.isEmpty(title) || StrUtil.isEmpty(content)) {
            return RestResult.error("标题和内容不能为空");
        }
        List<String> keywords = new ArrayList<>();
        AipNlp aipNlp = this.aipNlpConfig.getInstance();
        JSONObject res = aipNlp.keyword(title, content, new HashMap<>());
        if (res.has("error_code")) {
            return RestResult.error(res.getString("error_msg"));
        }
        JSONArray items = res.getJSONArray("items");
        if (items != null && items.length() > 0) {
            for (int i = 0; i < items.length(); i++) {
                keywords.add(items.getJSONObject(i).getString("tag"));
            }
        }
        return RestResult.ok(keywords);
    }

    /**
     * 摘要
     *
     * @param title
     * @param content
     * @param maxSummaryLen
     * @return
     */
    @RequestMapping("/summary")
    @ApiOperation(value="摘要", notes="摘要")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "正文", required = false, dataType = "String", paramType = "query")
    })
    public RestResult summary(String title, String content, int maxSummaryLen) {
        if (StrUtil.isEmpty(title) || StrUtil.isEmpty(content)) {
            return RestResult.error("标题和内容不能为空");
        }
        if (maxSummaryLen <= 0) {
            maxSummaryLen = 100;
        }
        AipNlp aipNlp = this.aipNlpConfig.getInstance();
        HashMap<String, Object> options = new HashMap<>();
        options.put("title", title);
        JSONObject res = aipNlp.newsSummary(content, maxSummaryLen, options);
        if (res.has("error_code")) {
            return RestResult.error(res.getString("error_msg"));
        }
        return RestResult.ok(res.getString("summary"));
    }
}