package com.deyatech.interview.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.interview.entity.Category;
import com.deyatech.interview.vo.CategoryVo;
import com.deyatech.interview.mapper.CategoryMapper;
import com.deyatech.interview.service.CategoryService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 访谈分类 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-26
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryMapper, Category> implements CategoryService {

    /**
     * 单个将对象转换为vo访谈分类
     *
     * @param category
     * @return
     */
    @Override
    public CategoryVo setVoProperties(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtil.copyProperties(category, categoryVo);
        return categoryVo;
    }

    /**
     * 批量将对象转换为vo访谈分类
     *
     * @param categorys
     * @return
     */
    @Override
    public List<CategoryVo> setVoProperties(Collection categorys){
        List<CategoryVo> categoryVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(categorys)) {
            for (Object category : categorys) {
                CategoryVo categoryVo = new CategoryVo();
                BeanUtil.copyProperties(category, categoryVo);
                categoryVos.add(categoryVo);
            }
        }
        return categoryVos;
    }

    /**
     * 检索访谈分类根据名称和站点
     *
     * @param category
     * @return
     */
    @Override
    public IPage<CategoryVo> pageByNameAndSiteId(Category category) {
        return baseMapper.pageByNameAndSiteId(getPageByBean(category), category);
    }

    /**
     * 检索访谈分类根据名称和站点
     *
     * @param category
     * @return
     */
    @Override
    public List<CategoryVo> listByNameAndSiteId(Category category) {
        return baseMapper.listByNameAndSiteId(category);
    }
}
