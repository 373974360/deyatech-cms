package com.deyatech.station.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Page;
import com.deyatech.station.service.PageService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.workflow.feign.WorkflowFeign;
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
    @Autowired
    SiteCache siteCache;
    @Autowired
    WorkflowFeign workflowFeign;
    @Autowired
    TemplateService templateService;

    /**
     * 每隔60秒执行, 单位：ms。
     */
    @Scheduled(fixedRate = 60000)
    public void run() {
        //刷新缓存
        siteCache.cacheSite();
        //发布页面
        String currTime = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss");
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
