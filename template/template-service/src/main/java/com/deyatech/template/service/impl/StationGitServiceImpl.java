package com.deyatech.template.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.feign.ResourceFeign;
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

    /**
     * 模板文件存放路径
     */
    @Value("${site.hosts-root}")
    String templateDir;
    @Autowired
    StationGitMapper stationGitMapper;
    @Autowired
    ResourceFeign resourceFeign;

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
        StationGroup stationGroup = resourceFeign.getStationGroupById(siteId).getData();
        String dir = templateDir + stationGroup.getEnglishName();
        JSONObject jsonObject = new JSONObject();
        if(StringUtils.isEmpty(path)){
            path = dir;
        }
        String files = FileResource.getFiles(dir, path);
        jsonObject.put("files", files);
        return jsonObject.toString();
    }

    @Override
    public String getTemplateAllFiles(String siteId) {
        JSONObject jsonObject = new JSONObject();
        StationGroup stationGroup = resourceFeign.getStationGroupById(siteId).getData();
        String dir = templateDir + stationGroup.getEnglishName();
        String files = FileResource.getAllFiles(dir);
        jsonObject.put("files", files);
        return jsonObject.toString();
    }

    @Override
    public String getFileContent(String path) {
        return FileResource.readToString2(path);
    }
}
