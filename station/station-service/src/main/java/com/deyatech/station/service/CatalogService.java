package com.deyatech.station.service;

import com.deyatech.station.entity.Catalog;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  栏目 服务类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-02
 */
public interface CatalogService extends BaseService<Catalog> {

    /**
     * 根据Catalog对象属性检索栏目的tree对象
     *
     * @param catalog
     * @return
     */
    Collection<CatalogVo> getCatalogTree(Catalog catalog);

    /**
     * 单个将对象转换为vo栏目
     *
     * @param catalog
     * @return
     */
    CatalogVo setVoProperties(Catalog catalog);

    /**
     * 批量将对象转换为vo栏目
     *
     * @param catalogs
     * @return
     */
    List<CatalogVo> setVoProperties(Collection catalogs);

    /**
     * 验证当前输入栏目名称是否已经存在
     *
     * @param catalog
     * @return
     */
    boolean existsName(Catalog catalog);

    /**
     * 验证当前输入栏目别名是否已经存在
     *
     * @param catalog
     * @return
     */
    boolean existsAliasName(Catalog catalog);

    /**
     * 验证当前输入栏目英文名称是否已经存在
     *
     * @param catalog
     * @return
     */
    boolean existsEname(Catalog catalog);

    /**
     * 保存或更新
     * @param entity
     * @return
     */
    boolean saveOrUpdate(CatalogVo entity);

    /**
     * 根据id批量删除
     * @param idList
     * @return
     */
    boolean removeByIds(List<String> idList);

    /**
     * 根据Catalog对象属性检索栏目的tree对象
     *
     * @param catalog
     * @return
     */
    Collection<CatalogVo> getUserCatalogTree(Catalog catalog);
}
