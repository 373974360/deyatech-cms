package com.deyatech.resource.service.impl;

import com.deyatech.resource.entity.StationGroupClassification;
import com.deyatech.resource.vo.StationGroupClassificationVo;
import com.deyatech.resource.mapper.StationGroupClassificationMapper;
import com.deyatech.resource.service.StationGroupClassificationService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.Constants;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
@Service
public class StationGroupClassificationServiceImpl extends BaseServiceImpl<StationGroupClassificationMapper, StationGroupClassification> implements StationGroupClassificationService {

    /**
     * 根据StationGroupClassification对象属性检索的tree对象
     *
     * @param stationGroupClassification
     * @return
     */
    @Override
    public Collection<StationGroupClassificationVo> getStationGroupClassificationTree(StationGroupClassification stationGroupClassification) {
        stationGroupClassification.setSortSql("sortNo asc");
        List<StationGroupClassificationVo> stationGroupClassificationVos = setVoProperties(super.listByBean(stationGroupClassification));
        List<StationGroupClassificationVo> rootStationGroupClassifications = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(stationGroupClassificationVos)) {
            for (StationGroupClassificationVo stationGroupClassificationVo : stationGroupClassificationVos) {
                stationGroupClassificationVo.setLabel(stationGroupClassificationVo.getName());
                if(StrUtil.isNotBlank(stationGroupClassificationVo.getTreePosition())){
                    String[] split = stationGroupClassificationVo.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                    stationGroupClassificationVo.setLevel(split.length);
                }else{
                    stationGroupClassificationVo.setLevel(Constants.DEFAULT_ROOT_LEVEL);
                }
                if (ObjectUtil.equal(stationGroupClassificationVo.getParentId(), Constants.ZERO)) {
                    rootStationGroupClassifications.add(stationGroupClassificationVo);
                }
                for (StationGroupClassificationVo childVo : stationGroupClassificationVos) {
                    if (ObjectUtil.equal(childVo.getParentId(), stationGroupClassificationVo.getId())) {
                        if (ObjectUtil.isNull(stationGroupClassificationVo.getChildren())) {
                            List<StationGroupClassificationVo> children = CollectionUtil.newArrayList();
                            children.add(childVo);
                            stationGroupClassificationVo.setChildren(children);
                        } else {
                            stationGroupClassificationVo.getChildren().add(childVo);
                        }
                    }
                }
            }
        }
        return rootStationGroupClassifications;
    }

    /**
     * 单个将对象转换为vo
     *
     * @param stationGroupClassification
     * @return
     */
    @Override
    public StationGroupClassificationVo setVoProperties(StationGroupClassification stationGroupClassification){
        StationGroupClassificationVo stationGroupClassificationVo = new StationGroupClassificationVo();
        BeanUtil.copyProperties(stationGroupClassification, stationGroupClassificationVo);
        return stationGroupClassificationVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param stationGroupClassifications
     * @return
     */
    @Override
    public List<StationGroupClassificationVo> setVoProperties(Collection stationGroupClassifications){
        List<StationGroupClassificationVo> stationGroupClassificationVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(stationGroupClassifications)) {
            for (Object stationGroupClassification : stationGroupClassifications) {
                StationGroupClassificationVo stationGroupClassificationVo = new StationGroupClassificationVo();
                BeanUtil.copyProperties(stationGroupClassification, stationGroupClassificationVo);
                stationGroupClassificationVos.add(stationGroupClassificationVo);
            }
        }
        return stationGroupClassificationVos;
    }

    /**
     * 根据分类编号统计名称件数
     *
     * @param parentId
     * @return
     */
    @Override
    public long countNameByParentId(String parentId, String name) {
        return baseMapper.countNameByParentId(parentId, name);
    }

    /**
     * 根据分类编号统计英文名称件数
     *
     * @param parentId
     * @return
     */
    @Override
    public long countEnglishNameByParentId(String parentId, String englishName) {
        return baseMapper.countEnglishNameByParentId(parentId, englishName);
    }
}
