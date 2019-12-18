package com.deyatech.resource.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.resource.entity.Setting;
import com.deyatech.resource.vo.SettingVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
     * 获取设置根据站点编号
     *
     * @param stationGroupId
     * @return
     */
    Setting getSettingByStationGroupId(String stationGroupId);

    /**
     * 删除设置根据站点编号
     *
     * @param stationGroupIds
     * @return
     */
    long removeByStationGroupId(List<String> stationGroupIds);

    /**
     * 保存或更新
     * @param setting
     * @return
     */
    boolean saveOrUpdateExtend(Setting setting);

    /**
     * 获取站点设置
     *
     * @param siteId
     * @return
     */
    Setting getSetting(String siteId);
}
