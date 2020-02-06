package com.deyatech.statistics.service;

import com.deyatech.statistics.vo.CatalogDataVo;
import com.deyatech.statistics.vo.UserDataQueryVo;

import java.util.List;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/2/5 15:14
 */
public interface CatalogDataService {

    /**
     * 栏目数据统计
     *
     * @param queryVo
     * @return
     */
    List<CatalogDataVo> getCatalogCountList(UserDataQueryVo queryVo);
}
