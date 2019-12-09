package com.deyatech.generate.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.deyatech.generate.entity.Page;
import com.deyatech.generate.service.PageService;
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
    PageService pageService;

    /**
     * 每隔60秒执行, 单位：ms。
     */
    @Scheduled(fixedRate = 60000)
    public void run() {
        String currTime = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss");
        System.out.println("开始执行定时任务==================："+currTime);
        List<Page> pageList = pageService.getPageListByCurrTime(currTime);
        if(CollectionUtil.isNotEmpty(pageList)){
            for(Page page:pageList){
                System.out.println("发布页面===============："+page.getPagePath()+page.getPageEnglishName()+".html");
                pageService.replayPage(page);

                //设置下次更新时间
                page.setLastDtime(currTime);
                page.setNextDtime(DateUtil.format(DateUtil.offsetSecond(DateUtil.parse(currTime),page.getPageInterval()),"yyyy-MM-dd HH:mm:ss"));
                pageService.updateById(page);
            }
        }
    }

}
