package com.deyatech.appeal.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.common.entity.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/10/30 20:19
 */
@FeignClient(value = "appeal-service")
public interface AppealFeign {

    /**
     * 网站前台根据条件获取信息列表
     *
     * @param maps
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/feign/appeal/getAppealList")
    RestResult<Page<RecordVo>> getAppealList(@RequestBody Map<String, Object> maps, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize);
    /**
     * 网站前台根据诉求编码和查询码查询诉求信息
     *
     * @param sqCode
     * @param queryCode
     * @return
     */
    @RequestMapping(value = "/feign/appeal/queryAppeal")
    RestResult<RecordVo> queryAppeal(@RequestParam("sqCode") String sqCode, @RequestParam("queryCode") String queryCode);
}
