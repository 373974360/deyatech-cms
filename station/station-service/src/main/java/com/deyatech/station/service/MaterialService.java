package com.deyatech.station.service;

import com.deyatech.common.base.BaseService;
import com.deyatech.station.entity.Material;
import com.deyatech.station.vo.MaterialVo;

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

    /**
     * 获取站点上传文件路径
     *
     * @param siteId
     * @return
     */
    String getSiteUploadPath(String siteId);

    /**
     * 根据url获取材料
     *
     * @param url
     * @return
     */
    List<MaterialVo> getDownloadMaterialsByUrl(String url);
}
