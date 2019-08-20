package com.deyatech.content.feign;

import com.deyatech.common.entity.RestResult;
import com.deyatech.content.entity.ReviewProcess;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * content模块feign远程调用类
 * </p>
 *
 * @author: csm.
 * @since: 2019/8/20
 */
@RequestMapping("/feign/content")
@FeignClient(value = "content-service")
public interface ContentFeign {

    /**
     * 单个保存或者更新内容审核流程
     *
     * @param reviewProcess
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate")
    RestResult<Boolean> saveOrUpdate(@RequestBody ReviewProcess reviewProcess);

}
