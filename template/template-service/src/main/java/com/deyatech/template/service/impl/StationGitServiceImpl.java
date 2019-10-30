package com.deyatech.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.deyatech.common.Constants;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.feign.ResourceFeign;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.template.entity.StationGit;
import com.deyatech.template.utils.FileResource;
import com.deyatech.template.vo.StationGitVo;
import com.deyatech.template.mapper.StationGitMapper;
import com.deyatech.template.service.StationGitService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 站点git模板地址信息 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-06
 */
@Service
public class StationGitServiceImpl extends BaseServiceImpl<StationGitMapper, StationGit> implements StationGitService {

    @Autowired
    StationFeign stationFeign;
    @Autowired
    StationGitMapper stationGitMapper;

    /**
     * 单个将对象转换为vo站点git模板地址信息
     *
     * @param stationGit
     * @return
     */
    @Override
    public StationGitVo setVoProperties(StationGit stationGit){
        StationGitVo stationGitVo = new StationGitVo();
        BeanUtil.copyProperties(stationGit, stationGitVo);
        return stationGitVo;
    }

    /**
     * 批量将对象转换为vo站点git模板地址信息
     *
     * @param stationGits
     * @return
     */
    @Override
    public List<StationGitVo> setVoProperties(Collection stationGits){
        List<StationGitVo> stationGitVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(stationGits)) {
            for (Object stationGit : stationGits) {
                StationGitVo stationGitVo = new StationGitVo();
                BeanUtil.copyProperties(stationGit, stationGitVo);
                stationGitVos.add(stationGitVo);
            }
        }
        return stationGitVos;
    }

    @Override
    public List<StationGitVo> listByStationGroupAndStationGit() {
        return stationGitMapper.listByStationGroupAndStationGit();
    }

    @Override
    public String getTemplateFiles(String siteId,String path) {
        String dir = stationFeign.getStationGroupTemplatePathBySiteId(siteId).getData();
        JSONObject jsonObject = new JSONObject();
        if(StringUtils.isEmpty(path)){
            path = dir;
        }
        String files = FileResource.getFiles(dir, path);
        jsonObject.put("files", files);
        return jsonObject.toString();
    }

    @Override
    public String getTemplateAllFiles(String siteId,String type) {
        JSONObject jsonObject = new JSONObject();
        String dir = stationFeign.getStationGroupTemplatePathBySiteId(siteId).getData();
        String files = FileResource.getAllFiles(dir,type);
        jsonObject.put("files", files);
        return jsonObject.toString();
    }

    @Override
    public String getFileContent(String path) {
        return FileResource.readToString2(path);
    }

    @Override
    public StationGit getStationGitBySiteId(String siteId) {
        StationGit stationGit = null;
        List<StationGit> list = getBaseMapper().selectList(new QueryWrapper<StationGit>()
                .eq("site_id",siteId));
        if(!list.isEmpty()){
            stationGit = list.get(0);
        }
        return stationGit;
    }
}
