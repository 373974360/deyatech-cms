package com.deyatech.api.service;

import com.deyatech.common.entity.RestResult;

/**
 * zsds api服务
 * @Author csm
 * @since 2019/11/11
 */
public interface ZsdsApiService {

    /**
     * 传入一天时间查当天和前一天物流配送信息
     * @param dateStr
     * @return
     */
    RestResult getLogisticsInfoByDate(String dateStr);

    /**
     * 传入一天时间查当天和前一天在线培训信息
     * @param dateStr
     * @return
     */
    RestResult getOnlineInfoByDate(String dateStr);

    /**
     * 传入一天时间查当天和前一天农产品信息
     * @param dateStr
     * @return
     */
    RestResult getAgriculturalInfoByDate(String dateStr);

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    RestResult getLogisticsInfoByDateRange(String startDate, String endDate);

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    RestResult getOnlineInfoByDateRange(String startDate, String endDate);

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    RestResult getAgriculturalInfoByDateRange(String startDate, String endDate);

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    RestResult getTodayLogisticsInfo();

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    RestResult getTodayOnlineInfo();

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    RestResult getTodayAgriculturalInfo();
}
