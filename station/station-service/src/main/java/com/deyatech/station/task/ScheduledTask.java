package com.deyatech.station.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Page;
import com.deyatech.station.rabbit.constants.RabbitMQConstants;
import com.deyatech.station.service.PageService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.workflow.feign.WorkflowFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Autowired
    private AmqpTemplate rabbitmqTemplate;

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

    /**
     * 每隔60秒执行, 单位：ms。
     */
    @Scheduled(fixedRate = 60000)
    public void timing() {
        String publicationDate = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
        log.info("定时发布：" + publicationDate);
        rabbitmqTemplate.convertAndSend(RabbitMQConstants.CMS_TASK_TOPIC_EXCHANGE, RabbitMQConstants.QUEUE_TIMING_PUBLISH_TEMPLATE, publicationDate);
    }
}
