package com.deyatech.station.service;

import com.deyatech.station.entity.File;
import com.deyatech.station.vo.FileVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 上传文件信息  服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-28
 */
public interface FileService extends BaseService<File> {

    /**
     * 单个将对象转换为vo
     *
     * @param file
     * @return
     */
    FileVo setVoProperties(File file);

    /**
     * 批量将对象转换为vo
     *
     * @param files
     * @return
     */
    List<FileVo> setVoProperties(Collection files);
}
