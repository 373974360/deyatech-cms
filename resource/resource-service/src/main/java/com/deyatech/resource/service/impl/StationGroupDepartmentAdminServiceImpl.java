package com.deyatech.resource.service.impl;

import com.deyatech.resource.entity.StationGroupDepartmentAdmin;
import com.deyatech.resource.vo.StationGroupDepartmentAdminVo;
import com.deyatech.resource.mapper.StationGroupDepartmentAdminMapper;
import com.deyatech.resource.service.StationGroupDepartmentAdminService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 站部门管理员 服务实现类
 * </p>
 *
 * @Author ycx
 * @since 2020-02-06
 */
@Service
public class StationGroupDepartmentAdminServiceImpl extends BaseServiceImpl<StationGroupDepartmentAdminMapper, StationGroupDepartmentAdmin> implements StationGroupDepartmentAdminService {

    /**
     * 单个将对象转换为vo站部门管理员
     *
     * @param stationGroupDepartmentAdmin
     * @return
     */
    @Override
    public StationGroupDepartmentAdminVo setVoProperties(StationGroupDepartmentAdmin stationGroupDepartmentAdmin){
        StationGroupDepartmentAdminVo stationGroupDepartmentAdminVo = new StationGroupDepartmentAdminVo();
        BeanUtil.copyProperties(stationGroupDepartmentAdmin, stationGroupDepartmentAdminVo);
        return stationGroupDepartmentAdminVo;
    }

    /**
     * 批量将对象转换为vo站部门管理员
     *
     * @param stationGroupDepartmentAdmins
     * @return
     */
    @Override
    public List<StationGroupDepartmentAdminVo> setVoProperties(Collection stationGroupDepartmentAdmins){
        List<StationGroupDepartmentAdminVo> stationGroupDepartmentAdminVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(stationGroupDepartmentAdmins)) {
            for (Object stationGroupDepartmentAdmin : stationGroupDepartmentAdmins) {
                StationGroupDepartmentAdminVo stationGroupDepartmentAdminVo = new StationGroupDepartmentAdminVo();
                BeanUtil.copyProperties(stationGroupDepartmentAdmin, stationGroupDepartmentAdminVo);
                stationGroupDepartmentAdminVos.add(stationGroupDepartmentAdminVo);
            }
        }
        return stationGroupDepartmentAdminVos;
    }

    /**
     * 是否站点部门管理员
     *
     * @param siteId
     * @param userId
     * @return
     */
    @Override
    public boolean isSiteDepartmentAdmin(String siteId, String userId) {
        int count = baseMapper.countSiteDepartmentAdmin(siteId, userId);
        return count > 0 ? true : false;
    }
}
