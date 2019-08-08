package com.deyatech.station.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.station.entity.ModelTemplate;
import com.deyatech.station.vo.ModelTemplateVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  内容模型模版 服务类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-06
 */
public interface ModelTemplateService extends BaseService<ModelTemplate> {

    /**
     * 单个将对象转换为vo内容模型模版
     *
     * @param modelTemplate
     * @return
     */
    ModelTemplateVo setVoProperties(ModelTemplate modelTemplate);

    /**
     * 批量将对象转换为vo内容模型模版
     *
     * @param modelTemplates
     * @return
     */
    List<ModelTemplateVo> setVoProperties(Collection modelTemplates);

    /**
     * 分页查询
     * @param modelTemplate
     * @return
     */
    IPage<ModelTemplateVo> pageByModelTemplate(ModelTemplate modelTemplate);

    /**
     * 根据ModelTemplate对象属性分页检索内容模型模版，按站点分组
     *
     * @param modelTemplate
     * @return
     */
    IPage<ModelTemplateVo> pageByModelTemplateGroupBySite(ModelTemplate modelTemplate);

    /**
     * 检查站点的内容模型是否已配置
     *
     * @param modelTemplate
     * @return
     */
    Boolean checkSiteContentModelExist(ModelTemplate modelTemplate);
}
