package com.deyatech.api.service.impl;

import cn.hutool.http.HttpStatus;
import com.deyatech.api.service.ZsdsApiService;
import com.deyatech.common.entity.RestResult;
import com.deyatech.zsds.feign.ZsdsFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * zsds api服务实现
 * @Author csm
 * @since 2019/11/11
 */
@Service
public class ZsdsApiServiceImpl implements ZsdsApiService {

    @Autowired
    ZsdsFeign zsdsFeign;

    /**
     * 传入一天时间查当天和前一天物流配送信息
     * @param dateStr
     * @return
     */
    @Override
    public RestResult getLogisticsInfoByDate(String dateStr) {
        RestResult restResult = zsdsFeign.getLogisticsInfoByDate(dateStr);
        if (restResult != null && HttpStatus.HTTP_OK == restResult.getStatus()) {
            return restResult;
        } else {
            return RestResult.error("接口调用失败");
        }
    }

    /**
     * 传入一天时间查当天和前一天在线培训信息
     * @param dateStr
     * @return
     */
    @Override
    public RestResult getOnlineInfoByDate(String dateStr) {
        RestResult restResult = zsdsFeign.getOnlineInfoByDate(dateStr);
        if (restResult != null && HttpStatus.HTTP_OK == restResult.getStatus()) {
            return restResult;
        } else {
            return RestResult.error("接口调用失败");
        }
    }

    /**
     * 传入一天时间查当天和前一天农产品信息
     * @param dateStr
     * @return
     */
    @Override
    public RestResult getAgriculturalInfoByDate(String dateStr) {
        RestResult restResult = zsdsFeign.getAgriculturalInfoByDate(dateStr);
        if (restResult != null && HttpStatus.HTTP_OK == restResult.getStatus()) {
            return restResult;
        } else {
            return RestResult.error("接口调用失败");
        }
    }

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public RestResult getLogisticsInfoByDateRange(String startDate, String endDate) {
        RestResult restResult = zsdsFeign.getLogisticsInfoByDateRange(startDate, endDate);
        if (restResult != null && HttpStatus.HTTP_OK == restResult.getStatus()) {
            return restResult;
        } else {
            return RestResult.error("接口调用失败");
        }
    }

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public RestResult getOnlineInfoByDateRange(String startDate, String endDate) {
        RestResult restResult = zsdsFeign.getOnlineInfoByDateRange(startDate, endDate);
        if (restResult != null && HttpStatus.HTTP_OK == restResult.getStatus()) {
            return restResult;
        } else {
            return RestResult.error("接口调用失败");
        }
    }

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public RestResult getAgriculturalInfoByDateRange(String startDate, String endDate) {
        RestResult restResult = zsdsFeign.getAgriculturalInfoByDateRange(startDate, endDate);
        if (restResult != null && HttpStatus.HTTP_OK == restResult.getStatus()) {
            return restResult;
        } else {
            return RestResult.error("接口调用失败");
        }
    }

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    @Override
    public RestResult getTodayLogisticsInfo() {
        RestResult restResult = zsdsFeign.getTodayLogisticsInfo();
        if (restResult != null && HttpStatus.HTTP_OK == restResult.getStatus()) {
            return restResult;
        } else {
            return RestResult.error("接口调用失败");
        }
    }

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    @Override
    public RestResult getTodayOnlineInfo() {
        RestResult restResult = zsdsFeign.getTodayOnlineInfo();
        if (restResult != null && HttpStatus.HTTP_OK == restResult.getStatus()) {
            return restResult;
        } else {
            return RestResult.error("接口调用失败");
        }
    }

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    @Override
    public RestResult getTodayAgriculturalInfo() {
        RestResult restResult = zsdsFeign.getTodayAgriculturalInfo();
        if (restResult != null && HttpStatus.HTTP_OK == restResult.getStatus()) {
            return restResult;
        } else {
            return RestResult.error("接口调用失败");
        }
    }
}
