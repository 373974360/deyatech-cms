package com.deyatech.resource.feign;

import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.StationGroup;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 描述：resouorve模块feign远程调用类
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/6 14:57
 */
@FeignClient(value = "resource-service")
public interface ResourceFeign {
    /**
     * 查询所有站点列表
     *
     * @return
     */
    @RequestMapping(value = "/feign/resource/getStationGroupAll", method = RequestMethod.GET)
    RestResult<List<StationGroup>> getStationGroupAll();
}
