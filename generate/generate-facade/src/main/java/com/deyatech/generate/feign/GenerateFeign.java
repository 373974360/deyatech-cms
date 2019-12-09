package com.deyatech.generate.feign;

import com.deyatech.common.entity.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/10/30 20:19
 */
@FeignClient(value = "generate-service")
public interface GenerateFeign {

    /**
     * 根据栏目ID 发布关联的页面静态页
     *
     * @param catalogId
     * @return
     */
    @RequestMapping(value = "/feign/generate/replyPageByCatalog")
    RestResult replyPageByCatalog(@RequestParam("catalogId") String catalogId);

}
