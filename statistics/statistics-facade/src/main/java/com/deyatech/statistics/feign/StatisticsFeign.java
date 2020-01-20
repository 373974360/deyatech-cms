package com.deyatech.statistics.feign;

import com.deyatech.common.entity.RestResult;
import com.deyatech.statistics.entity.TemplateAccess;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/19 09:59
 */
@FeignClient(value = "statistics-service")
public interface StatisticsFeign {


    /**
     * 增加访问信息
     * @param templateAccess
     * @return
     */
    @PostMapping(value = "/feign/statistics/insertTemplateAccess")
    RestResult insertTemplateAccess(@RequestBody TemplateAccess templateAccess);
}
