package com.deyatech.station.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.entity.CatalogRole;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.vo.CatalogRoleVo;
import com.deyatech.station.mapper.CatalogRoleMapper;
import com.deyatech.station.service.CatalogRoleService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 角色栏目关联 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-01
 */
@Service
public class CatalogRoleServiceImpl extends BaseServiceImpl<CatalogRoleMapper, CatalogRole> implements CatalogRoleService {

    @Autowired
    CatalogService catalogService;

    /**
     * 单个将对象转换为vo角色栏目关联
     *
     * @param catalogRole
     * @return
     */
    @Override
    public CatalogRoleVo setVoProperties(CatalogRole catalogRole){
        CatalogRoleVo catalogRoleVo = new CatalogRoleVo();
        BeanUtil.copyProperties(catalogRole, catalogRoleVo);
        return catalogRoleVo;
    }

    /**
     * 批量将对象转换为vo角色栏目关联
     *
     * @param catalogRoles
     * @return
     */
    @Override
    public List<CatalogRoleVo> setVoProperties(Collection catalogRoles){
        List<CatalogRoleVo> catalogRoleVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(catalogRoles)) {
            for (Object catalogRole : catalogRoles) {
                CatalogRoleVo catalogRoleVo = new CatalogRoleVo();
                BeanUtil.copyProperties(catalogRole, catalogRoleVo);
                catalogRoleVos.add(catalogRoleVo);
            }
        }
        return catalogRoleVos;
    }

    /**
     * 设置角色栏目
     *
     * @param roleId
     * @param catalogIds
     * @param siteId
     */
    @Override
    public void setRoleCatalogs(String roleId, List<String> catalogIds, String siteId) {
        if (StrUtil.isNotEmpty(siteId)) {
            QueryWrapper<Catalog> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("site_id", siteId);
            List<Catalog> allCatalogs = catalogService.list(queryWrapper);
            if (CollectionUtil.isNotEmpty(allCatalogs)) {
                for (Catalog c : allCatalogs) {
                    CatalogRole catalogRole = new CatalogRole();
                    catalogRole.setRoleId(roleId);
                    catalogRole.setCatalogId(c.getId());
                    this.removeByBean(catalogRole);
                }
            }
        } else {
            CatalogRole catalogRole = new CatalogRole();
            catalogRole.setRoleId(roleId);
            this.removeByBean(catalogRole);
        }
        if (CollectionUtil.isNotEmpty(catalogIds)) {
            List<CatalogRole> list = new ArrayList<>();
            for (String catalogId : catalogIds) {
                CatalogRole cu = new CatalogRole();
                cu.setRoleId(roleId);
                cu.setCatalogId(catalogId);
                list.add(cu);
            }
            this.saveOrUpdateBatch(list);
        }
    }
}
