package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 11:42
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.interview.feign.InterviewFeign;
import com.deyatech.interview.vo.ModelVo;
import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 模板内置对象 - 在线访谈工具
 */
@Component(TemplateConstants.TEMPLATE_OBJ_INTERVIEW_UTIL)
public class InterViewDataExpressionObject {

    @Autowired
    InterviewFeign interviewFeign;

    public IPage<ModelVo> getInterviewList(Map<String,Object> maps, Integer page, Integer pageSize){
        if (page == null || page < 0) {
            page = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        IPage<ModelVo> result = interviewFeign.getInterviewList(maps,page,pageSize).getData();
        return result;
    }

}
