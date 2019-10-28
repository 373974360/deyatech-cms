package com.deyatech.station.service;

import com.deyatech.station.entity.CatalogUser;
import com.deyatech.station.vo.CatalogUserVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  栏目用户关联信息 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-10
 */
public interface CatalogUserService extends BaseService<CatalogUser> {

    /**
     * 单个将对象转换为vo栏目用户关联信息
     *
     * @param catalogUser
     * @return
     */
    CatalogUserVo setVoProperties(CatalogUser catalogUser);

    /**
     * 批量将对象转换为vo栏目用户关联信息
     *
     * @param catalogUsers
     * @return
     */
    List<CatalogUserVo> setVoProperties(Collection catalogUsers);

    /**
     * 设置用户栏目
     *
     * @param userId
     * @param catalogIds
     * @param siteId
     */
    void setUserCatalogs(String userId, List<String> catalogIds, String siteId);
}
