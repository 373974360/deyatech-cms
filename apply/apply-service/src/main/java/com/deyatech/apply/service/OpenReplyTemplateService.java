package com.deyatech.apply.service;

import com.deyatech.apply.entity.OpenReplyTemplate;
import com.deyatech.apply.vo.OpenReplyTemplateVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2020-01-16
 */
public interface OpenReplyTemplateService extends BaseService<OpenReplyTemplate> {

    /**
     * 单个将对象转换为vo
     *
     * @param openReplyTemplate
     * @return
     */
    OpenReplyTemplateVo setVoProperties(OpenReplyTemplate openReplyTemplate);

    /**
     * 批量将对象转换为vo
     *
     * @param openReplyTemplates
     * @return
     */
    List<OpenReplyTemplateVo> setVoProperties(Collection openReplyTemplates);
}
