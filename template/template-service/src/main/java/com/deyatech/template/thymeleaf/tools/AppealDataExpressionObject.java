package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 11:42
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.appeal.feign.AppealFeign;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 模板内置对象 - 诉求工具
 */
@Component(TemplateConstants.TEMPLATE_OBJ_APPEAL_UTIL)
public class AppealDataExpressionObject {

    @Autowired
    AppealFeign appealFeign;

    public IPage<RecordVo> getAppealList(Map<String,Object> maps, Integer page, Integer pageSize){
        if (page == null || page < 0) {
            page = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        IPage<RecordVo> result = appealFeign.getAppealList(maps,page,pageSize).getData();
        return result;
    }

    public RecordVo queryAppeal(String sqCode,String queryCode){
        return appealFeign.queryAppeal(sqCode,queryCode).getData();
    }
}
