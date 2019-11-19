package com.deyatech.station.mapper;

import com.deyatech.admin.entity.Metadata;
import com.deyatech.admin.entity.MetadataCollection;
import com.deyatech.station.entity.TemplateFormOrder;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.vo.TemplateFormOrderVo;

import java.util.List;

/**
 * <p>
 * 内容表单顺 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-11-04
 */
public interface TemplateFormOrderMapper extends BaseMapper<TemplateFormOrder> {

    /**
     * 获取元数据
     *
     * @param collectionId
     * @return
     */
    List<TemplateFormOrderVo>  getMetadataByCollectionId(String collectionId);
    List<Metadata> getAllMetadataByByCollectionId(String collectionId);



    /**
     * 获取元数据集列表
     *
     * @param enName
     * @return
     */
    List<MetadataCollection> getCollectionList(String enName);
    MetadataCollection getCollectionById(String id);

    /**
     * 获取排序数据
     *
     * @param userId
     * @param collectionId
     * @return
     */
    List<TemplateFormOrderVo>  getSortDataByCollectionId(String userId, String collectionId);

    /**
     * 获页数和页名
     *
     * @param collectionId
     * @return
     */
    List<TemplateFormOrderVo>  getNumberAndNameByCollectionId(String userId, String collectionId);
}
