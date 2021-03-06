package com.deyatech.station.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.config.AipNlpConfig;
import com.deyatech.station.entity.Model;
import com.deyatech.station.entity.Template;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.ModelService;
import com.deyatech.station.service.PageService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.station.vo.TemplateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @Autowired
    PageService pageService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    CatalogService catalogService;
    @Autowired
    SiteCache siteCache;

    /**
     * 获取字段
     *
     * @param contentModelId
     * @return
     */
    @RequestMapping("/getBaseAndMetaField")
    @ApiOperation(value="获取字段", notes="获取字段")
    @ApiImplicitParam(name = "contentModelId", value = "内容模型ID", required = true, dataType = "String", paramType = "query")
    public RestResult getBaseAndMetaField(String contentModelId) {
        return RestResult.ok(templateService.getBaseAndMetaField(contentModelId));
    }

    /**
     * 获取动态表单
     *
     * @param contentModelId
     * @param templateId
     * @return
     */
    @RequestMapping("/getDynamicForm")
    @ApiOperation(value="获取动态表单", notes="获取动态表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contentModelId", value = "内容模型ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "templateId", value = "内容模板ID", required = true, dataType = "String", paramType = "query")
    })
    public RestResult getDynamicForm(String contentModelId, String templateId) {
        // 内容模型
        Model model = modelService.getById(contentModelId);
        if (Objects.isNull(model)) {
            return RestResult.error("模型不存在");
        }
        // 元数据集ID
        String collectionId = model.getMetaDataCollectionId();
        boolean check = adminFeign.checkMetadataCollectionById(collectionId).getData();
        if (!check) {
            return RestResult.error("模型关联元数据集不存在");
        }
        return RestResult.ok(templateService.getDynamicForm(contentModelId, templateId));
    }

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
     * 删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除内容模板", notes="根据内容模板对象ID批量逻辑删除内容模板信息")
    @ApiImplicitParam(name = "ids", value = "内容模板对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(String ids) {
        log.info(String.format("删除: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = templateService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 回收站
     *
     * @param ids
     * @return
     */
    @RequestMapping("/recycleByIds")
    @ApiOperation(value="回收站", notes="回收站")
    @ApiImplicitParam(name = "ids", value = "内容模板对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> recycleByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("回收站: %s ", JSONUtil.toJsonStr(ids)));
        return RestResult.ok(templateService.recycleByIds(ids));
    }

    /**
     * 还原
     *
     * @param ids
     * @return
     */
    @RequestMapping("/backByIds")
    @ApiOperation(value="还原", notes="还原")
    @ApiImplicitParam(name = "ids", value = "内容模板对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> backByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("还原: %s ", JSONUtil.toJsonStr(ids)));
        return RestResult.ok(templateService.backByIds(ids));
    }

    /**
     * 撤销
     *
     * @param ids
     * @return
     */
    @RequestMapping("/cancelByIds")
    @ApiOperation(value="撤销", notes="撤销")
    @ApiImplicitParam(name = "ids", value = "内容模板对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> cancelByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("撤销: %s ", JSONUtil.toJsonStr(ids)));
        return RestResult.ok(templateService.cancelByIds(ids));
    }

    /**
     * 发布
     *
     * @param ids
     * @return
     */
    @RequestMapping("/publishByIds")
    @ApiOperation(value="发布", notes="发布")
    @ApiImplicitParam(name = "ids", value = "内容模板对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> publishByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("发布: %s ", JSONUtil.toJsonStr(ids)));
        return RestResult.ok(templateService.publishByIds(ids));
    }

    /**
     * 送审
     *
     * @param ids
     * @return
     */
    @RequestMapping("/verifyByIds")
    @ApiOperation(value="送审", notes="送审")
    @ApiImplicitParam(name = "ids", value = "内容模板对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> verifyByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("送审: %s ", JSONUtil.toJsonStr(ids)));
        return RestResult.ok(templateService.verifyByIds(ids));
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
     * 翻页检索内容
     *
     * @param template
     * @return
     */
    @GetMapping("/pageByTemplate")
    @ApiOperation(value="根据Template对象属性分页检索内容模板", notes="根据Template对象属性分页检索内容模板信息")
    @ApiImplicitParam(name = "template", value = "内容模板对象", required = false, dataType = "Template", paramType = "query")
    public RestResult<IPage<TemplateVo>> pageByTemplate(TemplateVo template) {
        log.info(String.format("翻页检索内容: %s ",JSONUtil.toJsonStr(template)));
        IPage<TemplateVo> templates = templateService.pageByTemplate(template);
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
        templateService.genStaticPage(templateVo, RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE);
        return RestResult.ok(true);
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
        boolean result = templateService.reindex(templateVo,RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE);
        return RestResult.ok(result);
    }

    /**
     * 删除索引数据
     *
     * @param templateVo
     * @return
     */
    @GetMapping("/removeIndexData")
    @ApiOperation(value="删除索引数据", notes="删除索引数据")
    @ApiImplicitParam(name = "template", value = "内容模板扩展对象", required = false, dataType = "Template", paramType = "query")
    public RestResult<Boolean> removeIndexData(TemplateVo templateVo) {
        log.info(String.format("删除索引数据: %s ", templateVo));
        boolean result = templateService.reindex(templateVo,RabbitMQConstants.MQ_CMS_INDEX_COMMAND_DELETE);
        return RestResult.ok(result);
    }

    private static long startKeyword = 0L;

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
    public synchronized RestResult keyword(String title, String content) {
        if (StrUtil.isEmpty(title) || StrUtil.isEmpty(content)) {
            return RestResult.error("标题和内容不能为空");
        }
        if (content.length() > 32767) {
            content = content.substring(0, 32767);
        }
        if (title.length() > 40) {
            title = title.substring(0, 40);
        }
        List<String> keywords = new ArrayList<>();
        // 调用前计算时差
        long waitTime = (1000 / 2) - (System.currentTimeMillis() - startKeyword);
        try {
            if (waitTime > 0) {
                Thread.sleep(waitTime);
            }
        } catch (Exception e) {}
        JSONObject res = this.aipNlpConfig.getInstance().keyword(title, content, new HashMap<>());
        // 调用完成记录时间
        startKeyword = System.currentTimeMillis();
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
    private static long startSummary = 0L;
    /**
     * 摘要
     *
     * @param content
     * @param title
     * @return
     */
    @RequestMapping("/summary")
    @ApiOperation(value="摘要", notes="摘要")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "正文", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "标题", required = false, dataType = "String", paramType = "query")
    })
    public synchronized RestResult summary(String content, @RequestParam(value="title", required = false) String title) {
        if (StrUtil.isEmpty(content)) {
            return RestResult.error("正文不能为空");
        }
        if (content.length() > 3000) {
            content = content.substring(0, 3000);
        }
        if (StrUtil.isNotEmpty(title)) {
            if (title.length() > 200) {
                title = title.substring(0, 200);
            }
        } else {
            title = "";
        }
        HashMap<String, Object> options = new HashMap<>();
        options.put("title", title);
        int maxSummaryLen = 300;
        // 调用前计算时差
        long waitTime = (1000 / 2) - (System.currentTimeMillis() - startSummary);
        try {
            if (waitTime > 0) {
                Thread.sleep(waitTime);
            }
        } catch (Exception e) {}
        JSONObject res = this.aipNlpConfig.getInstance().newsSummary(content, maxSummaryLen, options);
        // 调用完成记录时间
        startSummary = System.currentTimeMillis();
        if (res.has("error_code")) {
            return RestResult.error(res.getString("error_msg"));
        }
        return RestResult.ok(res.getString("summary"));
    }

    /**
     * 获取登陆用户代办理任务列表
     *
     * @param templateVo
     * @return
     */
    @GetMapping("/getLoginUserTaskList")
    @ApiOperation(value="获取登陆用户代办理任务列表", notes="获取登陆用户代办理任务列表")
    @ApiImplicitParam(name = "template", value = "内容模板扩展对象", required = false, dataType = "Template", paramType = "query")
    RestResult<IPage<TemplateVo>> getLoginUserTaskList(TemplateVo templateVo) {
        return RestResult.ok(templateService.getLoginUserTaskList(templateVo));
    }
}