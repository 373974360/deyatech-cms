package com.deyatech.statistics.baidu;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.deyatech.common.entity.RestResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/19 16:57
 */
@Component
public class BaiDuAccess {

    @Value("${baidu.username}")
    private String username;
    @Value("${baidu.password}")
    private String password;
    @Value("${baidu.token}")
    private String token;
    @Value("${baidu.api}")
    private String api;
    @Value("${baidu.siteId}")
    private String siteId;

    private static final String CHARSET="UTF-8";

    public String getVisit(String method, HttpServletRequest request){
        String result = "";
        JSONObject body = JSONUtil.createObj();
        body.put("siteId", siteId);
        body.put("method", method);
        //今日流量
        if(method.equals("overview/getOutline")){
            //指标,数据单位
            body.put("metrics", "pv_count,visitor_count,ip_count,bounce_ratio,avg_visit_time");
        }
        //top 10搜索词
        if(method.equals("overview/getWord")){
            body.put("indicators","indicators");
            body.put("start_date",request.getParameter("start_date"));
            body.put("end_date",request.getParameter("end_date"));
            //指标,数据单位
            body.put("metrics", "simple_searchword_title,pv_count,ratio");
        }
        //top 10受访页面
        if(method.equals("overview/getVisitPage")){
            body.put("indicators","indicators");
            body.put("start_date",request.getParameter("start_date"));
            body.put("end_date",request.getParameter("end_date"));
            //指标,数据单位
            body.put("metrics", "simple_visitpage_title,pv_count,ratio");
        }
        //top 10来源网站
        if(method.equals("overview/getSourceSite")){
            body.put("indicators","indicators");
            body.put("start_date",request.getParameter("start_date"));
            body.put("end_date",request.getParameter("end_date"));
            //指标,数据单位
            body.put("metrics", "simple_link_title,pv_count,ratio");
        }
        //top 10 入口页面
        if(method.equals("overview/getLandingPage")){
            body.put("indicators","indicators");
            body.put("start_date",request.getParameter("start_date"));
            body.put("end_date",request.getParameter("end_date"));
            //指标,数据单位
            body.put("metrics", "simple_landingpage_title,pv_count,ratio");
        }
        //新老访客
        if(method.equals("overview/getVisitorType")){
            body.put("indicators","indicators");
            body.put("start_date",request.getParameter("start_date"));
            body.put("end_date",request.getParameter("end_date"));
            //指标,数据单位
            body.put("metrics", "");
        }
        //地域分布
        if(method.equals("overview/getDistrictRpt")){
            body.put("indicators","indicators");
            body.put("start_date",request.getParameter("start_date"));
            body.put("end_date",request.getParameter("end_date"));
            //指标,数据单位
            body.put("metrics", "pv_count,visitor_count,ip_count,bounce_ratio,avg_visit_time,trans_count");
        }
        //趋势图
        if(method.equals("overview/getTimeTrendRpt")){
            body.put("indicators","indicators");
            body.put("start_date",request.getParameter("start_date"));
            body.put("end_date",request.getParameter("end_date"));
            //指标,数据单位
            body.put("metrics", "simple_date_title,time,pv_count");
        }
        //年龄分布
        if(method.equals("overview/getAge")){
            //指标,数据单位
            body.put("metrics", "");
        }

        JSONObject params = JSONUtil.createObj();
        params.put("header", getHearder());
        params.put("body", body);

        try {
            byte[] res = BaiDuAccessUtil.post(api, params.toString(), CHARSET);
            result = new String(res);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject getHearder(){
        JSONObject header = JSONUtil.createObj();
        //用户名
        header.put("username", username);
        //用户密码
        header.put("password", password);
        //申请到的token
        header.put("token", token);
        header.put("account_type", "1");
        return header;
    }
}
