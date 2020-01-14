package com.deyatech.station.view;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.feign.AppealFeign;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.view.utils.ViewUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            pageSize = Integer.parseInt(varMap.get("pageSize").toString());
        }
        return appealFeign.getAppealList(varMap,page,pageSize);
    }


    /**
     * 获取诉求目的
     *
     * @return
     */
    @GetMapping(value = "/getAllPurpose")
    @ResponseBody
    public RestResult getAllPurpose(){
        return appealFeign.getAllPurpose();
    }


    /**
     * 满意度评价指标
     *
     * @return
     */
    @GetMapping(value = "/getAllSatisfaction")
    @ResponseBody
    public RestResult getAllSatisfaction(){
        return appealFeign.getAllSatisfaction();
    }


    /**
     * 根据信件ID查询满意度评价情况
     *
     * @return
     */
    @GetMapping(value = "/getAppealSatisCountByAppealId")
    @ResponseBody
    public RestResult getAppealSatisCountByAppealId(String id){
        return appealFeign.getAppealSatisCountByAppealId(id);
    }


    /**
     * 根据信件编码和查询码查询诉求详情
     *
     * @return
     */
    @GetMapping(value = "/getAppealBySqCodeAndQueryCode")
    @ResponseBody
    public RestResult getAppealBySqCodeAndQueryCode(String sqCode,String queryCode){
        RecordVo recordVo = appealFeign.queryAppeal(sqCode,queryCode).getData();
        if(ObjectUtil.isNotNull(recordVo)){
            return RestResult.ok(recordVo);
        }
        return RestResult.error("诉求信息不存在！");
    }


    /**
     * 根据信件ID查询诉求详情
     *
     * @return
     */
    @GetMapping(value = "/getAppealById")
    @ResponseBody
    public RestResult getAppealById(String id){
        RecordVo recordVo = appealFeign.getAppealById(id).getData();
        if(ObjectUtil.isNotNull(recordVo)){
            return RestResult.ok(recordVo);
        }
        return RestResult.error("诉求信息不存在！");
    }


    /**
     * 根据模型ID获取参与部门
     *
     * @return
     */
    @GetMapping(value = "/getPartDept")
    @ResponseBody
    public RestResult getPartDept(String modelId,String type){
        if(type.equals("list")){
            List<DepartmentVo> resultList = new ArrayList<>();
            List<DepartmentVo> departmentVos = appealFeign.getPartDept(modelId).getData();
            resultList = getAllPartDept(departmentVos,resultList);
            return RestResult.ok(resultList);
        }
        return appealFeign.getPartDept(modelId);
    }

    @PostMapping(value = "/addAppeal")
    public RestResult addAppeal(HttpServletRequest request) {
        Map<String,Object> varMap = new HashMap<>();
        ViewUtils.requestParams(varMap,request);
        Record record = new Record();
        BeanUtil.copyProperties(varMap,record);
        return RestResult.ok(appealFeign.addAppeal(record).getData());
    }



    public static List<DepartmentVo> getAllPartDept(List<DepartmentVo> departmentVos,List<DepartmentVo> resultList){
        if(departmentVos != null && !departmentVos.isEmpty()){
            for(DepartmentVo departmentVo:departmentVos){
                if(departmentVo.getChildren() != null && !departmentVo.getChildren().isEmpty()){
                    getAllPartDept(departmentVo.getChildren(),resultList);
                }else{
                    resultList.add(departmentVo);
                }
            }
        }
        return resultList;
    }
}
