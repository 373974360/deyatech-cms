package com.deyatech.station.service;

import com.deyatech.station.entity.Material;
import com.deyatech.station.vo.MaterialVo;
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
public interface MaterialService extends BaseService<Material> {

    /**
     * 单个将对象转换为vo
     *
     * @param material
     * @return
     */
    MaterialVo setVoProperties(Material material);

    /**
     * 批量将对象转换为vo
     *
     * @param files
     * @return
     */
    List<MaterialVo> setVoProperties(Collection files);
}
