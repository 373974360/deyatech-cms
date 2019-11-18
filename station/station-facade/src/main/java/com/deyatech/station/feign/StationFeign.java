package com.deyatech.station.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.station.config.SiteProperties;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.TemplateVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    /**
     * 获取nginx配置文件目录
     *
     * @return
     */
    @RequestMapping(value = "/feign/station/getSiteProperties", method = RequestMethod.GET)
    RestResult<SiteProperties> getSiteProperties();

    /**
     * 根据Catalog对象属性检索栏目的tree对象
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/feign/station/getCatalogTreeBySiteId", method = RequestMethod.GET)
    RestResult<Collection<CatalogVo>> getCatalogTreeBySiteId(@RequestParam("siteId") String siteId);


    /**
     * 网站前台根据条件获取信息列表
     *
     * @param maps
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/feign/station/getTemplateListView")
    RestResult<Page<TemplateVo>> getTemplateListView(@RequestBody Map<String, Object> maps, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize);

    /**
     * 检索站点下的内容
     * @param siteId
     * @param start
     * @param end
     * @param part
     * @param number
     * @return
     */
    @RequestMapping(value = "/feign/station/resetTemplateIndex")
    RestResult<Integer> resetTemplateIndexCode(@RequestParam("siteId") String siteId,
                                                    @RequestParam("start") String start,
                                                    @RequestParam("end") String end,
                                                    @RequestParam("part") String part,
                                                    @RequestParam("number") int number);
}
