package com.deyatech.resource.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.vo.StationGroupVo;
import com.deyatech.resource.mapper.StationGroupMapper;
import com.deyatech.resource.service.StationGroupService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

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
     * 根据分类编号统计网站个数
     * @param classificationId
     * @return
     */
    @Override
    public long countStationGroupByClassificationId(String classificationId) {
        return baseMapper.countStationGroupByClassificationId(classificationId);
    }

    /**
     * 根据分类编号列表统计站网站数
     * @param list
     * @return
     */
    @Override
    public long countStationGroupByClassificationIdList(List<String> list) {
        return baseMapper.countStationGroupByClassificationIdList(list);
    }

    /**
     * 根据条件查询网站
     *
     * @param stationGroupVo
     * @return
     */
    @Override
    public IPage<StationGroupVo> pageSelectByCondition(StationGroupVo stationGroupVo) {
        return baseMapper.pageSelectByCondition(getPageByBean(stationGroupVo), stationGroupVo);
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
}
