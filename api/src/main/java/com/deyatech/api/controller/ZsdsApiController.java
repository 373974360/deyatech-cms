package com.deyatech.api.controller;

import com.deyatech.api.service.ZsdsApiService;
import com.deyatech.common.entity.RestResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * zsds api controller
 * @Author csm
 * @since 2019/11/11
 */

@RestController
@RequestMapping("/api/zsds")
@Slf4j
public class ZsdsApiController {

    @Autowired
    ZsdsApiService zsdsApiService;

    /**
     * 传入一天时间查当天和前一天物流配送信息
     * @param dateStr
     * @return
     */
    @GetMapping("/getLogisticsInfoByDate")
    @ApiOperation(value = "传入一天时间查当天和前一天物流配送信息", notes = "传入一天时间查当天和前一天物流配送信息")
    @ApiImplicitParam(name = "dateStr", value = "时间", required = true, dataType = "Date", paramType = "query")
    public RestResult getLogisticsInfoByDate(String dateStr) {
        return zsdsApiService.getLogisticsInfoByDate(dateStr);
    }

    /**
     * 传入一天时间查当天和前一天在线培训信息
     * @param dateStr
     * @return
     */
    @GetMapping("/getOnlineInfoByDate")
    @ApiOperation(value = "传入一天时间查当天和前一天在线培训信息", notes = "传入一天时间查当天和前一天在线培训信息")
    @ApiImplicitParam(name = "dateStr", value = "时间", required = true, dataType = "Date", paramType = "query")
    public RestResult getOnlineInfoByDate(String dateStr) {
        return zsdsApiService.getOnlineInfoByDate(dateStr);
    }

    /**
     * 传入一天时间查当天和前一天农产品信息
     * @param dateStr
     * @return
     */
    @GetMapping("/getAgriculturalInfoByDate")
    @ApiOperation(value = "传入一天时间查当天和前一天农产品信息", notes = "传入一天时间查当天和前一天农产品信息")
    @ApiImplicitParam(name = "dateStr", value = "时间", required = true, dataType = "Date", paramType = "query")
    public RestResult getAgriculturalInfoByDate(String dateStr) {
        return zsdsApiService.getAgriculturalInfoByDate(dateStr);
    }

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/getLogisticsInfoByDateRange")
    @ApiOperation(value = "传入时间段，按天分组，计算一天所有部门总和", notes = "传入时间段，按天分组，计算一天所有部门总和")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startDate", value = "开始时间", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "endDate", value = "结束时间", required = true, dataType = "String", paramType = "query")
    })
    public RestResult getLogisticsInfoByDateRange(String startDate, String endDate) {
        return zsdsApiService.getLogisticsInfoByDateRange(startDate, endDate);
    }

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/getOnlineInfoByDateRange")
    @ApiOperation(value = "传入时间段，按天分组，计算一天所有部门总和", notes = "传入时间段，按天分组，计算一天所有部门总和")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始时间", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = true, dataType = "String", paramType = "query")
    })
    public RestResult getOnlineInfoByDateRange(String startDate, String endDate) {
        return zsdsApiService.getOnlineInfoByDateRange(startDate, endDate);
    }

    /**
     * 传入时间段，按天分组，计算一天所有部门总和
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/getAgriculturalInfoByDateRange")
    @ApiOperation(value = "传入时间段，按天分组，计算一天所有部门总和", notes = "传入时间段，按天分组，计算一天所有部门总和")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始时间", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = true, dataType = "String", paramType = "query")
    })
    public RestResult getAgriculturalInfoByDateRange(String startDate, String endDate) {
        return zsdsApiService.getAgriculturalInfoByDateRange(startDate, endDate);
    }

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    @GetMapping("/getTodayLogisticsInfo")
    @ApiOperation(value = "查询当天按部门分组，计算总和", notes = "查询当天按部门分组，计算总和")
    public RestResult getTodayLogisticsInfo() {
        return zsdsApiService.getTodayLogisticsInfo();
    }

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    @GetMapping("/getTodayOnlineInfo")
    @ApiOperation(value = "查询当天按部门分组，计算总和", notes = "查询当天按部门分组，计算总和")
    public RestResult getTodayOnlineInfo() {
        return zsdsApiService.getTodayOnlineInfo();
    }

    /**
     * 查询当天按部门分组，计算总和
     * @return
     */
    @GetMapping("/getTodayAgriculturalInfo")
    @ApiOperation(value = "查询当天按部门分组，计算总和", notes = "查询当天按部门分组，计算总和")
    public RestResult getTodayAgriculturalInfo() {
        return zsdsApiService.getTodayAgriculturalInfo();
    }
}
