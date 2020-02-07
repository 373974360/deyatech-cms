package com.deyatech.statistics.mapper;

import com.deyatech.statistics.vo.CatalogDataVo;
import com.deyatech.statistics.vo.UserDataQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/2/5 15:16
 */
public interface CatalogDataMapper {

    /**
     * 栏目数据统计
     *
     * @param queryVo
     * @return
     */
    List<CatalogDataVo> getCatalogCountList(@Param("queryVo") UserDataQueryVo queryVo);
}
