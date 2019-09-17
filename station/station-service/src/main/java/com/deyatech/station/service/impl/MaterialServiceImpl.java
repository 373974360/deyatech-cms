package com.deyatech.station.service.impl;

import com.deyatech.station.entity.Material;
import com.deyatech.station.vo.MaterialVo;
import com.deyatech.station.mapper.MaterialMapper;
import com.deyatech.station.service.MaterialService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 上传文件信息 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-28
 */
@Service
public class MaterialServiceImpl extends BaseServiceImpl<MaterialMapper, Material> implements MaterialService {

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
}
