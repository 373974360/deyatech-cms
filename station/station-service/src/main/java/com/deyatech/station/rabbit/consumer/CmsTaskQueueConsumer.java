package com.deyatech.station.rabbit.consumer;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.deyatech.common.enums.ContentOriginTypeEnum;
import com.deyatech.common.enums.ContentStatusEnum;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.entity.CatalogAggregation;
import com.deyatech.station.entity.CatalogTemplate;
import com.deyatech.station.entity.Template;
import com.deyatech.station.index.IndexService;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.service.*;
import com.deyatech.station.vo.CatalogAggregationVo;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.template.feign.TemplateFeign;
import com.deyatech.workflow.constant.ProcessConstant;
import com.deyatech.workflow.feign.WorkflowFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.*;

/**
 * CMS中的任务队列消息处理
 * @Author csm
 * @Date 2019/08/13
 */
@Slf4j
@Component
public class CmsTaskQueueConsumer {

    @Autowired
    IndexService indexService;
    @Autowired
    TemplateFeign templateFeign;
    @Autowired
    TemplateService templateService;
    @Autowired
    ModelService modelService;
    @Autowired
    CatalogService catalogService;
    @Autowired
    SiteCache siteCache;
    @Autowired
    WorkflowFeign workflowFeign;
    @Autowired
    PageService pageService;
    @Autowired
    CatalogAggregationService catalogAggregationService;
    @Autowired
    CatalogTemplateService catalogTemplateService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final String TOPIC_STATIC_PAGE_MESSAGE = "/topic/staticPage/message/";

    private final String TOPIC_REINDEX_MESSAGE = "/topic/reIndex/message/";

    /**
     * 聚合关联关系-栏目规则变更
     *
     * @param param
     */
    @RabbitListener(queues = RabbitMQConstants.QUEUE_AGGREGATION_CATALOG_CHANGE)
    public void aggregationCatalogChangeHandle(Map<String,Object> param) {
        // 变更的栏目
        String catalogId = (String) param.get("catalogId");
        // 栏目对应的聚合规则ID
        String aggregationId = (String) param.get("aggregationId");
        if (StrUtil.isEmpty(catalogId) || StrUtil.isEmpty(aggregationId)) {
            return;
        }
        try {
            Catalog catalog = catalogService.getById(catalogId);
            CatalogAggregation aggregation = catalogAggregationService.getById(aggregationId);
            if (Objects.isNull(catalog) || Objects.isNull(aggregation)) {
                return;
            }
            // 删除已有的聚合栏目内容关联关系
            CatalogTemplate catalogTemplate = new CatalogTemplate();
            catalogTemplate.setCatalogId(catalogId);
            catalogTemplate.setOriginType(ContentOriginTypeEnum.AGGREGATION.getCode());
            catalogTemplateService.removeByBean(catalogTemplate);
            // 聚合规则
            CatalogAggregationVo condition = new CatalogAggregationVo();
            BeanUtil.copyProperties(aggregation, condition);
            condition.analysisCondition();
            long offset;
            long size = 1;// 5000
            long page = 1;
            while (true) {
                offset = (page - 1) * size;
                page++;
                // 检索满足聚合规则的内容ID
                List<String> templateIds = catalogTemplateService.getAggregationTemplateId(condition, offset, size);
                if (CollectionUtil.isEmpty(templateIds)) {
                    break;
                } else {
                    List<CatalogTemplate> catalogTemplatesList = new ArrayList<>();
                    for (String templateId : templateIds) {
                        CatalogTemplate entity = new CatalogTemplate();
                        entity.setId(IdWorker.getIdStr());
                        entity.setCatalogId(catalogId);
                        entity.setTemplateId(templateId);
                        entity.setOriginType(ContentOriginTypeEnum.AGGREGATION.getCode());
                        catalogTemplatesList.add(entity);
                    }
                    // 批量插入聚合栏目内容关联关系
                    catalogTemplateService.insertCatalogTemplate(catalogTemplatesList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(String.format("【重建聚合关联关系】聚合规则变更异常：catalogId = %s, aggregationId = %s", catalogId, aggregationId));
        }
    }

    /**
     * 聚合关联关系-内容变更
     *
     * @param param
     */
    @RabbitListener(queues = RabbitMQConstants.QUEUE_AGGREGATION_TEMPLATE_CHANGE)
    public void aggregationTemplateChangeHandle(Map<String,Object> param) {
        String templateId = (String) param.get("templateId");
        if (StrUtil.isEmpty(templateId)) {
            return;
        }
        try {
            Template template = templateService.getById(templateId);
            if (Objects.isNull(template)) {
                return;
            }
            // 获取聚合规则包含给定栏目的栏目
            List<CatalogAggregationVo> catalogAggregationList = catalogAggregationService.getCatalogAggregationBySiteId(template.getSiteId());
            if (CollectionUtil.isEmpty(catalogAggregationList)) {
                return;
            }
            for (CatalogAggregationVo ca : catalogAggregationList) {
                CatalogTemplate entity = new CatalogTemplate();
                entity.setCatalogId(ca.getOwnerCatalogId());
                entity.setTemplateId(template.getId());
                entity.setOriginType(ContentOriginTypeEnum.AGGREGATION.getCode());
                // 匹配规则
                if (isMatchAggregationRule(ca, template)) {
                    // 检索原来有没有
                    Collection<CatalogTemplate> list = catalogTemplateService.listByBean(entity);
                    // 没有，则添加关联关系
                    if (CollectionUtil.isEmpty(list)) {
                        catalogTemplateService.saveOrUpdate(entity);
                    }
                } else {
                    // 不匹配规则，解除关联关系
                    catalogTemplateService.removeByBean(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(String.format("【重建聚合关联关系】内容变更异常：templateId = %s", templateId));
        }
    }
    /**
     * 内容是否匹配规则
     *
     * @param ca
     * @param template
     * @return
     */
    private boolean isMatchAggregationRule(CatalogAggregationVo ca, Template template) {
        // 生成条件
        ca.analysisCondition();
        // 栏目
        if (CollectionUtil.isNotEmpty(ca.getCatalogIdList())) {
            // 未包含栏目
            if (!ca.getCatalogIdList().contains(template.getCmsCatalogId())) {
                return false;
            }
        } else {
            return false;
        }
        // 关键字
        if (CollectionUtil.isNotEmpty(ca.getKeyList())) {
            boolean has = false;
            if (StrUtil.isNotEmpty(template.getKeyword())) {
                List<String> keyList = Arrays.asList(template.getKeyword().split(","));
                for (String keyword : keyList) {
                    // 包含关键字
                    if (ca.getKeyList().contains(keyword)) {
                        has = true;
                        break;
                    }
                }
            }
            if (!has) {
                return false;
            }
        }
        // 发布机构
        if (StrUtil.isNotEmpty(ca.getPublishOrganization())) {
            // 不匹配
            if (!ca.getPublishOrganization().equals(template.getSource())) {
                return false;
            }
        }
        // 发布时间段
        if (StrUtil.isNotEmpty(ca.getStartTime()) && StrUtil.isNotEmpty(ca.getStartTime())) {
            Date date = template.getResourcePublicationDate();
            if (Objects.isNull(date)) {
                return false;
            } else {
                String time = DateUtil.format(date, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
                if (ca.getStartTime().compareTo(time) > 0 || ca.getEndTime().compareTo(time) < 0) {
                    return false;
                }
            }
        }
        // 发布人
        if (StrUtil.isNotEmpty(ca.getPublisher())) {
            // 不匹配
            if (!ca.getPublisher().equals(template.getCreateBy())) {
                return false;
            }
        }
        return true;
    }
    /**
     * 内容状态切换处理: 静态页、索引、工作流
     *
     * @param param
     */
    @RabbitListener(queues = RabbitMQConstants.QUEUE_CONTENT_STATUS_SWITCH_HANDLE)
    public void contentStatusSwitchHandle(Map<String,Object> param) {
        try {
            String action = (String) param.get("action");
            String templateId = (String) param.get("templateId");
            Integer fromStatus = (Integer) param.get("fromStatus");
            Integer status = (Integer) param.get("status");
            TemplateVo templateVo = new TemplateVo();
            BeanUtil.copyProperties(templateService.getById(templateId), templateVo);
            // 检索内容所属栏目
            Catalog catalog = catalogService.getById(templateVo.getCmsCatalogId());
            // 设置工作流ID
            templateVo.setWorkflowId(catalog.getWorkflowId());
            // 设置工作流Key
            templateVo.setWorkflowKey(catalog.getWorkflowKey());
            // 是否工作流
            boolean hasWorkflow = false;
            // 栏目有工作流
            if(YesNoEnum.YES.getCode().equals(catalog.getWorkflowEnable()) && StrUtil.isNotEmpty(catalog.getWorkflowId())) {
                hasWorkflow = true;
            }
            switch (action) {
                // 删除到回收站
                case "recycle":
                    // 已发布
                    if (ContentStatusEnum.PUBLISH.getCode() == fromStatus) {
                        // 删除静态页和索引
                        templateService.deletePageAndIndexById(templateVo);
                    }
                    // 删除流程
                    workflowFeign.deleteInstanceByBusinessId(templateVo.getId(), "删除内容");
                    break;

                // 从回收站还原内容
                case "back":
                    // 栏目有工作流
                    if(hasWorkflow) {
                        // 还原到发布状态 或者 待审状态 时不做处理
                        if (ContentStatusEnum.PUBLISH.getCode() == status || ContentStatusEnum.VERIFY.getCode() == status) {
                            // 启动工作流
                            templateService.startWorkflow(templateVo);
                        } else {
                            // 已发布
                            if (ContentStatusEnum.PUBLISH.getCode() == fromStatus) {
                                // 添加静态页和索引
                                templateService.addPageAndIndexById(templateVo);
                            }
                        }
                    } else {
                        // 已发布
                        if (ContentStatusEnum.PUBLISH.getCode() == fromStatus) {
                            // 添加静态页和索引
                            templateService.addPageAndIndexById(templateVo);
                        }
                    }
                    break;

                // 再送审
                case "verify":
                    // 栏目有工作流
                    if(hasWorkflow) {
                        // 启动工作流
                        templateService.startWorkflow(templateVo);
                    }
                    break;

                // 撤销
                case "cancel":
                    // 已发布
                    if (ContentStatusEnum.PUBLISH.getCode() == fromStatus) {
                        // 删除静态页和索引
                        templateService.deletePageAndIndexById(templateVo);
                    }
                    // 删除流程
                    workflowFeign.deleteInstanceByBusinessId(templateVo.getId(), "撤销内容");
                    break;

                // 发布
                case "publish":
                    // 已发布
                    if (ContentStatusEnum.PUBLISH.getCode() == fromStatus) {
                        // 删除静态页和索引
                        templateService.addPageAndIndexById(templateVo);
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("内容状态切换处理异常: " + e.getMessage());
        }
    }

    /**
     * 内容审核通过
     * 注意：通过审核页面操作
     *
     * @param param
     */
    @RabbitListener(queues = ProcessConstant.QUEUE_PROCESS_FINISH)
    public void contentFinish(Map<String,Object> param) {
        String templateId = (String) param.get(ProcessConstant.BUSINESS_ID);
        log.info("内容审核通过： 内容ID = " + templateId);
        if (StrUtil.isNotEmpty(templateId)) {
            Template template = new Template();
            template.setId(templateId);
            templateService.contentFinish(template);
        }
    }

    /**
     * 内容审核拒绝
     * 注意：通过审核页面操作
     *
     * @param param
     */
    @RabbitListener(queues = ProcessConstant.QUEUE_PROCESS_REJECT)
    public void contentReject(Map<String,Object> param) {
        String templateId = (String) param.get(ProcessConstant.BUSINESS_ID);
        String reason = (String) param.get(ProcessConstant.REASON);
        log.info("内容审核拒绝： 内容ID = " + templateId + ", 理由：" + reason);
        if (StrUtil.isNotEmpty(templateId)) {
            Template template = new Template();
            template.setId(templateId);
            template.setReason(reason);
            templateService.contentReject(template);
        }
    }

//    /**
//     * 内容审核删除
//     * 注意：通过审核页面操作
//     *
//     * @param param
//     */
//    @RabbitListener(queues = ProcessConstant.QUEUE_PROCESS_DELETE)
//    public void contentDelete(Map<String,Object> param) {
//        String templateId = (String) param.get(ProcessConstant.BUSINESS_ID);
//        String reason = (String) param.get(ProcessConstant.REASON);
//        log.info("内容审核删除： 内容ID = " + templateId + ", 理由：" + reason);
//    }

    /**
     * 生成内容索引编码
     * @param param
     */
    @RabbitListener(queues = "#{queueResetIndexCode.name}")
    public void handlerLiveMessage(Map<String,Object> param) {
        log.info(String.format("生成内容索引编码：%s", JSONUtil.toJsonStr(param)));
        templateService.resetTemplateIndexCodeHandler(param);
    }

    /**
     * 处理缓存列表页任务
     * @param catId
     */
    @RabbitListener(queues = RabbitMQConstants.QUEUE_NAME_LIST_PAGE_TASK)
    public void handleCmsListTask(String catId) {
        log.info(String.format("处理发布静态页任务：%s", JSONUtil.toJsonStr(catId)));
        CatalogVo catalogVo = catalogService.setVoProperties(catalogService.getById(catId));
        //清楚历史缓存
        siteCache.clearCache(catalogVo.getPathName());
        String siteTemplateRoot = siteCache.getStationGroupTemplatePathBySiteId(catalogVo.getSiteId());
        Map<String,Object> varMap = new HashMap<>();
        varMap.put("site",siteCache.getStationGroupById(catalogVo.getSiteId()));
        varMap.put("catalog",catalogVo);
        varMap.put("rootCatalog",getRootCatalog(catalogVo.getSiteId(),catalogVo.getId()));
        String template = catalogVo.getListTemplate();
        if(StringUtils.isNotBlank(template)){
            String cachekey = catalogVo.getPathName();
            varMap.put("namePath",cachekey);
            for(int i=1;i<=10;i++){
                varMap.put("pageNo",i);
                String process = templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
                siteCache.cacheTemplate(cachekey,StrUtil.toString(i),process);
            }
        }
    }

    /**
     * 处理生成静态页面任务--进度条
     * @param dataMap
     */
    @RabbitListener(queues = RabbitMQConstants.QUEUE_NAME_STATIC_PAGE_TASK)
    public void handleCmsStaticTask(Map<String,Object> dataMap) {
        log.info(String.format("处理发布静态页任务：%s", JSONUtil.toJsonStr(dataMap)));
        String messageCode = dataMap.get("messageCode").toString();
        Map<String, Object> maps = (Map<String, Object>) dataMap.get("maps");
        IPage<TemplateVo> templates = templateService.getTemplateListView(maps,1, Integer.parseInt(maps.get("totle").toString()));
        if(CollectionUtil.isNotEmpty(templates.getRecords())){
            Map<String,Object> result = new HashMap();
            result.put("totle",String.valueOf(templates.getTotal()));
            int i = 0;
            for(TemplateVo templateVo:templates.getRecords()){
                i ++;
                templateVo = templateService.setViewVoProperties(templateVo);
                // 创建/删除、更新静态页
                templateFeign.generateStaticTemplate(templateVo, messageCode);
                result.put("currNo",String.valueOf(i));
                result.put("currTitle",templateVo.getTitle());
                if(messageCode.equals(RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE)){
                    //向客户端发送进度
                    messagingTemplate.convertAndSend(TOPIC_STATIC_PAGE_MESSAGE + templateVo.getSiteId() + "/", result);
                }
            }
        }
    }

    /**
     * 处理索引任务
     * @param dataMap
     */
    @RabbitListener(queues = RabbitMQConstants.QUEUE_NAME_INDEX_TASK)
    public void handleCmsIndexTask(Map<String,Object> dataMap) {
        log.info(String.format("处理索引任务：%s", JSONUtil.toJsonStr(dataMap)));
        String messageCode = dataMap.get("messageCode").toString();
        Map<String, Object> maps = (Map<String, Object>) dataMap.get("maps");
        IPage<TemplateVo> templates = templateService.getTemplateListView(maps,1,Integer.parseInt(maps.get("totle").toString()));
        if(CollectionUtil.isNotEmpty(templates.getRecords())){
            Map<String,Object> result = new HashMap();
            result.put("totle",String.valueOf(templates.getTotal()));
            int i = 0;
            for(TemplateVo templateVo:templates.getRecords()){
                i++;
                // 获取索引
                String index = modelService.getIndexByModelId(templateVo.getContentModelId());
                templateVo.setIndex(index);
                result.put("currNo",String.valueOf(i));
                result.put("currTitle",templateVo.getTitle());
                // 索引/删除、更新数据
                if (RabbitMQConstants.MQ_CMS_INDEX_COMMAND_ADD.equalsIgnoreCase(messageCode)) {
                    this.addIndex(templateVo);
                } else if (RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE.equalsIgnoreCase(messageCode)) {
                    this.updateIndex(templateVo);
                    //向客户端发送进度
                    messagingTemplate.convertAndSend(TOPIC_REINDEX_MESSAGE + templateVo.getSiteId() + "/", result);
                } else if (RabbitMQConstants.MQ_CMS_INDEX_COMMAND_DELETE.equalsIgnoreCase(messageCode)) {
                    this.deleteIndex(templateVo);
                    //向客户端发送进度
                    messagingTemplate.convertAndSend(TOPIC_REINDEX_MESSAGE + templateVo.getSiteId() + "/", result);
                } else {
                    log.warn("未知的索引任务: %s ", JSONUtil.toJsonStr(templateVo));
                }
            }
        }

    }

    private void addIndex(TemplateVo templateVo) {
        // TODO 设置其他附加属性this.setVoProperties(template） 设置contentModelName
        TemplateVo templateVoResult = templateService.setVoProperties(templateVo);
        BeanMap dataRow = BeanMap.create(templateVoResult);
        HashMap<Object, Object> objectObjectHashMap = CollectionUtil.newHashMap();
        objectObjectHashMap.putAll(dataRow);
        // 筛选要删除的key
        Set<Object> removeKey = new HashSet<>();
        for (Object key : objectObjectHashMap.keySet()) {
            Object value = objectObjectHashMap.get(key);
            if (value == null) {
                log.debug(key + " value is null ");
                continue;
            }
            if (!(value instanceof String)
                    && !(value instanceof Short)
                    && !(value instanceof Byte)
                    && !(value instanceof Float)
                    && !(value instanceof Double)
                    && !(value instanceof Integer)
                    && !(value instanceof Long)
                    && !(value instanceof Date)
                    && !(value instanceof Boolean)
                    && !(value instanceof Array)
                    && !(value instanceof List)
                    && !(value instanceof Map)
            ) {
                removeKey.add(key);
                log.warn(key + " value type is  " + value.getClass() + " for ignore add to index");
            }
            if (value instanceof Date) {
                log.debug(key + "  convert to  string  " + value.getClass());
                objectObjectHashMap.put(key, DateFormatUtils.format((Date) value, "yyyy-MM-dd'T'HH:mm:ss.sss'Z'"));
            }
        }
        for (Object key : removeKey) {
            objectObjectHashMap.remove(key);
        }
        // 向索引中添加数据
        indexService.addData(templateVo.getIndex(), templateVo.getId(), objectObjectHashMap);
    }

    private void deleteIndex(TemplateVo templateVo) {
        // 删除索引中数据
        indexService.deleteData(templateVo.getIndex(), templateVo.getId());
    }

    private void updateIndex(TemplateVo templateVo) {
        this.deleteIndex(templateVo);
        this.addIndex(templateVo);
    }




    public CatalogVo getRootCatalog(String siteId, String catalogId) {
        CatalogVo resultCatalogVo = null;
        Collection<CatalogVo> catalogVoCollection = siteCache.getCatalogTreeBySiteId(siteId);
        CatalogVo catalogVo = getCatalog(catalogVoCollection,catalogId);
        if(ObjectUtil.isNotNull(catalogVo)){
            if(StrUtil.isNotBlank(catalogVo.getTreePosition())){
                String temp[] = catalogVo.getTreePosition().substring(1).split("&");
                resultCatalogVo = getCatalog(catalogVoCollection,temp[0]);
            }else{
                resultCatalogVo = catalogVo;
            }
        }
        return resultCatalogVo;
    }

    public CatalogVo getCatalog(Collection<CatalogVo> catalogVoCollection,String catalogId ){
        CatalogVo catalogVo = null;
        for(CatalogVo catalogVo1:catalogVoCollection){
            if(catalogVo1.getId().equals(catalogId)){
                catalogVo = catalogVo1;
                break;
            }else if(ObjectUtil.isNotNull(catalogVo1.getChildren())){
                catalogVo = getCatalog(catalogVo1.getChildren(),catalogId);
                if(ObjectUtil.isNotNull(catalogVo)){
                    break;
                }
            }
        }
        return catalogVo;
    }

    public static void main(String[] args) {
        TemplateVo templateVo = new TemplateVo();
        templateVo.setId("1");
        templateVo.setContentId("11");
        BeanMap dataRow = BeanMap.create(templateVo);
        System.out.println(dataRow.get("id"));
        System.out.println(dataRow.get("contentId"));
    }

}
