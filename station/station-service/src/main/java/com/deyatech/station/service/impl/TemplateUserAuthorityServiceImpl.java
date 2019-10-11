package com.deyatech.station.service.impl;

import com.deyatech.station.entity.TemplateUserAuthority;
import com.deyatech.station.vo.TemplateUserAuthorityVo;
import com.deyatech.station.mapper.TemplateUserAuthorityMapper;
import com.deyatech.station.service.TemplateUserAuthorityService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

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
}
