package com.deyatech.station.service;

import com.deyatech.station.entity.PageType;
import com.deyatech.station.vo.PageTypeVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-12-09
 */
public interface PageTypeService extends BaseService<PageType> {

    /**
     * 根据PageType对象属性检索的tree对象
     *
     * @param pageType
     * @return
     */
    Collection<PageTypeVo> getPageTypeTree(PageType pageType);

    /**
     * 单个将对象转换为vo
     *
     * @param pageType
     * @return
     */
    PageTypeVo setVoProperties(PageType pageType);

    /**
     * 批量将对象转换为vo
     *
     * @param pageTypes
     * @return
     */
    List<PageTypeVo> setVoProperties(Collection pageTypes);



    /**
     * 检查分类下是否存在页面
     *
     * @param id
     * @return
     */
    boolean checkPageExist(String id);
}
