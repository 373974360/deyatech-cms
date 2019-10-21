package com.deyatech.template.feign;


import com.deyatech.common.entity.RestResult;
import com.deyatech.station.vo.TemplateVo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.Map;

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
     * 获取静态页后缀名
     *
     * @return
     */
    @RequestMapping(value = "/feign/template/getPageSuffix", method = RequestMethod.GET)
    RestResult<String> getPageSuffix();

    /**
     * 生成静态页面
     *
     * @param templateRootPath 模板根路径
     * @param templatePath 模板路径
     * @param distFile     输出文件路径
     * @param varMap       模板用到的变量
     * @return
     */
    @RequestMapping(value = "/feign/template/generateStaticPage", method = RequestMethod.POST)
    RestResult generateStaticPage(@RequestParam("templateRootPath") String templateRootPath, @RequestParam("templatePath") String templatePath, @RequestParam("distFile") File distFile, @RequestBody Map<String, Object> varMap);

    /**
     * 生成静态页面
     *
     * @param templateVo       模板用到的变量
     * @return
     */
    @RequestMapping(value = "/feign/template/generateStaticTemplate", method = RequestMethod.POST)
    RestResult generateStaticTemplate(@RequestBody TemplateVo templateVo);



    /**
     * 处理模板文件返回字符串
     *
     * @param templatePath 模板路径
     * @param varMap       模板用到的变量
     * @return
     */
    @RequestMapping(value = "/feign/template/thyToString", method = RequestMethod.POST)
    RestResult<String> thyToString(@RequestParam("siteTemplateRoot") String siteTemplateRoot,@RequestParam("templatePath")  String templatePath,@RequestBody Map<String, Object> varMap);

    /**
     * 检查模板地址是否存在
     *
     * @param templatePath
     * @return
     */
    @RequestMapping(value = "/feign/template/existsTemplatePath")
    RestResult<Boolean> existsTemplatePath(@RequestParam("templatePath") String templatePath);
}
