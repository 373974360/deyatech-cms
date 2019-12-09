package com.deyatech.generate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.generate.entity.Page;
import com.deyatech.generate.entity.PageType;
import com.deyatech.generate.service.PageService;
import com.deyatech.generate.vo.PageTypeVo;
import com.deyatech.generate.mapper.PageTypeMapper;
import com.deyatech.generate.service.PageTypeService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.Constants;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-12-09
 */
@Service
public class PageTypeServiceImpl extends BaseServiceImpl<PageTypeMapper, PageType> implements PageTypeService {

    @Autowired
    PageService pageService;

    /**
     * 根据PageType对象属性检索的tree对象
     *
     * @param pageType
     * @return
     */
    @Override
    public Collection<PageTypeVo> getPageTypeTree(PageType pageType) {
        pageType.setSortSql("sortNo asc");
        List<PageTypeVo> PageTypeVos = setVoProperties(super.listByBean(pageType));
        List<PageTypeVo> rootPageTypes = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(PageTypeVos)) {
            for (PageTypeVo PageTypeVo : PageTypeVos) {
                QueryWrapper<Page> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("type_id", PageTypeVo.getId());
                PageTypeVo.setCount(pageService.count(queryWrapper));
                PageTypeVo.setLabel(PageTypeVo.getName());
                if(StrUtil.isNotBlank(PageTypeVo.getTreePosition())){
                    String[] split = PageTypeVo.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                    PageTypeVo.setLevel(split.length);
                }else{
                    PageTypeVo.setLevel(Constants.DEFAULT_ROOT_LEVEL);
                }
                if (ObjectUtil.equal(PageTypeVo.getParentId(), Constants.ZERO)) {
                    rootPageTypes.add(PageTypeVo);
                }
                for (PageTypeVo childVo : PageTypeVos) {
                    if (ObjectUtil.equal(childVo.getParentId(), PageTypeVo.getId())) {
                        if (ObjectUtil.isNull(PageTypeVo.getChildren())) {
                            List<PageTypeVo> children = CollectionUtil.newArrayList();
                            children.add(childVo);
                            PageTypeVo.setChildren(children);
                        } else {
                            PageTypeVo.getChildren().add(childVo);
                        }
                    }
                }
            }
        }
        return rootPageTypes;
    }

    /**
     * 单个将对象转换为vo
     *
     * @param pageType
     * @return
     */
    @Override
    public PageTypeVo setVoProperties(PageType pageType){
        PageTypeVo pageTypeVo = new PageTypeVo();
        BeanUtil.copyProperties(pageType, pageTypeVo);
        return pageTypeVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param pageTypes
     * @return
     */
    @Override
    public List<PageTypeVo> setVoProperties(Collection pageTypes){
        List<PageTypeVo> pageTypeVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(pageTypes)) {
            for (Object pageType : pageTypes) {
                PageTypeVo pageTypeVo = new PageTypeVo();
                BeanUtil.copyProperties(pageType, pageTypeVo);
                pageTypeVos.add(pageTypeVo);
            }
        }
        return pageTypeVos;
    }



    @Override
    public boolean checkPageExist(String id) {
        Page page = new Page();
        page.setTypeId(id);
        return CollectionUtil.isNotEmpty(pageService.listByBean(page));
    }
}
