package com.deyatech.station.service.impl;

import com.deyatech.station.entity.File;
import com.deyatech.station.vo.FileVo;
import com.deyatech.station.mapper.FileMapper;
import com.deyatech.station.service.FileService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
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
public class FileServiceImpl extends BaseServiceImpl<FileMapper, File> implements FileService {

    /**
     * 单个将对象转换为vo
     *
     * @param file
     * @return
     */
    @Override
    public FileVo setVoProperties(File file){
        FileVo fileVo = new FileVo();
        BeanUtil.copyProperties(file, fileVo);
        return fileVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param files
     * @return
     */
    @Override
    public List<FileVo> setVoProperties(Collection files){
        List<FileVo> fileVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(files)) {
            for (Object file : files) {
                FileVo fileVo = new FileVo();
                BeanUtil.copyProperties(file, fileVo);
                fileVos.add(fileVo);
            }
        }
        return fileVos;
    }
}
