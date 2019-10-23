package com.deyatech.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.enums.WaterMarkTypeEnum;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.resource.entity.Setting;
import com.deyatech.resource.mapper.SettingMapper;
import com.deyatech.resource.service.SettingService;
import com.deyatech.resource.vo.SettingVo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
     * 获取设置根据站点编号
     *
     * @param stationGroupId
     * @return
     */
    @Override
    public Setting getSettingByStationGroupId(String stationGroupId) {
        return baseMapper.getSettingByStationGroupId(stationGroupId);
    }

    /**
     * 删除设置根据站点编号
     *
     * @param stationGroupIds
     * @return
     */
    @Override
    public long removeByStationGroupId(List<String> stationGroupIds) {
        return baseMapper.removeByStationGroupId(stationGroupIds);
    }

    /**
     * 保存或更新
     * @param setting
     * @return
     */
    @Override
    public boolean saveOrUpdateExtend(Setting setting) {
        if (Objects.nonNull(setting) && StrUtil.isNotEmpty(setting.getId())) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            // 没有缩略图
            if (YesNoEnum.NO.getCode().equals(setting.getThumbnailEnable())) {
                updateWrapper.set("thumbnail_width", null);
                updateWrapper.set("thumbnail_height", null);
            }
            // 没有水印
            if (YesNoEnum.NO.getCode().equals(setting.getWatermarkEnable())) {
                updateWrapper.set("watermark_type", null);
                updateWrapper.set("watermark_width", null);
                updateWrapper.set("watermark_height", null);
                updateWrapper.set("watermark_transparency", null);
                updateWrapper.set("watermark_url", null);
                updateWrapper.set("watermark_word", null);
                updateWrapper.set("watermark_position", null);
            } else {
                // 图片
                if (WaterMarkTypeEnum.PICTURE.getCode() == setting.getWatermarkType()) {
                    updateWrapper.set("watermark_word", null);
                    // 文字
                } else if (WaterMarkTypeEnum.WORD.getCode() == setting.getWatermarkType()) {
                    updateWrapper.set("watermark_url", null);
                }
            }
            updateWrapper.eq("id_", setting.getId());
            return super.update(setting, updateWrapper);
        } else {
            return super.save(setting);
        }
    }
}
