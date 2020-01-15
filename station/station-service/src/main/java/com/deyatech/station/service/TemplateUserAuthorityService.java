package com.deyatech.station.service;

import com.deyatech.common.base.BaseService;
import com.deyatech.station.entity.TemplateUserAuthority;
import com.deyatech.station.vo.TemplateUserAuthorityVo;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  内容模板用户权限信息 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-11
 */
public interface TemplateUserAuthorityService extends BaseService<TemplateUserAuthority> {

    /**
     * 单个将对象转换为vo内容模板用户权限信息
     *
     * @param templateUserAuthority
     * @return
     */
    TemplateUserAuthorityVo setVoProperties(TemplateUserAuthority templateUserAuthority);

    /**
     * 批量将对象转换为vo内容模板用户权限信息
     *
     * @param templateUserAuthoritys
     * @return
     */
    List<TemplateUserAuthorityVo> setVoProperties(Collection templateUserAuthoritys);


    /**
     * 获取用户权限
     *
     * @param userIds
     * @return
     */
    String getUsersAuthority(List<String> userIds);

    /**
     * 设置用户权限
     *
     * @param userIds
     * @param authority
     */
    void setUsersAuthority(List<String> userIds, String authority);
}
