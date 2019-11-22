package com.deyatech.station.mapper;

import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.entity.Material;
import com.deyatech.station.vo.MaterialVo;

import java.util.List;

/**
 * <p>
 * 上传文件信息 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-08-28
 */
public interface MaterialMapper extends BaseMapper<Material> {
    /**
     * 根据url获取材料
     *
     * @param list
     * @return
     */
    List<MaterialVo> getDownloadMaterialsByUrl(List<String> list);
}
