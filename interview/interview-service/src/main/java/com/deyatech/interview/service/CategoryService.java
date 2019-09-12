package com.deyatech.interview.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.interview.entity.Category;
import com.deyatech.interview.vo.CategoryVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  访谈分类 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-26
 */
public interface CategoryService extends BaseService<Category> {

    /**
     * 单个将对象转换为vo访谈分类
     *
     * @param category
     * @return
     */
    CategoryVo setVoProperties(Category category);

    /**
     * 批量将对象转换为vo访谈分类
     *
     * @param categorys
     * @return
     */
    List<CategoryVo> setVoProperties(Collection categorys);

    /**
     * 检索访谈分类根据名称和站点
     *
     * @param category
     * @return
     */
    IPage<CategoryVo> pageByNameAndSiteId(Category category);

    /**
     * 检索访谈分类根据名称和站点
     *
     * @param category
     * @return
     */
    List<CategoryVo> listByNameAndSiteId(Category category);
}
