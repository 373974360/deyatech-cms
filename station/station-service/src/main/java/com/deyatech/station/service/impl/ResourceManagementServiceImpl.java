package com.deyatech.station.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.entity.Metadata;
import com.deyatech.admin.entity.MetadataCollection;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.common.Constants;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.entity.Template;
import com.deyatech.station.mapper.ResourceManagementMapper;
import com.deyatech.station.service.ResourceManagementService;
import com.deyatech.station.service.TemplateFormOrderService;
import com.deyatech.station.service.TemplateService;
import com.deyatech.station.vo.ResourceManagementVo;
import com.deyatech.station.vo.TemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-13
 */
@Service
public class ResourceManagementServiceImpl implements ResourceManagementService {

    @Autowired
    ResourceManagementMapper resourceManagementMapper;
    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    TemplateFormOrderService formOrderService;
    @Autowired
    TemplateService templateService;

    private Page getPage(ResourceManagementVo resource) {
        Page page = new Page();
        if (ObjectUtil.isNotNull(resource.getPage())) {
            page.setCurrent(resource.getPage());
        } else {
            page.setCurrent(Constants.DEFAULT_CURRENT_PAGE);
        }
        if (ObjectUtil.isNotNull(resource.getSize())) {
            page.setSize(resource.getSize());
        } else {
            page.setSize(Constants.DEFAULT_PAGE_SIZE);
        }
        return page;
    }

    /**
     * 翻页检索资源
     *
     * @param resource
     * @return
     */
    @Override
    public IPage<TemplateVo> pageByResourceManagement(ResourceManagementVo resource) {
        // 缓存
        Map<String, Map<String, Metadata>> matedataMapCache = new HashMap<>();
        IPage<TemplateVo> page = resourceManagementMapper.pageByResourceManagement(getPage(resource), resource);
        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            Map<String, String> departmentNameMap = templateService.getDepartmentIdNameMap();
            Map<String, Metadata> metadataMap;
            for (TemplateVo template : page.getRecords()) {
                if (StrUtil.isEmpty(template.getMetaDataCollectionId()) || StrUtil.isEmpty(template.getContentId())) {
                    continue;
                }
                template.setSourceName(departmentNameMap.get(template.getSource()) == null ? template.getSource() : departmentNameMap.get(template.getSource()));
                // 根据分类检索元数据
                if (Objects.isNull(matedataMapCache.get(template.getMetaDataCollectionId()))) {
                    metadataMap = getMetadatas(template.getMetaDataCollectionId());
                    matedataMapCache.put(template.getMetaDataCollectionId(), metadataMap);
                } else {
                    metadataMap = matedataMapCache.get(template.getMetaDataCollectionId());
                }
                if (Objects.isNull(metadataMap)) {
                    continue;
                }
                List<ResourceManagementVo> contentList = new ArrayList<>();
                template.setContentList(contentList);
                Map<String, Object> dataMap = adminFeign.getMetadataById(template.getMetaDataCollectionId(), template.getContentId()).getData();
                if (Objects.nonNull(dataMap)) {
                    Iterator<String> keys = dataMap.keySet().iterator();
                    while(keys.hasNext()) {
                        String key = keys.next();
                        Metadata md = metadataMap.get(key);
                        if (Objects.nonNull(md)) {
                            contentList.add(new ResourceManagementVo(md.getName(), key, objectToString(dataMap.get(key))));
                        }
                    }
                }
            }
        }
        return page;
    }

    private String objectToString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    private Map<String, Metadata> getMetadatas(String collectionId) {
        // 元数据集
        MetadataCollection collection = formOrderService.getCollectionById(collectionId);
        // 基础字段映射
        Map<String, Metadata> metadataMap = Template.baseFields();
        metadataMap = metadataMap.values().stream().collect(Collectors.toMap(Metadata::getBriefName, md -> md));
        // 元数据
        List<Metadata> metadatas = formOrderService.getAllMetadataByByCollectionId(collectionId);
        if (CollectionUtil.isNotEmpty(metadatas)) {
            for (Metadata md : metadatas) {
                String key = collection.getMdPrefix() + md.getBriefName();
                md.setBriefName(key);
                metadataMap.put(key, md);
            }
        }
        return metadataMap;
    }

}
