package com.deyatech.station.view;

import com.deyatech.appeal.feign.AppealFeign;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.view.utils.ViewUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/7 17:16
 */
@RestController
@Slf4j
@RequestMapping("/ajax")
public class AjaxController {

    @Autowired
    AppealFeign appealFeign;
    /**
     * 动态首页
     *
     * @return
     */
    @GetMapping(value = "/getAppealList")
    @ResponseBody
    public RestResult getAppealList(HttpServletRequest request){
        Map<String,Object> varMap = new HashMap<>();
        ViewUtils.requestParams(varMap,request);
        int page = 1;
        int pageSize = 10;
        if (varMap.containsKey("page")) {
            page = Integer.parseInt(varMap.get("page").toString());
        }
        if (varMap.containsKey("pageSize")) {
            pageSize = 10;
        }
        return appealFeign.getAppealList(varMap,page,pageSize);
    }
}
