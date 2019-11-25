package com.deyatech.station.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.entity.Material;
import com.deyatech.station.vo.MaterialDirectoryVo;
import com.deyatech.station.vo.MaterialVo;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据目录检索材料
     *
     * @param directory
     * @return
     */
    IPage<MaterialVo> pageByDirectory(@Param("page") IPage page, @Param("directory") MaterialDirectoryVo directory);

    /**
     * 翻页检索
     *
     * @param page
     * @param material
     * @return
     */
    IPage<MaterialVo> pageByMaterial(@Param("page") IPage page, @Param("material") Material material);

    /**
     * 删除材料
     *
     * @param ids
     * @return
     */
    int deleteMaterialByIds(@Param("ids") List<String> ids);

    /**
     * 检索材料
     *
     * @param ids
     * @return
     */
    List<MaterialVo> selectMaterialByIds(@Param("ids") List<String> ids);
}
