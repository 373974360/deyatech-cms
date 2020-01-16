package com.deyatech.apply.service.impl;

import com.deyatech.apply.entity.OpenReplyTemplate;
import com.deyatech.apply.vo.OpenReplyTemplateVo;
import com.deyatech.apply.mapper.OpenReplyTemplateMapper;
import com.deyatech.apply.service.OpenReplyTemplateService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2020-01-16
 */
@Service
public class OpenReplyTemplateServiceImpl extends BaseServiceImpl<OpenReplyTemplateMapper, OpenReplyTemplate> implements OpenReplyTemplateService {

    /**
     * 单个将对象转换为vo
     *
     * @param openReplyTemplate
     * @return
     */
    @Override
    public OpenReplyTemplateVo setVoProperties(OpenReplyTemplate openReplyTemplate){
        OpenReplyTemplateVo openReplyTemplateVo = new OpenReplyTemplateVo();
        BeanUtil.copyProperties(openReplyTemplate, openReplyTemplateVo);
        return openReplyTemplateVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param openReplyTemplates
     * @return
     */
    @Override
    public List<OpenReplyTemplateVo> setVoProperties(Collection openReplyTemplates){
        List<OpenReplyTemplateVo> openReplyTemplateVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(openReplyTemplates)) {
            for (Object openReplyTemplate : openReplyTemplates) {
                OpenReplyTemplateVo openReplyTemplateVo = new OpenReplyTemplateVo();
                BeanUtil.copyProperties(openReplyTemplate, openReplyTemplateVo);
                openReplyTemplateVos.add(openReplyTemplateVo);
            }
        }
        return openReplyTemplateVos;
    }
}
