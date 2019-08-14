package com.deyatech.station.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.station.entity.Template;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.vo.TemplateVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 内容模板 Mapper 接口
 * </p>
 *
 * @Author csm.
 * @since 2019-08-06
 */
public interface TemplateMapper extends BaseMapper<Template> {

    /**
     * 分页查询
     * @param page
     * @param template
     * @return
     */
    IPage<TemplateVo> pageByTemplate(@Param("page") Page<Template> page, @Param("template") Template template);

}
