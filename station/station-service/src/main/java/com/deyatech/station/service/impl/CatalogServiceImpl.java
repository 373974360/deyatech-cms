package com.deyatech.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.common.Constants;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.entity.CatalogAggregation;
import com.deyatech.station.entity.CatalogRole;
import com.deyatech.station.entity.Template;
import com.deyatech.station.mapper.CatalogMapper;
import com.deyatech.station.service.CatalogAggregationService;
import com.deyatech.station.service.CatalogRoleService;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.station.vo.CatalogAggregationVo;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.template.feign.TemplateFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 栏目 服务实现类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-02
 */
@Service
public class CatalogServiceImpl extends BaseServiceImpl<CatalogMapper, Catalog> implements CatalogService {

    @Autowired
    private CatalogAggregationService catalogAggregationService;
    @Autowired
    TemplateService templateService;
    @Autowired
    AdminFeign adminFeign;
    @Autowired
    CatalogRoleService catalogRoleService;
    @Autowired
    TemplateFeign templateFeign;

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

    /**
     * 根据Catalog对象属性检索栏目的tree对象
     *
     * @param siteIds
     * @return
     */
    @Override
    public Collection<CatalogVo> getCatalogTreeBySiteIds(List<String> siteIds) {
        List<CatalogVo> catalogVos = baseMapper.getCatalogBySiteIds(siteIds);
        return getTree(catalogVos);
    }

    private List<CatalogVo> getTree(List<CatalogVo> catalogVos) {
        List<CatalogVo> rootCatalogs = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(catalogVos)) {
            for (CatalogVo catalogVo : catalogVos) {
                QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("cms_catalog_id", catalogVo.getId());
                catalogVo.setTemplateCount(templateService.count(queryWrapper));
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
        if(catalogVo.getFlagExternal() == 0){
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
        if (this.existsName(entity)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "当前栏目中已存在相同名称");
        }
        if (this.existsAliasName(entity)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "当前栏目中已存在相同别名");
        }
        if (this.existsEname(entity)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "当前栏目中已存在相同英文名称");
        }

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
        }

        // 保存或更新
        boolean parent = super.saveOrUpdate(entity);

        // 覆盖子栏目
        boolean children = true;
        if (BooleanUtil.isTrue(entity.getCoverage())) {
            // 装填子栏目信息
            List<Catalog> catalogList = this.coverageChildren(entity);
            // 更新子栏目
            children = super.updateBatchById(catalogList);
        }

        return aggregation && parent && children;
    }

    /**
     * 设置路径名
     */
    private void setCatalogPathName(Catalog entity) {
        String parentPathName = null;
        if (!"0".equals(entity.getParentId())) {
            Catalog catalogResult = super.getById(entity.getParentId());
            if (ObjectUtil.isNotNull(catalogResult)) {
                parentPathName = catalogResult.getEname();
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
        // 查询是否有子栏目，有子栏目删除子栏目
        for (String id : idList) {
            List<CatalogVo> children = this.getChildCatalogTree(id);
            this.setRemoveChildren(children, all);
        }

        return super.removeByIds(all);
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
    public Collection<CatalogVo> getUserCatalogTree(Catalog catalog) {
        List<CatalogVo> rootCatalogs = CollectionUtil.newArrayList();
        RestResult<List<String>> roleResult =  adminFeign.getRoleIdsByUserId(UserContextHelper.getUserId());
        List<String> roleIds = roleResult.getData();
        if (CollectionUtil.isEmpty(roleIds)) {
            throw new BusinessException( HttpStatus.HTTP_INTERNAL_ERROR, "没有分配角色");
        }
        QueryWrapper<CatalogRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds);
        Collection<CatalogRole> roleCatalogs = catalogRoleService.list(queryWrapper);

        if (CollectionUtil.isNotEmpty(roleCatalogs)) {
            List<String> catalogIds = roleCatalogs.stream().map(CatalogRole::getCatalogId).collect(Collectors.toList());

            catalog.setSortSql("sortNo asc");
            List<CatalogVo> catalogVos = setVoProperties(super.listByBean(catalog));
            if (CollectionUtil.isNotEmpty(catalogVos)) {
                List<CatalogVo> okCatalogVos = catalogVos.stream().filter(c -> catalogIds.contains(c.getId())).collect(Collectors.toList());

                for (CatalogVo catalogVo : okCatalogVos) {
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
                    for (CatalogVo childVo : okCatalogVos) {
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
        }
        return rootCatalogs;
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
}
