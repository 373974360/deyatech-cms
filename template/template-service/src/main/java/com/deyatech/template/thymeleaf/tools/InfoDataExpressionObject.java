package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 11:42
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.vo.TemplateVo;
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


    /**
     * 获取新闻列表
     *
     * @param maps 搜索条件
     * @param page 页码
     * @param pageSize 每页条数
     *
     * @return IPage<TemplateVo>
     * */
    public IPage<TemplateVo> getInfoList(Map<String,Object> maps, Integer page, Integer pageSize){
        if (page == null || page < 0) {
            page = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        IPage<TemplateVo> result = stationFeign.getTemplateListView(maps,page,pageSize).getData();
        return result;
    }

}
