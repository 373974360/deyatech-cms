package com.deyatech.resource.feign;

import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.Setting;
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

    /**
     * 检索站点
     *
     * @return
     */
    @RequestMapping(value = "/feign/resource/getStationGroupById")
    RestResult<StationGroup> getStationGroupById(@RequestParam("id") String id);

    /**
     * 获取站点设置
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/feign/resource/getStationSetting")
    RestResult<Setting> getStationSetting(@RequestParam(value = "siteId", required = false) String siteId);

    /**
     * 是否站点管理员
     *
     * @param siteId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/feign/resource/isSiteAdmin")
    RestResult<Boolean> isSiteAdmin(@RequestParam("siteId") String siteId, @RequestParam("userId") String userId);

    /**
     * 是否站点部门管理员
     *
     * @param siteId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/feign/resource/isSiteDepartmentAdmin")
    RestResult<Boolean> isSiteDepartmentAdmin(@RequestParam("siteId") String siteId, @RequestParam("userId") String userId);
}
