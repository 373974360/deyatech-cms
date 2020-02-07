package com.deyatech.statistics.controller;

import com.deyatech.common.entity.RestResult;
import com.deyatech.statistics.service.CatalogDataService;
import com.deyatech.statistics.vo.UserDataQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/2/5 15:16
 */
@Slf4j
@RestController
@RequestMapping("/statistics/catalogData")
@Api(tags = {"栏目数据统计"})
public class CatalogDataController {

    @Autowired
    CatalogDataService catalogDataService;

    /**
     * 站点数据统计
     *
     * @param queryVo
     * @return
     */
    @RequestMapping("/getCatalogCountList")
    @ApiOperation(value="栏目数据统计", notes="栏目数据统计")
    @ApiImplicitParam(name = "queryVo", value = "对象", required = true, dataType = "UserDataQueryVo", paramType = "query")
    public RestResult getCatalogCountList(UserDataQueryVo queryVo) {
        return RestResult.ok(catalogDataService.getCatalogCountList(queryVo));
    }
}
