package com.deyatech.station.service.impl;

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
    public String existsName(Catalog catalog) {
        Catalog catalogResult = this.getCatalogByDifferentNames(catalog);
        if (ObjectUtil.isNotNull(catalogResult) && !catalogResult.getId().equals(catalog.getId())) {
            String message = "栏目名称已存在";
            return message;
        }
        return null;
    }

    /**
     * 验证当前输入栏目别名是否已经存在
     *
     * @param catalog
     * @return
     */
    @Override
    public String existsAliasName(Catalog catalog) {
        Catalog catalogResult = this.getCatalogByDifferentNames(catalog);
        if (ObjectUtil.isNotNull(catalogResult) && !catalogResult.getId().equals(catalog.getId())) {
            String message = "栏目别名已存在";
            return message;
        }
        return null;
    }

    /**
     * 验证当前输入栏目英文名称是否已经存在
     *
     * @param catalog
     * @return
     */
    @Override
    public String existsEname(Catalog catalog) {
        Catalog catalogResult = this.getCatalogByDifferentNames(catalog);
        if (ObjectUtil.isNotNull(catalogResult) && !catalogResult.getId().equals(catalog.getId())) {
            String message = "栏目英文名称已存在";
            return message;
        }
        return null;
    }

    private Catalog getCatalogByDifferentNames(Catalog catalog) {
        Catalog catalogQuery = new Catalog();
        catalogQuery.setParentId(catalog.getParentId());
        catalogQuery.setSiteId(catalog.getSiteId());
        if (StrUtil.isNotEmpty(catalog.getName())) {
            catalogQuery.setName(catalog.getName());
        }
        else if (StrUtil.isNotEmpty(catalog.getAliasName())) {
            catalogQuery.setAliasName(catalog.getAliasName());
        }
        else if (StrUtil.isNotEmpty(catalog.getEname())) {
            catalogQuery.setEname(catalog.getEname());
        }
        Catalog catalogResult = super.getByBean(catalogQuery);
        return catalogResult;
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
