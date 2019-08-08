package com.deyatech.resource.feign;

import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.StationGroup;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述：resouorve模块feign远程调用类
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/6 14:57
 */
@FeignClient(value = "resource-service")
public interface ResourceFeign {


    /**
     * 根据站点ID返回站点信息
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/feign/resource/getStationGroupById", method = RequestMethod.GET)
    RestResult<StationGroup> getStationGroupById(@RequestParam("siteId") String siteId);
}
