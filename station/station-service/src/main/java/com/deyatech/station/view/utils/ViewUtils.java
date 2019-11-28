package com.deyatech.station.view.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/11/5 11:43
 */
public class ViewUtils {



    public static void main(String args[]){
        String namePath = "xwzx/details/info/1186498265441447938";
        String params = namePath.substring(namePath.indexOf("/details/")+9);
        System.out.println(params.substring(params.indexOf("/")+1));
    }

    /**
     * namePath 解析
     * */
    public static Map<String,String> analysisNamePath(String namePath, String type){
        Map<String,String> result = new HashMap<>();
        String pathName = "";
        //栏目地址解析
        if(type.equals("catagory")){
            //列表模板
            if(namePath.indexOf("/list")>0){
                pathName = namePath.substring(0,namePath.indexOf("/list"));
                String pageNo = namePath.substring(namePath.lastIndexOf("/")+1);
                try{
                    int i = Integer.parseInt(pageNo);
                }catch (Exception e){
                    pageNo = "1";
                }
                result.put("pageNo",pageNo);
                result.put("type","list");
            }
            //频道模板
            if(namePath.indexOf("/index")>0){
                pathName = namePath.substring(0,namePath.indexOf("/index"));
                result.put("pageNo","1");
                result.put("type","index");
            }
        }
        //详情地址解析
        if(type.equals("details")){
            pathName = namePath.substring(0,namePath.indexOf("/details"));
            String params = namePath.substring(namePath.indexOf("/details/")+9);
            result.put("type",params.substring(0,params.indexOf("/")));
            result.put("infoId",params.substring(params.indexOf("/")+1));
        }
        //表单地址解析
        if(type.equals("form")){
            pathName = namePath.substring(0,namePath.indexOf("/form"));
            String params = namePath.substring(namePath.indexOf("/form/")+6);
            result.put("type",params.substring(0,params.indexOf("/")));
            result.put("modelId",params.substring(params.indexOf("/")+1));
        }
        result.put("pathName",pathName);
        return result;
    }

    /**
     * request参数解析
     * */
    public static Map<String,Object> requestParams(Map<String,Object> varMap, HttpServletRequest request){
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
            Object o = e.nextElement();
            String arr = (String) o;
            String value = request.getParameter(arr);
            varMap.put(arr,value);
        }
        return varMap;
    }
}
