package com.deyatech.cas.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/10/30 20:19
 */
@FeignClient(value = "appeal-service")
public interface CasFeign {


}
