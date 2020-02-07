package com.deyatech.statistics.mapper;

import com.deyatech.statistics.vo.SiteDataVo;
import com.deyatech.statistics.vo.UserDataQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/2/5 15:16
 */
public interface SiteDataMapper {

    /**
     * 站点数据统计
     *
     * @param queryVo
     * @return
     */
    List<SiteDataVo> getSiteCountList(@Param("queryVo") UserDataQueryVo queryVo);
}
