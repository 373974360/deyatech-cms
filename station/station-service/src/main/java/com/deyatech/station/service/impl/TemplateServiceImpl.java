package com.deyatech.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.entity.Metadata;
import com.deyatech.admin.entity.MetadataCollection;
import com.deyatech.admin.entity.User;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.DictionaryVo;
import com.deyatech.admin.vo.MetadataCollectionMetadataVo;
import com.deyatech.admin.vo.MetadataCollectionVo;
import com.deyatech.admin.vo.MetadataVo;
import com.deyatech.assembly.feign.AssemblyFeign;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.ContentStatusEnum;
import com.deyatech.common.enums.MaterialUsePlaceEnum;
import com.deyatech.common.enums.TemplateAuthorityEnum;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.common.utils.ColumnUtil;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.*;
import com.deyatech.station.index.IndexService;
import com.deyatech.station.mapper.TemplateMapper;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.service.*;
import com.deyatech.station.vo.*;
import com.deyatech.workflow.feign.WorkflowFeign;
import com.deyatech.workflow.vo.ProcessInstanceVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 内容模板 服务实现类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-06
 */
@Service
@Slf4j
public class TemplateServiceImpl extends BaseServiceImpl<TemplateMapper, Template> implements TemplateService {

    @Autowired
    private CatalogService catalogService;
    @Autowired
    private AmqpTemplate rabbitmqTemplate;
    @Autowired
    private ModelService modelService;
    @Autowired
    private WorkflowFeign workflowFeign;
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    private ModelTemplateServiceImpl modelTemplateService;
    @Autowired
    private SiteCache siteCache;
    @Autowired
    CatalogRoleService catalogRoleService;
    @Autowired
    TemplateRoleAuthorityService templateRoleAuthorityService;
    @Autowired
    TemplateFormOrderService formOrderService;
    @Autowired
    AssemblyFeign assemblyFeign;
    @Autowired
    MaterialService materialService;

    @Autowired
    IndexService indexService;
    @Autowired
    ObjectMapper objectMapper;

    /**
     * 获取字段
     *
     * @param contentModelId
     * @return
     */
    @Override
    public Map<String, Object> getBaseAndMetaField(String contentModelId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Metadata> metadataMap = Template.baseFields();
        List<String> baseFields = metadataMap.values().stream().map(Metadata::getBriefName).collect(Collectors.toList());
        result.put("baseFields", baseFields);

        List<String> metaFields = new ArrayList<>();
        // 内容模型
        Model model = modelService.getById(contentModelId);
        MetadataCollection collection = formOrderService.getCollectionById(model.getMetaDataCollectionId());
        List<Metadata> metadatas = formOrderService.getAllMetadataByByCollectionId(model.getMetaDataCollectionId());
        if (CollectionUtil.isNotEmpty(metadatas)) {
            for (Metadata md : metadatas) {
                metaFields.add(collection.getMdPrefix() + md.getBriefName());
            }
        }
        result.put("metaFields", metaFields);
        result.put("metaDataCollectionId", model.getMetaDataCollectionId());
        return result;
    }

    /**
     * 获取动态表单
     *
     * @param contentModelId
     * @param templateId
     * @return
     */
    @Override
    public List<TemplateDynamicFormVo> getDynamicForm(String contentModelId, String templateId) {
        List<TemplateDynamicFormVo> result = new ArrayList<>();
        // 内容模型
        Model model = modelService.getById(contentModelId);
        // 元数据集ID
        String collectionId = model.getMetaDataCollectionId();
        // 元数据映射
        Map<String, Metadata> metadataMap = getMetadatas(collectionId);
        // 表单顺序映射
        Map<String, Object> orderMap = formOrderService.getFormOrderByCollectionId(collectionId);
        // 元数据字段数据映射
        Map<String, Object> metadataFieldDataMap = new HashMap<>();
        // 获取数据
        if (StrUtil.isNotEmpty(templateId)) {
            Template template = super.getById(templateId);
            Map<String, Object> baseMap = ColumnUtil.objectToColumnMap(template);
            if (Objects.nonNull(baseMap)) metadataFieldDataMap.putAll(baseMap);
            if (StrUtil.isNotEmpty(template.getContentId())) {
                Map<String, Object> metaMap = adminFeign.getMetadataById(collectionId, template.getContentId()).getData();
                if (Objects.nonNull(metaMap)) metadataFieldDataMap.putAll(metaMap);
            }
        }
        // 每一页已排好的顺序
        List<List<TemplateFormOrderVo>> SortedDatas =  (List<List<TemplateFormOrderVo>>) orderMap.get("orders");
        // 页码页名列表
        List<TemplateFormOrderVo> pages = (List<TemplateFormOrderVo>) orderMap.get("pages");
        for (TemplateFormOrderVo p : pages) {
            TemplateDynamicFormVo form = new TemplateDynamicFormVo();
            // 添加动态表单
            result.add(form);
            // 页码
            form.setPageNumber(p.getPageNumber());
            // 页名
            form.setPageName(p.getPageName());
            // 表单对象
            Map<String, Object> pageModel = new HashMap<>();
            form.setPageModel(pageModel);
            // 下拉框、单选框、复选框展示数据
            Map<String, Object> pageList = new HashMap<>();
            form.setPageList(pageList);
            // 表单行
            List<List<Metadata>> rows = new ArrayList<>();
            form.setRows(rows);
            // 取出当前页排好的表单
            List<TemplateFormOrderVo> formSortedData = SortedDatas.get(p.getPageNumber() - 1);
            for (int i = 0; i < formSortedData.size(); i++) {
                // 有序元数据ID对象
                TemplateFormOrderVo current = formSortedData.get(i);
                // 获取元数据
                Metadata currentMetadata = metadataMap.get(current.getMetadataId());
                // 每行元素
                List<Metadata> elements = new ArrayList<>();
                // 添加行元素
                rows.add(elements);
                // 设置表单绑定数据
                putPageModel(pageModel, currentMetadata, metadataFieldDataMap);
                // 设置下拉框、单选框、复选框展示数据
                putPageList(pageList, currentMetadata);
                // 添加表单项目
                elements.add(currentMetadata);

                // 不是最后一个元素并且当前元素是半行，需要补成一整行。
                if (i != formSortedData.size() - 1 && currentMetadata.getControlLength() == 1) {
                    // 下一个有序元数据ID对象
                    TemplateFormOrderVo next = formSortedData.get(i + 1);
                    // 获取下一个元数据
                    Metadata nextMetadata = metadataMap.get(next.getMetadataId());
                    // 下一个也是半行，则合并一行
                    if (nextMetadata.getControlLength() == 1) {
                        // 设置下一个表单绑定数据
                        putPageModel(pageModel, nextMetadata, metadataFieldDataMap);
                        // 设置下一个下拉框、单选框、复选框展示数据
                        putPageList(pageList, nextMetadata);
                        // 添加表单项目
                        elements.add(nextMetadata);
                        // 索引指向当前处理的元素
                        i = i + 1;
                    }
                }

            }// 内for
        }// 外for
        return result;
    }

    private void putPageList(Map<String, Object> pageList, Metadata md) {
        // 下拉框、单选框、复选框
        if (md.getControlType().equals("selectElement") || md.getControlType().equals("radioElement") || md.getControlType().equals("checkboxElement")) {
            List<DictionaryVo> list = adminFeign.getDictionaryListByIndexId(md.getDictionaryId()).getData();
            pageList.put(md.getBriefName(), list);
        }
    }

    private boolean isObjectStringEmpty(Object value) {
        if (Objects.isNull(value)) {
            return true;
        } else {
            if (StrUtil.isEmpty(value.toString())) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void putPageModel(Map<String, Object> pageModel, Metadata md, Map<String, Object> metadataFieldDataMap) {
        Object value = metadataFieldDataMap.get(md.getBriefName());
        // 开关switch
        if ("switchElement".equals(md.getControlType())) {
            pageModel.put(md.getBriefName(), isObjectStringEmpty(value) ? YesNoEnum.NO.getCode() : value);
        }
        // 复选框checkbox
        else if ("checkboxElement".equals(md.getControlType())) {
            // 绑定数组
            pageModel.put("checkbox_" + md.getBriefName(), isObjectStringEmpty(value) ? new ArrayList<String>() : Arrays.asList(value.toString().split(",")));
            pageModel.put(md.getBriefName(), isObjectStringEmpty(value) ? "" : value);
        }
        // 图片image
        else if ("imageElement".equals(md.getControlType())) {
            if (isObjectStringEmpty(value)) {
                pageModel.put(md.getBriefName(), "");
                pageModel.put("image_" + md.getBriefName(), new ArrayList<>());
            } else {
                pageModel.put(md.getBriefName(), value);
                Material materail = new Material();
                materail.setUrl(value.toString());
                 materail = materialService.getByBean(materail);
                MaterialVo materailVo = materialService.setVoProperties(materail);
                if (Objects.isNull(materail)) {
                    pageModel.put("image_" + md.getBriefName(), new ArrayList<>());
                } else {
                    StringBuilder url = new StringBuilder("/manage/station/material/showImageBySiteIdAndUrl?siteId=");
                    url.append(materail.getSiteId());
                    url.append("&url=");
                    url.append(materail.getUrl());
                    materailVo.setUrl(url.toString());
                    materailVo.setValue(materail.getUrl());
                    pageModel.put("image_" + md.getBriefName(), Arrays.asList(materailVo));
                }
            }
        }
        // 附件file
        else if ("fileElement".equals(md.getControlType())) {
            pageModel.put(md.getBriefName(), isObjectStringEmpty(value) ? "" : value);
            // 绑定数组
            if (isObjectStringEmpty(value)) {
                pageModel.put("file_" + md.getBriefName(), new ArrayList<Material>());
            } else {
                pageModel.put("file_" + md.getBriefName(), materialService.getDownloadMaterialsByUrl(value.toString()));
            }
        }
        // 其他
        else {
            pageModel.put(md.getBriefName(), isObjectStringEmpty(value) ? "" : value);
        }
    }

    private Map<String, Metadata> getMetadatas(String collectionId) {
        // 元数据集
        MetadataCollection collection = formOrderService.getCollectionById(collectionId);
        // 基础字段映射
        Map<String, Metadata> metadataMap = Template.baseFields();
        // 元数据
        List<Metadata> metadatas = formOrderService.getAllMetadataByByCollectionId(collectionId);
        if (CollectionUtil.isNotEmpty(metadatas)) {
            for (Metadata md : metadatas) {
                md.setBriefName(collection.getMdPrefix() + md.getBriefName());
                metadataMap.put(md.getId(), md);
            }
        }
        return metadataMap;
    }

    /**
     * 单个将对象转换为vo内容模板
     *
     * @param template
     * @return
     */
    @Override
    public TemplateVo setVoProperties(Template template){
        TemplateVo templateVo = baseMapper.queryTemplateById(template.getId());
        BeanUtil.copyProperties(template, templateVo);
        // 查询元数据结构及数据
        this.queryMetadata(templateVo);
        // 查询模型模板
        this.queryModelTemplate(templateVo);
        return templateVo;
    }

    /**
     * 批量将对象转换为vo内容模板
     *
     * @param templates
     * @return
     */
    @Override
    public List<TemplateVo> setVoProperties(Collection templates){
        List<TemplateVo> templateVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(templates)) {
            for (Object template : templates) {
                TemplateVo templateVo = new TemplateVo();
                BeanUtil.copyProperties(template, templateVo);
                templateVos.add(templateVo);
            }
        }
        return templateVos;
    }

    /**
     * 查询模型模板
     *
     * @param templateVo
     * @return
     */
    private void queryModelTemplate(TemplateVo templateVo) {
        //  查询模板配置
        ModelTemplate mt = new ModelTemplate();
        mt.setCmsCatalogId(templateVo.getCmsCatalogId());
        mt.setContentModelId(templateVo.getContentModelId());
        mt.setSiteId(templateVo.getSiteId());
        ModelTemplate modelTemplate = modelTemplateService.getByBean(mt);
        // 如果为空，查询站点默认模板
        if (ObjectUtil.isNull(modelTemplate)) {
            QueryWrapper<ModelTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNull("cms_catalog_id");
            queryWrapper.eq("content_model_id", templateVo.getContentModelId());
            queryWrapper.eq("site_id", templateVo.getSiteId());
            modelTemplate = modelTemplateService.getOne(queryWrapper);
        }
        if (Objects.nonNull(modelTemplate)) {
            templateVo.setTemplatePath(modelTemplate.getTemplatePath());
        }
    }

    /**
     * 查询元数据结构及数据
     * @param templateVo
     */
    private void queryMetadata(TemplateVo templateVo) {
        if (StrUtil.isNotEmpty(templateVo.getMetaDataCollectionId())){
            // 查询元数据结构
            MetadataCollectionVo metadataCollectionVo = new MetadataCollectionVo();
            metadataCollectionVo.setId(templateVo.getMetaDataCollectionId());
            List<MetadataCollectionVo> metadataCollectionVoList = adminFeign.findAllData(metadataCollectionVo).getData();
            if (CollectionUtil.isNotEmpty(metadataCollectionVoList)) {
                templateVo.setMetadataCollectionVo(metadataCollectionVoList.get(0));
            }
        }
        if (StrUtil.isNotEmpty(templateVo.getMetaDataCollectionId())
                && StrUtil.isNotEmpty(templateVo.getContentId())) {
            // 查询元数据记录信息
            Map content = adminFeign.getMetadataById(templateVo.getMetaDataCollectionId(), templateVo.getContentId()).getData();
            templateVo.setContent(content);
        }
    }

    private void checkUrl(String oldUrl, String newUrl, List<String> oldUrlList, List<String> newUrlList) {
        // 原来有
        if (StrUtil.isNotEmpty(oldUrl)) {
            // 现在有
            if (StrUtil.isNotEmpty(newUrl)) {
                // 不相等处理
                if (!oldUrl.equals(newUrl)) {
                    oldUrlList.add(oldUrl);
                    newUrlList.add(newUrl);
                }
            } else {
                oldUrlList.add(oldUrl);
            }

            // 原来没
        } else {
            // 现在有
            if (StrUtil.isNotEmpty(newUrl)) {
                newUrlList.add(newUrl);
            }
        }
    }

    private void contentUrl(boolean hasId , TemplateVo templateVo, Map<String, Object> contentMap, List<String> oldUrlList, List<String> newUrlList) {
        // 附件和图片字段
        List<String> fileImageFields = new ArrayList<>();
        TemplateVo templateVoDB = new TemplateVo();
        templateVoDB.setMetaDataCollectionId(templateVo.getMetaDataCollectionId());
        templateVoDB.setContentId(templateVo.getContentId());
        this.queryMetadata(templateVoDB);
        Map<String, Object> contentMapDB = templateVoDB.getContent();
        MetadataCollectionVo metadataCollectionVoDB = templateVoDB.getMetadataCollectionVo();
        if (Objects.nonNull(metadataCollectionVoDB)) {
            List<MetadataCollectionMetadataVo> metadataList = metadataCollectionVoDB.getMetadataList();
            if (CollectionUtil.isNotEmpty(metadataList)) {
                for (MetadataCollectionMetadataVo cmd : metadataList) {
                    MetadataVo md = cmd.getMetadata();
                    if (Objects.nonNull(md)) {
                        if ("fileElement".equals(md.getControlType()) || "imageElement".equals(md.getControlType())) {
                            fileImageFields.add(metadataCollectionVoDB.getMdPrefix() + md.getBriefName());
                        }
                    }
                }
            }
        }
        // 更新
        if (hasId) {
            for (String field : fileImageFields) {
                checkUrl((String) contentMapDB.get(field), (String) contentMap.get(field), oldUrlList, newUrlList);
            }
            // 新增
        } else {
            for (String field : fileImageFields) {
                checkUrl(null, (String) contentMap.get(field), oldUrlList, newUrlList);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateTemplateVo(TemplateVo templateVo) {
        // 获取栏目信息
        Catalog catalog = null;
        if (this.checkTitleExist(templateVo)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "当前栏目中已存在该标题内容");
        }
        // 外链
        if (YesNoEnum.YES.getCode() == templateVo.getFlagExternal()) {
            if (StrUtil.isEmpty(templateVo.getUrl())) {
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "外链内容必须填写URL");
            }
        } else {
            // 获取栏目信息
            catalog = catalogService.getById(templateVo.getCmsCatalogId());
            if (Objects.isNull(catalog)) {
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "栏目信息不存在");
            }
        }
        List<String> oldUrlList = new ArrayList<>();
        List<String> newUrlList = new ArrayList<>();
        boolean hasId = StrUtil.isNotBlank(templateVo.getId());
        // 更新
        if (hasId) {
            Template templateDB = super.getById(templateVo.getId());
            // 标记材料
            checkUrl(templateDB.getThumbnail(), templateVo.getThumbnail(), oldUrlList, newUrlList);
            // 新增
        } else {
            // 标记材料
            checkUrl(null, templateVo.getThumbnail(), oldUrlList, newUrlList);
        }
        // 非外链保存或更新元数据
        if (YesNoEnum.NO.getCode() == templateVo.getFlagExternal() && StrUtil.isNotEmpty(templateVo.getContentMapStr())) {
            // contentMap 数据库字段
            Map<String, Object> contentMap = dataConvert(templateVo.getMetaDataCollectionId(), templateVo.getContentMapStr());
            // 标记材料
            contentUrl(hasId , templateVo, contentMap, oldUrlList, newUrlList);
            String contentId = adminFeign.saveOrUpdateMetadata(templateVo.getMetaDataCollectionId(), templateVo.getContentId(), contentMap).getData();
            // 如果是插入数据， 回填contentId
            if (StrUtil.isEmpty(templateVo.getContentId())) {
                templateVo.setContentId(contentId);
            }
        }

        // 草稿
        if ("true".equals(templateVo.getDraftFlag())) {
            templateVo.setStatus(ContentStatusEnum.DRAFT.getCode());
        } else {
            // 新增时设置状态, 编辑时不改变状态
            if (!hasId) {
                // 有工作流
                if (StrUtil.isNotEmpty(templateVo.getWorkflowKey())) {
                    // 审核中
                    templateVo.setStatus(ContentStatusEnum.VERIFY.getCode());
                } else {
                    // 已发布
                    templateVo.setStatus(ContentStatusEnum.PUBLISH.getCode());
                }
            }
        }
        // 发布时间
        templateVo.setResourcePublicationDate(new Date());
        // 新增时生成索引码
        if (!hasId) {
            RestResult<String> resultIndexCode = assemblyFeign.getNextIndexCodeBySiteId(templateVo.getSiteId());
            // 索引编码
            templateVo.setIndexCode(resultIndexCode.getData());
        }
        // 保存内容
        boolean result = super.saveOrUpdate(templateVo);
        if (result) {
            // 新增时
            if (!hasId) {
                // 如果当前栏目绑定了工作流 启动工作流
                if(catalog.getWorkflowEnable().equals(YesNoEnum.YES.getCode())){
                    startWorkflow(templateVo);
                }
                // 非外链
                if (YesNoEnum.NO.getCode() == templateVo.getFlagExternal()) {
                    // 根据主键ID命名 静态资源文件URL
                    templateVo.setUrl("/" + catalog.getPathName() + "/" + templateVo.getId() + ".html");
                    super.updateById(templateVo);
                }
            }
            // 发布状态
            if (ContentStatusEnum.PUBLISH.getCode() == templateVo.getStatus()) {
                // 生成静态页面任务
                this.addStaticPageTask(templateVo);
                // 默认都创建索引, 索引任务
                this.addIndexTask(templateVo, hasId ? RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE : RabbitMQConstants.MQ_CMS_INDEX_COMMAND_ADD);
            }
            // 标记材料
            materialService.markMaterialUsePlace(oldUrlList, newUrlList, MaterialUsePlaceEnum.STATION_TEMPLATE.getCode());
        }
        return result;
    }

    private void startWorkflow(TemplateVo templateVo) {
        // 启动工作流
        ProcessInstanceVo processInstanceVo = new ProcessInstanceVo();
        processInstanceVo.setActDefinitionKey(templateVo.getWorkflowKey());
        processInstanceVo.setBusinessId(String.valueOf(System.currentTimeMillis()));
        processInstanceVo.setUserId(UserContextHelper.getUserId());
        processInstanceVo.setSource("CMS");
        Map<String, Object> mapParams = CollectionUtil.newHashMap();
        mapParams.put("title", templateVo.getTitle());
        mapParams.put("author", templateVo.getAuthor());
        mapParams.put("templateId", templateVo.getId());
        mapParams.put("siteId", templateVo.getSiteId());
        processInstanceVo.setVariables(mapParams);
        workflowFeign.startInstance(processInstanceVo);
    }

    /**
     * 元数据转换
     *
     * @param collectionId
     * @param contentMapStr
     * @return
     */
    private Map<String, Object> dataConvert(String collectionId, String contentMapStr) {
        Map<String, Object> data = new HashMap<>();
        // 元数据映射
        Map<String, Metadata> metadataMap = getMetadataMapByField(collectionId);
        Map<String, Object> viewDataMap = JSONUtil.toBean(contentMapStr, Map.class);
        if (Objects.nonNull(viewDataMap)) {
            Iterator<String> keys = viewDataMap.keySet().iterator();
            while(keys.hasNext()) {
                String key = keys.next();
                Metadata md = metadataMap.get(key);
                String dataType = md.getDataType();
                Object value = viewDataMap.get(key);
                switch (dataType) {
                    case "int":
                        if (isObjectStringEmpty(value)) {
                            data.put(key, null);
                        } else {
                            data.put(key, Integer.parseInt(value.toString()));
                        }
                        break;
                    case "float":
                        if (isObjectStringEmpty(value)) {
                            data.put(key, null);
                        } else {
                            data.put(key, Float.parseFloat(value.toString()));
                        }
                        break;
                    case "date":
                        String controlType = md.getControlType();
                        switch (controlType) {
                            case "dateElement":
                                data.put(key, parseDate(value, "yyyy-MM-dd"));
                                break;
                            case "timeElement":
                                data.put(key, parseDate(value, "HH:mm:ss"));
                                break;
                            case "datetimeElement":
                                data.put(key, parseDate(value, "yyyy-MM-dd HH:mm:ss"));
                                break;
                            default:
                                data.put(key, value);
                        }
                        break;
                    default:
                        data.put(key, value);
                }

            }
        }
        return data;
    }

    private Date parseDate(Object date, String pattern) {
        if (isObjectStringEmpty(date))
            return null;
        try {
            String value = date.toString();
            if ("HH:mm:ss".equals(pattern)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd");
                value = tmp.format(new Date()) + " " + date.toString();
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    private Map<String, Metadata> getMetadataMapByField(String collectionId) {
        // 元数据集
        MetadataCollection collection = formOrderService.getCollectionById(collectionId);
        // 基础字段映射
        Map<String, Metadata> metadataMap = new HashMap<>();
        // 元数据
        List<Metadata> metadatas = formOrderService.getAllMetadataByByCollectionId(collectionId);
        if (CollectionUtil.isNotEmpty(metadatas)) {
            for (Metadata md : metadatas) {
                String key = collection.getMdPrefix() + md.getBriefName();
                md.setBriefName(key);
                metadataMap.put(key, md);
            }
        }
        return metadataMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(String ids) {
        List<Map> mapList = JSONUtil.toList(JSONUtil.parseArray(ids), Map.class);
        // 删除元数据
        List<Map> metaDataMapList = mapList.stream().filter(m -> ObjectUtil.isNotNull(m.get("contentId")) && ObjectUtil.isNotNull(m.get("metaDataCollectionId"))).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(metaDataMapList)) {
            adminFeign.removeMetadataByIds(metaDataMapList);
        }

        // 删除索引
        for (Map map : mapList) {
            Template template = new Template();
            template.setId((String) map.get("id"));
            template.setContentModelId((String) map.get("contentModelId"));
            this.addIndexTask(template, RabbitMQConstants.MQ_CMS_INDEX_COMMAND_DELETE);
        }

        List<String> idList = mapList.stream().map(m -> (String) m.get("id")).collect(Collectors.toList());
        return super.removeByIds(idList);
    }

    /**
     * 添加生成静态页面任务到队列
     * @param template
     */
    private void addStaticPageTask(Template template) {
        Template t = new Template();
        BeanUtil.copyProperties(template, t);
        rabbitmqTemplate.convertAndSend(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE, RabbitMQConstants.QUEUE_NAME_STATIC_PAGE_TASK, t);
    }

    /**
     * 添加索引任务到队列
     * @param template
     * @param code
     */
    private void addIndexTask(Template template, String code) {
        TemplateVo templateVo = new TemplateVo();
        BeanUtil.copyProperties(template, templateVo);
        templateVo.setCode(code);
        // 获取索引
        String index = modelService.getIndexByModelId(template.getContentModelId());
        templateVo.setIndex(index);
        rabbitmqTemplate.convertAndSend(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE, RabbitMQConstants.QUEUE_NAME_INDEX_TASK, templateVo);
    }

    /**
     * 判断Template对象标题是否存在
     *
     * @param template
     * @return
     */
    @Override
    public boolean checkTitleExist(Template template) {
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site_id", template.getSiteId())
                .eq("cms_catalog_id", template.getCmsCatalogId()).eq("title_", template.getTitle());
        if (StrUtil.isNotEmpty(template.getId())) {
            queryWrapper.ne("id_", template.getId());
        }
        return super.count(queryWrapper) > 0;
    }

    /**
     * 生成静态页
     *
     * @param templateVo
     * @return
     */
    @Override
    public boolean genStaticPage(TemplateVo templateVo) {
        Collection<Template> templateList = this.getTemplateList(templateVo);
        if (CollectionUtil.isNotEmpty(templateList)) {
            for (Template template : templateList) {
                // 添加任务，发送MQ消息 TODO
                try {
                    this.addStaticPageTask(template);
                } catch (Exception e) {
                    log.error("生成内容静态页出错", e);
                }
            }
        }
        return true;
    }

    private Collection<Template> getTemplateList(TemplateVo templateVo) {
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        // 内容为已发布状态
//        queryWrapper.eq("status_", 2);
        if (StrUtil.isNotEmpty(templateVo.getSiteId())) {
            queryWrapper.eq("site_id", templateVo.getSiteId());
        }
        if (StrUtil.isNotEmpty(templateVo.getCmsCatalogId())) {
            // 查询当前栏目及子栏目id
            QueryWrapper<Catalog> catalogQueryWrapper = new QueryWrapper<>();
            catalogQueryWrapper.select("id_").like("tree_position", templateVo.getCmsCatalogId())
                    .or().eq("id_", templateVo.getCmsCatalogId());
            List<Catalog> catalogList = catalogService.list(catalogQueryWrapper);
            List<String> catalogIds = catalogList.stream().map(Catalog::getId).collect(Collectors.toList());
            queryWrapper.in("cms_catalog_id", catalogIds);
        }
        if (StrUtil.isNotEmpty(templateVo.getIds())) {
            queryWrapper.in("id_", Arrays.asList(templateVo.getIds().split(",")));
        }
        Collection<Template> templateList = super.list(queryWrapper);
        return templateList;
    }
    /**
     * 生成索引
     *
     * @param templateVo
     * @return
     */
    @Override
    public boolean reindex(TemplateVo templateVo) {
        Collection<Template> templateList = this.getTemplateList(templateVo);
        if (CollectionUtil.isNotEmpty(templateList)) {
            for (Template template : templateList) {
                // 添加任务，发送MQ消息 TODO
                try {
                    this.addIndexTask(template, RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE);
                } catch (Exception e) {
                    log.error("生成索引出错", e);
                }
            }
        }
        return true;
    }

    /**
     * 删除索引数据
     *
     * @param template
     * @return
     */
    @Override
    public boolean removeIndexData(Template template) {
        Collection<Template> templateList = this.listByBean(template);
        if (CollectionUtil.isNotEmpty(templateList)) {
            for (Template t : templateList) {
                // 添加任务，发送MQ消息
                try {
                    this.addIndexTask(t, RabbitMQConstants.MQ_CMS_INDEX_COMMAND_DELETE);
                } catch (Exception e) {
                    log.error("删除索引数据出错", e);
                }
            }
        }
        return true;
    }

    /**
     * 分页查询
     * @param entity
     * @return
     */
    @Override
    public IPage<TemplateVo> pageByTemplate(Template entity) {
        // 检索栏目ID
        List<String> catalogIdList = new ArrayList<>();
        String userId = UserContextHelper.getUserId();
        RestResult<List<String>> roleResult =  adminFeign.getRoleIdsByUserId(userId);
        List<String> roleIds = roleResult.getData();
        if (CollectionUtil.isEmpty(roleIds)) {
            throw new BusinessException( HttpStatus.HTTP_INTERNAL_ERROR, "没有分配角色");
        }
        QueryWrapper<Catalog> countQueryWrapper = new QueryWrapper<>();
        countQueryWrapper.eq("parent_id", entity.getCmsCatalogId());
        int count = catalogService.count(countQueryWrapper);
        // 有子栏目
        if (count > 0) {
            QueryWrapper<CatalogRole> queryWrapperCatalogRole = new QueryWrapper<>();
            queryWrapperCatalogRole.in("role_id", roleIds);
            Collection<CatalogRole> roleCatalogs = catalogRoleService.list(queryWrapperCatalogRole);
            if (CollectionUtil.isNotEmpty(roleCatalogs)) {
                // 用户分配的栏目ID
                List<String> userCatalogIds = roleCatalogs.stream().map(CatalogRole::getCatalogId).collect(Collectors.toList());
                // 检索当前栏目的所有子栏目
                Collection<Catalog> allCatalogList = catalogService.list();
                List<String> childrenCatalogIds = new ArrayList<>();
                getChildrenCatalogId(entity.getCmsCatalogId(), allCatalogList, childrenCatalogIds);
                // 有权限的子栏目ID
                catalogIdList = childrenCatalogIds.stream().filter(c -> userCatalogIds.contains(c)).collect(Collectors.toList());
            }
        }
        catalogIdList.add(entity.getCmsCatalogId());

        // 检索用户ID
        List<String> userIdList = new ArrayList<>();
        QueryWrapper<TemplateRoleAuthority> queryWrapperTemplateRoleAuthority = new QueryWrapper<>();
        queryWrapperTemplateRoleAuthority.in("role_id", roleIds);
        Collection<TemplateRoleAuthority> templateRoleAuthoritys = templateRoleAuthorityService.list(queryWrapperTemplateRoleAuthority);

        String authority = "";
        if (CollectionUtil.isNotEmpty(templateRoleAuthoritys)) {
            long allCount = templateRoleAuthoritys.stream().filter( a -> TemplateAuthorityEnum.ALL.getCode().equals(a.getAuthority())).count();
            if (allCount > 0) {
                authority = TemplateAuthorityEnum.ALL.getCode();
            } else {
                long departMentCount = templateRoleAuthoritys.stream().filter( a -> TemplateAuthorityEnum.DEPARTMENT.getCode().equals(a.getAuthority())).count();
                if (departMentCount > 0) {
                    authority = TemplateAuthorityEnum.DEPARTMENT.getCode();
                } else {
                    long userCount = templateRoleAuthoritys.stream().filter( a -> TemplateAuthorityEnum.USER.getCode().equals(a.getAuthority())).count();
                    if (userCount > 0) {
                        authority = TemplateAuthorityEnum.USER.getCode();
                    } else {
                        throw new BusinessException( HttpStatus.HTTP_INTERNAL_ERROR, "没有内容权限");
                    }
                }
            }
        }
        if (TemplateAuthorityEnum.ALL.getCode().equals(authority)) {
        } else if (TemplateAuthorityEnum.DEPARTMENT.getCode().equals(authority)) {
            RestResult<List<User>> result = adminFeign.getAllUserIdInUserDepartment(userId);
            List<User> users = result.getData();
            if (CollectionUtil.isNotEmpty(users)) {
                userIdList = users.stream().map(User::getId).collect(Collectors.toList());
            }
        } else if (TemplateAuthorityEnum.USER.getCode().equals(authority)) {
            userIdList.add(userId);
        }
        return baseMapper.pageByTemplate(getPageByBean(entity), entity, catalogIdList, userIdList);
    }

    private void getChildrenCatalogId(String catalogId, Collection<Catalog> allCatalogList, List<String> childrenCatalogIds) {
        if (StrUtil.isEmpty(catalogId) || CollectionUtil.isEmpty(allCatalogList) || childrenCatalogIds == null) return;
        for (Catalog c : allCatalogList) {
            if (catalogId.equals(c.getParentId())) {
                childrenCatalogIds.add(c.getId());
                getChildrenCatalogId(c.getId(), allCatalogList, childrenCatalogIds);
            }
        }
    }





    /************************************************************************************************
     *
     *                                      以下为网站前台调用接口
     *
     * **********************************************************************************************/
    /**
     * 分页查询
     * @param maps
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public IPage<TemplateVo> getTemplateListView(Map<String, Object> maps, Integer page, Integer pageSize) {
        Page<Template> pages = getPageByBean(new Template());
        pages.setCurrent(page);
        pages.setSize(pageSize);
        if(maps.containsKey("catId")){
            List<String> cmsCatalogId = new ArrayList<>();
            String catIds = maps.get("catId").toString();
            String[] temp = catIds.split(",");
            for(String catId:temp){
                Collection<CatalogVo> catalogVos = getCatalogChildrenTree(maps.get("siteId").toString(),catId);
                cmsCatalogId.add(catId);
                cmsCatalogId = getCatalogChildrenIds(catalogVos,cmsCatalogId);
            }
            System.out.println(cmsCatalogId);
            maps.put("cmsCatalogId",cmsCatalogId);
        }
        return baseMapper.getTemplateListView(pages,maps);
    }

    @Override
    public Map<String, Object> search(Map<String, Object> map) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //关键字搜索的表单域
        String[] fields = {"title","resourceSummary","resourceContent"};
        String searchFields = map.get("scope") == null ? "" : map.get("scope").toString();
        if (StrUtil.isNotBlank(searchFields)) {
            fields = searchFields.split(",");
        }
        String keywords = map.get("keywords") == null ? "" : map.get("keywords").toString();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keywords, fields);
        multiMatchQueryBuilder.analyzer("ik_smart");
        multiMatchQueryBuilder.field("title", 2f);

        //站点id
        String siteId = map.get("siteId") == null ? "" : map.get("siteId").toString();
        if (StrUtil.isNotBlank(siteId)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("siteId", siteId));
        }
        //栏目
        String catalogId = map.get("catalogId") == null ? "" : map.get("catalogId").toString();
        if (StrUtil.isNotBlank(catalogId)) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("cmsCatalogId", catalogId.split(",")));
        }
        //关键字
        boolQueryBuilder.must(multiMatchQueryBuilder);

        String timeMin = map.get("timeMin") == null ? "" : map.get("timeMin").toString();
        String timeMax = map.get("timeMax") == null ? "" : map.get("timeMax").toString();
        if (StrUtil.isNotBlank(timeMin) || StrUtil.isNotBlank(timeMax)) {
            // 时间范围
            String timeField = map.get("timeField") == null ? "resourcePublicationDate" : map.get("timeField").toString();
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(timeField);
            if (StrUtil.isNotBlank(timeMin)) {
                rangeQueryBuilder.gte(timeMin);
            }
            if (StrUtil.isNotBlank(timeMax)) {
                rangeQueryBuilder.lt(timeMax);
            }
            boolQueryBuilder.must(rangeQueryBuilder);
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //高亮显示
        searchSourceBuilder.highlighter(new HighlightBuilder()
                .field("title", 800000, 0)
                .field("resourceContent", 800000, 0)
                .field("resourceSummary" , 800000, 0));
        searchSourceBuilder.query(boolQueryBuilder);

        //排序
        SortOrder sortOrder = SortOrder.DESC;
        String sortBy = map.get("sortBy") == null ? "resourcePublicationDate" : map.get("sortBy").toString();

        Integer page = map.get("page") == null ? 1 : Integer.parseInt(map.get("page").toString());
        Integer size = map.get("size") == null ? 10 : Integer.parseInt(map.get("size").toString());
        searchSourceBuilder.sort(sortBy, sortOrder);
        searchSourceBuilder.from((page - 1) * size);
        searchSourceBuilder.size(size);
        String query = searchSourceBuilder.toString();
        log.info("搜索：" + query);
        String result = indexService.selectDataByESQueryJSON("cms_*", query);
        log.info("搜索结果：" + result);
        JSONObject jsonNode = JSONUtil.parseObj(result);
        if (jsonNode == null) {
            //搜索失败
            log.error("搜索失败");
            throw new RuntimeException("搜索失败");
        }
        //用时
        int took = jsonNode.getInt("took");
        //是否超时
        boolean timed_out = jsonNode.getBool("timed_out");
        JSONObject hits = jsonNode.getJSONObject("hits");
        int totalElements = hits.getInt("total");
        int totalPages = ((totalElements + size - 1) / size);
        //结果集
        List<Map<String, Object>> records = new ArrayList<>();
        hits.getJSONArray("hits").forEach(hit -> {
            JSONObject source = JSONUtil.parseObj(hit).getJSONObject("_source");
            for (Map.Entry<String, Object> entry : source.entrySet()) {
                String value = source.getStr(entry.getKey());
                if(StrUtil.isBlank(value)){
                    source.put(entry.getKey(),"");
                }
            }
            JSONObject highlight = JSONUtil.parseObj(hit).getJSONObject("highlight");
            highlight.entrySet().forEach((field) -> {
                JSONArray obj = JSONUtil.parseArray(field.getValue());
                source.put(field.getKey(),obj.get(0).toString());
            });
            records.add(source);
        });
        map.put("records", records);
        map.put("timedOut", timed_out);
        map.put("took", took);
        map.put("total", totalElements);
        map.put("pages", totalPages);
        map.put("prevPage", page > 1 ? page - 1 : 1);
        map.put("nextPage", page < totalPages ? page + 1 : page);
        return map;
    }



    /**
     * 根据条件获取站点栏目
     * @param siteId 站点ID
     * @param parentId 父节点ID
     * @return
     */
    public Collection<CatalogVo> getCatalogChildrenTree(String siteId,String parentId) {
        Collection<CatalogVo> catalogVoCollection = siteCache.getCatalogTreeBySiteId(siteId);
        Collection<CatalogVo> reslut = CollectionUtil.newArrayList();
        return getCatalogChildrenTree(catalogVoCollection,reslut,parentId);
    }
    public Collection<CatalogVo> getCatalogChildrenTree(Collection<CatalogVo> catalogVoCollection,Collection<CatalogVo> reslut,String parentId){
        if(parentId.equals("0")){
            return catalogVoCollection;
        }
        for(CatalogVo catalogVo:catalogVoCollection){
            if(catalogVo.getId().equals(parentId)){
                reslut = catalogVo.getChildren();
                break;
            }else if(CollectionUtil.isNotEmpty(catalogVo.getChildren())){
                reslut = getCatalogChildrenTree(catalogVo.getChildren(),reslut,parentId);
            }
        }
        return reslut;
    }
    public List<String> getCatalogChildrenIds(Collection<CatalogVo> catalogVos,List<String> ids){
        if(CollectionUtil.isNotEmpty(catalogVos)){
            for(CatalogVo catalogVo:catalogVos){
                ids.add(catalogVo.getId());
                if(CollectionUtil.isNotEmpty(catalogVo.getChildren())){
                    ids = getCatalogChildrenIds(catalogVo.getChildren(),ids);
                }
            }
        }
        return ids;
    }


    /**
     * 统计栏目下的内容
     *
     * @param catalogId
     * @return
     */
    @Override
    public int countTemplateByCatalogId(String catalogId) {
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cms_catalog_id", catalogId);
        return super.count(queryWrapper);
    }

    /**
     * 检索站点下的内容
     * @param siteId
     * @param start
     * @param end
     * @param part
     * @param number
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String resetTemplateIndexCode(String siteId, String start, String end, String part, int number) {
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id_");
        queryWrapper.eq("site_id", siteId);
        if (StrUtil.isNotEmpty(start) && StrUtil.isNotEmpty(end)) {
            queryWrapper.between("DATE_FORMAT(resource_publication_date, '%Y-%m-%d')", start, end);
        }
        queryWrapper.orderByAsc("resource_publication_date");
        int total = super.count(queryWrapper);
        if (total == 0) {
            return "0";
        }
        final int SIZE = 1000;
        int totalPage = total / SIZE;
        if (total % SIZE != 0) {
            totalPage += 1;
        }
        boolean result = true;
        StringBuilder format = new StringBuilder("%0");
        format.append(number);
        format.append("d");
        int value = 1;
        Page page = new Page();
        page.setSize(SIZE);
        for (int p = 1; p <= totalPage; p++) {
            page.setCurrent(p);
            IPage<Template> resultPage = super.page(page, queryWrapper);
            List<Template> list = resultPage.getRecords();
            if (CollectionUtil.isNotEmpty(list)) {
                for (Template t : list) {
                    StringBuilder indexCode = new StringBuilder();
                    if (StrUtil.isNotEmpty(part)) {
                        indexCode.append(part);
                    }
                    indexCode.append(String.format(format.toString(), value++));
                    t.setIndexCode(indexCode.toString());
                }
                result &= super.updateBatchById(list);
            }
        }
        if (result) {
            return String.valueOf(total);
        } else {
            return "0";
        }
    }

    /**
     * 更新信息状态
     *
     * @param ids
     * @param status
     * @return
     */
    public int updateStatusByIds(List<String> ids, int status) {
        return baseMapper.updateStatusByIds(ids, status);
    }

    /**
     * 更新权重
     *
     * @param sortNo
     * @param id
     * @return
     */
    public int updateSortNoById(int sortNo, String id) {return baseMapper.updateSortNoById(sortNo, id);}

    /**
     * 更新置顶
     *
     * @param flagTop
     * @param id
     * @return
     */
    public int updateFlagTopById(boolean flagTop, String id) {return baseMapper.updateFlagTopById(flagTop, id);}



}
