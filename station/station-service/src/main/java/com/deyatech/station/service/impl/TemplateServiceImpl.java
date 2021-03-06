package com.deyatech.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.entity.Metadata;
import com.deyatech.admin.entity.MetadataCollection;
import com.deyatech.admin.entity.User;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.*;
import com.deyatech.assembly.feign.AssemblyFeign;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.*;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.common.utils.ColumnUtil;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.*;
import com.deyatech.station.index.IndexService;
import com.deyatech.station.mapper.TemplateMapper;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.service.*;
import com.deyatech.station.vo.*;
import com.deyatech.template.feign.TemplateFeign;
import com.deyatech.workflow.feign.WorkflowFeign;
import com.deyatech.workflow.vo.ProcessInstanceVo;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
public class TemplateServiceImpl extends BaseServiceImpl<TemplateMapper, Template> implements TemplateService{
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
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
    PageService pageService;
    @Autowired
    TemplateFeign templateFeign;
    @Autowired
    CatalogTemplateService catalogTemplateService;
    @Autowired
    CatalogAggregationService catalogAggregationService;

    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 100, 10, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

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
            // 定时标记补入
            metadataFieldDataMap.put("timing_", template.getTiming());
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
            pageModel.put(md.getBriefName(), isObjectStringEmpty(value) ? "" : value);
            // 绑定数组
            if (isObjectStringEmpty(value)) {
                pageModel.put("image_" + md.getBriefName(), new ArrayList<>());
            } else {
                pageModel.put("image_" + md.getBriefName(), materialService.getDisplayMaterialsByUrl(value.toString()));
            }
        }
        // 组图 image array
        else if("imageArrayElement".equals(md.getControlType()) ){
            pageModel.put(md.getBriefName(), isObjectStringEmpty(value) ? "" : value);
            if(ObjectUtil.isNotNull(value)){
                JSONArray array = JSONUtil.parseArray(value);
                String urls = "";
                Map<String,String> map = new HashMap<>();
                for(Object obj:array){
                    String url = JSONUtil.parseObj(obj).get("url").toString();
                    map.put(url,JSONUtil.parseObj(obj).get("remark").toString());
                    urls+="," + url;
                }
                urls = urls.substring(1);
                List<MaterialVo> materialVos = materialService.getDisplayMaterialsByUrl(urls);
                List<MaterialVo> materialSortVos = CollectionUtil.newArrayList();
                if(CollectionUtil.isNotEmpty(materialVos)){
                    for(MaterialVo materialVo:materialVos){
                        materialVo.setName(map.get(materialVo.getValue()));
                    }
                    String arrayUrls[] = urls.split(",");
                    for(String str:arrayUrls){
                        for(MaterialVo materialVo:materialVos){
                            if(str.equals(materialVo.getValue())){
                                materialSortVos.add(materialVo);
                            }
                        }
                    }
                }
                pageModel.put("imagearray_" + md.getBriefName(), materialSortVos);
            }else{
                pageModel.put("imagearray_" + md.getBriefName(), new ArrayList<>());
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
            // 本次有发布时间时,补入定时标记
            if ("resource_publication_date".equals(md.getBriefName())) {
                pageModel.put("timing_", metadataFieldDataMap.get("timing_"));
            }
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
        Map<String, String> departmentNameMap = this.getDepartmentIdNameMap();
        templateVo.setSourceName(departmentNameMap.get(templateVo.getSource()) == null ? templateVo.getSource() : departmentNameMap.get(templateVo.getSource()));
        // 查询元数据结构及数据
        this.queryMetadata(templateVo);
        // 查询模型模板
        this.queryModelTemplate(templateVo);
        return templateVo;
    }

    /**
     * 获取部门编号名称映射
     *
     * @return
     */
    @Override
    public Map<String, String> getDepartmentIdNameMap() {
        List<Department> departmentList = adminFeign.getAllDepartments().getData();
        Map<String, String> departmentNameMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(departmentList)) {
            departmentNameMap = departmentList.stream().collect(Collectors.toMap(Department::getId, Department::getName));
        }
        return departmentNameMap;
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
                if(ObjectUtil.isNotNull(contentMapDB)){
                    checkUrl((String) contentMapDB.get(field), (String) contentMap.get(field), oldUrlList, newUrlList);
                }
            }
            // 新增
        } else {
            for (String field : fileImageFields) {
                checkUrl(null, (String) contentMap.get(field), oldUrlList, newUrlList);
            }
        }
    }

    @Override
    //@Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateTemplateVo(TemplateVo templateVo) {
//        if (this.checkTitleExist(templateVo)) {
//            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "当前栏目中已存在该标题内容");
//        }
        // 外链
        if (YesNoEnum.YES.getCode() == templateVo.getFlagExternal()) {
            if (StrUtil.isEmpty(templateVo.getUrl())) {
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "外链内容必须填写URL");
            }
        }
        // 获取栏目信息
        Catalog catalog = catalogService.getById(templateVo.getCmsCatalogId());
        if (Objects.isNull(catalog)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "栏目信息不存在");
        }
        boolean isNeedAggregation = false;
        List<String> oldUrlList = new ArrayList<>();
        List<String> newUrlList = new ArrayList<>();
        boolean hasId = StrUtil.isNotBlank(templateVo.getId());
        // 更新
        if (hasId) {
            isNeedAggregation = this.isNeedAggregation(templateVo);
            Template templateDB = super.getById(templateVo.getId());
            // 标记材料
            checkUrl(templateDB.getThumbnail(), templateVo.getThumbnail(), oldUrlList, newUrlList);
            // 新增
        } else {
            isNeedAggregation = true;
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
                // 如果当前栏目绑定了工作流 并且 有流程ID
                if (YesNoEnum.YES.getCode() == catalog.getWorkflowEnable() && StrUtil.isNotEmpty(catalog.getWorkflowId())) {
                    // 审核中
                    templateVo.setStatus(ContentStatusEnum.VERIFY.getCode());
                    templateVo.setTiming(YesNoEnum.NO.getCode());
                } else {
                    // 定时发布
                    if (YesNoEnum.YES.getCode() == templateVo.getTiming()) {
                        templateVo.setStatus(ContentStatusEnum.TIMING.getCode());
                    } else {
                        // 已发布
                        templateVo.setStatus(ContentStatusEnum.PUBLISH.getCode());
                    }
                }
            }
            // 编辑时原则上不改变数据状态
            else {
                // 若把已发布改为定时
                if (ContentStatusEnum.PUBLISH.getCode() == templateVo.getStatus() && YesNoEnum.YES.getCode() == templateVo.getTiming()) {
                    templateVo.setStatus(ContentStatusEnum.TIMING.getCode());
                    // 再聚合
                    isNeedAggregation = true;
                    // 删除聚合关联关系
                    catalogTemplateService.deleteAggregationByTemplateId(templateVo.getId());
                    // 删除静态页和索引
                    sendPageAndIndexHandleMessage("delete", Arrays.asList(new String[]{templateVo.getId()}));
                }
            }
        }
        // 新增时生成索引码
        if (!hasId) {
            RestResult<String> resultIndexCode = assemblyFeign.getNextIndexCodeBySiteId(templateVo.getSiteId());
            // 索引编码
            templateVo.setIndexCode(resultIndexCode.getData());
            templateVo.setSortNo(1);
        }
        // 保存内容
        boolean result = super.saveOrUpdate(templateVo);
        if (result) {
            // 非外链
            if (YesNoEnum.NO.getCode() == templateVo.getFlagExternal()) {
                String newUrl = "/" + catalog.getPathName() + "/" + templateVo.getId() + ".html";
                // 路径变更时
                if (!newUrl.equals(templateVo.getUrl())) {
                    // 根据主键ID命名 静态资源文件URL
                    templateVo.setUrl(newUrl);
                    super.updateById(templateVo);
                }
            }
            // 新增时
            if (!hasId) {
                // 保存栏目内容关联表
                CatalogTemplate catalogTemplate = new CatalogTemplate();
                catalogTemplate.setCatalogId(templateVo.getCmsCatalogId());
                catalogTemplate.setTemplateId(templateVo.getId());
                catalogTemplate.setOriginType(ContentOriginTypeEnum.ADD.getCode());
                catalogTemplateService.saveOrUpdate(catalogTemplate);
                // 如果当前栏目绑定了工作流 并且 有流程ID
                if(YesNoEnum.YES.getCode() == catalog.getWorkflowEnable() && StrUtil.isNotEmpty(catalog.getWorkflowId())){
                    startWorkflow(templateVo);
                }
            }
            // 发布状态
            if (ContentStatusEnum.PUBLISH.getCode() == templateVo.getStatus()) {
                // 清除前台模板缓存
                cacheCatalogList(catalog.getId());
                // 生成静态页面任务
                TemplateVo templateVo1 = new TemplateVo();
                templateVo1.setIds(templateVo.getId());
                this.genStaticPage(templateVo1,RabbitMQConstants.MQ_CMS_INDEX_COMMAND_ADD);
                // 默认都创建索引, 索引任务
                this.reindex(templateVo1, hasId ? RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE : RabbitMQConstants.MQ_CMS_INDEX_COMMAND_ADD);
                //发布新闻所属栏目关联的页面静态页
                pageService.replyPageByCatalog(templateVo.getCmsCatalogId());
            }
            Runnable runnable = () -> {
                // 标记材料
                materialService.markMaterialUsePlace(oldUrlList, newUrlList, MaterialUsePlaceEnum.STATION_TEMPLATE.getCode());
            };
            executor.execute(runnable);

            // 发布状态 且 相关数据变更
            if (ContentStatusEnum.PUBLISH.getCode() == templateVo.getStatus() && isNeedAggregation) {
                // 发送聚合消息
                this.sendAggregationTemplateHandleMessage(templateVo.getId());
            }
        }
        return result;
    }


    /**
     * 是否需要应用聚合
     * 新增或者和聚合有关的数据变更时需要
     *
     * @param templateVo
     * @return
     */
    private boolean isNeedAggregation(TemplateVo templateVo) {
        if (StrUtil.isNotEmpty(templateVo.getId())) {
            Template oldTemplate = super.getById(templateVo.getId());

            String catalogId = Objects.isNull(templateVo.getCmsCatalogId()) ? "" : templateVo.getCmsCatalogId();
            // 栏目变更
            if (!catalogId.equals(oldTemplate.getCmsCatalogId())) {
                return true;
            }

            List<String> keyList = null;
            if (StrUtil.isNotEmpty(templateVo.getKeyword())) {
                keyList = Arrays.asList(templateVo.getKeyword().split(","));
            }
            List<String> oldKeyList = null;
            if (StrUtil.isNotEmpty(oldTemplate.getKeyword())) {
                oldKeyList = Arrays.asList(oldTemplate.getKeyword().split(","));
            }
            // 关键字变更
            if (!listEquals(keyList, oldKeyList)) {
                return true;
            }

            String source = Objects.isNull(templateVo.getSource()) ? "" : templateVo.getSource();
            // 发布机构变更
            if (!source.equals(oldTemplate.getSource())) {
                return true;
            }

            // 发布时间段
            String time = DateUtil.format(templateVo.getResourcePublicationDate(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
            String oldTime = DateUtil.format(oldTemplate.getResourcePublicationDate(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
            if (!time.equals(oldTime)) {
                return true;
            }

            // 发布人 不能变更不用检查
        }
        return false;
    }

    private boolean listEquals(List<String> newList, List<String> oldList) {
        if ((newList == null && oldList != null) || (newList != null && oldList == null)) {
            return false;
        }
        if (newList == null && oldList == null)
            return true;
        if (newList.size() == oldList.size() && newList.containsAll(oldList) && oldList.containsAll(newList))
            return true;
        return false;
    }

    /**
     * 启动工作流
     *
     * @param templateVo
     */
    @Override
    public void startWorkflow(TemplateVo templateVo) {
        // 启动工作流
        ProcessInstanceVo processInstanceVo = new ProcessInstanceVo();
        processInstanceVo.setActDefinitionKey(templateVo.getWorkflowKey());
        processInstanceVo.setActDefinitionId(templateVo.getWorkflowId());
        processInstanceVo.setSource("CMS");
        processInstanceVo.setUserId(UserContextHelper.getUserId());
        // 业务ID
        processInstanceVo.setBusinessId(templateVo.getId());
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
     * 内容列表页面 生成静态页
     *
     * @param templateVo
     * @return
     */
    @Override
    public void genStaticPage(TemplateVo templateVo,String messageCode) {
        Map<String,Object> maps = new HashMap<>();
        if (StrUtil.isNotEmpty(templateVo.getSiteId())) {
            maps.put("siteId", templateVo.getSiteId());
        }
        if (StrUtil.isNotEmpty(templateVo.getCmsCatalogId())) {
            // 查询当前栏目及子栏目id
            QueryWrapper<Catalog> catalogQueryWrapper = new QueryWrapper<>();
            catalogQueryWrapper.select("id_").like("tree_position", templateVo.getCmsCatalogId())
                    .or().eq("id_", templateVo.getCmsCatalogId());
            List<Catalog> catalogList = catalogService.list(catalogQueryWrapper);
            List<String> catalogIds = catalogList.stream().map(Catalog::getId).collect(Collectors.toList());
            maps.put("cmsCatalogId", catalogIds);
        }
        if (StrUtil.isNotEmpty(templateVo.getIds())) {
            maps.put("id", Arrays.asList(templateVo.getIds().split(",")));
        }
        IPage<TemplateVo> templates = getTemplateListView(maps,1,1);
        if(templates.getPages() > 0){
            maps.put("totle",templates.getTotal());
            this.addStaticPageTask(maps, messageCode);
        }
    }
    /**
     * 添加生成静态页面任务到队列--带进度条
     * @param maps
     */
    @Override
    public void addStaticPageTask(Map<String,Object> maps,String messageCode) {
        log.info(String.format("新增发布静态页任务：%s", JSONUtil.toJsonStr(maps)));
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("maps",maps);
        dataMap.put("messageCode",messageCode);
        rabbitmqTemplate.convertAndSend(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE, RabbitMQConstants.QUEUE_NAME_STATIC_PAGE_TASK, dataMap);
    }
    /**
     * 生成索引
     *
     * @param templateVo
     * @return
     */
    @Override
    public boolean reindex(TemplateVo templateVo,String messageCode) {
        Map<String,Object> maps = new HashMap<>();
        if (StrUtil.isNotEmpty(templateVo.getSiteId())) {
            maps.put("siteId", templateVo.getSiteId());
        }
        if (StrUtil.isNotEmpty(templateVo.getCmsCatalogId())) {
            // 查询当前栏目及子栏目id
            QueryWrapper<Catalog> catalogQueryWrapper = new QueryWrapper<>();
            catalogQueryWrapper.select("id_").like("tree_position", templateVo.getCmsCatalogId())
                    .or().eq("id_", templateVo.getCmsCatalogId());
            List<Catalog> catalogList = catalogService.list(catalogQueryWrapper);
            List<String> catalogIds = catalogList.stream().map(Catalog::getId).collect(Collectors.toList());
            maps.put("cmsCatalogId", catalogIds);
        }
        if (StrUtil.isNotEmpty(templateVo.getIds())) {
            maps.put("id", Arrays.asList(templateVo.getIds().split(",")));
        }
        IPage<TemplateVo> templates = getTemplateListView(maps,1,1);
        if(templates.getPages() > 0){
            maps.put("totle",templates.getTotal());
            this.addIndexTask(maps, messageCode);
        }
        return true;
    }

    @Override
    public void cacheCatalogList(String catId) {
        rabbitmqTemplate.convertAndSend(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE, RabbitMQConstants.QUEUE_NAME_LIST_PAGE_TASK, catId);
    }

    /**
     * 添加索引任务到队列
     * @param maps
     * @param messageCode
     */
    private void addIndexTask(Map<String,Object> maps,String messageCode) {
        log.info(String.format("新增生成索引任务：%s", JSONUtil.toJsonStr(maps)));
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("maps",maps);
        dataMap.put("messageCode",messageCode);
        rabbitmqTemplate.convertAndSend(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE, RabbitMQConstants.QUEUE_NAME_INDEX_TASK, dataMap);
    }

    /**
     * 内容审核通过
     *
     * @param template
     * @return
     */
    @Override
    public boolean contentFinish(Template template) {
        log.info(String.format("内容审核通过: %s ", JSONUtil.toJsonStr(template)));
        // 修改状态为发布
        template.setStatus(ContentStatusEnum.PUBLISH.getCode());
        boolean result = this.updateById(template);
        if (result) {
            TemplateVo templateVo = new TemplateVo();
            BeanUtil.copyProperties(super.getById(template.getId()), templateVo);
            this.addPageAndIndexById(Arrays.asList(new String[]{templateVo.getId()}));
        }
        return result;
    }

    /**
     * 内容审核拒绝
     *
     * @param template
     * @return
     */
    @Override
    public boolean contentReject(Template template) {
        log.info(String.format("内容审核拒绝: %s ", JSONUtil.toJsonStr(template)));
        Template entity = new Template();
        entity.setId(template.getId());
        entity.setStatus(ContentStatusEnum.REJECT.getCode());
        entity.setReason(template.getReason());
        return this.updateById(entity);
    }

    /**
     * 彻底删除
     * 回收站的可以彻底删除
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(String ids) {
        List<Map> mapList = JSONUtil.toList(JSONUtil.parseArray(ids), Map.class);
        // 删除元数据
        List<Map> metaDataMapList = mapList.stream().filter(m -> ObjectUtil.isNotNull(m.get("contentId")) && ObjectUtil.isNotNull(m.get("metaDataCollectionId"))).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(metaDataMapList)) {
            adminFeign.removeMetadataByIds(metaDataMapList);
        }
        List<String> idList = mapList.stream().map(m -> (String) m.get("id")).collect(Collectors.toList());
        List<String> publishIds = new ArrayList<>();
        List<String> verifyIds = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(idList)) {
            for (String id : idList) {
                Template template = super.getById(id);
                // 待审被删除
                if (ContentStatusEnum.VERIFY.getCode() == template.getStatus()) {
                    verifyIds.add(id);
                }
                // 已发布被删除
                if (ContentStatusEnum.PUBLISH.getCode() == template.getStatus()) {
                    publishIds.add(id);
                }
            }
        }
        boolean flag = super.removeByIds(idList);
        if (flag) {
            // 删除和内容相关的所有关系
            QueryWrapper<CatalogTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("template_id", idList);
            catalogTemplateService.remove(queryWrapper);
            for (String id : verifyIds) {
                // 删除流程
                workflowFeign.deleteInstanceByBusinessId(id, "删除内容");
            }
            // 删除静态页和索引
            sendPageAndIndexHandleMessage("delete", publishIds);
        }
        return flag;
    }

    /**
     * 删除内容到回收站
     * 除彻底删除外其他状态都可以删除到回收站
     *
     * @param ids
     * @return
     */
    @Override
    public boolean recycleByIds(List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            List<String> publishIds = new ArrayList<>();
            for (String id : ids) {
                Template template = super.getById(id);
                int status = template.getStatus();
                // 还原用状态保存
                template.setOriginalStatus(status);
                template.setStatus(ContentStatusEnum.RECYCLE.getCode());
                boolean result = super.updateById(template);
                if (result) {
                    // 待审被删除到回收站
                    if (ContentStatusEnum.VERIFY.getCode() == status) {
                        // 删除流程
                        workflowFeign.deleteInstanceByBusinessId(template.getId(), "删除内容");
                    }
                    // 已发布被删除到回收站
                    if (ContentStatusEnum.PUBLISH.getCode() == status) {
                        publishIds.add(id);
                        // 删除聚合关联关系
                        catalogTemplateService.deleteAggregationByTemplateId(id);
                    }
                }
            }
            // 删除静态页和索引
            sendPageAndIndexHandleMessage("delete", publishIds);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从回收站还原内容
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean backByIds(List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            List<Template> list = new ArrayList<>();
            for (String id : ids) {
                Template template = super.getById(id);
                // 非回收站状态
                if (ContentStatusEnum.RECYCLE.getCode() != template.getStatus()) {
                    throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "仅回收站的可以还原");
                }
                list.add(template);
            }
            List<String> publishIds = new ArrayList<>();
            for (Template template : list) {
                Integer originalStatus = template.getOriginalStatus();
                Catalog catalog = catalogService.getById(template.getCmsCatalogId());
                // 栏目有工作流
                if(YesNoEnum.YES.getCode() == catalog.getWorkflowEnable() && StrUtil.isNotEmpty(catalog.getWorkflowId())) {
                    // 还原到发布状态 或者 待审状态
                    if (ContentStatusEnum.PUBLISH.getCode() == originalStatus || ContentStatusEnum.VERIFY.getCode() == originalStatus) {
                        // 待审
                        template.setOriginalStatus(ContentStatusEnum.VERIFY.getCode());
                        template.setStatus(ContentStatusEnum.VERIFY.getCode());
                    } else {
                        // 还原状态
                        template.setStatus(template.getOriginalStatus());
                    }
                } else {
                    // 还原状态
                    template.setStatus(template.getOriginalStatus());
                }
                boolean result = super.updateById(template);
                if (result) {
                    // 待审状态
                    if (ContentStatusEnum.VERIFY.getCode() == template.getStatus()) {
                        TemplateVo templateVo = new TemplateVo();
                        BeanUtil.copyProperties(template, templateVo);
                        // 启动工作流
                        this.startWorkflow(templateVo);
                    }
                    // 发布
                    if (ContentStatusEnum.PUBLISH.getCode() == template.getStatus()) {
                        publishIds.add(template.getId());
                    }
                }
            }
            // 添加静态页和索引
            sendPageAndIndexHandleMessage("add", publishIds);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 送审
     * 来源：已撤销的送审、已驳回的送审
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean verifyByIds(List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            List<Template> list = new ArrayList<>();
            for (String id : ids) {
                Template template = super.getById(id);
                // 检索内容所属栏目
                Catalog catalog = catalogService.getById(template.getCmsCatalogId());
                // 栏目有工作流
                if(YesNoEnum.YES.getCode() == catalog.getWorkflowEnable() && StrUtil.isNotEmpty(catalog.getWorkflowId())) {
                    list.add(template);
                } else {
                    throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "没有工作流不能送审");
                }
            }
            for (Template template : list) {
                template.setOriginalStatus(ContentStatusEnum.VERIFY.getCode());
                template.setStatus(ContentStatusEnum.VERIFY.getCode());
                boolean result = super.updateById(template);
                if (result) {
                    TemplateVo templateVo = new TemplateVo();
                    BeanUtil.copyProperties(template, templateVo);
                    // 启动工作流
                    this.startWorkflow(templateVo);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 撤销
     * 来源：已发布的撤销，待审核的撤销
     *
     * @param ids
     * @return
     */
    @Override
    public boolean cancelByIds(List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            List<String> publishIds = new ArrayList<>();
            for (String id : ids) {
                Template template = super.getById(id);
                int status = template.getStatus();
                // 已发布的撤销
                if (ContentStatusEnum.PUBLISH.getCode() == status) {
                    publishIds.add(id);
                }
                template.setOriginalStatus(ContentStatusEnum.CANCEL.getCode());
                template.setStatus(ContentStatusEnum.CANCEL.getCode());
                boolean result = super.updateById(template);
                if (result) {
                    // 待审核的撤销
                    if (ContentStatusEnum.VERIFY.getCode() == status) {
                        // 删除流程
                        workflowFeign.deleteInstanceByBusinessId(template.getId(), "撤销内容");
                    }
                    // 已发布的撤销
                    if (ContentStatusEnum.PUBLISH.getCode() == status) {
                        // 删除聚合关联关系
                        catalogTemplateService.deleteAggregationByTemplateId(id);
                    }
                }
            }
            // 删除静态页和索引
            sendPageAndIndexHandleMessage("delete", publishIds);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 发布
     * 来源： 已撤销的发布
     *
     * @param ids
     * @return
     */
    @Override
    public boolean publishByIds(List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            List<String> publishIds = new ArrayList<>();
            List<Template> list = new ArrayList<>();
            for (String id : ids) {
                Template template = super.getById(id);
                // 检索内容所属栏目
                Catalog catalog = catalogService.getById(template.getCmsCatalogId());
                // 栏目有工作流
                if(YesNoEnum.YES.getCode() == catalog.getWorkflowEnable() && StrUtil.isNotEmpty(catalog.getWorkflowId())) {
                    throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "有工作流，请送审");
                } else {
                    // 撤销状态可以发布
                    if (ContentStatusEnum.CANCEL.getCode() == template.getStatus()) {
                        list.add(template);
                        publishIds.add(id);
                    } else {
                        throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "仅撤销的可以发布");
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(list)) {
                for (Template template : list) {
                    template.setOriginalStatus(ContentStatusEnum.PUBLISH.getCode());
                    template.setStatus(ContentStatusEnum.PUBLISH.getCode());
                    super.updateById(template);
                }
            }
            // 添加静态页和索引
            sendPageAndIndexHandleMessage("add", publishIds);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 发送处理消息
     *
     * @param publishIds
     */
    private void sendPageAndIndexHandleMessage(String action, List<String> publishIds) {
        if (CollectionUtil.isNotEmpty(publishIds)) {
            Map<String, Object> param = new HashMap<>();
            param.put("action", action);
            param.put("publishIds", publishIds);
            rabbitmqTemplate.convertAndSend(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE, RabbitMQConstants.QUEUE_CONTENT_STATUS_SWITCH_HANDLE, param);
        }
    }

    /**
     * 发送聚合消息
     *
     * @param templateId
     */
    private void sendAggregationTemplateHandleMessage(String templateId) {
        if (StrUtil.isEmpty(templateId)) {
            return;
        }
        // 发送聚合消息
        Map<String, String> param = new HashMap<>();
        param.put("templateId", templateId);
        rabbitmqTemplate.convertAndSend(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE, RabbitMQConstants.QUEUE_AGGREGATION_TEMPLATE_CHANGE, param);
    }

    /**
     * 获取定时发布数据
     *
     * @param publicationDate
     * @return
     */
    @Override
    public List<String> getTimingPublishTemplateList(String publicationDate) {
        return baseMapper.getTimingPublishTemplateList(publicationDate);
    }

    /**
     * 更新定时发布的数据状态未发布
     *
     * @param ids
     * @return
     */
    @Override
    public int updateTimingPublishTemplate(List<String> ids) {
        return baseMapper.updateTimingPublishTemplate(ids);
    }

    /**
     * 获取物理数据
     *
     * @param id
     * @return
     */
    @Override
    public TemplateVo getPhysicsTemplateById(String id) {
        return baseMapper.getPhysicsTemplateById(id);
    }

    /**
     * 删除静态页和索引
     *
     * @param ids
     * @return
     */
    @Override
    public void deletePageAndIndexById(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        try {
            Set<String> catalogIds = new HashSet<>();
            for (String id :ids) {
                TemplateVo templateVo = baseMapper.getPhysicsTemplateById(id);
                catalogIds.add(templateVo.getCmsCatalogId());
                // 获取聚合规则包含给定栏目的栏目
                this.addAggregationCatalog(catalogIds, templateVo.getCmsCatalogId());
                // 发送聚合消息
                this.sendAggregationTemplateHandleMessage(templateVo.getId());
                // 删除态页 参数: siteId url
                templateFeign.generateStaticTemplate(templateVo, RabbitMQConstants.MQ_CMS_INDEX_COMMAND_DELETE);
                // 获取索引
                String index = modelService.getIndexByModelId(templateVo.getContentModelId());
                // 删除索引中数据
                if (StrUtil.isNotEmpty(index))
                    indexService.deleteData(index, templateVo.getId());
            }
            for (String catalogId : catalogIds) {
                // 清理页面缓存
                cacheCatalogList(catalogId);
                // 发布新闻所属栏目关联的页面静态页
                pageService.replyPageByCatalog(catalogId);
            }
        } catch(Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    /**
     * 添加静态页和索引
     *
     * @param ids
     * @return
     */
    @Override
    public void addPageAndIndexById(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        try {
            Set<String> catalogIds = new HashSet<>();
            for (String id :ids) {
                Template template = this.getById(id);
                TemplateVo templateVo = new TemplateVo();
                BeanUtil.copyProperties(template, templateVo);
                // 发布状态
                if (ContentStatusEnum.PUBLISH.getCode() == templateVo.getStatus()) {
                    catalogIds.add(templateVo.getCmsCatalogId());
                    // 获取聚合规则包含给定栏目的栏目
                    this.addAggregationCatalog(catalogIds, templateVo.getCmsCatalogId());
                    // 发送聚合消息
                    this.sendAggregationTemplateHandleMessage(templateVo.getId());
                    // 生成静态页面任务
                    this.genStaticPage(templateVo, RabbitMQConstants.MQ_CMS_INDEX_COMMAND_ADD);
                    // 默认都创建索引, 索引任务
                    this.reindex(templateVo, RabbitMQConstants.MQ_CMS_INDEX_COMMAND_ADD);
                }
            }
            for (String catalogId : catalogIds) {
                // 清理页面缓存
                cacheCatalogList(catalogId);
                // 发布新闻所属栏目关联的页面静态页
                pageService.replyPageByCatalog(catalogId);
            }
        } catch(Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    /**
     * 获取聚合规则包含给定栏目的栏目
     *
     * @param catalogIds
     * @param catalogId
     */
    private void addAggregationCatalog(Set<String> catalogIds, String catalogId) {
        // 获取聚合规则包含给定栏目的栏目
        List<CatalogAggregationVo> catalogAggregationVoList = catalogAggregationService.getCatalogAggregationByCatalogId(catalogId);
        if (CollectionUtil.isNotEmpty(catalogAggregationVoList)) {
            for (CatalogAggregationVo ca : catalogAggregationVoList) {
                catalogIds.add(ca.getOwnerCatalogId());
            }
        }
    }

    /**
     * 获取登陆用户代办理任务列表
     *
     * @param templateVo
     * @return
     */
    @Override
    public IPage<TemplateVo> getLoginUserTaskList(TemplateVo templateVo) {
        Page<TemplateVo> result = new Page();
        result.setCurrent(templateVo.getPage());
        result.setSize(templateVo.getSize());
        String userId = UserContextHelper.getUserId();
        if (StrUtil.isEmpty(userId)) {
            log.error("用户编号不存在");
            result.setTotal(0);
            result.setRecords(null);
            return result;
        }
        templateVo.setUserId(userId);
        // 用户部门
        UserVo userVo = adminFeign.getUserByUserId(userId).getData();
        if (Objects.nonNull(userVo)) {
            templateVo.setDepartmentId(userVo.getDepartmentId());
        }
        // 用户角色
        List<String> roleIdList = adminFeign.getRoleIdsByUserId(userId).getData();
        templateVo.setRoleIdList(roleIdList);
        return baseMapper.getLoginUserTaskList(this.getPageByBean(templateVo), templateVo);
    }

    /**
     * 分页查询
     * @param entity
     * @return
     */
    @Override
    public IPage<TemplateVo> pageByTemplate(TemplateVo entity) {
        long start = System.nanoTime();
        String siteId = entity.getSiteId();
        String userId = UserContextHelper.getUserId();
        // 分配给用户的栏目
        List<CatalogVo> catalogList = baseMapper.getUserCatalog(siteId, userId);
        if (CollectionUtil.isEmpty(catalogList)) {
            throw new BusinessException( HttpStatus.HTTP_INTERNAL_ERROR, "没有分配栏目权限");
        }
        // 分配给用户的内容权限
        List<String> authorityList = baseMapper.getUserAuthority(siteId, userId);
        if (CollectionUtil.isEmpty(authorityList)) {
            throw new BusinessException( HttpStatus.HTTP_INTERNAL_ERROR, "没有分配数据权限");
        }

        // 检索条件: 栏目ID
        List<String> catalogIdList = new ArrayList<>();
        // 当前栏目ID
        catalogIdList.add(entity.getCmsCatalogId());
        // 加入当前栏目的子栏目ID
        getChildrenCatalogId(entity.getCmsCatalogId(), catalogList, catalogIdList);

        // 检索条件: 用户ID
        List<String> userIdList = null;
        // 部门权限
        if (authorityList.contains(TemplateAuthorityEnum.DEPARTMENT.getCode())) {
            userIdList = baseMapper.getUserIdOfUserDepartment(userId);
        }
        // 用户权限
        else if (authorityList.contains(TemplateAuthorityEnum.USER.getCode())) {
            userIdList = new ArrayList<>();
            userIdList.add(userId);
        }

        Page<TemplateVo> result = new Page();
        result.setCurrent(entity.getPage());
        result.setSize(entity.getSize());
        Map<String, Object> countResult = baseMapper.countByTemplate(entity, catalogIdList, userIdList);
        long total = (long) countResult.get("number");
        result.setTotal(total);
        if (total > 0) {
            long offset = (entity.getPage() - 1) * entity.getSize();
            List<TemplateVo> list = baseMapper.pageByTemplate(entity, catalogIdList, userIdList, offset, entity.getSize());
            setUserDepartmentCatalogValue(list, catalogList);
            result.setRecords(list);
        }
        log.info("内容检索耗时: " + getMillisTime(System.nanoTime() - start) + " 毫秒");
        return result;
    }

    private void setUserDepartmentCatalogValue(List<TemplateVo> list, List<CatalogVo> catalogList) {
        if (CollectionUtil.isNotEmpty(list)) {
            Map<String, String> catalogLevelNameMap = new HashMap<>();
            Map<String, CatalogVo> catalogMap = new HashMap<>();
            Map<String, String> departmentMap = new HashMap<>();
            Map<String, String> userMap = new HashMap<>();
            Map<String, String> userDepartmentMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(catalogList)) {
                catalogList.stream().parallel().forEach(catalogVo -> {
                    catalogMap.put(catalogVo.getId(), catalogVo);
                    StringBuilder levelName = new StringBuilder();
                    String treePosition = catalogVo.getTreePosition();
                    if (StrUtil.isNotEmpty(treePosition)) {
                        String[] ids = treePosition.substring(1).split("&");
                        for (String id : ids) {
                            for (CatalogVo c : catalogList) {
                                if (id.equals(c.getId())) {
                                    levelName.append("＼");
                                    levelName.append(c.getName());
                                    break;
                                }
                            }
                        }
                    }
                    levelName.append("＼");
                    levelName.append(catalogVo.getName());
                    catalogLevelNameMap.put(catalogVo.getId(), levelName.toString().substring(1));
                });
            }
            List<Department> departmentList = siteCache.getAllDepartment();
            if (CollectionUtil.isNotEmpty(departmentList)) {
                departmentList.stream().parallel().forEach(department -> departmentMap.put(department.getId(), department.getName()));
            }
            List<User> userList = siteCache.getAllUser();
            if (CollectionUtil.isNotEmpty(userList)) {
                userList.stream().parallel().forEach(user -> {
                    userMap.put(user.getId(), user.getName());
                    userDepartmentMap.put(user.getId(), departmentMap.get(user.getDepartmentId()));
                });
            }
            list.stream().parallel().forEach(t -> {
                CatalogVo catalogVo = catalogMap.get(t.getCmsCatalogId());
                t.setWorkflowId(catalogVo.getWorkflowId());
                t.setCmsCatalogPathName(catalogVo.getPathName());
                // 栏目层级路径
                t.setCatalogLevelName(catalogLevelNameMap.get(t.getCmsCatalogId()));
                // 来源
                String sourceName = departmentMap.get(t.getSource());
                if (Objects.isNull(sourceName)) {
                    t.setSourceName(t.getSource());
                } else {
                    t.setSourceName(sourceName);
                }
                t.setCreateUserName(userMap.get(t.getCreateBy()));
                t.setUpdateUserName(userMap.get(t.getUpdateBy()));
                t.setCreateUserDepartmentName(userDepartmentMap.get(t.getCreateBy()));
                t.setUpdateUserDepartmentName(userDepartmentMap.get(t.getUpdateBy()));
            });
        }
    }

    /**
     * 获取全部的子栏目ID
     *
     * @param catalogId
     * @param allCatalogList
     * @param catalogIdList
     */
    private void getChildrenCatalogId(String catalogId, Collection<CatalogVo> allCatalogList, List<String> catalogIdList) {
        for (Catalog c : allCatalogList) {
            // 子栏目
            if (catalogId.equals(c.getParentId())) {
                catalogIdList.add(c.getId());
                getChildrenCatalogId(c.getId(), allCatalogList, catalogIdList);
            }
        }
    }

    /**
     * 用户
     *
     * @return
     */
    @Override
    public List<User> getAllUser() {
        return baseMapper.getAllUser();
    }

    /**
     * 部门
     *
     * @return
     */
    @Override
    public List<Department> getAllDepartment() {
        return baseMapper.getAllDepartment();
    }

    private String getMillisTime(long time) {
        return String.valueOf(time / 1000000);
    }
    /*
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
        IPage<TemplateVo> result = baseMapper.pageByTemplate(getPageByBean(entity), entity, catalogIdList, userIdList);
        return result;
    }*/






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
        if(maps.containsKey("catId")){
            List<String> cmsCatalogId = new ArrayList<>();
            String catIds = maps.get("catId").toString();
            String[] temp = catIds.split(",");
            for(String catId:temp){
                Collection<CatalogVo> catalogVos = getCatalogChildrenTree(maps.get("siteId").toString(),catId);
                cmsCatalogId.add(catId);
                cmsCatalogId = getCatalogChildrenIds(catalogVos,cmsCatalogId);
            }
            maps.put("cmsCatalogId",cmsCatalogId);
        }
        Page<TemplateVo> pages = new Page<>();
        pages.setCurrent(page);
        pages.setSize(pageSize);
        int totle = baseMapper.getTemplateListCount(maps);
        pages.setTotal(totle);
        if (totle > 0) {
            maps.put("page", (page - 1) * pageSize);
            maps.put("pageSize",pageSize);
            List<TemplateVo> list = baseMapper.getTemplateList(maps);
            pages.setRecords(list);
            //是否加载元数据字段
            if(maps.containsKey("metadata") && Boolean.parseBoolean(maps.get("metadata").toString())){
                pages.setRecords(setViewVoProperties(pages.getRecords()));
            }
        }
        return pages;
    }

    /**
     * 根据ID查看详情
     * 前台专用
     * @param id
     * @return
     */
    @Override
    public TemplateVo getTemplateById(String id) {
        TemplateVo templateVo = baseMapper.queryTemplateById(id);
        setViewVoProperties(templateVo);
        return templateVo;
    }
    /**
     * 批量将对象转换为vo内容模板
     * 前台专用
     * @param templates
     * @return
     */
    @Override
    public List<TemplateVo> setViewVoProperties(Collection<TemplateVo> templates){
        List<TemplateVo> templateVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(templates)) {
            for (TemplateVo templateVo : templates) {
                setViewVoProperties(templateVo);
                templateVos.add(templateVo);
            }
        }
        return templateVos;
    }
    /**
     * 单个将对象转换为vo内容模板
     * 前台专用
     * @param templateVo
     * @return
     */
    @Override
    public TemplateVo setViewVoProperties(TemplateVo templateVo){
        Map<String, String> departmentNameMap = this.getDepartmentIdNameMap();
        templateVo.setSourceName(departmentNameMap.get(templateVo.getSource()) == null ? templateVo.getSource() : departmentNameMap.get(templateVo.getSource()));
        // 查询元数据结构及数据
        this.queryViewMetadata(templateVo);
        // 查询模型模板
        this.queryModelTemplate(templateVo);
        return templateVo;
    }
    /**
     * 查询元数据
     * 前台专用
     * @param templateVo
     */
    private void queryViewMetadata(TemplateVo templateVo) {
        if (StrUtil.isNotEmpty(templateVo.getMetaDataCollectionId())
                && StrUtil.isNotEmpty(templateVo.getContentId())) {
            // 查询元数据记录信息
            Map content = adminFeign.getMetadataById(templateVo.getMetaDataCollectionId(), templateVo.getContentId()).getData();
            templateVo.setContent(content);
        }
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
    public void resetTemplateIndexCode(String siteId, String start, String end, String part, int number) {
        Map<String,Object> param = new HashMap<>();
        param.put("siteId", siteId);
        param.put("start", start);
        param.put("end", end);
        param.put("part", part);
        param.put("number", number);
        rabbitmqTemplate.convertAndSend(RabbitMQConstants.FANOUT_EXCHANGE_RESET_INDEX_CODE, "", param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetTemplateIndexCodeHandler(Map<String,Object> param) {
        String siteId = param.get("siteId").toString();
        String start = (String) param.get("start");
        String end = (String) param.get("end");
        String part = (String) param.get("part");
        int number = Integer.parseInt(param.get("number").toString());
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id_");
        queryWrapper.eq("site_id", siteId);
        if (StrUtil.isNotEmpty(start) && StrUtil.isNotEmpty(end)) {
            queryWrapper.between("DATE_FORMAT(resource_publication_date, '%Y-%m-%d')", start, end);
        }
        queryWrapper.orderByAsc("resource_publication_date");
        int total = super.count(queryWrapper);
        if (total == 0) {
            messagingTemplate.convertAndSend("/topic/reset/index/code/" + siteId + "/", total + "," + total);
            return;
        }
        final int size = 100;
        int totalPage = total / size;
        if (total % size != 0) {
            totalPage += 1;
        }
        boolean result = true;
        StringBuilder format = new StringBuilder("%0");
        format.append(number);
        format.append("d");
        int value = 1;
        for (int p = 1; p <= totalPage; p++) {
            List<Template> list = baseMapper.pageTemplateListForRestIndexCode(siteId, start, end, (p - 1) * size, size);
            if (CollectionUtil.isNotEmpty(list)) {
                for (Template t : list) {
                    StringBuilder indexCode = new StringBuilder();
                    if (StrUtil.isNotEmpty(part)) {
                        indexCode.append(part);
                    }
                    indexCode.append(String.format(format.toString(), value++));
                    t.setIndexCode(indexCode.toString());
                }
                boolean flag = super.updateBatchById(list);
                result &= flag;
                if (flag) {
                    messagingTemplate.convertAndSend("/topic/reset/index/code/" + siteId + "/", (value - 1) + "," + total);
                }
            }
        }
        if (result) {
            assemblyFeign.updateNextSerialBySiteId(siteId, value);
        }
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




    /**
     * 更新并获取浏览次数
     *
     * @param id
     * @return
     * */
    @Override
    public int getTemplateClickCount(String id) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id_",id);
        queryWrapper.select("ifnull(max(views_), 0) + 1 as views");
        int views = Integer.parseInt(super.getMap(queryWrapper).get("views").toString());
        Template template = new Template();
        template.setId(id);
        template.setViews(views);
        super.updateById(template);
        return views;
    }

    /**
     * 统计栏目的内容数
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> countCatalogTemplate() {
        return baseMapper.countCatalogTemplate();
    }


}
