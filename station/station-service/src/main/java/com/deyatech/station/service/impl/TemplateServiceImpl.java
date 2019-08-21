package com.deyatech.station.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PinyinUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.content.entity.ReviewProcess;
import com.deyatech.content.feign.ContentFeign;
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

        // 保存
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
            mapParams.put("contentId", templateVo.getId());
            mapParams.put("siteId", templateVo.getSiteId());
            processInstanceVo.setVariables(mapParams);
            workflowFeign.startInstance(processInstanceVo);
        }

        // 生成静态页面任务 TODO
        this.addStaticPageTask(templateVo);

        // 索引任务 TODO
        if (BooleanUtil.isTrue(templateVo.getFlagSearch())) {
            this.addIndexTask(templateVo, toUpdate ? RabbitMQConstants.MQ_CMS_INDEX_COMMAND_UPDATE : RabbitMQConstants.MQ_CMS_INDEX_COMMAND_ADD);
        } else {
            this.addIndexTask(templateVo, RabbitMQConstants.MQ_CMS_INDEX_COMMAND_DELETE);
        }

        return true;
    }

    /**
     * 添加生成静态页面任务到队列
     * @param template
     */
    private void addStaticPageTask(Template template) {

        rabbitmqTemplate.convertAndSend(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE, RabbitMQConstants.QUEUE_NAME_STATIC_PAGE_TASK, template);
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
}
