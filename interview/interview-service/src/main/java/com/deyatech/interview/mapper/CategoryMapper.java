package com.deyatech.interview.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.interview.entity.Category;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.interview.vo.CategoryVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 访谈分类 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-08-26
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 检索访谈分类根据名称和站点
     *
     * @param page
     * @param category
     * @return
     */
    IPage<CategoryVo> pageByNameAndSiteId(@Param("page") IPage<Category> page, @Param("category") Category category);
}
