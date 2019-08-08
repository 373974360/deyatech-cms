package com.deyatech.station.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.station.entity.ModelTemplate;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.vo.ModelTemplateVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 内容模型模版 Mapper 接口
 * </p>
 *
 * @Author csm.
 * @since 2019-08-06
 */
public interface ModelTemplateMapper extends BaseMapper<ModelTemplate> {

    /**
     * 分页查询
     * @param page
     * @param modelTemplate
     * @return
     */
    IPage<ModelTemplateVo> pageByModelTemplate(@Param("page") Page<ModelTemplate> page, @Param("modelTemplate") ModelTemplate modelTemplate);

}
