package com.deyatech.resource.feign.impl;

import com.deyatech.common.entity.RestResult;
import com.deyatech.resource.entity.Setting;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.feign.ResourceFeign;
import com.deyatech.resource.service.SettingService;
import com.deyatech.resource.service.StationGroupDepartmentAdminService;
import com.deyatech.resource.service.StationGroupService;
import com.deyatech.resource.service.StationGroupUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/8/6 15:19
 */
@RestController
public class ResourceFeignImpl implements ResourceFeign {

    @Autowired
    StationGroupService stationGroupService;

    @Autowired
    SettingService settingService;

    @Autowired
    StationGroupUserService stationGroupUserService;

    @Autowired
    StationGroupDepartmentAdminService stationGroupDepartmentAdminService;

    @Override
    public RestResult<List<StationGroup>> getStationGroupAll() {
        return RestResult.ok(stationGroupService.list());
    }

    @Override
    public RestResult<StationGroup> getStationGroupById(String id) {
        return RestResult.ok(stationGroupService.getById(id));
    }

    @Override
    public RestResult<Setting> getStationSetting(String siteId) {
        return RestResult.ok(settingService.getSetting(siteId));
    }

    @Override
    public RestResult<Boolean> isSiteAdmin(String siteId, String userId) {
        return RestResult.ok(stationGroupUserService.isSiteAdmin(siteId, userId));
    }

    @Override
    public RestResult<Boolean> isSiteDepartmentAdmin(String siteId, String userId) {
        return RestResult.ok(stationGroupDepartmentAdminService.isSiteDepartmentAdmin(siteId, userId));
    }
}
