package com.deyatech.resource.mapper;

import com.deyatech.resource.entity.Setting;
import com.deyatech.common.base.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-08-09
 */
public interface SettingMapper extends BaseMapper<Setting> {

    /**
     * 获取设置根据站群编号
     *
     * @param stationGroupId
     * @return
     */
    Setting getSettingByStationGroupId(String stationGroupId);

    /**
     * 删除设置根据站群编号
     *
     * @param list
     * @return
     */
    long removeByStationGroupId(List<String> list);
}
