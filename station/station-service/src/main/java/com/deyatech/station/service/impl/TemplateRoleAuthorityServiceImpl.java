package com.deyatech.station.service.impl;

import cn.hutool.core.map.MapUtil;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.entity.TemplateRoleAuthority;
import com.deyatech.station.vo.TemplateRoleAuthorityVo;
import com.deyatech.station.mapper.TemplateRoleAuthorityMapper;
import com.deyatech.station.service.TemplateRoleAuthorityService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 角色内容权限 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-01
 */
@Service
public class TemplateRoleAuthorityServiceImpl extends BaseServiceImpl<TemplateRoleAuthorityMapper, TemplateRoleAuthority> implements TemplateRoleAuthorityService {

    /**
     * 单个将对象转换为vo角色内容权限
     *
     * @param templateRoleAuthority
     * @return
     */
    @Override
    public TemplateRoleAuthorityVo setVoProperties(TemplateRoleAuthority templateRoleAuthority){
        TemplateRoleAuthorityVo templateRoleAuthorityVo = new TemplateRoleAuthorityVo();
        BeanUtil.copyProperties(templateRoleAuthority, templateRoleAuthorityVo);
        return templateRoleAuthorityVo;
    }

    /**
     * 批量将对象转换为vo角色内容权限
     *
     * @param templateRoleAuthoritys
     * @return
     */
    @Override
    public List<TemplateRoleAuthorityVo> setVoProperties(Collection templateRoleAuthoritys){
        List<TemplateRoleAuthorityVo> templateRoleAuthorityVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(templateRoleAuthoritys)) {
            for (Object templateRoleAuthority : templateRoleAuthoritys) {
                TemplateRoleAuthorityVo templateRoleAuthorityVo = new TemplateRoleAuthorityVo();
                BeanUtil.copyProperties(templateRoleAuthority, templateRoleAuthorityVo);
                templateRoleAuthorityVos.add(templateRoleAuthorityVo);
            }
        }
        return templateRoleAuthorityVos;
    }

    /**
     * 关联站点个数
     *
     * @param roleIds
     * @return
     */
    @Override
    public List<Map<String, Object>> getStationCount(List<String> roleIds) {return baseMapper.getStationCount(roleIds);}

    /**
     * 关联栏目个数
     *
     * @param roleIds
     * @return
     */
    @Override
    public List<Map<String, Object>> getCatalogCount(List<String> roleIds){return baseMapper.getCatalogCount(roleIds);}

    /**
     * 关联内容个数
     *
     * @param roleIds
     * @return
     */
    @Override
    public List<Map<String, Object>> getContentCount(List<String> roleIds) {return baseMapper.getContentCount(roleIds);}

    /**
     * 获取角色页面上的数量统计结果
     *
     * @param roleIds
     * @return
     */
    @Override
    public Map<String, String> getRoleViewCount(List<String> roleIds) {
        Map<String, String> result = MapUtil.newHashMap();
        if (CollectionUtil.isNotEmpty(roleIds)) {
            List<Map<String, Object>> stationList = getStationCount(roleIds);
            Map<String, Long> stationMap = MapUtil.newHashMap();
            if (CollectionUtil.isNotEmpty(stationList)) {
                for (Map<String, Object> m : stationList) {
                    stationMap.put((String) m.get("roleId"), (Long) m.get("cnt"));
                }
            }
            List<Map<String, Object>> catalogList = getCatalogCount(roleIds);
            Map<String, Long> catalogMap = MapUtil.newHashMap();
            if (CollectionUtil.isNotEmpty(catalogList)) {
                for (Map<String, Object> m : catalogList) {
                    catalogMap.put((String) m.get("roleId"), (Long) m.get("cnt"));
                }
            }
            List<Map<String, Object>> contentList = getContentCount(roleIds);
            Map<String, Long> contentMap = MapUtil.newHashMap();
            if (CollectionUtil.isNotEmpty(contentList)) {
                for (Map<String, Object> m : contentList) {
                    contentMap.put((String) m.get("roleId"), (Long) m.get("cnt"));
                }
            }
            roleIds.stream().forEach(id -> {
                StringBuilder count = new StringBuilder();
                if (Objects.nonNull(stationMap.get(id))) {
                    count.append(stationMap.get(id));
                } else {
                    count.append(0);
                }
                count.append("_");
                if (Objects.nonNull(catalogMap.get(id))) {
                    count.append(catalogMap.get(id));
                } else {
                    count.append(0);
                }
                count.append("_");
                if (Objects.nonNull(contentMap.get(id))) {
                    count.append(contentMap.get(id));
                } else {
                    count.append(0);
                }
                result.put(id, count.toString());
            });
        }
        return result;
    }
}
