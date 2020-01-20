package com.deyatech.statistics.service.impl;

import com.deyatech.statistics.entity.TemplateAccess;
import com.deyatech.statistics.vo.TemplateAccessVo;
import com.deyatech.statistics.mapper.TemplateAccessMapper;
import com.deyatech.statistics.service.TemplateAccessService;
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
 * @since 2020-01-19
 */
@Service
public class TemplateAccessServiceImpl extends BaseServiceImpl<TemplateAccessMapper, TemplateAccess> implements TemplateAccessService {

    /**
     * 单个将对象转换为vo
     *
     * @param templateAccess
     * @return
     */
    @Override
    public TemplateAccessVo setVoProperties(TemplateAccess templateAccess){
        TemplateAccessVo templateAccessVo = new TemplateAccessVo();
        BeanUtil.copyProperties(templateAccess, templateAccessVo);
        return templateAccessVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param templateAccesss
     * @return
     */
    @Override
    public List<TemplateAccessVo> setVoProperties(Collection templateAccesss){
        List<TemplateAccessVo> templateAccessVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(templateAccesss)) {
            for (Object templateAccess : templateAccesss) {
                TemplateAccessVo templateAccessVo = new TemplateAccessVo();
                BeanUtil.copyProperties(templateAccess, templateAccessVo);
                templateAccessVos.add(templateAccessVo);
            }
        }
        return templateAccessVos;
    }
}
