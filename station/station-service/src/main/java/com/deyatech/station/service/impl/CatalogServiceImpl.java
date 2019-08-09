package com.deyatech.station.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.mapper.CatalogMapper;
import com.deyatech.station.service.CatalogService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.Constants;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

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
    CatalogMapper catalogMapper;
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
        List<CatalogVo> catalogVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(catalogs)) {
            for (Object catalog : catalogs) {
                CatalogVo catalogVo = new CatalogVo();
                BeanUtil.copyProperties(catalog, catalogVo);
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

    @Override
    public boolean saveOrUpdate(Catalog entity) {
        // 设置排序号
        if (ObjectUtil.isNull(entity.getSortNo())) {
            int maxSortNo = catalogMapper.selectMaxSortNo();
            entity.setSortNo(maxSortNo + 1);
        }
        // 设置路径名
        String parentPathName = null;
        if (!"0".equals(entity.getParentId())) {
            Catalog catalogResult = super.getById(entity.getParentId());
            if (ObjectUtil.isNotNull(catalogResult)) {
                parentPathName = catalogResult.getEname();
            }
        }
        entity.setPathName(parentPathName == null ? entity.getEname() : (parentPathName + "/" + entity.getEname()));

        // TODO 发送栏目修改的消息
//        jmsTemplate.convertAndSend(JmsConfig.TOPIC_CMS, AppMessage.newAppMessage(ActiveMQConstats.MQ_MESSAGE_CODE_CATALOG_EDIT, cmsCatalog.getId()));

        return super.saveOrUpdate(entity);
    }
}
