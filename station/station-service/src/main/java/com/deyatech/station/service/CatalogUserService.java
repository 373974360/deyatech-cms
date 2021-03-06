package com.deyatech.station.service;

import com.deyatech.common.base.BaseService;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.entity.CatalogUser;
import com.deyatech.station.vo.CatalogUserVo;
import org.springframework.web.bind.annotation.RequestParam;

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
     * @param userIds
     * @param catalogIds
     */
    void setUsersCatalogs(List<String> userIds, List<String> catalogIds);

    /**
     * 取得用户栏目
     *
     * @param userIds
     * @return
     */
    List<String> getUsersCatalogs(List<String> userIds);

    /**
     * 删除用户栏目栏目根据栏目编号
     *
     * @param catalogIds
     * @return
     */
    int removeUserCatalogByCatalogIds(List<String> catalogIds);

    /**
     * 根据栏目ID获取栏目用户的权限列表
     *
     * @param catalogId
     * @return
     * */
    List<CatalogUserVo> getCatalogUserListByCatalogId(@RequestParam("catalogId") String catalogId);
}
