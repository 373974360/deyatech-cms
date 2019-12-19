package com.deyatech.station.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.deyatech.common.Constants;
import com.deyatech.common.enums.ContentStatusEnum;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Page;
import com.deyatech.station.service.PageService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.workflow.entity.BusinessStash;
import com.deyatech.workflow.feign.WorkflowFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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
    /**
     * 每隔60秒执行, 单位：ms。
     */
    @Scheduled(fixedRate = 60000)
    public void contentStatus() {
        BusinessStash businessStash = new BusinessStash();
        businessStash.setKey(Constants.VARIABLE_STASH);
        businessStash.setType(Constants.VARIABLE_STASH_TYPE_AUTO_PASS);
        Collection<BusinessStash> list = workflowFeign.getBusinessStashList(businessStash).getData();
        if (CollectionUtil.isNotEmpty(list)) {
            List<String> ids = list.stream().map(BusinessStash::getValue).distinct().collect(Collectors.toList());
            log.info("定时更新的内容ID：" + ids.toString());
            templateService.updateStatusByIds(ids, ContentStatusEnum.PUBLISH.getCode());
            List<String> deleteIds = list.stream().map(BusinessStash::getId).distinct().collect(Collectors.toList());
            workflowFeign.removeBusinessStashByIds(deleteIds);
        }
    }
}
