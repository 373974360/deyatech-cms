package com.deyatech.appeal.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.appeal.entity.Model;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.service.ModelService;
import com.deyatech.appeal.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 定时任务模块类
 * </p>
 *
 * @author: machaowei
 * @since 2019-7-29
 */
@Slf4j
@Component
public class ScheduledTask {

    @Autowired
    RecordService recordService;
    @Autowired
    ModelService modelService;
    @Autowired
    AdminFeign adminFeign;

    /**
     * 每隔60秒执行, 单位：ms。
     */
    @Scheduled(fixedRate = 60000)
    public void run() {
        String nowDate = DateUtil.formatDateTime(new Date());
        log.info("诉求定时任务开始执行*****" + nowDate);
        List<Record> list = recordService.getNotEndSQList();
        if(CollectionUtil.isNotEmpty(list)){
            for(Record record:list){
                Model model = modelService.getById(record.getModelId());
                if(ObjectUtil.isNull(model)){
                    continue;
                }
                //截止日期
                Date date = record.getTimeLimit();
                //预警时间
                int warnNum = model.getReminderDay();
                //黄牌时间
                int yellowNum = model.getYellowDay();
                //红牌时间
                int redNum = model.getRedDay();

                Date startDate = DateUtil.parseDate(DateUtil.format(new Date(),"yyyy-MM-dd"));
                Date endDate = DateUtil.parseDate(DateUtil.formatDate(date).substring(0,10));

                //当前日期到截止日期的工作日天数
                int workDate = Integer.parseInt(adminFeign.workIntervalDayAfter(startDate,endDate).getData().toString());
                //预警件
                if(warnNum >= workDate){
                    record.setAlarmFlag(1);
                }
                //黄牌件
                if(yellowNum >= workDate){
                    record.setAlarmFlag(2);
                }
                //红牌件
                if(redNum >= workDate){
                    record.setAlarmFlag(3);
                }
                recordService.updateById(record);
            }
        }
    }
}
