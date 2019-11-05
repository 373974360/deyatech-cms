package com.deyatech.station.service.impl;

import com.deyatech.station.entity.Template;
import com.deyatech.station.entity.TemplateFormOrder;
import com.deyatech.station.vo.TemplateFormOrderVo;
import com.deyatech.station.mapper.TemplateFormOrderMapper;
import com.deyatech.station.service.TemplateFormOrderService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 内容表单顺 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-04
 */
@Service
public class TemplateFormOrderServiceImpl extends BaseServiceImpl<TemplateFormOrderMapper, TemplateFormOrder> implements TemplateFormOrderService {

    /**
     * 单个将对象转换为vo内容表单顺
     *
     * @param templateFormOrder
     * @return
     */
    @Override
    public TemplateFormOrderVo setVoProperties(TemplateFormOrder templateFormOrder){
        TemplateFormOrderVo templateFormOrderVo = new TemplateFormOrderVo();
        BeanUtil.copyProperties(templateFormOrder, templateFormOrderVo);
        return templateFormOrderVo;
    }

    /**
     * 批量将对象转换为vo内容表单顺
     *
     * @param templateFormOrders
     * @return
     */
    @Override
    public List<TemplateFormOrderVo> setVoProperties(Collection templateFormOrders){
        List<TemplateFormOrderVo> templateFormOrderVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(templateFormOrders)) {
            for (Object templateFormOrder : templateFormOrders) {
                TemplateFormOrderVo templateFormOrderVo = new TemplateFormOrderVo();
                BeanUtil.copyProperties(templateFormOrder, templateFormOrderVo);
                templateFormOrderVos.add(templateFormOrderVo);
            }
        }
        return templateFormOrderVos;
    }

    /**
     * 获取排序数据
     *
     * @param collectionId
     * @return
     */
    @Override
    public Map<String, Object> getSortDataByCollectionId(String collectionId) {
        Map<String, Object> result = new HashMap<>();
        // 未排序
        List<TemplateFormOrderVo> unsorted = new ArrayList<>();
        // 已排序
        List<List<TemplateFormOrderVo>> sorted = new ArrayList<>();
        // 元数据
        List<TemplateFormOrderVo> metadata = baseMapper.getMetadataByCollectionId(collectionId);
        // 排序数据
        List<TemplateFormOrderVo> sortedData = baseMapper.getSortDataByCollectionId(collectionId);
        // 没有
        if (CollectionUtil.isEmpty(sortedData)) {
            getBaseFields(collectionId).stream().forEach(t -> unsorted.add(t));
            if (CollectionUtil.isNotEmpty(metadata)) {
                metadata.stream().forEach(t -> unsorted.add(t));
            }
            result.put("maxPageNumber", 0);
        } else {
            // 总页数
            int maxPageNumber = sortedData.stream().max((t1,t2)-> t1.getPageNumber() > t2.getPageNumber() ? 1 : -1).get().getPageNumber();
            result.put("maxPageNumber", maxPageNumber);
            for (int page = 0; page < maxPageNumber; page++) {
                sorted.add(new ArrayList<>());
            }
            // 基础映射
            List<TemplateFormOrderVo> baseSortedDataList = sortedData.stream().filter(t -> t.getMetadataId().length() <= 3).collect(Collectors.toList());
            baseSortedDataList.stream().forEach(order -> sorted.get(order.getPageNumber() - 1).add(order));
            // 元数据映射
            Map<String, TemplateFormOrderVo> metadataSortedDataMap = sortedData.stream().filter(t -> t.getMetadataId().length() > 3).collect(Collectors.toMap(TemplateFormOrderVo::getMetadataId, t -> t));
            // 处理元数据增加减少变更
            if (CollectionUtil.isNotEmpty(metadata)) {
                metadata.stream().forEach(t -> {
                    TemplateFormOrderVo order = metadataSortedDataMap.get(t.getMetadataId());
                    // 变更重新复制，减少丢弃
                    if (Objects.nonNull(order)) {
                        order.setMetadataName(t.getMetadataName());
                        sorted.get(order.getPageNumber() - 1).add(order);
                    } else {
                        // 增加
                        unsorted.add(t);
                    }
                });
            }
            for (int i = 0; i < sorted.size(); i++) {
                sorted.set(i, sorted.get(i).stream().sorted((t1,t2)-> t1.getPageNumber() > t2.getPageNumber() ? 1 : -1).collect(Collectors.toList()));
            }
        }
        result.put("unsorted", unsorted);
        result.put("sorted", sorted);
        return result;
    }

    private List<TemplateFormOrderVo> getBaseFields(String collectionId) {
        List<TemplateFormOrderVo> list = new ArrayList<>();
        Map<String, String> baseFields = Template.baseFields();
        Iterator<String> keys = baseFields.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            String value = baseFields.get(key);
            String[] array = value.split(",");
            TemplateFormOrderVo item = new TemplateFormOrderVo();
            item.setCollectionId(collectionId);
            item.setMetadataId(key);
            item.setMetadataName(array[1]);
            list.add(item);
        }
        return list.stream().sorted(Comparator.comparing(TemplateFormOrderVo::getMetadataId)).collect(Collectors.toList());
    }
}
