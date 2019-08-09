package com.deyatech.resource.service.impl;

import com.deyatech.resource.entity.Setting;
import com.deyatech.resource.vo.SettingVo;
import com.deyatech.resource.mapper.SettingMapper;
import com.deyatech.resource.service.SettingService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-09
 */
@Service
public class SettingServiceImpl extends BaseServiceImpl<SettingMapper, Setting> implements SettingService {

    /**
     * 单个将对象转换为vo
     *
     * @param setting
     * @return
     */
    @Override
    public SettingVo setVoProperties(Setting setting){
        SettingVo settingVo = new SettingVo();
        BeanUtil.copyProperties(setting, settingVo);
        return settingVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param settings
     * @return
     */
    @Override
    public List<SettingVo> setVoProperties(Collection settings){
        List<SettingVo> settingVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(settings)) {
            for (Object setting : settings) {
                SettingVo settingVo = new SettingVo();
                BeanUtil.copyProperties(setting, settingVo);
                settingVos.add(settingVo);
            }
        }
        return settingVos;
    }

    /**
     * 获取设置根据站群编号
     *
     * @param stationGroupId
     * @return
     */
    @Override
    public Setting getSettingByStationGroupId(String stationGroupId) {
        return baseMapper.getSettingByStationGroupId(stationGroupId);
    }

    /**
     * 删除设置根据站群编号
     *
     * @param stationGroupIds
     * @return
     */
    @Override
    public long removeByStationGroupId(List<String> stationGroupIds) {
        return baseMapper.removeByStationGroupId(stationGroupIds);
    }
}
