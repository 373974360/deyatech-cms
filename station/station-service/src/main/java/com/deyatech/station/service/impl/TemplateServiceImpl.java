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
import com.deyatech.admin.entity.User;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.MetadataCollectionVo;
import com.deyatech.common.Constants;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.ContentStatusEnum;
import com.deyatech.common.enums.TemplateAuthorityEnum;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.content.feign.ContentFeign;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.*;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.service.*;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.station.mapper.TemplateMapper;
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
    private ContentFeign contentFeign;
    @Autowired
    private WorkflowFeign workflowFeign;
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    private ModelTemplateServiceImpl modelTemplateService;
    @Autowired
    private SiteCache siteCache;
    @Autowired
    CatalogUserService catalogUserService;
    @Autowired
    TemplateUserAuthorityService templateUserAuthorityService;

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
                // 查询元数据结构及数据
                this.queryMetadata(templateVo);
                // 查询模型模板
                this.queryModelTemplate(templateVo);
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

        // 保存或更新元数据
        if (BooleanUtil.isFalse(templateVo.getFlagExternal()) && StrUtil.isNotEmpty(templateVo.getContentMapStr())) {
            Map contentMap = JSONUtil.toBean(templateVo.getContentMapStr(), Map.class);
            String contentId = adminFeign.saveOrUpdateMetadata(templateVo.getMetaDataCollectionId(), templateVo.getContentId(), contentMap).getData();
            // 如果是插入数据， 回填contentId
            if (StrUtil.isEmpty(templateVo.getContentId())) {
                templateVo.setContentId(contentId);
            }
        }

        // 保存草稿
        if ("true".equals(templateVo.getDraftFlag())) {
            // 草稿
            templateVo.setStatus(ContentStatusEnum.DRAFT.getCode());
        } else {
            // 工作流
            if (StrUtil.isNotEmpty(templateVo.getWorkflowKey())) {
                /*
                // 启动审核流程生命周期 TODO
                ReviewProcess reviewProcess = new ReviewProcess();
                reviewProcess.setContentId(templateVo.getId());
                reviewProcess.setWorkflowId(workflowId);
                // 审核生命周期状态：0.启动 1.审核 2.完成
                reviewProcess.setStatus(0);
                contentFeign.saveOrUpdate(reviewProcess);
                */

                // 启动工作流 TODO
                ProcessInstanceVo processInstanceVo = new ProcessInstanceVo();
                processInstanceVo.setActDefinitionKey(templateVo.getWorkflowKey());
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

                // 审核中
                templateVo.setStatus(ContentStatusEnum.VERIFY.getCode());
            } else {
                // 已发布
                templateVo.setStatus(ContentStatusEnum.PUBLISH.getCode());
            }
        }

        // 发布时间
        templateVo.setResourcePublicationDate(new Date());
        // 保存内容
        boolean res = super.saveOrUpdate(templateVo);
        if (res && ContentStatusEnum.PUBLISH.getCode() == templateVo.getStatus()) {
            // 生成静态页面任务
            this.addStaticPageTask(templateVo);
            // 默认都创建索引, 索引任务
            this.addIndexTask(templateVo, toUpdate ? RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE : RabbitMQConstants.MQ_CMS_INDEX_COMMAND_ADD);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(String ids) {
        List<Map> mapList = JSONUtil.toList(JSONUtil.parseArray(ids), Map.class);
        // 删除元数据
        List<Map> metaDataMapList = mapList.stream().filter(m -> ObjectUtil.isNotNull(m.get("contentId"))).collect(Collectors.toList());
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
        // 检索用户ID
        List<String> userIdList = new ArrayList<>();
        String userId = UserContextHelper.getUserId();
        QueryWrapper<Catalog> countQueryWrapper = new QueryWrapper<>();
        countQueryWrapper.eq("parent_id", entity.getCmsCatalogId());
        int count = catalogService.count(countQueryWrapper);
        // 有子栏目
        if (count > 0) {
            // 用户分配的栏目
            CatalogUser catalogUser = new CatalogUser();
            catalogUser.setUserId(userId);
            Collection<CatalogUser> userCatalogs = catalogUserService.listByBean(catalogUser);
            if (CollectionUtil.isNotEmpty(userCatalogs)) {
                // 用户分配的栏目ID
                List<String> userCatalogIds = userCatalogs.stream().map(CatalogUser::getCatalogId).collect(Collectors.toList());
                // 检索当前栏目的所有子栏目
                Collection<Catalog> allCatalogList = catalogService.list();
                List<String> childrenCatalogIds = new ArrayList<>();
                getChildrenCatalogId(entity.getCmsCatalogId(), allCatalogList, childrenCatalogIds);
                // 有权限的子栏目ID
                catalogIdList = childrenCatalogIds.stream().filter(c -> userCatalogIds.contains(c)).collect(Collectors.toList());
            }
        }
        catalogIdList.add(entity.getCmsCatalogId());
        // 栏目内容权限
        TemplateUserAuthority templateUserAuthority = new TemplateUserAuthority();
        templateUserAuthority.setUserId(userId);
        Collection<TemplateUserAuthority> templateUserAuthoritys = templateUserAuthorityService.listByBean(templateUserAuthority);
        String authority = "";
        if (CollectionUtil.isNotEmpty(templateUserAuthoritys)) {
            Iterator<TemplateUserAuthority> iterator = templateUserAuthoritys.iterator();
            authority = iterator.next().getAuthority();
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
    public Integer resetTemplateIndex(String siteId, String start, String end, String part, int number) {
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id_");
        queryWrapper.eq("site_id", siteId);
        queryWrapper.between("DATE_FORMAT(resource_publication_date, '%Y-%m-%d')", start, end);
        queryWrapper.orderByAsc("resource_publication_date");
        int total = super.count(queryWrapper);
        if (total == 0) {
            return new Integer(0);
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
                for (Template t : resultPage.getRecords()) {
                    StringBuilder indexCode = new StringBuilder(part);
                    indexCode.append(String.format(format.toString(), value++));
                    t.setIndexCode(indexCode.toString());
                }
                result &= super.updateBatchById(list);
            }
        }
        if (result) {
            return new Integer(total);
        } else {
            return new Integer(0);
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
