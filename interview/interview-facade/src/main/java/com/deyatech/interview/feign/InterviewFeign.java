package com.deyatech.interview.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.common.entity.RestResult;
import com.deyatech.interview.vo.CategoryVo;
import com.deyatech.interview.vo.ModelVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/11/13 16:12
 */
@FeignClient(value = "interview-service")
public interface InterviewFeign {

    /**
     * 网站前台根据条件获取信息列表
     *
     * @param maps
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/feign/interview/getInterviewList")
    RestResult<Page<ModelVo>> getInterviewList(@RequestBody Map<String, Object> maps, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize);


    /**
     * 访谈详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/feign/interview/getInterviewById")
    RestResult<ModelVo> getInterviewById(@RequestParam("id") String id);


    /**
     * 访谈分类
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/feign/interview/getInterviewCatagoryById")
    RestResult<CategoryVo> getInterviewCatagoryById(@RequestParam("id") String id);

}
