package com.deyatech.generate.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.template.feign.TemplateFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/9/16 14:39
 */
@Slf4j
@RestController
@RequestMapping("/generate/createHtml")
@Api(tags = {"生成静态页面"})
public class GenerateController {


    @Autowired
    StationFeign stationFeign;
    @Autowired
    TemplateFeign templateFeign;



    /**
     * 根据条件生成静态页面
     *
     * @param ids
     * @param timeFrame
     * @return
     */
    @PostMapping("/createHtml")
    @ApiOperation(value="根据条件生成静态页面", notes="根据条件生成静态页面")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "栏目ID数组", required = false, dataType = "ids", paramType = "query"),
        @ApiImplicitParam(name = "timeFrame", value = "时间范围", required = false, dataType = "timeFrame", paramType = "query")
    })
    public RestResult createHtml(@RequestParam(value="ids[]",required=false) List<String> ids,@RequestParam(value="timeFrame[]",required=false) List<String> timeFrame) {
        Integer currPage = 1;
        Integer pageSize = 50;
        Map<String,Object> maps = new HashMap<>();
        if(ids!= null && !ids.isEmpty()){
            maps.put("cmsCatalogId",ids);
        }
        if(timeFrame != null && !timeFrame.isEmpty()){
            maps.put("startTime",timeFrame.get(0));
            maps.put("endTime",timeFrame.get(1));
        }
        IPage<TemplateVo> templates = stationFeign.getTemplateListView(maps,currPage,pageSize).getData();
        List<TemplateVo> list;
        if(templates.getPages() > 0){
            for(int i = 1;i <= templates.getPages();i++){
                list = stationFeign.getTemplateListView(maps,i,pageSize).getData().getRecords();
                if(list != null && !list.isEmpty()){
                    for(TemplateVo templateVo:list){
                        if(!templateVo.getFlagExternal()){
                            templateFeign.generateStaticTemplate(templateVo);
                        }
                    }
                }
            }
        }
        return RestResult.ok();
    }
}
