package com.deyatech.generate.mapper;

import com.deyatech.common.base.BaseMapper;
import com.deyatech.generate.entity.PageCatalog;
import org.apache.ibatis.annotations.Param;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/9/10 18:19
 */
public interface PageCatalogMapper extends BaseMapper<PageCatalog> {

    void clearPageCatalogById(@Param("pageId") String pageId);

}
