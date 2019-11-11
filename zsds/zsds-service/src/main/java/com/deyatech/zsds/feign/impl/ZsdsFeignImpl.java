package com.deyatech.zsds.feign.impl;

import com.deyatech.common.entity.RestResult;
import com.deyatech.zsds.feign.ZsdsFeign;
import com.deyatech.zsds.service.AgriculturalProductsUpDownService;
import com.deyatech.zsds.service.LogisticsDistributionService;
import com.deyatech.zsds.service.OnlineTrainingService;
import com.deyatech.zsds.vo.AgriculturalProductsUpDownVo;
import com.deyatech.zsds.vo.LogisticsDistributionVo;
import com.deyatech.zsds.vo.OnlineTrainingVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author csm
 * @since 2019/11/11
 */
@RestController
@Slf4j
public class ZsdsFeignImpl implements ZsdsFeign {

    @Autowired
    private LogisticsDistributionService logisticsDistributionService;

    @Autowired
    private OnlineTrainingService onlineTrainingService;

    @Autowired
    private AgriculturalProductsUpDownService agriculturalProductsUpDownService;


    @Override
    public RestResult getLogisticsInfoByDate(String dateStr) {
        List<LogisticsDistributionVo> logisticsDistributionVos = logisticsDistributionService.getLogisticsInfoByDate(dateStr);
        return RestResult.ok(logisticsDistributionVos);
    }

    @Override
    public RestResult getOnlineInfoByDate(String dateStr) {
        List<OnlineTrainingVo> onlineTrainingVos = onlineTrainingService.getOnlineInfoByDate(dateStr);
        return RestResult.ok(onlineTrainingVos);
    }

    @Override
    public RestResult getAgriculturalInfoByDate(String dateStr) {
        List<AgriculturalProductsUpDownVo> agriculturalProductsUpDownVos = agriculturalProductsUpDownService.getAgriculturalInfoByDate(dateStr);
        return RestResult.ok(agriculturalProductsUpDownVos);
    }

    @Override
    public RestResult getLogisticsInfoByDateRange(String startDate, String endDate) {
        List<LogisticsDistributionVo> logisticsDistributionVos = logisticsDistributionService.getLogisticsInfoByDateRange(startDate, endDate);
        return RestResult.ok(logisticsDistributionVos);
    }

    @Override
    public RestResult getOnlineInfoByDateRange(String startDate, String endDate) {
        List<OnlineTrainingVo> onlineTrainingVos = onlineTrainingService.getOnlineInfoByDateRange(startDate, endDate);
        return RestResult.ok(onlineTrainingVos);
    }

    @Override
    public RestResult getAgriculturalInfoByDateRange(String startDate, String endDate) {
        List<AgriculturalProductsUpDownVo> agriculturalProductsUpDownVos = agriculturalProductsUpDownService.getAgriculturalInfoByDateRange(startDate, endDate);
        return RestResult.ok(agriculturalProductsUpDownVos);
    }

    @Override
    public RestResult getTodayLogisticsInfo() {
        List<LogisticsDistributionVo> logisticsDistributionVos = logisticsDistributionService.getTodayLogisticsInfo();
        return RestResult.ok(logisticsDistributionVos);
    }

    @Override
    public RestResult getTodayOnlineInfo() {
        List<OnlineTrainingVo> onlineTrainingVos = onlineTrainingService.getTodayOnlineInfo();
        return RestResult.ok(onlineTrainingVos);
    }

    @Override
    public RestResult getTodayAgriculturalInfo() {
        List<AgriculturalProductsUpDownVo> agriculturalProductsUpDownVos = agriculturalProductsUpDownService.getTodayAgriculturalInfo();
        return RestResult.ok(agriculturalProductsUpDownVos);
    }
}
