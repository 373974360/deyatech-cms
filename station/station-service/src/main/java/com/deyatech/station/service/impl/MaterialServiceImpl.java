package com.deyatech.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.common.Constants;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Material;
import com.deyatech.station.mapper.MaterialMapper;
import com.deyatech.station.service.MaterialService;
import com.deyatech.station.vo.MaterialVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 上传文件信息 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-28
 */
@Service
@Slf4j
public class MaterialServiceImpl extends BaseServiceImpl<MaterialMapper, Material> implements MaterialService {

    @Autowired
    SiteCache siteCache;

    /**
     * 单个将对象转换为vo
     *
     * @param material
     * @return
     */
    @Override
    public MaterialVo setVoProperties(Material material){
        MaterialVo fileVo = new MaterialVo();
        BeanUtil.copyProperties(material, fileVo);
        return fileVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param files
     * @return
     */
    @Override
    public List<MaterialVo> setVoProperties(Collection files){
        List<MaterialVo> fileVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(files)) {
            for (Object file : files) {
                MaterialVo fileVo = new MaterialVo();
                BeanUtil.copyProperties(file, fileVo);
                fileVos.add(fileVo);
            }
        }
        return fileVos;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            Material material = getById(id);
            File file = new File(material.getPath());
            if (!file.exists() || file.delete()) {
                removeById(id);
            }
        }
        return true;
    }

    /**
     * 获取站点上传文件路径
     *
     * @param siteId
     * @return
     */
    @Override
    public String getSiteUploadPath(String siteId) {
        String path =  new File(siteCache.getStationGroupRootPath(siteId), Constants.UPLOAD_DEFAULT_PREFIX_URL).getAbsolutePath();
        path = path.replace("\\", "/");
        if (!path.endsWith("/")) {
            path += "/";
        }
        log.info("站点材料上传路径：" + path);
        return path;
    }

    /**
     * 根据url获取材料
     *
     * @param url
     * @return
     */
    @Override
    public List<MaterialVo> getDownloadMaterialsByUrl(String url) {
        if (StrUtil.isEmpty(url))
            return new ArrayList<>();
        return baseMapper.getDownloadMaterialsByUrl(Arrays.asList(url.split(",")));
    }
}
