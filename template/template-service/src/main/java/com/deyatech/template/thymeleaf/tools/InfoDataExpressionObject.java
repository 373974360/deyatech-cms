package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 11:42
 */

import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 模板内置对象 - 内容工具
 */
@Component(TemplateConstants.TEMPLATE_OBJ_INFO_UTIL)
public class InfoDataExpressionObject {

    public String getInfoList(Map<String,Object> maps,Integer page, Integer pageSize){
        return "Hello Word!";
    }

}
