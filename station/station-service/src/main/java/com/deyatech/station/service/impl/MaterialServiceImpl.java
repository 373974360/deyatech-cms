package com.deyatech.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.common.Constants;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.station.cache.SiteCache;
import com.deyatech.station.entity.Material;
import com.deyatech.station.mapper.MaterialMapper;
import com.deyatech.station.service.MaterialService;
import com.deyatech.station.vo.MaterialDirectoryVo;
import com.deyatech.station.vo.MaterialVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    /**
     * 获取站点目录树
     *
     * @param siteId
     * @return
     */
    @Override
    public List<MaterialDirectoryVo> getDirectoryTree(String siteId) {
        List<MaterialDirectoryVo> tree = new ArrayList<>();
        getTree(this.getSiteUploadPath(siteId), tree);
        return tree;
    }

    /**
     * 递归树目录
     *
     * @param path
     * @param tree
     */
    private static void getTree(String path, List<MaterialDirectoryVo> tree) {
        try {
            Files.newDirectoryStream(Paths.get(path))
                    .forEach(p -> {
                        File file = p.toFile();
                        if (file.isDirectory()) {
                            MaterialDirectoryVo directory = new MaterialDirectoryVo();
                            directory.setLabel(file.getName());
                            directory.setChildren(new ArrayList<>());
                            directory.setPath(file.getAbsolutePath());
                            getTree(file.getAbsolutePath(), directory.getChildren());
                            tree.add(directory);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据树目录分页检索
     *
     * @param directory
     * @return
     */
    @Override
    public IPage<MaterialVo> pageByDirectory(MaterialDirectoryVo directory) {
        Page page = new Page();
        page.setCurrent(directory.getPage());
        page.setSize(directory.getSize());
        return baseMapper.pageByDirectory(page, directory);
    }

    /**
     * 根据材料翻页检索
     *
     * @param material
     * @return
     */
    @Override
    public IPage<MaterialVo> pageByMaterial(Material material) {
        return baseMapper.pageByMaterial(getPageByBean(material),material);
    }

    /**
     * 物理删除材料
     *
     * @param ids
     * @return
     */
    @Override
    public int deletePhysicsMaterialByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return 0;
        }
        List<MaterialVo> list = baseMapper.selectMaterialByIds(ids);
        int count = baseMapper.deleteMaterialByIds(ids);
        if (count > 0) {
            list.stream().forEach(m -> {
                try {
                    log.info("删除文件：" + m.getPath());
                    Files.deleteIfExists(Paths.get(m.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return count;
    }
}
