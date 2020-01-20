package com.deyatech.statistics.controller;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/19 17:12
 */
import com.deyatech.common.entity.RestResult;
import com.deyatech.statistics.baidu.BaiDuAccess;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/statistics/baiduAccess")
@Api(tags = {"接口"})
public class BaiDuAccessController {

    @Autowired
    BaiDuAccess baiDuAccess;

    /**
     * 今日流量
     *
     * @return
     */
    @GetMapping("/getOutline")
    public RestResult getOutline(HttpServletRequest request) {
        return RestResult.ok(baiDuAccess.getVisit("overview/getOutline",request));
    }

    /**
     * top 10搜索词
     *
     * @return
     */
    @GetMapping("/getWord")
    public RestResult getWord(HttpServletRequest request) {
        return RestResult.ok(baiDuAccess.getVisit("overview/getWord",request));
    }

    /**
     * top 10受访页面
     *
     * @return
     */
    @GetMapping("/getVisitPage")
    public RestResult getVisitPage(HttpServletRequest request) {
        return RestResult.ok(baiDuAccess.getVisit("overview/getVisitPage",request));
    }

    /**
     * top 10来源网站
     *
     * @return
     */
    @GetMapping("/getSourceSite")
    public RestResult getSourceSite(HttpServletRequest request) {
        return RestResult.ok(baiDuAccess.getVisit("overview/getSourceSite",request));
    }

    /**
     * top 10 入口页面
     *
     * @return
     */
    @GetMapping("/getLandingPage")
    public RestResult getLandingPage(HttpServletRequest request) {
        return RestResult.ok(baiDuAccess.getVisit("overview/getLandingPage",request));
    }

    /**
     * 新老访客
     *
     * @return
     */
    @GetMapping("/getVisitorType")
    public RestResult getVisitorType(HttpServletRequest request) {
        return RestResult.ok(baiDuAccess.getVisit("overview/getVisitorType",request));
    }

    /**
     * 地域分布
     *
     * @return
     */
    @GetMapping("/getDistrictRpt")
    public RestResult getDistrictRpt(HttpServletRequest request) {
        return RestResult.ok(baiDuAccess.getVisit("overview/getDistrictRpt",request));
    }

    /**
     * 趋势图
     *
     * @return
     */
    @GetMapping("/getTimeTrendRpt")
    public RestResult getTimeTrendRpt(HttpServletRequest request) {
        return RestResult.ok(baiDuAccess.getVisit("overview/getTimeTrendRpt",request));
    }

    /**
     * 年龄分布
     *
     * @return
     */
    @GetMapping("/getAge")
    public RestResult getAge(HttpServletRequest request) {
        return RestResult.ok(baiDuAccess.getVisit("overview/getAge",request));
    }
}
