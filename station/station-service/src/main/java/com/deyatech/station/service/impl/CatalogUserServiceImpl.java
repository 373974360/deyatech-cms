package com.deyatech.station.service.impl;

import com.deyatech.station.entity.CatalogUser;
import com.deyatech.station.vo.CatalogUserVo;
import com.deyatech.station.mapper.CatalogUserMapper;
import com.deyatech.station.service.CatalogUserService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 栏目用户关联信息 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-10
 */
@Service
public class CatalogUserServiceImpl extends BaseServiceImpl<CatalogUserMapper, CatalogUser> implements CatalogUserService {

    /**
     * 单个将对象转换为vo栏目用户关联信息
     *
     * @param catalogUser
     * @return
     */
    @Override
    public CatalogUserVo setVoProperties(CatalogUser catalogUser){
        CatalogUserVo catalogUserVo = new CatalogUserVo();
        BeanUtil.copyProperties(catalogUser, catalogUserVo);
        return catalogUserVo;
    }

    /**
     * 批量将对象转换为vo栏目用户关联信息
     *
     * @param catalogUsers
     * @return
     */
    @Override
    public List<CatalogUserVo> setVoProperties(Collection catalogUsers){
        List<CatalogUserVo> catalogUserVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(catalogUsers)) {
            for (Object catalogUser : catalogUsers) {
                CatalogUserVo catalogUserVo = new CatalogUserVo();
                BeanUtil.copyProperties(catalogUser, catalogUserVo);
                catalogUserVos.add(catalogUserVo);
            }
        }
        return catalogUserVos;
    }

    /**
     * 设置用户栏目
     *
     * @param userId
     * @param catalogIds
     */
    @Override
    public void setUserCatalogs(String userId, List<String> catalogIds) {
        CatalogUser catalogUser = new CatalogUser();
        catalogUser.setUserId(userId);
        this.removeByBean(catalogUser);
        if (CollectionUtil.isNotEmpty(catalogIds)) {
            List<CatalogUser> list = new ArrayList<>();
            for (String catalogId : catalogIds) {
                CatalogUser cu = new CatalogUser();
                cu.setUserId(userId);
                cu.setCatalogId(catalogId);
                list.add(cu);
            }
            this.saveOrUpdateBatch(list);
        }
    }
}
