package com.deyatech.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.admin.entity.Metadata;
import com.deyatech.admin.entity.MetadataCollection;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.station.entity.Template;
import com.deyatech.station.entity.TemplateFormOrder;
import com.deyatech.station.mapper.TemplateFormOrderMapper;
import com.deyatech.station.service.TemplateFormOrderService;
import com.deyatech.station.vo.TemplateFormOrderVo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        String userId = UserContextHelper.getUserId();
        Map<String, Object> result = new HashMap<>();
        // 未排序
        List<TemplateFormOrderVo> unsortedList = new ArrayList<>();
        // 已排序
        List<List<TemplateFormOrderVo>> sortedList = new ArrayList<>();
        // 元数据
        List<TemplateFormOrderVo> metadata = baseMapper.getMetadataByCollectionId(collectionId);
        metadata.stream().forEach(m -> m.setUserId(userId));
        // 排序数据
        List<TemplateFormOrderVo> sortedData = baseMapper.getSortDataByCollectionId(userId, collectionId);
        // 没有
        if (CollectionUtil.isEmpty(sortedData)) {
            // 未排序添加-基础字段
            getBaseFieldsList(collectionId).stream().forEach(t -> unsortedList.add(t));
            if (CollectionUtil.isNotEmpty(metadata)) {
                // 未排序添加-元数据字段
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
            Map<String, Metadata> baseFieldsMap = Template.baseFields();
            // 将对象添加到对应的页
            baseSortedDataList.stream().forEach(order -> {
                order.setMetadataName(baseFieldsMap.get(order.getMetadataId()).getName());
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
                sortedList.set(i, sortedList.get(i).stream().sorted(Comparator.comparing(TemplateFormOrderVo::getSortNo)).collect(Collectors.toList()));
            }
        }
        result.put("unsorted", unsortedList);
        result.put("sorted", sortedList);
        return result;
    }

    /**
     * 默认排序数据
     *
     * @param collectionId
     * @return
     */
    private List<TemplateFormOrderVo> getBaseFieldsList(String collectionId) {
        String userId = UserContextHelper.getUserId();
        List<TemplateFormOrderVo> list = new ArrayList<>();
        Map<String, Metadata> baseFields = Template.baseFields();
        Iterator<String> keys = baseFields.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            TemplateFormOrderVo item = new TemplateFormOrderVo();
            item.setUserId(userId);
            item.setCollectionId(collectionId);
            item.setMetadataId(key);
            item.setMetadataName(baseFields.get(key).getName());
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
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateByJson(String collectionId, String json) {
        try {
            String userId = UserContextHelper.getUserId();
            TemplateFormOrder entity = new TemplateFormOrder();
            entity.setUserId(userId);
            entity.setCollectionId(collectionId);
            super.removeByBean(entity);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, TemplateFormOrder.class);
            List<TemplateFormOrder> list = mapper.readValue(json, javaType);
            list.stream().forEach(o -> {
                o.setUserId(userId);
                o.setCollectionId(collectionId);
            });
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

    /**
     * 获取表单顺序
     *
     * @param collectionId
     * @return
     */
    @Override
    public Map<String, Object> getFormOrderByCollectionId(String collectionId) {
        String userId = UserContextHelper.getUserId();
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> map = getSortDataByCollectionId(collectionId);
        // 未排序
        List<TemplateFormOrderVo> unsortedList = (List<TemplateFormOrderVo>) map.get("unsorted");
        // 已排序
        List<List<TemplateFormOrderVo>> sortedList = (List<List<TemplateFormOrderVo>>) map.get("sorted");
        // 页码
        List<TemplateFormOrderVo> pages = baseMapper.getNumberAndNameByCollectionId(userId, collectionId);
        // 有排序
        if (CollectionUtil.isNotEmpty(pages)) {
            // 有新增未排序的
            if (CollectionUtil.isNotEmpty(unsortedList)) {
                // 没有排序
                TemplateFormOrderVo form = new TemplateFormOrderVo();
                form.setPageNumber(pages.size() + 1);
                form.setPageName("未排序表单");
                pages.add(form);
                sortedList.add(unsortedList);
            }
            result.put("pages", pages);
            result.put("orders", sortedList);
        } else {
            // 没有排序
            TemplateFormOrderVo form = new TemplateFormOrderVo();
            form.setPageNumber(1);
            form.setPageName("未排序表单");
            pages = new ArrayList<>();
            pages.add(form);
            result.put("pages", pages);

            List<Object> base = new ArrayList<>();
            base.add(unsortedList);
            result.put("orders", base);
        }
        return result;
    }

    /**
     * 获页数和页名
     *
     * @param collectionId
     * @return
     */
    @Override
    public List<TemplateFormOrderVo> getNumberAndNameByCollectionId(String collectionId) {
        return baseMapper.getNumberAndNameByCollectionId(UserContextHelper.getUserId(), collectionId);
    }

    @Override
    public List<Metadata> getAllMetadataByByCollectionId(String collectionId) {
        return baseMapper.getAllMetadataByByCollectionId(collectionId);
    }

    @Override
    public MetadataCollection getCollectionById(String id) {
        return  baseMapper.getCollectionById(id);
    }
}
