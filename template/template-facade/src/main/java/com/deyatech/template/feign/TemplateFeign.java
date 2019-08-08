package com.deyatech.template.feign;


import com.deyatech.common.entity.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述：template模块feign远程调用类
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/6 14:57
 */
@FeignClient(value = "template-service")
public interface TemplateFeign {


    /**
     * 根据站点ID读取模板文件
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/feign/template/getTemplateFiles", method = RequestMethod.GET)
    RestResult<String> getTemplateFiles(@RequestParam("siteId") String siteId);
    /**
     * 获取模板根路径
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/feign/template/getTemplateRootPath", method = RequestMethod.GET)
    RestResult<String> getTemplateRootPath(@RequestParam("siteId") String siteId);
}
