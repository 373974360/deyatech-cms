package com.deyatech.statistics.feign.impl;

import cn.hutool.core.date.DateUtil;
import com.deyatech.common.entity.RestResult;
import com.deyatech.statistics.entity.TemplateAccess;
import com.deyatech.statistics.feign.StatisticsFeign;
import com.deyatech.statistics.service.TemplateAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/19 10:02
 */
@RestController
public class StatisticsFeignImpl implements StatisticsFeign {

    @Autowired
    TemplateAccessService templateAccessService;

    @Override
    public RestResult insertTemplateAccess(TemplateAccess templateAccess) {
        templateAccess.setAccessDay(DateUtil.format(new Date(),"dd"));
        templateAccess.setAccessMonth(DateUtil.format(new Date(),"MM"));
        templateAccess.setAccessYear(DateUtil.format(new Date(),"yyyy"));
        templateAccess.setAccessTime(DateUtil.now());
        return RestResult.ok(templateAccessService.save(templateAccess));
    }
}
