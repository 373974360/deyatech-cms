package com.deyatech.zsds.feign;

import com.deyatech.common.entity.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author csm
 * @since 2019/11/11
 */
@FeignClient(value = "zsds-service")
public interface ZsdsFeign {


    /**
     * 传入一天时间查当天和前一天物流配送信息
     * @param dateStr
     * @return
     */
    @RequestMapping(value = "/feign/zsds/logisticsDistribution/getLogisticsInfoByDate")
    RestResult getLogisticsInfoByDate(@RequestParam("dateStr") String dateStr);

    /**
     * 传入一天时间查当天和前一天在线培训信息
     * @param dateStr
     * @return
     */
    @RequestMapping(value = "/feign/zsds/onlineTraining/getOnlineInfoByDate")
    RestResult getOnlineInfoByDate(@RequestParam("dateStr") String dateStr);

    /**
     * 传入一天时间查当天和前一天农产品信息
     * @param dateStr
     * @return
     */
    @RequestMapping(value = "/feign/zsds/agriculturalProductsUpDown/getAgriculturalInfoByDate")
    RestResult getAgriculturalInfoByDate(@RequestParam("dateStr") String dateStr);

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/feign/zsds/logisticsDistribution/getLogisticsInfoByDateRange")
    RestResult getLogisticsInfoByDateRange(@RequestParam("startDate") String startDate, @RequestParam("endDate")String endDate);

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/feign/zsds/onlineTraining/getOnlineInfoByDateRange")
    RestResult getOnlineInfoByDateRange(@RequestParam("startDate") String startDate, @RequestParam("endDate")String endDate);

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/feign/zsds/agriculturalProductsUpDown/getAgriculturalInfoByDateRange")
    RestResult getAgriculturalInfoByDateRange(@RequestParam("startDate") String startDate, @RequestParam("endDate")String endDate);

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    @RequestMapping(value = "/feign/zsds/logisticsDistribution/getTodayLogisticsInfo")
    RestResult getTodayLogisticsInfo();

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    @RequestMapping(value = "/feign/zsds/onlineTraining/getTodayOnlineInfo")
    RestResult getTodayOnlineInfo();

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    @RequestMapping(value = "/feign/zsds/agriculturalProductsUpDown/getTodayAgriculturalInfo")
    RestResult getTodayAgriculturalInfo();
}
