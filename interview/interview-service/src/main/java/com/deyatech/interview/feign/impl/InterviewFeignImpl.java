package com.deyatech.interview.feign.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.common.entity.RestResult;
import com.deyatech.interview.entity.Category;
import com.deyatech.interview.entity.Guest;
import com.deyatech.interview.entity.Model;
import com.deyatech.interview.feign.InterviewFeign;
import com.deyatech.interview.service.CategoryService;
import com.deyatech.interview.service.GuestService;
import com.deyatech.interview.service.ModelService;
import com.deyatech.interview.vo.CategoryVo;
import com.deyatech.interview.vo.ModelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/11/13 16:14
 */
@RestController
public class InterviewFeignImpl implements InterviewFeign {

    @Autowired
    ModelService modelService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    GuestService guestService;

    @Override
    public RestResult<Page<ModelVo>> getInterviewList(Map<String, Object> maps, Integer page, Integer pageSize) {
        IPage<ModelVo> records = modelService.getInterviewList(maps, page, pageSize);
        records.setRecords(modelService.setVoProperties(records.getRecords()));
        return RestResult.ok(records);
    }

    @Override
    public RestResult<ModelVo> getInterviewById(String id) {
        Model model = modelService.getById(id);
        ModelVo modelVo = modelService.setVoProperties(model);

        Guest guest = new Guest();
        guest.setModelId(model.getId());
        modelVo.setGuestVoList(guestService.setVoProperties(guestService.listByBean(guest)));
        return RestResult.ok(modelVo);
    }

    @Override
    public RestResult<CategoryVo> getInterviewCatagoryById(String id) {
        Category category = categoryService.getById(id);
        return RestResult.ok(categoryService.setVoProperties(category));
    }
}
