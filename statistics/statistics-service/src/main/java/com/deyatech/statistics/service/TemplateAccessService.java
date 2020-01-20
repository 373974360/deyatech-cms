package com.deyatech.statistics.service;

import com.deyatech.statistics.entity.TemplateAccess;
import com.deyatech.statistics.vo.TemplateAccessVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2020-01-19
 */
public interface TemplateAccessService extends BaseService<TemplateAccess> {

    /**
     * 单个将对象转换为vo
     *
     * @param templateAccess
     * @return
     */
    TemplateAccessVo setVoProperties(TemplateAccess templateAccess);

    /**
     * 批量将对象转换为vo
     *
     * @param templateAccesss
     * @return
     */
    List<TemplateAccessVo> setVoProperties(Collection templateAccesss);
}
