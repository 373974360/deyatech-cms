package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 11:42
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.appeal.entity.Purpose;
import com.deyatech.appeal.feign.AppealFeign;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模板内置对象 - 诉求工具
 */
@Component(TemplateConstants.TEMPLATE_OBJ_APPEAL_UTIL)
public class AppealDataExpressionObject {

    @Autowired
    AppealFeign appealFeign;

    public IPage<RecordVo> getAppealList(Map<String,Object> maps, Integer page, Integer pageSize){
        if (page == null || page < 0) {
            page = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        IPage<RecordVo> result = appealFeign.getAppealList(maps,page,pageSize).getData();
        return result;
    }

    public RecordVo queryAppeal(String sqCode,String queryCode){
        return appealFeign.queryAppeal(sqCode,queryCode).getData();
    }


    public List<Purpose> getPurposeList(){
        return appealFeign.getAllPurpose().getData();
    }


    public List<DepartmentVo> getPartDept(String modelId){
        List<DepartmentVo> resultList = new ArrayList<>();
        List<DepartmentVo> departmentVos = appealFeign.getPartDept(modelId).getData();
        resultList = getAllPartDept(departmentVos,resultList);
        return resultList;
    }
    public List<DepartmentVo> getTreePartDept(String modelId){
        return appealFeign.getPartDept(modelId).getData();
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
