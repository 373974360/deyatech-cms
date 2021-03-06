package com.deyatech.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.deyatech.admin.entity.Dictionary;
import com.deyatech.admin.entity.Role;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.common.Constants;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.ContentOriginTypeEnum;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.*;
import com.deyatech.station.mapper.CatalogMapper;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.service.*;
import com.deyatech.station.vo.CatalogAggregationVo;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.template.feign.TemplateFeign;
import com.deyatech.workflow.entity.IProcessDefinition;
import com.deyatech.workflow.feign.WorkflowFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * <p>
 * 栏目 服务实现类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-02
 */
@Service
@Slf4j
public class CatalogServiceImpl extends BaseServiceImpl<CatalogMapper, Catalog> implements CatalogService {

    @Autowired
    CatalogAggregationService catalogAggregationService;
    @Autowired
    TemplateService templateService;
    @Autowired
    AdminFeign adminFeign;
    @Autowired
    CatalogUserService catalogUserService;
    @Autowired
    TemplateFeign templateFeign;
    @Autowired
    SiteCache siteCache;
    @Autowired
    WorkflowFeign workflowFeign;
    @Autowired
    private AmqpTemplate rabbitmqTemplate;
    @Autowired
    CatalogTemplateService catalogTemplateService;

    /**
     * 根据Catalog对象属性检索栏目的tree对象
     *
     * @param catalog
     * @return
     */
    @Override
    public Collection<CatalogVo> getCatalogTree(Catalog catalog) {
        catalog.setSortSql("sortNo asc");
        List<CatalogVo> catalogVos = setVoProperties(super.listByBean(catalog));
        return getTree(catalogVos);
    }
    @Override
    public Collection<CatalogVo> getAsyncCatalogTree(Catalog catalog) {
        if (StrUtil.isEmpty(catalog.getSiteId())) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "站点编号不存在");
        }
        if (StrUtil.isEmpty(catalog.getParentId())) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "站点父编号不存在");
        }
        long start = System.nanoTime();
        catalog.setSortSql("sortNo asc");
        List<CatalogVo> catalogVos = baseMapper.getCatalogList(catalog);
        if (CollectionUtil.isNotEmpty(catalogVos)) {
            final Map<String, String> childrenCatalogCountMap = new HashMap<>();
            final Map<String, String> columnTypeTreePositionMap = new HashMap<>();
            final Map<String, Integer> catalogTemplateCountMap = new HashMap<>();
            List<Map<String, Object>> childrenCatalogCountList =  baseMapper.getCountChildrenCatalog();
            if (CollectionUtil.isNotEmpty(childrenCatalogCountList)) {
                childrenCatalogCountList.stream().forEach(item -> childrenCatalogCountMap.put(item.get("catalogId").toString(), item.get("number").toString()));
            }
            List<Dictionary> dictionaryList = adminFeign.getDictionaryByIndexId("column_type").getData();
            if (CollectionUtil.isNotEmpty(dictionaryList)) {
                dictionaryList.stream().forEach(item -> columnTypeTreePositionMap.put(item.getId(), item.getTreePosition()));
            }
            List<Map<String, Object>> catalogTemplateCountList = templateService.countCatalogTemplate();
            if (CollectionUtil.isNotEmpty(catalogTemplateCountList)) {
                catalogTemplateCountList.stream().forEach(item -> catalogTemplateCountMap.put(item.get("catalogId").toString(), Integer.parseInt(item.get("number").toString())));
            }
            catalogVos.stream().forEach(c -> {
                if (StrUtil.isNotEmpty(c.getTreePosition())) {
                    String[] split = c.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                    c.setLevel(split.length);
                } else {
                    c.setLevel(Constants.DEFAULT_ROOT_LEVEL);
                }
                if (StrUtil.isNotEmpty(childrenCatalogCountMap.get(c.getId()))) {
                    c.setChildNum(childrenCatalogCountMap.get(c.getId()));
                    c.setLeaf(false);
                } else {
                    c.setChildNum(null);
                    c.setLeaf(true);
                }
                if(StrUtil.isNotEmpty(c.getColumnType())) {
                    c.setColumnTypeTreePosition(columnTypeTreePositionMap.get(c.getColumnType()));
                }
                if (Objects.isNull(catalogTemplateCountMap.get(c.getId()))) {
                    c.setTemplateCount(0);
                } else {
                    c.setTemplateCount(catalogTemplateCountMap.get(c.getId()));
                }
            });
        }
        log.info("栏目检索耗时：" + getMillisTime(System.nanoTime() - start) + "毫秒");
        return catalogVos;
    }
    private String getMillisTime(long time) {
        return String.valueOf(time / 1000000);
    }
    /**
     * 根据Catalog对象属性检索栏目的tree对象
     *
     * @param siteIds
     * @return
     */
    @Override
    public Collection<CatalogVo> getCatalogTreeBySiteIds(List<String> siteIds) {
        Collection<CatalogVo> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(siteIds)) {
            for (String siteId : siteIds) {
                Collection<CatalogVo> catalogVoCollection = siteCache.getCatalogTreeBySiteId(siteId);
                if (CollectionUtil.isNotEmpty(catalogVoCollection))
                    list.addAll(catalogVoCollection);
            }
        }
        if (CollectionUtil.isEmpty(list)) {
            List<CatalogVo> catalogVos = baseMapper.getCatalogBySiteIds(siteIds);
            return getTree(catalogVos);
        } else {
            return list;
        }
    }

    private List<CatalogVo> getTree(List<CatalogVo> catalogVos) {
        List<CatalogVo> rootCatalogs = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(catalogVos)) {
            for (CatalogVo catalogVo : catalogVos) {
                catalogVo.setLabel(catalogVo.getName());
                if(StrUtil.isNotBlank(catalogVo.getTreePosition())){
                    String[] split = catalogVo.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                    catalogVo.setLevel(split.length);
                }else{
                    catalogVo.setLevel(Constants.DEFAULT_ROOT_LEVEL);
                }
                if (ObjectUtil.equal(catalogVo.getParentId(), Constants.ZERO)) {
                    rootCatalogs.add(catalogVo);
                }
                for (CatalogVo childVo : catalogVos) {
                    if (ObjectUtil.equal(childVo.getParentId(), catalogVo.getId())) {
                        if (ObjectUtil.isNull(catalogVo.getChildren())) {
                            List<CatalogVo> children = CollectionUtil.newArrayList();
                            children.add(childVo);
                            catalogVo.setChildren(children);
                        } else {
                            catalogVo.getChildren().add(childVo);
                        }
                    }
                }
            }
        }
        return rootCatalogs;
    }
    /**
     * 单个将对象转换为vo栏目
     *
     * @param catalog
     * @return
     */
    @Override
    public CatalogVo setVoProperties(Catalog catalog){
        CatalogVo catalogVo = new CatalogVo();
        BeanUtil.copyProperties(catalog, catalogVo);
        String pageSuffix = templateFeign.getPageSuffix().getData();
        catalogVo.setIndexUrl("/"+catalogVo.getPathName()+"/index" + pageSuffix);
        if(catalogVo.getFlagExternal() == null || catalogVo.getFlagExternal() == 0){
            catalogVo.setLinkUrl("/"+catalogVo.getPathName()+"/list" + pageSuffix);
        }
        return catalogVo;
    }

    /**
     * 批量将对象转换为vo栏目
     *
     * @param catalogs
     * @return
     */
    @Override
    public List<CatalogVo> setVoProperties(Collection catalogs){
        String pageSuffix = templateFeign.getPageSuffix().getData();
        List<CatalogVo> catalogVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(catalogs)) {
            for (Object catalog : catalogs) {
                CatalogVo catalogVo = new CatalogVo();
                BeanUtil.copyProperties(catalog, catalogVo);
                // 装填聚合栏目
                if (YesNoEnum.YES.getCode().equals(catalogVo.getFlagAggregation())) {
                    CatalogAggregationVo catalogAggregationVo = catalogAggregationService.getCatalogAggregationById(catalogVo.getAggregationId());
                    catalogVo.setCatalogAggregation(catalogAggregationVo);
                }
                catalogVo.setIndexUrl("/"+catalogVo.getPathName()+"/index" + pageSuffix);
                if(catalogVo.getFlagExternal() == 0){
                    catalogVo.setLinkUrl("/"+catalogVo.getPathName()+"/list" + pageSuffix);
                }
                catalogVos.add(catalogVo);
            }
        }
        return catalogVos;
    }

    /**
     * 验证当前输入栏目名称是否已经存在
     *
     * @param catalog
     * @return
     */
    @Override
    public boolean existsName(Catalog catalog) {
        return this.getCatalogByDifferentNames(catalog);
    }

    /**
     * 验证当前输入栏目别名是否已经存在
     *
     * @param catalog
     * @return
     */
    @Override
    public boolean existsAliasName(Catalog catalog) {
        return this.getCatalogByDifferentNames(catalog);
    }

    /**
     * 验证当前输入栏目英文名称是否已经存在
     *
     * @param catalog
     * @return
     */
    @Override
    public boolean existsEname(Catalog catalog) {
        return this.getCatalogByDifferentNames(catalog);
    }

    private boolean getCatalogByDifferentNames(Catalog catalog) {
        QueryWrapper<Catalog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", catalog.getParentId())
                .eq("site_id", catalog.getSiteId());
        if (StrUtil.isNotEmpty(catalog.getId())) {
            queryWrapper.ne("id_", catalog.getId());
        }
        if (StrUtil.isNotEmpty(catalog.getName())) {
            queryWrapper.eq("name", catalog.getName());
        }
        else if (StrUtil.isNotEmpty(catalog.getAliasName())) {
            queryWrapper.eq("alias_name", catalog.getAliasName());
        }
        else if (StrUtil.isNotEmpty(catalog.getEname())) {
            queryWrapper.eq("ename", catalog.getEname());
        }
        return super.count(queryWrapper) > 0;
    }

    /**
     * 保存或更新
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(CatalogVo entity) {
        Catalog oldCatalog = null;
        if (StrUtil.isNotEmpty(entity.getId())) {
            oldCatalog = super.getById(entity.getId());
        }
        if (this.existsName(entity)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "当前栏目中已存在相同名称");
        }
        if (this.existsAliasName(entity)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "当前栏目中已存在相同别名");
        }
        if (this.existsEname(entity)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "当前栏目中已存在相同英文名称");
        }
        // 是否聚合
        boolean isNeedAggregation = this.isNeedAggregation(entity);
        // 设置排序号
        if (ObjectUtil.isNull(entity.getSortNo())) {
            int maxSortNo = baseMapper.selectMaxSortNo();
            entity.setSortNo(maxSortNo + 1);
        }
        // 设置路径名
        this.setCatalogPathName(entity);

        // 聚合栏目了，保存聚合栏目信息
        boolean aggregation = true;
        if (YesNoEnum.YES.getCode().equals(entity.getFlagAggregation())) {
            CatalogAggregation catalogAggregation = JSONUtil.toBean(entity.getCatalogAggregationInfo(), CatalogAggregation.class);
            aggregation = catalogAggregationService.saveOrUpdate(catalogAggregation);
            // 如果是插入数据， 回填aggregationId
            if (StrUtil.isEmpty(entity.getAggregationId())) {
                entity.setAggregationId(catalogAggregation.getId());
            }
        } else {
            // 删除旧聚合信息
            if (Objects.nonNull(oldCatalog) && StrUtil.isNotEmpty(oldCatalog.getAggregationId())) {
                catalogAggregationService.deleteCatalogAggregationById(oldCatalog.getAggregationId());
            }
            entity.setAggregationId("");
        }
        String catId = entity.getId();

        //修改栏目英文名重新命名文件夹
        if(StrUtil.isNotBlank(entity.getId())){
            if(!entity.getEname().equals(oldCatalog.getEname())){
                String oldFilePath = siteCache.getStationGroupRootPath(entity.getSiteId())+"/"+oldCatalog.getPathName();
                String newFilePath = siteCache.getStationGroupRootPath(entity.getSiteId())+"/"+entity.getPathName();
                File old = new File(oldFilePath);
                if (old.exists()) {
                    old.renameTo(new File(newFilePath));
                }
            }
        }
        // 保存或更新
        boolean parent = super.saveOrUpdate(entity);
        if(parent){
            if(StrUtil.isBlank(catId)){
                systemRoleAddCatalog(entity);
            }
            // 聚合规则变更
            if (isNeedAggregation) {
                Map<String, String> param = new HashMap<>();
                param.put("catalogId", entity.getId());
                param.put("aggregationId", entity.getAggregationId());
                rabbitmqTemplate.convertAndSend(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE, RabbitMQConstants.QUEUE_AGGREGATION_CATALOG_CHANGE, param);
            }
        }
        // 覆盖子栏目
        boolean children = true;
        if (BooleanUtil.isTrue(entity.getCoverage())) {
            // 装填子栏目信息
            List<Catalog> catalogList = this.coverageChildren(entity);
            if (CollectionUtil.isNotEmpty(catalogList)) {
                // 更新子栏目
                children = super.updateBatchById(catalogList);
            }
        }

        return aggregation && parent && children;
    }

    /**
     * 判断是否需要聚合
     *
     * @param entity
     * @return
     */
    private boolean isNeedAggregation(CatalogVo entity) {
        boolean isNeed = false;
        if (YesNoEnum.YES.getCode().equals(entity.getFlagAggregation())) {
            // 新增
            if (StrUtil.isEmpty(entity.getId())) {
                isNeed = true;
            } else {
                // 编辑
                if (StrUtil.isNotEmpty(entity.getAggregationId())) {
                    // 栏目
                    String newCatalogId = "";
                    String oldCatalogId = "";
                    // 关键字
                    String newKeyword = "";
                    String oldKeyword = "";
                    // 发布机构
                    String newOrganization = "";
                    String oldOrganization = "";
                    // 发布时间
                    String newTime = "";
                    String oldTime = "";
                    // 发布人
                    String newPublisher = "";
                    String oldPublisher = "";

                    CatalogAggregation newAgg = JSONUtil.toBean(entity.getCatalogAggregationInfo(), CatalogAggregation.class);
                    CatalogAggregation oldAgg = catalogAggregationService.getById(entity.getAggregationId());
                    // 新规则
                    if (Objects.nonNull(newAgg)) {
                        newCatalogId = Objects.isNull(newAgg.getCmsCatalogId()) ? "" : newAgg.getCmsCatalogId();
                        newKeyword = Objects.isNull(newAgg.getKeyword()) ? "" : newAgg.getKeyword();
                        newOrganization = Objects.isNull(newAgg.getPublishOrganization()) ? "" : newAgg.getPublishOrganization();
                        newPublisher = Objects.isNull(newAgg.getPublisher()) ? "" : newAgg.getPublisher();
                        newTime = Objects.isNull(newAgg.getPublishTime()) ? "" : newAgg.getPublishTime();
                    }
                    // 就规则
                    if (Objects.nonNull(oldAgg)) {
                        oldCatalogId = Objects.isNull(oldAgg.getCmsCatalogId()) ? "" : oldAgg.getCmsCatalogId();
                        oldKeyword = Objects.isNull(oldAgg.getKeyword()) ? "" : oldAgg.getKeyword();
                        oldOrganization = Objects.isNull(oldAgg.getPublishOrganization()) ? "" : oldAgg.getPublishOrganization();
                        oldTime = Objects.isNull(oldAgg.getPublishTime()) ? "" : oldAgg.getPublishTime();
                        oldPublisher = Objects.isNull(oldAgg.getPublisher()) ? "" : oldAgg.getPublisher();
                    }
                    List<String> newCatalogList = null;
                    List<String> oldCatalogList = null;
                    if (StrUtil.isNotEmpty(newCatalogId)) {
                        newCatalogList = Arrays.asList(newCatalogId.split("&"));
                    }
                    if (StrUtil.isNotEmpty(oldCatalogId)) {
                        oldCatalogList = Arrays.asList(oldCatalogId.split("&"));
                    }

                    List<String> newKeywordList = null;
                    List<String> oldKeywordList = null;
                    if (StrUtil.isNotEmpty(newKeyword)) {
                        newKeywordList = Arrays.asList(newKeyword.split(","));
                    }
                    if (StrUtil.isNotEmpty(oldKeyword)) {
                        oldKeywordList = Arrays.asList(oldKeyword.split(","));
                    }

                    // 规则变更
                    if (!listEquals(newCatalogList, oldCatalogList) ||
                            !listEquals(newKeywordList, oldKeywordList) ||
                            !newOrganization.equals(oldOrganization) ||
                            !newTime.equals(oldTime) ||
                            !newPublisher.equals(oldPublisher)) {
                        isNeed = true;
                    }
                }
            }
        }

        if (!isNeed && StrUtil.isNotEmpty(entity.getId())) {
            // 删除聚合数据
            CatalogTemplate catalogTemplate = new CatalogTemplate();
            catalogTemplate.setCatalogId(entity.getId());
            catalogTemplate.setOriginType(ContentOriginTypeEnum.AGGREGATION.getCode());
            catalogTemplateService.removeByBean(catalogTemplate);
        }

        return isNeed;
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
     * 给系统角色默认加上栏目权限
     */
    private void systemRoleAddCatalog(CatalogVo catalogVo){
        List<UserVo> userVoList = adminFeign.getUserListByType(3).getData();
        if(CollectionUtil.isNotEmpty(userVoList)){
            for(UserVo user:userVoList){
                CatalogUser catalogUser = new CatalogUser();
                catalogUser.setUserId(user.getId());
                catalogUser.setCatalogId(catalogVo.getId());
                catalogUserService.save(catalogUser);
            }
        }
    }

    /**
     * 设置路径名
     */
    private void setCatalogPathName(Catalog entity) {
        String parentPathName = null;
        if (!"0".equals(entity.getParentId())) {
            Catalog catalogResult = super.getById(entity.getParentId());
            if (ObjectUtil.isNotNull(catalogResult)) {
                parentPathName = catalogResult.getPathName();
            }
        }
        entity.setPathName(parentPathName == null ? entity.getEname() : (parentPathName + "/" + entity.getEname()));
    }

    /**
     * 装填子栏目信息
     * @param entity
     * @return
     */
    private List<Catalog> coverageChildren(CatalogVo entity) {
        List<Catalog> catalogList = new ArrayList<>();
        // 根据父分类编号查找子分类信息
        List<CatalogVo> catalogTree = this.getChildCatalogTree(entity.getId());
        this.coverageChildren(catalogTree, entity, catalogList);
        return  catalogList;
    }

    private void coverageChildren(Collection<CatalogVo> catalogTree, CatalogVo entity, List<Catalog> catalogList) {
        for (CatalogVo catalogVo : catalogTree) {
            // 非外链
            if (catalogVo.getFlagExternal() == 0) {
                Catalog children = new Catalog();
                // 复制对象
                BeanUtil.copyProperties(entity, children);
                // 一些子栏目原有属性不可修改
                children.setId(catalogVo.getId());
                children.setParentId(catalogVo.getParentId());
                children.setTreePosition(catalogVo.getTreePosition());
                children.setName(catalogVo.getName());
                children.setAliasName(catalogVo.getAliasName());
                children.setEname(catalogVo.getEname());
                children.setSortNo(catalogVo.getSortNo());
                children.setPathName(catalogVo.getPathName());
                children.setVersion(catalogVo.getVersion());
                // 设置路径名
                this.setCatalogPathName(children);
                catalogList.add(children);
            }
            if (CollectionUtil.isNotEmpty(catalogVo.getChildren())) {
                this.coverageChildren(catalogVo.getChildren(), entity, catalogList);
            }
        }
    }

    /**
     * 根据父分类编号查找子分类信息
     * @param catalogId
     * @return
     */
    private List<CatalogVo> getChildCatalogTree(String catalogId) {
        Catalog catalog = new Catalog();
        catalog.setSortSql("sortNo asc");
        List<CatalogVo> catalogVos = setVoProperties(super.listByBean(catalog));
        CatalogVo parentCategoryVo = null;
        if (CollectionUtil.isNotEmpty(catalogVos)) {
            for (CatalogVo catalogVo : catalogVos) {
                if (catalogVo.getId().equals(catalogId)) {
                    parentCategoryVo = catalogVo;
                }
                catalogVo.setLabel(catalogVo.getName());
                if(StrUtil.isNotBlank(catalogVo.getTreePosition())){
                    String[] split = catalogVo.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                    catalogVo.setLevel(split.length);
                }else{
                    catalogVo.setLevel(Constants.DEFAULT_ROOT_LEVEL);
                }
                for (CatalogVo childVo : catalogVos) {
                    if (ObjectUtil.equal(childVo.getParentId(), catalogVo.getId())) {
                        if (ObjectUtil.isNull(catalogVo.getChildren())) {
                            List<CatalogVo> children = CollectionUtil.newArrayList();
                            children.add(childVo);
                            catalogVo.setChildren(children);
                        } else {
                            catalogVo.getChildren().add(childVo);
                        }
                    }
                }
            }
        }
        return parentCategoryVo.getChildren();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(List<String> idList) {
        List<String> all = new ArrayList<>(idList);
        List<String> paths = new ArrayList<>();
        // 查询是否有子栏目，有子栏目删除子栏目
        for (String id : idList) {
            Catalog catalog = super.getById(id);
            String path = siteCache.getStationGroupRootPath(catalog.getSiteId());
            path.replace("\\", "/");
            if (!path.endsWith("/")) {
                path += "/";
            }
            path += catalog.getPathName();
            paths.add(path);
            List<CatalogVo> children = this.getChildCatalogTree(id);
            this.setRemoveChildren(children, all);
        }
        boolean result = super.removeByIds(all);
        if (result) {
            // 删除用户栏目
            catalogUserService.removeUserCatalogByCatalogIds(all);
            // 删除栏目静态页
            removeCatalogStaticPage(paths);
        }
        return result;
    }

    /**
     * 删除栏目静态页
     *
     * @param paths
     */
    private void removeCatalogStaticPage(List<String> paths) {
        if (CollectionUtil.isEmpty(paths)) {
            return;
        }
        for (String path : paths) {
            try {
                File file = new File(path);
                if (!file.exists()) {
                    continue;
                }
                Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).peek(System.out::println).map(Path::toFile).forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void setRemoveChildren(Collection<CatalogVo> children, List<String> all) {
        if (CollectionUtil.isNotEmpty(children)) {
            for (CatalogVo child : children) {
                all.add(child.getId());
                this.setRemoveChildren(child.getChildren(), all);
            }
        }
    }

    /**
     * 根据Catalog对象属性检索栏目的tree对象
     *
     * @param catalog
     * @return
     */
    @Override
    public List<CatalogVo> getUserCatalogTree(Catalog catalog, String userId) {
        if (StrUtil.isEmpty(catalog.getSiteId())) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "站点编号不存在");
        }
        if (StrUtil.isEmpty(catalog.getParentId())) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "站点父编号不存在");
        }
        List<CatalogVo> catalogs = baseMapper.getUserCatalogList(userId, catalog);
        setNumAndLeaf(catalogs);
        return catalogs;
    }

    /**
     * 根据Catalog对象属性检索栏目的tree对象
     *
     * @param catalog
     * @param departmentId
     * @return
     */
    @Override
    public List<CatalogVo> getDepartmentCatalogTree(Catalog catalog, String departmentId) {
        if (StrUtil.isEmpty(catalog.getSiteId())) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "站点编号不存在");
        }
        if (StrUtil.isEmpty(catalog.getParentId())) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "站点父编号不存在");
        }
        List<CatalogVo> catalogs = baseMapper.getDepartmentCatalogList(departmentId, catalog);
        setNumAndLeaf(catalogs);
        return catalogs;
    }

    /**
     * 设置数量和是否叶子
     *
     * @param catalogs
     */
    private void setNumAndLeaf(List<CatalogVo> catalogs) {
        if (CollectionUtil.isNotEmpty(catalogs)) {
            final Map<String, String> childrenCatalogCountMap = new HashMap<>();
            List<Map<String, Object>> childrenCatalogCountList =  baseMapper.getCountChildrenCatalog();
            if (CollectionUtil.isNotEmpty(childrenCatalogCountList)) {
                childrenCatalogCountList.stream().forEach(item -> childrenCatalogCountMap.put(item.get("catalogId").toString(), item.get("number").toString()));
            }
            catalogs.stream().forEach(c -> {
                if (StrUtil.isNotEmpty(childrenCatalogCountMap.get(c.getId()))) {
                    c.setChildNum(childrenCatalogCountMap.get(c.getId()));
                    c.setLeaf(false);
                } else {
                    c.setChildNum(null);
                    c.setLeaf(true);
                }
            });
        }
    }

    /**
     * 更新隐藏
     *
     * @param allowHidden
     * @param id
     * @return
     */
    public int updateAllowHiddenById(int allowHidden, String id) {return baseMapper.updateAllowHiddenById(allowHidden, id);}

    /**
     * 更新归档
     *
     * @param placeOnFile
     * @param id
     * @return
     */
    public int updatePlaceOnFileById(int placeOnFile, String id) {return baseMapper.updatePlaceOnFileById(placeOnFile, id);}

    /**
     * 清除工作流
     *
     * @param actDefinitionIdList
     */
    @Override
    public void clearWorkFlow(List<String> actDefinitionIdList) {
        if (CollectionUtil.isNotEmpty(actDefinitionIdList)) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.set("workflow_enable", 0);
            updateWrapper.set("workflow_key", null);
            updateWrapper.set("workflow_id", null);
            updateWrapper.in("workflow_id", actDefinitionIdList);
            super.update(updateWrapper);
        }
    }

    /**
     * 更新工作流
     *
     */
    @Override
    public void updateWorkFlow() {
        QueryWrapper<Catalog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("workflow_enable", 1);
        queryWrapper.isNotNull("workflow_id");
        List<Catalog> list = super.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            for (Catalog c : list) {
                RestResult<IProcessDefinition> restResult = workflowFeign.getActDefinitionIdAndKey(c.getWorkflowId());
                if (Objects.nonNull(restResult) && Objects.nonNull(restResult.getData())) {
                    IProcessDefinition definition = restResult.getData();
                    c.setWorkflowId(definition.getActDefinitionId());
                    c.setWorkflowKey(definition.getActDefinitionKey());
                    super.updateById(c);
                }
            }
        }
    }

    /**
     * 获取栏目使用的工作流ID
     *
     * @return
     */
    @Override
    public List<String> getAllCatalogWorkFlowId() {
        return baseMapper.getAllCatalogWorkFlowId();
    }




    /**
     * 复制子目录至目标目录
     *
     * @param sourceCatId
     * @param toCatId
     * @return
     */
    @Override
    public boolean copyChildrenCatalog(String sourceCatId, String toCatId) {
        //目标栏目
        Catalog toCatalog = this.getById(toCatId);
        Collection<Catalog> sourceCatalogs = this.getCatalogTreeByParentId(sourceCatId);
        return copyChildrenCatalog(sourceCatalogs,toCatalog);
    }
    public Collection<Catalog> getCatalogTreeByParentId(String catId) {
        QueryWrapper<Catalog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", catId);
        List<Catalog> catalogs = super.list(queryWrapper);
        return catalogs;
    }
    public boolean copyChildrenCatalog(Collection<Catalog> sourceCatalogVos,Catalog toCatalog){
        if(CollectionUtil.isNotEmpty(sourceCatalogVos)){
            for(Catalog catalog:sourceCatalogVos){
                if(!toCatalog.getId().equals(catalog.getId())){
                    catalog.setParentId(toCatalog.getId());
                    if (StrUtil.isEmpty(toCatalog.getTreePosition())) {
                        catalog.setTreePosition("&"+toCatalog.getId());
                    } else {
                        catalog.setTreePosition(toCatalog.getTreePosition()+"&"+toCatalog.getId());
                    }

                    Catalog newCatalog = new Catalog();
                    BeanUtil.copyProperties(catalog,newCatalog);
                    newCatalog.setId(null);
                    newCatalog.setPathName(toCatalog.getPathName()+"/"+newCatalog.getEname());
                    this.save(newCatalog);
                    //给系统角色默认加上栏目权限
                    systemRoleAddCatalog(setVoProperties(newCatalog));
                    Collection<Catalog> children = this.getCatalogTreeByParentId(catalog.getId());
                    if(CollectionUtil.isNotEmpty(children)){
                        copyChildrenCatalog(children,newCatalog);
                    }
                }

            }
        }
        return true;
    }
}
