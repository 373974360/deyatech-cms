package com.deyatech.station.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.station.entity.CatalogUser;
import com.deyatech.station.mapper.CatalogUserMapper;
import com.deyatech.station.service.CatalogService;
import com.deyatech.station.service.CatalogUserService;
import com.deyatech.station.vo.CatalogUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    CatalogService catalogService;

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
     * @param userIds
     * @param catalogIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setUsersCatalogs(List<String> userIds, List<String> catalogIds) {
        if (CollectionUtil.isNotEmpty(userIds)) {
            for (String userId : userIds) {
                // 删除原来的
                CatalogUser catalogUser = new CatalogUser();
                catalogUser.setUserId(userId);
                this.removeByBean(catalogUser);
                // 添加新的
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
    }

    /**
     * 取得用户栏目
     *
     * @param userIds
     * @return
     */
    @Override
    public List<String> getUsersCatalogs(List<String> userIds) {
        if (CollectionUtil.isEmpty(userIds)) {
            return null;
        }
        QueryWrapper<CatalogUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIds);
        List<CatalogUser> all = super.list(queryWrapper);
        if (CollectionUtil.isEmpty(all)) {
            return null;
        }
        if (userIds.size() == 1) {
            return all.stream().map(CatalogUser::getCatalogId).distinct().collect(Collectors.toList());
        }
        // 取出所有用户的交集栏目
        Map<String, List<String>> userCatalogIdsMap = new HashMap<>();
        int minLen = Integer.MAX_VALUE;
        List<String> resultCatalogIds = null;
        for (String userId : userIds) {
            List<String> catalogIds = all.stream().filter(cu -> cu.getUserId().equals(userId)).map(CatalogUser::getCatalogId).distinct().sorted(Comparator.comparing(catalogId -> catalogId)).collect(Collectors.toList());
            // 有一个用户没有，则交集为空
            if (CollectionUtil.isEmpty(catalogIds)) {
                return null;
            }
            if (minLen > catalogIds.size()) {
                minLen = catalogIds.size();
                resultCatalogIds = catalogIds;
            }
            userCatalogIdsMap.put(userId, catalogIds);
        }
        for (String userId : userIds) {
            resultCatalogIds.retainAll(userCatalogIdsMap.get(userId));
        }
        return resultCatalogIds;
    }

}
