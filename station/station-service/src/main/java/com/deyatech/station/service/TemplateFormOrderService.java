package com.deyatech.station.service;

import com.deyatech.admin.entity.MetadataCollection;
import com.deyatech.common.base.BaseService;
import com.deyatech.station.entity.TemplateFormOrder;
import com.deyatech.station.vo.TemplateFormOrderVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  内容表单顺 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-04
 */
public interface TemplateFormOrderService extends BaseService<TemplateFormOrder> {

    /**
     * 单个将对象转换为vo内容表单顺
     *
     * @param templateFormOrder
     * @return
     */
    TemplateFormOrderVo setVoProperties(TemplateFormOrder templateFormOrder);

    /**
     * 批量将对象转换为vo内容表单顺
     *
     * @param templateFormOrders
     * @return
     */
    List<TemplateFormOrderVo> setVoProperties(Collection templateFormOrders);

    /**
     * 获取排序数据
     *
     * @param collectionId
     * @return
     */
    Map<String, Object> getSortDataByCollectionId(String collectionId);

    /**
     * 保存或者更新
     *
     * @param collectionId
     * @param json
     * @return
     */
    boolean saveOrUpdateByJson(String collectionId, String json);

    /**
     * 获取元数据集列表
     *
     * @param enName
     * @return
     */
    List<MetadataCollection> getCollectionList(String enName);
}
