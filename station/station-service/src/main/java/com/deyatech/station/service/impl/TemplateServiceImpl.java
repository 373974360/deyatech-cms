package com.deyatech.station.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PinyinUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.entity.Template;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.ModelService;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.station.mapper.TemplateMapper;
import com.deyatech.station.service.TemplateService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 单个将对象转换为vo内容模板
     *
     * @param template
     * @return
     */
    @Override
    public TemplateVo setVoProperties(Template template){
        TemplateVo templateVo = new TemplateVo();
        BeanUtil.copyProperties(template, templateVo);
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

    @Override
    public boolean saveOrUpdate(Template entity) {
        boolean toUpdate = StrUtil.isNotBlank(entity.getId());
        if (this.checkTitleExist(entity)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "当前栏目中已存在该标题内容");
        }

        if (StrUtil.isEmpty(entity.getUrl())) {
            if (BooleanUtil.isTrue(entity.getFlagExternal())) {
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "外链内容必须填写URL");
            }
            // 获取栏目信息
            Catalog catalog = catalogService.getById(entity.getCmsCatalogId());
            // 设置URL
            if (ObjectUtil.isNotNull(catalog)) {
                entity.setUrl("/" + catalog.getPathName() + "/" + PinyinUtil.getAllFirstLetter(entity.getTitle()) + ".html");
            } else {
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "未查到栏目相关信息");
            }
        }

        // 设置内容发布状态：1-草稿，2-已发布
        entity.setStatus(1);

        // 保存
        super.saveOrUpdate(entity);

        // 启动审核流程生命周期 TODO
/*        ContentReviewProcess contentReviewProcess = new ContentReviewProcess();
        String workflowId = (String) contentTemplateDto.getContent().get("workflowId");
        contentReviewProcess.setWorkflowId(workflowId);
        contentReviewProcess.setStatus(0);
        ContentTemplate contentTemplate = BeanMapper.map(contentTemplateDto, ContentTemplate.class);
        contentReviewProcess.setContentTemplate(contentTemplate);
        contentReviewProcessManager.save(contentReviewProcess);*/

        // 启动工作流 TODO
        /*ProcessInstanceVO processInstanceVO = new ProcessInstanceVO();
        processInstanceVO.setActDefinitionKey(workflowId);
        processInstanceVO.setBusinessId(String.valueOf(System.currentTimeMillis()));
        processInstanceVO.setSource("CMS");
        processInstanceVO.setStartTime(new Date());
        Map<String, Object> mapParams = new HashMap();
        mapParams.put("title", contentTemplateDto.getTitle());
        mapParams.put("author", contentTemplateDto.getAuthor());
        mapParams.put("contentId", contentTemplateDto.getId());
        mapParams.put("siteId", contentTemplateDto.getSiteId());
        processInstanceVO.setVariables(mapParams);
        processInstanceApi.startInstance(processInstanceVO);*/

        // 生成静态页面任务 TODO
        this.addStaticPageTask(entity);

        // 索引任务 TODO
        if (BooleanUtil.isTrue(entity.getFlagSearch())) {
            this.addIndexTask(entity, toUpdate ? RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE : RabbitMQConstants.MQ_CMS_INDEX_COMMAND_ADD);
        } else {
            this.addIndexTask(entity, RabbitMQConstants.MQ_CMS_INDEX_COMMAND_DELETE);
        }

        return true;
    }

    /**
     * 添加生成静态页面任务到队列
     * @param template
     */
    private void addStaticPageTask(Template template) {
        // TODO 设置其他附加属性this.setVoProperties(template）
        TemplateVo templateVo = this.setVoProperties(template);
        rabbitmqTemplate.convertAndSend(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE, RabbitMQConstants.QUEUE_NAME_STATIC_PAGE_TASK, templateVo);
    }

    /**
     * 添加索引任务到队列
     * @param template
     * @param code
     */
    private void addIndexTask(Template template, String code) {
        // TODO 设置其他附加属性this.setVoProperties(template）
        TemplateVo templateVo = this.setVoProperties(template);
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
}