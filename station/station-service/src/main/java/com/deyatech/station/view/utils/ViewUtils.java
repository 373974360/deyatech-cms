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


    /**
     * namePath 解析
     * */
    public static Map<String,String> analysisNamePath(String namePath, String type){
        Map<String,String> result = new HashMap<>();
        String pathName = namePath.substring(0,namePath.indexOf("_"));
        //栏目页 频道页
        if(type.equals("catagory")){
            String ext = namePath.substring(namePath.lastIndexOf("_")+1);
            if(ext.equals("index")){
                result.put("type",ext);
            }else{
                if(ext.equals("catagory")){
                    result.put("pageNo","1");
                }else{
                    result.put("pageNo",ext);
                }
                result.put("type","list");
            }
        }
        //详情页
        if(type.equals("content")){
            String paramsPath = namePath.substring(namePath.indexOf("_")+1);
            paramsPath = paramsPath.substring(paramsPath.indexOf("_")+1,paramsPath.lastIndexOf("_"));
            result.put("infoId",namePath.substring(namePath.lastIndexOf("_")+1));
            result.put("type",paramsPath);
        }
        //表单页
        if(type.equals("form")){
            String paramsPath = namePath.substring(namePath.indexOf("_")+1);
            paramsPath = paramsPath.substring(paramsPath.indexOf("_")+1,paramsPath.lastIndexOf("_"));
            result.put("modelId",namePath.substring(namePath.lastIndexOf("_")+1));
            result.put("type",paramsPath);
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
