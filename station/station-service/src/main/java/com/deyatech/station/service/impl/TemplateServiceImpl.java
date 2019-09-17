package com.deyatech.station.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PinyinUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.MetadataCollectionVo;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.content.entity.ReviewProcess;
import com.deyatech.content.feign.ContentFeign;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.ModelTemplate;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.entity.Template;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.ModelService;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.ModelTemplateVo;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.station.mapper.TemplateMapper;
import com.deyatech.station.service.TemplateService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.workflow.feign.WorkflowFeign;
import com.deyatech.workflow.vo.ProcessInstanceVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private TemplateMapper templateMapper;
    @Autowired
    private ContentFeign contentFeign;
    @Autowired
    private WorkflowFeign workflowFeign;
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    private ModelTemplateServiceImpl modelTemplateService;
    @Autowired
    private SiteCache siteCache;

    /**
     * 单个将对象转换为vo内容模板
     *
     * @param template
     * @return
     */
    @Override
    public TemplateVo setVoProperties(Template template){
        TemplateVo templateVo = templateMapper.queryTemplateById(template.getId());
        BeanUtil.copyProperties(template, templateVo);

        // 查询元数据结构
        MetadataCollectionVo metadataCollectionVo = new MetadataCollectionVo();
        metadataCollectionVo.setId(templateVo.getMetaDataCollectionId());
        List<MetadataCollectionVo> metadataCollectionVoList = adminFeign.findAllData(metadataCollectionVo).getData();
        templateVo.setMetadataCollectionVo(metadataCollectionVoList.get(0));
        // 查询元数据记录信息
        Map content = adminFeign.getMetadataById(templateVo.getMetaDataCollectionId(), templateVo.getContentId()).getData();
        templateVo.setContent(content);

        //  查询模板配置
        ModelTemplate mt = new ModelTemplate();
        mt.setCmsCatalogId(templateVo.getCmsCatalogId());
        mt.setContentModelId(templateVo.getContentModelId());
        mt.setSiteId(templateVo.getSiteId());
        ModelTemplate modelTemplate = modelTemplateService.getByBean(mt);
        templateVo.setTemplatePath(modelTemplate.getTemplatePath());
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
                if (StrUtil.isNotEmpty(templateVo.getMetaDataCollectionId())){
                    // 查询元数据结构
                    MetadataCollectionVo metadataCollectionVo = new MetadataCollectionVo();
                    metadataCollectionVo.setId(templateVo.getMetaDataCollectionId());
                    List<MetadataCollectionVo> metadataCollectionVoList = adminFeign.findAllData(metadataCollectionVo).getData();
                    templateVo.setMetadataCollectionVo(metadataCollectionVoList.get(0));
                }
                if (StrUtil.isNotEmpty(templateVo.getMetaDataCollectionId())
                        && StrUtil.isNotEmpty(templateVo.getContentId())) {
                    // 查询元数据记录信息
                    Map content = adminFeign.getMetadataById(templateVo.getMetaDataCollectionId(), templateVo.getContentId()).getData();
                    templateVo.setContent(content);
                }

                templateVos.add(templateVo);
            }
        }
        return templateVos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateTemplateVo(TemplateVo templateVo) {
        boolean toUpdate = StrUtil.isNotBlank(templateVo.getId());
        if (this.checkTitleExist(templateVo)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "当前栏目中已存在该标题内容");
        }

        if (StrUtil.isEmpty(templateVo.getUrl())) {
            if (BooleanUtil.isTrue(templateVo.getFlagExternal())) {
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "外链内容必须填写URL");
            }
            // 获取栏目信息
            Catalog catalog = catalogService.getById(templateVo.getCmsCatalogId());
            // 设置URL
            if (ObjectUtil.isNotNull(catalog)) {
                templateVo.setUrl("/" + catalog.getPathName() + "/" + PinyinUtil.getAllFirstLetter(templateVo.getTitle()) + ".html");
            } else {
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "未查到栏目相关信息");
            }
        }

        // 设置内容发布状态：1-草稿，2-已发布
        templateVo.setStatus(1);

        // 保存或更新元数据
        if (BooleanUtil.isFalse(templateVo.getFlagExternal()) && StrUtil.isNotEmpty(templateVo.getContentMapStr())) {
            Map contentMap = JSONUtil.toBean(templateVo.getContentMapStr(), Map.class);
            String contentId = adminFeign.saveOrUpdateMetadata(templateVo.getMetaDataCollectionId(), templateVo.getContentId(), contentMap).getData();
            // 如果是插入数据， 回填contentId
            if (StrUtil.isEmpty(templateVo.getContentId())) {
                templateVo.setContentId(contentId);
            }
        }

        // 保存内容
        super.saveOrUpdate(templateVo);

        // 工作流相关
        String workflowKey = templateVo.getWorkflowKey();
        if (StrUtil.isNotEmpty(workflowKey)) {
            // 启动审核流程生命周期 TODO
/*            ReviewProcess reviewProcess = new ReviewProcess();
            reviewProcess.setContentId(templateVo.getId());
            reviewProcess.setWorkflowId(workflowId);
            // 审核生命周期状态：0.启动 1.审核 2.完成
            reviewProcess.setStatus(0);
            contentFeign.saveOrUpdate(reviewProcess);*/

            // 启动工作流 TODO
            ProcessInstanceVo processInstanceVo = new ProcessInstanceVo();
            processInstanceVo.setActDefinitionKey(workflowKey);
            processInstanceVo.setBusinessId(String.valueOf(System.currentTimeMillis()));
            processInstanceVo.setSource("CMS");
            processInstanceVo.setUserId(UserContextHelper.getUserId());
            Map<String, Object> mapParams = CollectionUtil.newHashMap();
            mapParams.put("title", templateVo.getTitle());
            mapParams.put("author", templateVo.getAuthor());
            mapParams.put("templateId", templateVo.getId());
            mapParams.put("siteId", templateVo.getSiteId());
            processInstanceVo.setVariables(mapParams);
            workflowFeign.startInstance(processInstanceVo);
        }

        // 生成静态页面任务 TODO
        this.addStaticPageTask(templateVo);

        // 默认都创建索引, 索引任务 TODO
        this.addIndexTask(templateVo, toUpdate ? RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE : RabbitMQConstants.MQ_CMS_INDEX_COMMAND_ADD);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(String ids) {
        List<Map> mapList = JSONUtil.toList(JSONUtil.parseArray(ids), Map.class);
        // 删除元数据
        adminFeign.removeMetadataByIds(mapList);

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
        queryWrapper.eq("status_", 2);
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
            queryWrapper.in("id_", templateVo.getIds().split(","));
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
     * 分页查询
     * @param entity
     * @return
     */
    @Override
    public IPage<TemplateVo> pageByTemplate(Template entity) {
        return templateMapper.pageByTemplate(getPageByBean(entity), entity);
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
        return templateMapper.getTemplateListView(pages,maps);
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
}
