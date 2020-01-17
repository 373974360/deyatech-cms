package com.deyatech.assembly.feign;

import com.deyatech.common.entity.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/11/5 14:24
 */
@FeignClient(value = "assembly-service")
public interface AssemblyFeign {

    /**
     * 获取下一个索引码根据站点ID
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/feign/assembly/indexCode/getPartDept")
    RestResult<String> getNextIndexCodeBySiteId(@RequestParam("siteId") String siteId);

    /**
     * 更新站点索引编码规则流水号
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/feign/assembly/indexCode/updateNextSerialBySiteId")
    RestResult<String> updateNextSerialBySiteId(@RequestParam("siteId") String siteId, @RequestParam("value") int value);
}
