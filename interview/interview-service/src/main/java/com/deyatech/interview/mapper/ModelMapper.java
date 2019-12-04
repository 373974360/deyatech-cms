package com.deyatech.interview.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.interview.entity.Model;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.interview.vo.ModelVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 访谈模型 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-08-26
 */
public interface ModelMapper extends BaseMapper<Model> {

    /**
     * 检索访谈模型根据分类和名称
     *
     * @param page
     * @param model
     * @return
     */
    IPage<ModelVo> pageByCategoryAndName(@Param("page") IPage<Model> page, @Param("siteId") String siteId,  @Param("model") Model model);
}
