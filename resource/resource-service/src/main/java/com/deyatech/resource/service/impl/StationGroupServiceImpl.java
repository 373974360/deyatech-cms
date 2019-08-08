package com.deyatech.resource.service.impl;

import cn.hutool.core.util.StrUtil;
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
import java.util.Objects;

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
     * 统计名称件数
     *
     * @param id
     * @param name
     * @return
     */
    @Override
    public long countName(String id, String name) {
        return baseMapper.countName(id, name);
    }

    /**
     * 统计英文名称件数
     *
     * @param id
     * @param englishName
     * @return
     */
    @Override
    public long countEnglishName(String id, String englishName) {
        return baseMapper.countEnglishName(id, englishName);
    }

    /**
     * 统计简称件数
     *
     * @param id
     * @param abbreviation
     * @return
     */
    @Override
    public long countAbbreviation(String id, String abbreviation) {
        return baseMapper.countAbbreviation(id, abbreviation);
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
     * 启用或停用站群
     *
     * @param id
     * @param flag
     * @return
     */
    @Override
    public boolean runOrStopStationById(String id, String flag) {
        long count = 0;
        if ("run".equals(flag)) {
            count = baseMapper.updateEnableById(id, EnableEnum.ENABLE.getCode());
        } else if ("stop".equals(flag)) {
            count = baseMapper.updateEnableById(id, EnableEnum.DISABLE.getCode());
        }
        if (count > 0) {
            // 启用停用 站群下所有域名 nginx 配置
            domainService.runOrStopStationGroupNginxConfigAndPage(id);
            return true;
        } else {
            return false;
        }
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
            // 删除 站群下所有域名 nginx 配置
            domainService.removeStationGroupNginxConfigAndPage(ids);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新或保存
     * @param stationGroup
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateAndNginx(StationGroup stationGroup) {
        String oldStationGroupEnglishName = null;
        // 修改
        if (Objects.nonNull(stationGroup) && StrUtil.isNotEmpty(stationGroup.getId())) {
            // 旧站群
            StationGroup oldStationGroup = this.getById(stationGroup.getId());
            // 若站群英文名称变更，则 nginx 需要做对应变更
            if (!stationGroup.getEnglishName().equals(oldStationGroup.getEnglishName())) {
                oldStationGroupEnglishName = oldStationGroup.getEnglishName();
            }
        }
        boolean result = super.saveOrUpdate(stationGroup);
        if (result && StrUtil.isNotEmpty(oldStationGroupEnglishName)) {
            // 更新 站群下所有域名 nginx 配置
            domainService.updateStationGroupNginxConfigAndPage(stationGroup.getId(), oldStationGroupEnglishName);
        }
        return result;
    }
}
