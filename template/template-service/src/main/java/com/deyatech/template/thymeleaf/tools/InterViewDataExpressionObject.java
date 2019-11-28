package com.deyatech.template.thymeleaf.tools;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/26 11:42
 */

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
import com.deyatech.interview.feign.InterviewFeign;
import com.deyatech.interview.vo.ModelVo;
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
 * 模板内置对象 - 在线访谈工具
 */
@Component(TemplateConstants.TEMPLATE_OBJ_INTERVIEW_UTIL)
public class InterViewDataExpressionObject {

    @Autowired
    InterviewFeign interviewFeign;

    @Autowired
    StationFeign stationFeign;



    /**
     * 获取访谈列表
     *
     * @param maps 搜索条件
     * @param page 页码
     * @param pageSize 每页条数
     *
     * @return IPage<ModelVo>
     * */
    public IPage<ModelVo> getInterviewList(Map<String,Object> maps, Integer page, Integer pageSize){
        if (page == null || page < 0) {
            page = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        IPage<ModelVo> result = interviewFeign.getInterviewList(maps,page,pageSize).getData();

        Collection<CatalogVo> catalogVoCollection = stationFeign.getCatalogTreeBySiteId(maps.get("siteId").toString()).getData();
        CatalogVo catalogVo = getCatalog(catalogVoCollection,maps.get("catalogId").toString());

        String pageDynamicSuffix = TemplateConstants.PAGE_SUFFIX;

        PageUtil<ModelVo> pageUtil = new PageUtil<>();
        pageUtil.setCurrent(result.getCurrent());
        pageUtil.setPages(result.getPages());
        pageUtil.setTotal(result.getTotal());
        pageUtil.setSize(result.getSize());
        pageUtil.setRecords(result.getRecords());

        for(ModelVo modelVo:result.getRecords()){
            modelVo.setUrl("/"+catalogVo.getPathName()+"/details/interview/"+modelVo.getId()+pageDynamicSuffix);
        }

        return PageUrlUtil.setUrl(pageUtil,catalogVo,pageDynamicSuffix);
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
