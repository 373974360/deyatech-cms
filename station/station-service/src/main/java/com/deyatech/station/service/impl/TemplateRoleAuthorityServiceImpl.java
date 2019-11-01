package com.deyatech.station.service.impl;

import com.deyatech.station.entity.TemplateRoleAuthority;
import com.deyatech.station.vo.TemplateRoleAuthorityVo;
import com.deyatech.station.mapper.TemplateRoleAuthorityMapper;
import com.deyatech.station.service.TemplateRoleAuthorityService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 角色内容权限 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-01
 */
@Service
public class TemplateRoleAuthorityServiceImpl extends BaseServiceImpl<TemplateRoleAuthorityMapper, TemplateRoleAuthority> implements TemplateRoleAuthorityService {

    /**
     * 单个将对象转换为vo角色内容权限
     *
     * @param templateRoleAuthority
     * @return
     */
    @Override
    public TemplateRoleAuthorityVo setVoProperties(TemplateRoleAuthority templateRoleAuthority){
        TemplateRoleAuthorityVo templateRoleAuthorityVo = new TemplateRoleAuthorityVo();
        BeanUtil.copyProperties(templateRoleAuthority, templateRoleAuthorityVo);
        return templateRoleAuthorityVo;
    }

    /**
     * 批量将对象转换为vo角色内容权限
     *
     * @param templateRoleAuthoritys
     * @return
     */
    @Override
    public List<TemplateRoleAuthorityVo> setVoProperties(Collection templateRoleAuthoritys){
        List<TemplateRoleAuthorityVo> templateRoleAuthorityVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(templateRoleAuthoritys)) {
            for (Object templateRoleAuthority : templateRoleAuthoritys) {
                TemplateRoleAuthorityVo templateRoleAuthorityVo = new TemplateRoleAuthorityVo();
                BeanUtil.copyProperties(templateRoleAuthority, templateRoleAuthorityVo);
                templateRoleAuthorityVos.add(templateRoleAuthorityVo);
            }
        }
        return templateRoleAuthorityVos;
    }

    /**
     * 关联站点个数
     *
     * @param roleIds
     * @return
     */
    @Override
    public Map<String, String> getStationCount(List<String> roleIds) {return baseMapper.getStationCount(roleIds);}

    /**
     * 关联栏目个数
     *
     * @param roleIds
     * @return
     */
    @Override
    public Map<String, String> getCatalogCount(List<String> roleIds){return baseMapper.getCatalogCount(roleIds);}

    /**
     * 关联内容个数
     *
     * @param roleIds
     * @return
     */
    @Override
    public Map<String, String> getContentCount(List<String> roleIds) {return baseMapper.getContentCount(roleIds);}
}
