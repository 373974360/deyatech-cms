package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 11:42
 */

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.assembly.feign.AssemblyFeign;
import com.deyatech.assembly.vo.ApplyOpenModelVo;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.template.thymeleaf.utils.PageUrlUtil;
import com.deyatech.template.thymeleaf.utils.PageUtil;
import com.deyatech.template.thymeleaf.utils.TemplateConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 模板内置对象 - 依申请公开工具
 */
@Component(TemplateConstants.TEMPLATE_OBJ_APPLYOPEN_UTIL)
public class ApplyOpenDataExpressionObject {

    @Autowired
    AssemblyFeign assemblyFeign;

    @Autowired
    StationFeign stationFeign;

    /**
     * 获取依申请公开列表
     *
     * @param maps 搜索条件
     * @param page 页码
     * @param pageSize 每页条数
     *
     * @return IPage<ApplyOpenRecordVo>
     * */
    public IPage<ApplyOpenRecordVo> getApplyOpenlList(Map<String,Object> maps, Integer page, Integer pageSize){
        if (page == null || page < 0) {
            page = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        IPage<ApplyOpenRecordVo> result = assemblyFeign.getApplyOpenList(maps,page,pageSize).getData();

        Collection<CatalogVo> catalogVoCollection = stationFeign.getCatalogTreeBySiteId(maps.get("siteId").toString()).getData();
        CatalogVo catalogVo = getCatalog(catalogVoCollection,maps.get("catId").toString());

        String pageDynamicSuffix = TemplateConstants.PAGE_SUFFIX;

        PageUtil<ApplyOpenRecordVo> pageUtil = new PageUtil<>();
        pageUtil.setCurrent(result.getCurrent());
        pageUtil.setPages(result.getPages());
        pageUtil.setTotal(result.getTotal());
        pageUtil.setSize(result.getSize());
        pageUtil.setRecords(result.getRecords());

        for(ApplyOpenRecordVo applyOpenRecordVo:result.getRecords()){
            applyOpenRecordVo.setUrl("/"+catalogVo.getPathName()+"/details/applyopen/"+applyOpenRecordVo.getId()+pageDynamicSuffix);
        }

        return PageUrlUtil.setUrl(pageUtil,catalogVo,pageDynamicSuffix);
    }

    /**
     * 根据申请编码和查询码查询申请详情
     *
     * @param ysqCode 搜索条件
     * @param queryCode 页码
     *
     * @return ApplyOpenRecordVo
     * */
    public ApplyOpenRecordVo queryApplyOpen(String ysqCode,String queryCode){
        return assemblyFeign.queryApplyOpen(ysqCode,queryCode).getData();
    }


    /**
     * 根据模型ID 查询模型信息
     *
     * @param modelId 模型ID
     * @param siteId 站点ID
     * @param catId 栏目ID
     *
     * @return ModelVo
     * */
    public ApplyOpenModelVo getModelById(String modelId, String siteId, String catId){
        ApplyOpenModelVo modelVo = assemblyFeign.getApplyOpenModelById(modelId).getData();

        Collection<CatalogVo> catalogVoCollection = stationFeign.getCatalogTreeBySiteId(siteId).getData();
        CatalogVo catalogVo = getCatalog(catalogVoCollection,catId);

        modelVo.setFormUrl("/"+catalogVo.getPathName()+"/form/applyopen/"+modelVo.getId()+TemplateConstants.PAGE_SUFFIX);
        return modelVo;
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
        List<DepartmentVo> departmentVos = assemblyFeign.getPartDept(modelId).getData();
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


    public CatalogVo getCatalog(Collection<CatalogVo> catalogVoCollection,String catalogId ){
        CatalogVo catalogVo = null;
        for(CatalogVo catalogVo1:catalogVoCollection){
            if(catalogVo1.getId().equals(catalogId)){
                catalogVo = catalogVo1;
                break;
            }else if(ObjectUtil.isNotNull(catalogVo1.getChildren())){
                catalogVo = getCatalog(catalogVo1.getChildren(),catalogId);
                if(ObjectUtil.isNotNull(catalogVo)){
                    break;
                }
            }
        }
        return catalogVo;
    }
}
