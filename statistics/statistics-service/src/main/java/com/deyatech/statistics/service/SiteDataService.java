package com.deyatech.statistics.service;

import com.deyatech.statistics.vo.SiteDataVo;
import com.deyatech.statistics.vo.UserDataQueryVo;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/2/5 15:14
 */
public interface SiteDataService {

    /**
     * 站点数据统计
     *
     * @param queryVo
     * @return
     */
    List<SiteDataVo> getSiteCountList(UserDataQueryVo queryVo);
}
