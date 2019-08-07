package com.deyatech.resource.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.common.enums.EnableEnum;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.service.DomainService;
import com.deyatech.resource.vo.StationGroupVo;
import com.deyatech.resource.mapper.StationGroupMapper;
import com.deyatech.resource.service.StationGroupService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 站群 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
@Service
public class StationGroupServiceImpl extends BaseServiceImpl<StationGroupMapper, StationGroup> implements StationGroupService {

    @Autowired
    DomainService domainService;

    /**
     * 单个将对象转换为vo站群
     *
     * @param stationGroup
     * @return
     */
    @Override
    public StationGroupVo setVoProperties(StationGroup stationGroup){
        StationGroupVo stationGroupVo = new StationGroupVo();
        BeanUtil.copyProperties(stationGroup, stationGroupVo);
        return stationGroupVo;
    }

    /**
     * 批量将对象转换为vo站群
     *
     * @param stationGroups
     * @return
     */
    @Override
    public List<StationGroupVo> setVoProperties(Collection stationGroups){
        List<StationGroupVo> stationGroupVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(stationGroups)) {
            for (Object stationGroup : stationGroups) {
                StationGroupVo stationGroupVo = new StationGroupVo();
                BeanUtil.copyProperties(stationGroup, stationGroupVo);
                stationGroupVos.add(stationGroupVo);
            }
        }
        return stationGroupVos;
    }

    /**
     * 根据分类编号统计站群个数
     * @param classificationId
     * @return
     */
    @Override
    public long countStationGroupByClassificationId(String classificationId) {
        return baseMapper.countStationGroupByClassificationId(classificationId);
    }

    /**
     * 根据分类编号列表统计站站群数
     * @param list
     * @return
     */
    @Override
    public long countStationGroupByClassificationIdList(List<String> list) {
        return baseMapper.countStationGroupByClassificationIdList(list);
    }

    /**
     * 根据条件查询站群
     *
     * @param stationGroupVo
     * @return
     */
    @Override
    public IPage<StationGroupVo> pageSelectByStationGroupVo(StationGroupVo stationGroupVo) {
        return baseMapper.pageSelectByStationGroupVo(getPageByBean(stationGroupVo), stationGroupVo);
    }

    /**
     * 根据条件查询所有站群
     * @param stationGroupVo
     * @return
     */
    @Override
    public Collection<StationGroupVo> listSelectByStationGroupVo(StationGroupVo stationGroupVo) {
        return baseMapper.listSelectByStationGroupVo(stationGroupVo);
    }

    /**
     * 根据分类编号统计名称件数
     *
     * @param id
     * @param classificationId
     * @param name
     * @return
     */
    @Override
    public long countNameByClassificationId(String id, String classificationId, String name) {
        return baseMapper.countNameByClassificationId(id, classificationId, name);
    }

    /**
     * 根据分类编号统计英文名称件数
     *
     * @param id
     * @param classificationId
     * @param englishName
     * @return
     */
    @Override
    public long countEnglishNameByClassificationId(String id, String classificationId, String englishName) {
        return baseMapper.countEnglishNameByClassificationId(id, classificationId, englishName);
    }

    /**
     * 根据分类编号统计简称件数
     *
     * @param id
     * @param classificationId
     * @param abbreviation
     * @return
     */
    @Override
    public long countAbbreviationByClassificationId(String id, String classificationId, String abbreviation) {
        return baseMapper.countAbbreviationByClassificationId(id, classificationId, abbreviation);
    }

    /**
     * 修改状态根据编号
     *
     * @param id
     * @param flag
     * @return
     */
    @Override
    public long runOrStopStationById(String id, String flag) {
        long count = 0;
        StationGroup stationGroup = getById(id);
        if ("run".equals(flag)) {
            count = baseMapper.updateEnableById(id, EnableEnum.ENABLE.getCode());
            if (count > 0) {
                domainService.enableNginxConfig(stationGroup);
            }
        } else if ("stop".equals(flag)) {
            count = baseMapper.updateEnableById(id, EnableEnum.DISABLE.getCode());
            if (count > 0) {
                domainService.disableNginxConfig(stationGroup);
            }
        }
        return count;
    }

    /**
     * 根据编号检索站群
     *
     * @param id
     * @return
     */
    @Override
    public StationGroup getById(Serializable id) {
        return baseMapper.getStationGroupById(id);
    }

    /**
     * 删除站群
     *
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeStationGroupAndConfig(List<String> ids, Map<String, StationGroup> maps) {
        // 删除站群
        long count = baseMapper.updateEnableByIds(ids, EnableEnum.DELETED.getCode());
        if (count > 0) {
            for(String id : ids) {
                // 删除站群下的域名
                domainService.updateEnableByStationGroupId(id, EnableEnum.DELETED.getCode());
                // 删除禁用的配置文件
                domainService.deleteNginxConfig(maps.get(id));
            }
            return true;
        } else {
            return false;
        }
    }
}
