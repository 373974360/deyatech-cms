package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/9 13:48
 */

import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import org.springframework.stereotype.Component;

/**
 * 模板内置对象 - 格式化工具
 */
@Component(TemplateConstants.TEMPLATE_OBJ_FORMAT_UTIL)
public class FormatExpressionObject {

    /**
     * 字符串截取
     * */
    public String subStr(String str,int startNum,int endNum){
        return str.substring(startNum,endNum);
    }
}
