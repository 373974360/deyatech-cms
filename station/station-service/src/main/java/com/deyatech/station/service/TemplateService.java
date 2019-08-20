package com.deyatech.station.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.station.entity.Template;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  内容模板 服务类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-06
 */
public interface TemplateService extends BaseService<Template> {

    /**
     * 单个将对象转换为vo内容模板
     *
     * @param template
     * @return
     */
    TemplateVo setVoProperties(Template template);

    /**
     * 批量将对象转换为vo内容模板
     *
     * @param templates
     * @return
     */
    List<TemplateVo> setVoProperties(Collection templates);

    /**
     * 保存或更新
     * @param templateVo
     * @return
     */
    boolean saveOrUpdateTemplateVo(TemplateVo templateVo);

    /**
     * 分页查询
     * @param entity
     * @return
     */
    IPage<TemplateVo> pageByTemplate(Template entity);

    /**
     * 判断Template对象标题是否存在
     *
     * @param template
     * @return
     */
    boolean checkTitleExist(Template template);

    /**
     * 生成静态页
     *
     * @param templateVo
     * @return
     */
    boolean genStaticPage(TemplateVo templateVo);

    /**
     * 生成索引
     *
     * @param templateVo
     * @return
     */
    boolean reindex(TemplateVo templateVo);

}
