package com.deyatech.station.feign;

import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.StationGroup;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/15 14:19
 */
@FeignClient(value = "station-service")
public interface StationFeign {


    /**
     * 获取站点模板根路径
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/feign/station/getStationGroupTemplatePathBySiteId", method = RequestMethod.GET)
    RestResult<String> getStationGroupTemplatePathBySiteId(@RequestParam("siteId") String siteId);
    /**
     * 获取站点根路径
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/feign/station/getStationGroupRootPath", method = RequestMethod.GET)
    RestResult<String> getStationGroupRootPath(@RequestParam("siteId") String siteId);
    /**
     * 获取站点
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/feign/station/getStationGroupById", method = RequestMethod.GET)
    RestResult<StationGroup> getStationGroupById(@RequestParam("siteId") String siteId);
}
