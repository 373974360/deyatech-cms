package com.deyatech.monitor.task;


import cn.hutool.core.date.DateUtil;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.deyatech.common.aliyun.AliyunUtil;
import com.deyatech.common.submail.SubMailMessage;
import com.deyatech.monitor.entity.Group;
import com.deyatech.monitor.entity.GroupSite;
import com.deyatech.monitor.entity.Site;
import com.deyatech.monitor.entity.SiteManager;
import com.deyatech.monitor.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

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

    @Value("${tts}")
    private String tts;

    @Autowired
    GroupService groupService;
    @Autowired
    GroupSiteService groupSiteService;
    @Autowired
    SiteService siteService;
    @Autowired
    SiteManagerService siteManagerService;
    @Autowired
    ManagerService managerService;
    @Autowired
    AliyunUtil aliyunUtil;
    /**
     * 每隔60秒执行, 单位：ms。
     */
    @Scheduled(fixedRate = 60000)
    public void run() {
        String currTime = DateUtils.getDateTime();
        System.out.println("执行时间"+currTime);
        List<Group> groupList = groupService.getGroupList(currTime);
        for(Group group:groupList){
            List<GroupSite> groupSites = (ArrayList)groupSiteService.listByBean(new GroupSite().setGroupId(group.getId()));
            if(!groupSites.isEmpty()){
                for(GroupSite groupSite:groupSites){
                    Site site = siteService.getById(groupSite.getSiteId());
                    String httpCode = MonitorUtils.doGet(site.getSiteDomain());
                    //状态码不是200
                    if(!httpCode.equals("200")){
                        String phontArr = "";
                        List<SiteManager> siteManagers = (ArrayList)siteManagerService.listByBean(new SiteManager().setSiteId(site.getId()));
                        if(!siteManagers.isEmpty()){
                            for(SiteManager siteManager:siteManagers){
                                phontArr += ","+managerService.getById(siteManager.getManagerId()).getUserPhone();
                            }
                            phontArr = phontArr.substring(1);
                        }
                        sendMessage(tts,phontArr,site.getSiteName());
                    }
                }
            }


            /**
             * 以下为计算任务组下次执行时间
             * */
            String nextTime = "";
            //固定时刻触发
            if(group.getTriggerType()==1){
                nextTime = DateUtils.getDateTimeAfter(currTime,group.getIncrementSeconds());
            }
            //日历周期触发
            if(group.getTriggerType()==2){
                //每日
                if(group.getCalendarType()==1){
                    nextTime = DateUtils.getDateTimeAfter(currTime,24*60*60).substring(0,10)+" "+group.getCalendarTime()+":00";
                }
                //每周
                if(group.getCalendarType()==2){
                    nextTime = DateUtils.getDateTimeAfter(currTime,24*60*60).substring(0,10)+" "+group.getCalendarTime()+":00";
                    if(StringUtils.isNotEmpty(group.getCalendarWorkday())){
                        int workDay = workDay(currTime);
                        String[] workDays = group.getCalendarWorkday().split(",");
                        bubbleSort(workDays,workDays.length);
                        for(int i=0;i<workDays.length;i++){
                            if(workDay == Integer.parseInt(workDays[i])){
                                if(i == workDays.length-1){
                                    nextTime = DateUtils.getDateTimeAfter(currTime,(7-Integer.parseInt(workDays[i])+Integer.parseInt(workDays[0]))*24*60*60).substring(0,10)+" "+group.getCalendarTime()+":00";
                                }else{
                                    nextTime = DateUtils.getDateTimeAfter(currTime,(Integer.parseInt(workDays[i+1])-Integer.parseInt(workDays[i]))*24*60*60).substring(0,10)+" "+group.getCalendarTime()+":00";
                                }
                            }
                        }
                    }
                }
            }
            group.setNextDtime(nextTime);
            group.setLastDtime(DateUtils.getDateTime());
            groupService.saveOrUpdate(group);
        }
    }

    public static int workDay(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.parseDate(date));
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if(day==0){
            day=7;
        }
        return day;
    }

    public static void bubbleSort(String[] arr,int n){
        if (n <= 1){
            return;
        }
        for (int i = 0; i < n; ++i) {
            boolean flag = false;
            for (int j = 0; j < n - i - 1; ++j) {
                if (Integer.parseInt(arr[j]) > Integer.parseInt(arr[j + 1])) {
                    String temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    flag = true;
                }
            }
            if (!flag){
                break;
            }
        }
    }


    public void sendMessage(String tts,String phoneArr,String siteName){
        if(StringUtils.isNotEmpty(phoneArr)){
            String[] arr = phoneArr.split(",");
            for(String phone:arr){
                Map<String,String> vars = new HashMap<>();
                vars.put("siteName",siteName);
                SubMailMessage subMailMessage = new SubMailMessage();
                subMailMessage.setTo(phone);
                subMailMessage.setVars(vars);
                SingleCallByTtsResponse resp = aliyunUtil.sendVoice(subMailMessage,tts);
                System.out.println("code:"+resp.getCode()+";message:"+resp.getMessage());
            }
        }
    }
}
