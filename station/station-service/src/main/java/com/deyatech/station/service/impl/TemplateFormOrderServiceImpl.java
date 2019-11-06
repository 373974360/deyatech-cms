package com.deyatech.station.service.impl;

import com.deyatech.admin.entity.MetadataCollection;
import com.deyatech.station.entity.Template;
import com.deyatech.station.entity.TemplateFormOrder;
import com.deyatech.station.vo.TemplateFormOrderVo;
import com.deyatech.station.mapper.TemplateFormOrderMapper;
import com.deyatech.station.service.TemplateFormOrderService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    ObjectMapper mapper = new ObjectMapper();

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
        List<TemplateFormOrderVo> unsortedList = new ArrayList<>();
        // 已排序
        List<List<TemplateFormOrderVo>> sortedList = new ArrayList<>();
        // 元数据
        List<TemplateFormOrderVo> metadata = baseMapper.getMetadataByCollectionId(collectionId);
        // 排序数据
        List<TemplateFormOrderVo> sortedData = baseMapper.getSortDataByCollectionId(collectionId);
        // 没有
        if (CollectionUtil.isEmpty(sortedData)) {
            getBaseFieldsList(collectionId).stream().forEach(t -> unsortedList.add(t));
            if (CollectionUtil.isNotEmpty(metadata)) {
                metadata.stream().forEach(t -> unsortedList.add(t));
            }
            result.put("maxPageNumber", 0);
        } else {
            // 总页数
            int maxPageNumber = sortedData.stream().max((t1,t2)-> t1.getPageNumber() > t2.getPageNumber() ? 1 : -1).get().getPageNumber();
            result.put("maxPageNumber", maxPageNumber);
            // 构建每一页的空列表
            for (int page = 0; page < maxPageNumber; page++) {
                sortedList.add(new ArrayList<>());
            }
            // 基础映射
            List<TemplateFormOrderVo> baseSortedDataList = sortedData.stream().filter(t -> t.getMetadataId().length() <= 3).collect(Collectors.toList());
            Map<String, String> baseFieldsMap = Template.baseFields();
            // 将对象添加到对应的页
            baseSortedDataList.stream().forEach(order -> {
                String value = baseFieldsMap.get(order.getMetadataId());
                order.setMetadataName(value.split(",")[1]);
                sortedList.get(order.getPageNumber() - 1).add(order);
            });
            // 元数据映射
            Map<String, TemplateFormOrderVo> metadataSortedDataMap = sortedData.stream().filter(t -> t.getMetadataId().length() > 3).collect(Collectors.toMap(TemplateFormOrderVo::getMetadataId, t -> t));
            // 处理元数据增加减少变更
            if (CollectionUtil.isNotEmpty(metadata)) {
                metadata.stream().forEach(t -> {
                    TemplateFormOrderVo order = metadataSortedDataMap.get(t.getMetadataId());
                    // 变更重新复制，减少丢弃
                    if (Objects.nonNull(order)) {
                        order.setMetadataName(t.getMetadataName());
                        sortedList.get(order.getPageNumber() - 1).add(order);
                    } else {
                        // 增加
                        unsortedList.add(t);
                    }
                });
            }
            for (int i = 0; i < sortedList.size(); i++) {
                //sortedList.set(i, sortedList.get(i).stream().sorted((t1,t2)-> t1.getSortNo() > t2.getSortNo() ? 1 : -1).collect(Collectors.toList()));
                sortedList.set(i, sortedList.get(i).stream().sorted(Comparator.comparing(TemplateFormOrderVo::getSortNo)).collect(Collectors.toList()));
            }
        }
        result.put("unsorted", unsortedList);
        result.put("sorted", sortedList);
        return result;
    }

    private List<TemplateFormOrderVo> getBaseFieldsList(String collectionId) {
        List<TemplateFormOrderVo> list = new ArrayList<>();
        Map<String, String> baseFields = Template.baseFields();
        Iterator<String> keys = baseFields.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            String value = baseFields.get(key);
            TemplateFormOrderVo item = new TemplateFormOrderVo();
            item.setCollectionId(collectionId);
            item.setMetadataId(key);
            item.setMetadataName(value.split(",")[1]);
            list.add(item);
        }
        return list.stream().sorted(Comparator.comparing(TemplateFormOrderVo::getMetadataId)).collect(Collectors.toList());
    }

    /**
     * 保存或者更新
     *
     * @param collectionId
     * @param json
     * @return
     */
    @Override
    public boolean saveOrUpdateByJson(String collectionId, String json) {
        try {
            TemplateFormOrder entity = new TemplateFormOrder();
            entity.setCollectionId(collectionId);
            super.removeByBean(entity);

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, TemplateFormOrder.class);
            List<TemplateFormOrder> list = mapper.readValue(json, javaType);
            return super.saveOrUpdateBatch(list);
        } catch (IOException e) {
           e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取元数据集列表
     *
     * @param enName
     * @return
     */
    @Override
    public List<MetadataCollection> getCollectionList(String enName) { return baseMapper.getCollectionList(enName); }
}
