package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 11:42
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.assembly.feign.AssemblyFeign;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模板内置对象 - 依申请公开工具
 */
@Component(TemplateConstants.TEMPLATE_OBJ_APPLYOPEN_UTIL)
public class ApplyOpenDataExpressionObject {

    @Autowired
    AssemblyFeign assemblyFeign;

    public IPage<ApplyOpenRecordVo> getApplyOpenlList(Map<String,Object> maps, Integer page, Integer pageSize){
        if (page == null || page < 0) {
            page = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        IPage<ApplyOpenRecordVo> result = assemblyFeign.getApplyOpenList(maps,page,pageSize).getData();
        return result;
    }

    public ApplyOpenRecordVo queryApplyOpen(String ysqCode,String queryCode){
        return assemblyFeign.queryApplyOpen(ysqCode,queryCode).getData();
    }

    public List<DepartmentVo> getPartDept(String modelId){
        List<DepartmentVo> resultList = new ArrayList<>();
        List<DepartmentVo> departmentVos = assemblyFeign.getPartDept(modelId).getData();
        resultList = getAllPartDept(departmentVos,resultList);
        return resultList;
    }
    public List<DepartmentVo> getTreePartDept(String modelId){
        return assemblyFeign.getPartDept(modelId).getData();
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
