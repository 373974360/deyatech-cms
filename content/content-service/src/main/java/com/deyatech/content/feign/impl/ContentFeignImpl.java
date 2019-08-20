package com.deyatech.content.feign.impl;

import com.deyatech.common.entity.RestResult;
import com.deyatech.content.entity.ReviewProcess;
import com.deyatech.content.feign.ContentFeign;
import com.deyatech.content.service.ReviewProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ContentFeign实现类
 * </p>
 *
 * @author: csm.
 * @since: 2019/8/20
 */
@RestController
public class ContentFeignImpl implements ContentFeign {

    @Autowired
    ReviewProcessService reviewProcessService;

    @Override
    public RestResult<Boolean> saveOrUpdate(ReviewProcess reviewProcess) {
        boolean result = reviewProcessService.saveOrUpdate(reviewProcess);
        return RestResult.ok(result);
    }
}
