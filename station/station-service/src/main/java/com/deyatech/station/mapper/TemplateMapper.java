package com.deyatech.station.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.station.entity.Template;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.vo.TemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
    IPage<TemplateVo> pageByTemplate(
            @Param("page") Page<Template> page,
            @Param("template") Template template,
            @Param("catalogIdList") List<String> catalogIdList,
            @Param("userIdList") List<String> userIdList);


    /**
     * 根据ID查询对象
     * @param id
     * @return
     */
    TemplateVo queryTemplateById(@Param("id") String id);


    /**
     * 分页查询
     * @param page
     * @param map
     * @return
     */
    IPage<TemplateVo> getTemplateListView(@Param("page") Page<Template> page,@Param("map") Map<String,Object> map);

    /**
     * 更新信息状态
     *
     * @param ids
     * @param status
     * @return
     */
    int updateStatusByIds(@Param("ids") List<String> ids, @Param("status") int status);

    /**
     * 更新权重
     *
     * @param sortNo
     * @param id
     * @return
     */
    int updateSortNoById(@Param("sortNo") int sortNo, @Param("id") String id);

    /**
     * 更新置顶
     *
     * @param flagTop
     * @param id
     * @return
     */
    int updateFlagTopById(@Param("flagTop") boolean flagTop, @Param("id") String id);
}
