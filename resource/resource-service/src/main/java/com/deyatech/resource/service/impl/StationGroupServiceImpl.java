package com.deyatech.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.Constants;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.enums.EnableEnum;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.entity.StationGroupClassification;
import com.deyatech.resource.mapper.StationGroupMapper;
import com.deyatech.resource.service.*;
import com.deyatech.resource.vo.StationGroupClassificationVo;
import com.deyatech.resource.vo.StationGroupVo;
import com.deyatech.station.feign.StationFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 站点 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
@Service
public class StationGroupServiceImpl extends BaseServiceImpl<StationGroupMapper, StationGroup> implements StationGroupService {

    @Autowired
    DomainService domainService;
    @Autowired
    SettingService settingService;
    @Autowired
    StationGroupClassificationService classificationService;
    @Autowired
    StationFeign stationFeign;
    @Autowired
    StationGroupRoleService stationGroupRoleService;

    @Override
    public Collection<StationGroupClassificationVo> getRoleStationCascader(String roleId) {
        List<StationGroupVo> filterStationGroupList = baseMapper.selectStationGroupByRoleId(roleId);
        if (CollectionUtil.isEmpty(filterStationGroupList))
            return CollectionUtil.newArrayList();
        return getClassificationStationTree(filterStationGroupList);
    }

    @Override
    public Collection<StationGroupClassificationVo> getUserStationCascader(String userId) {
        List<StationGroupVo> filterStationGroupList = baseMapper.selectStationGroupByUserId(userId);
        if (CollectionUtil.isEmpty(filterStationGroupList))
            return CollectionUtil.newArrayList();
        return getClassificationStationTree(filterStationGroupList);
    }

    /**
     * 根据StationGroupClassification对象属性检索的tree对象
     *
     * @param filterStationGroupList
     * @return
     */
    @Override
    public Collection<StationGroupClassificationVo> getClassificationStationTree(List<StationGroupVo> filterStationGroupList) {
        List<StationGroupClassificationVo> rootClassificationList = CollectionUtil.newArrayList();
        StationGroupClassification classification = new StationGroupClassification();
        classification.setSortSql("sortNo asc");
        List<StationGroupClassificationVo> classificationList = classificationService.setVoProperties(classificationService.listByBean(classification));
        if (CollectionUtil.isNotEmpty(classificationList)) {
            for (StationGroupClassificationVo stationGroupClassificationVo : classificationList) {
                stationGroupClassificationVo.setLabel(stationGroupClassificationVo.getName());
                if(StrUtil.isNotBlank(stationGroupClassificationVo.getTreePosition())){
                    String[] split = stationGroupClassificationVo.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                    stationGroupClassificationVo.setLevel(split.length);
                }else{
                    stationGroupClassificationVo.setLevel(Constants.DEFAULT_ROOT_LEVEL);
                }
                if (ObjectUtil.equal(stationGroupClassificationVo.getParentId(), Constants.ZERO)) {
                    rootClassificationList.add(stationGroupClassificationVo);
                }
                for (StationGroupClassificationVo childVo : classificationList) {
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
            List<StationGroupClassificationVo> resultList = CollectionUtil.newArrayList();
            for (StationGroupClassificationVo node : rootClassificationList) {
                addNode(filterStationGroupList, resultList, node);
            }
            rootClassificationList = resultList;
        }
        return rootClassificationList;
    }

    private void addNode(List<StationGroupVo> filterStationGroupList, List<StationGroupClassificationVo> resultList, StationGroupClassificationVo childrenNode) {
        if (CollectionUtil.isNotEmpty(childrenNode.getChildren())) {
            List<StationGroupClassificationVo> subList = CollectionUtil.newArrayList();
            for (StationGroupClassificationVo subNode : childrenNode.getChildren()) {
                addNode(filterStationGroupList, subList, subNode);
            }
            if (subList.size() > 0) {
                childrenNode.setChildren(subList);
                resultList.add(childrenNode);
            }
        } else {

            List<StationGroupClassificationVo> stationGroupList = getUserStationGroupList(filterStationGroupList, childrenNode);
            if (CollectionUtil.isNotEmpty(stationGroupList)) {
                childrenNode.setChildren(stationGroupList);
                resultList.add(childrenNode);
            }
        }

    }

    private List<StationGroupClassificationVo> getUserStationGroupList(List<StationGroupVo> filterStationGroupList, StationGroupClassificationVo node) {
        if (CollectionUtil.isNotEmpty(filterStationGroupList)) {
            List<StationGroupVo> stationGroupVos = filterStationGroupList.stream().filter(s -> s.getStationGroupClassificationId().equals(node.getId())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(stationGroupVos)) {
                List<StationGroupClassificationVo> children = CollectionUtil.newArrayList();
                for (StationGroupVo station : stationGroupVos) {
                    StationGroupClassificationVo c = new StationGroupClassificationVo();
                    c.setId(station.getId());
                    c.setName(station.getName());
                    c.setLabel(station.getName());
                    c.setLevel(node.getLevel() + 1);
                    if (StrUtil.isNotEmpty(node.getTreePosition())) {
                        c.setTreePosition(node.getTreePosition() + '&' + node.getId());
                    } else {
                        c.setTreePosition('&' + node.getId() );
                    }
                    children.add(c);
                }
                return children;
            }
        }
        return null;
    }

    /**
     * 单个将对象转换为vo站点
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
     * 批量将对象转换为vo站点
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
     * 根据分类编号统计站点个数
     * @param classificationId
     * @return
     */
    @Override
    public long countStationGroupByClassificationId(String classificationId) {
        return baseMapper.countStationGroupByClassificationId(classificationId);
    }

    /**
     * 根据分类编号列表统计站站点数
     * @param list
     * @return
     */
    @Override
    public long countStationGroupByClassificationIdList(List<String> list) {
        return baseMapper.countStationGroupByClassificationIdList(list);
    }

    /**
     * 根据条件查询站点
     *
     * @param stationGroupVo
     * @return
     */
    @Override
    public IPage<StationGroupVo> pageSelectByStationGroupVo(StationGroupVo stationGroupVo) {
        return baseMapper.pageSelectByStationGroupVo(getPageByBean(stationGroupVo), stationGroupVo);
    }

    /**
     * 根据条件查询所有站点
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
     * 根据编号检索站点
     *
     * @param id
     * @return
     */
    @Override
    public StationGroup getById(Serializable id) {
        return baseMapper.getStationGroupById(id);
    }

    /**
     * 启用或停用站点
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
            // 启用停用 站点下所有域名 nginx 配置
            domainService.runOrStopStationGroupNginxConfig(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除站点
     *
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeStationGroupAndConfig(List<String> ids, Map<String, StationGroup> maps) {
        // 删除站点
        long count = baseMapper.updateEnableByIds(ids, EnableEnum.DELETED.getCode());
        if (count > 0) {
            // 删除站点角色关联
            stationGroupRoleService.removeByStationGroupId(ids);
            // 删除站点配置
            settingService.removeByStationGroupId(ids);
            // 删除 站点下所有域名 nginx 配置
            domainService.removeStationGroupNginxConfig(ids, maps);
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
        String hostRootDir = this.getHostsRootDir();
        boolean flag;
        // 编辑
        if (StrUtil.isNotEmpty(stationGroup.getId())) {
            // 旧站点
            String oldStationGroupEnglishName = null;
            StationGroup oldStationGroup = this.getById(stationGroup.getId());
            // 若站点英文名称变更，则 nginx 需要做对应变更
            if (!stationGroup.getEnglishName().equals(oldStationGroup.getEnglishName())) {
                oldStationGroupEnglishName = oldStationGroup.getEnglishName();
            }
            flag = baseMapper.updateStationGroupById(stationGroup) > 0 ? true : false;
            if (flag) {
                // 文件夹重命名
                File src = new File(hostRootDir, oldStationGroupEnglishName);
                File dest = new File(hostRootDir, stationGroup.getEnglishName());
                src.renameTo(dest);
            }
            // 新增
        } else {
            flag = super.save(stationGroup);
            if (flag) {
                File dir = new File(hostRootDir, stationGroup.getEnglishName());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }

        }
        return flag;
    }

    private String getHostsRootDir() {
        String hostRoot = stationFeign.getSiteProperties().getData().getHostsRoot();
        hostRoot = hostRoot.replace("\\", "/");
        if (!hostRoot.endsWith("/")) {
            hostRoot += "/";
        }
        return hostRoot;
    }
}
