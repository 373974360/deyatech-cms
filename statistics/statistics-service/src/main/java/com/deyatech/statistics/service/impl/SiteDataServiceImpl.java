package com.deyatech.statistics.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.statistics.mapper.SiteDataMapper;
import com.deyatech.statistics.service.SiteDataService;
import com.deyatech.statistics.vo.SiteDataVo;
import com.deyatech.statistics.vo.UserDataQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/2/5 15:15
 */
@Slf4j
@Service
public class SiteDataServiceImpl implements SiteDataService {

    @Autowired
    SiteDataMapper siteDataMapper;

    @Override
    public List<SiteDataVo> getSiteCountList(UserDataQueryVo queryVo) {
        List<SiteDataVo> siteDataVoList = siteDataMapper.getSiteCountList(queryVo);
        if(CollectionUtil.isNotEmpty(siteDataVoList)){
            for(SiteDataVo siteDataVo:siteDataVoList){
                //发稿率
                siteDataVo.setReleaseRate();
                //日平均发稿量
                String startTime = siteDataMapper.getSiteCountMinDate(queryVo);
                String endTime = siteDataMapper.getSiteCountMaxDate(queryVo);
                siteDataVo.setReleaseAverage(startTime,endTime);
            }
        }
        return siteDataVoList;
    }
}
