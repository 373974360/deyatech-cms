package com.deyatech.resource.service;

import com.deyatech.resource.entity.Setting;
import com.deyatech.resource.vo.SettingVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-09
 */
public interface SettingService extends BaseService<Setting> {

    /**
     * 单个将对象转换为vo
     *
     * @param setting
     * @return
     */
    SettingVo setVoProperties(Setting setting);

    /**
     * 批量将对象转换为vo
     *
     * @param settings
     * @return
     */
    List<SettingVo> setVoProperties(Collection settings);

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
     * @param stationGroupIds
     * @return
     */
    long removeByStationGroupId(List<String> stationGroupIds);
}
