package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 11:42
 */

import com.deyatech.station.feign.StationFeign;
import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 模板内置对象 - 内容工具
 */
@Component(TemplateConstants.TEMPLATE_OBJ_INFO_UTIL)
public class InfoDataExpressionObject {

    @Autowired
    StationFeign stationFeign;

    public Map<String,Object> getInfoList(Map<String,Object> maps, Integer page, Integer pageSize){
        if (page == null || page < 0) {
            page = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        Map<String,Object> result = stationFeign.getTemplateListView(maps,page,pageSize).getData();
        return result;
    }

}
