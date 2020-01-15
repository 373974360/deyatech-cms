package com.deyatech.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.station.entity.TemplateUserAuthority;
import com.deyatech.station.mapper.TemplateUserAuthorityMapper;
import com.deyatech.station.service.TemplateUserAuthorityService;
import com.deyatech.station.vo.TemplateUserAuthorityVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 内容模板用户权限信息 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-11
 */
@Service
public class TemplateUserAuthorityServiceImpl extends BaseServiceImpl<TemplateUserAuthorityMapper, TemplateUserAuthority> implements TemplateUserAuthorityService {

    /**
     * 单个将对象转换为vo内容模板用户权限信息
     *
     * @param templateUserAuthority
     * @return
     */
    @Override
    public TemplateUserAuthorityVo setVoProperties(TemplateUserAuthority templateUserAuthority){
        TemplateUserAuthorityVo templateUserAuthorityVo = new TemplateUserAuthorityVo();
        BeanUtil.copyProperties(templateUserAuthority, templateUserAuthorityVo);
        return templateUserAuthorityVo;
    }

    /**
     * 批量将对象转换为vo内容模板用户权限信息
     *
     * @param templateUserAuthoritys
     * @return
     */
    @Override
    public List<TemplateUserAuthorityVo> setVoProperties(Collection templateUserAuthoritys){
        List<TemplateUserAuthorityVo> templateUserAuthorityVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(templateUserAuthoritys)) {
            for (Object templateUserAuthority : templateUserAuthoritys) {
                TemplateUserAuthorityVo templateUserAuthorityVo = new TemplateUserAuthorityVo();
                BeanUtil.copyProperties(templateUserAuthority, templateUserAuthorityVo);
                templateUserAuthorityVos.add(templateUserAuthorityVo);
            }
        }
        return templateUserAuthorityVos;
    }

    /**
     * 获取用户权限
     *
     * @param userIds
     * @return
     */
    @Override
    public String getUsersAuthority(List<String> userIds) {
        if (CollectionUtil.isEmpty(userIds)) {
            return null;
        }
        QueryWrapper<TemplateUserAuthority> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIds);
        List<TemplateUserAuthority> all = super.list(queryWrapper);
        if (CollectionUtil.isEmpty(all)) {
            return null;
        }
        if (userIds.size() == 1) {
            return all.stream().map(TemplateUserAuthority::getAuthority).distinct().collect(Collectors.toList()).get(0);
        }
        // 取出所有用户的交集权限
        Map<String, List<String>> userAuthorityMap = new HashMap<>();
        int minLen = Integer.MAX_VALUE;
        List<String> resultAuthority = null;
        for (String userId : userIds) {
            List<String> authorityIds = all.stream().filter(cu -> cu.getUserId().equals(userId)).map(TemplateUserAuthority::getAuthority).distinct().sorted(Comparator.comparing(authorityId -> authorityId)).collect(Collectors.toList());
            // 有一个用户没有，则交集为空
            if (CollectionUtil.isEmpty(authorityIds)) {
                return null;
            }
            if (minLen > authorityIds.size()) {
                minLen = authorityIds.size();
                resultAuthority = authorityIds;
            }
            userAuthorityMap.put(userId, authorityIds);
        }
        for (String userId : userIds) {
            resultAuthority.retainAll(userAuthorityMap.get(userId));
        }
        return resultAuthority.size() > 0 ? resultAuthority.get(0) : null;
    }

    /**
     * 设置用户权限
     *
     * @param userIds
     * @param authority
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setUsersAuthority(List<String> userIds, String authority) {
        if (CollectionUtil.isNotEmpty(userIds)) {
            for (String userId : userIds) {
                // 删除原来的
                TemplateUserAuthority templateUserAuthority = new TemplateUserAuthority();
                templateUserAuthority.setUserId(userId);
                super.removeByBean(templateUserAuthority);
                // 添加新的
                if (StrUtil.isNotEmpty(authority)) {
                    templateUserAuthority.setAuthority(authority);
                    super.saveOrUpdate(templateUserAuthority);
                }
            }
        }
    }
}
