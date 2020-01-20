package com.deyatech.statistics.mapper;

import com.deyatech.statistics.entity.TemplateAccess;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.statistics.vo.TemplateAccessVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2020-01-19
 */
public interface TemplateAccessMapper extends BaseMapper<TemplateAccess> {


    int getAccessCountByCatalogCount(@Param("templateAccessVo")TemplateAccessVo templateAccessVo);
    List<TemplateAccessVo> getAccessCountByCatalog(@Param("templateAccessVo")TemplateAccessVo templateAccessVo);

    int getAccessCountByInfoCount(@Param("templateAccessVo")TemplateAccessVo templateAccessVo);
    List<TemplateAccessVo> getAccessCountByInfo(@Param("templateAccessVo")TemplateAccessVo templateAccessVo);
}
