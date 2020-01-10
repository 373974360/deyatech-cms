package com.deyatech.station.rabbit.consumer;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.enums.ContentStatusEnum;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.entity.Template;
import com.deyatech.station.index.IndexService;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.ModelService;
import com.deyatech.station.service.PageService;
import com.deyatech.station.service.TemplateService;
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
    private SimpMessagingTemplate messagingTemplate;

    private final String TOPIC_STATIC_PAGE_MESSAGE = "/topic/staticPage/message/";

    private final String TOPIC_REINDEX_MESSAGE = "/topic/reIndex/message/";

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
            varMap.put("namePath",catalogVo.getPathName());
            for(int i=1;i<=10;i++){
                varMap.put("pageNo",i);
                templateFeign.thyToString(siteTemplateRoot,template,varMap).getData();
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
