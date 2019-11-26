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
import com.deyatech.appeal.entity.Satisfaction;
import com.deyatech.appeal.feign.AppealFeign;
import com.deyatech.appeal.vo.RecordSatisfactionVo;
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

    /**
     * 获取诉求列表
     *
     * @param maps 搜索条件
     * @param page 页码
     * @param pageSize 每页条数
     *
     * @return IPage<RecordVo>
     * */
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

    /**
     * 根据诉求编码和查询码查询诉求详情
     *
     * @param sqCode 搜索条件
     * @param queryCode 页码
     *
     * @return RecordVo
     * */
    public RecordVo queryAppeal(String sqCode,String queryCode){
        return appealFeign.queryAppeal(sqCode,queryCode).getData();
    }

    /**
     * 根据业务ID，信件类型得到总数
     * @param maps countType 信件类型 all 所有,wsl 未受理信件,sl 受理信件　bj　办结信件（回复件）
     * 			   countData years本年度  yesterday昨天的  ultimo 上月的 instant 本月 curday 当天
     * @return String
     */
     public String getAllAppealCount(Map<String,Object> maps){
        return appealFeign.getAllAppealCount(maps).getData();
    }

    /**
     * 查询诉求目的
     *
     * @return List<Purpose>
     * */
    public List<Purpose> getPurposeList(){
        return appealFeign.getAllPurpose().getData();
    }

    /**
     * 查询满意度评价指标
     *
     * @return List<Satisfaction>
     * */
    public List<Satisfaction> getSatisfactionList(){
        return appealFeign.getAllSatisfaction().getData();
    }


    /**
     * 根据信件ID查询满意度评价情况
     *
     * @param appealId 信件ID
     *
     * @return List<RecordSatisfactionVo>
     * */
    public List<RecordSatisfactionVo> getAppealSatisCountByAppealId(String appealId){
        return appealFeign.getAppealSatisCountByAppealId(appealId).getData();
    }

    /**
     * 根据业务模型ID查询参与部门---列表模式
     *
     * @param modelId 诉求业务模型ID
     *
     * @return List<DepartmentVo>
     * */
    public List<DepartmentVo> getPartDept(String modelId){
        List<DepartmentVo> resultList = new ArrayList<>();
        List<DepartmentVo> departmentVos = appealFeign.getPartDept(modelId).getData();
        resultList = getAllPartDept(departmentVos,resultList);
        return resultList;
    }

    /**
     * 根据业务模型ID查询参与部门---树结构
     *
     * @param modelId 诉求业务模型ID
     *
     * @return List<DepartmentVo>
     * */
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
