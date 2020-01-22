package com.deyatech.statistics.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.statistics.entity.TemplateAccess;
import com.deyatech.statistics.vo.TemplateAccessVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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



    /**
     * 根据栏目统计访问量
     *
     * @param templateAccessVo
     * @return
     */
    IPage<TemplateAccessVo> getAccessCountByCatalog(TemplateAccessVo templateAccessVo);



    /**
     * 根据信息统计访问量
     *
     * @param templateAccessVo
     * @return
     */
    IPage<TemplateAccessVo> getAccessCountByInfo(TemplateAccessVo templateAccessVo);
}
