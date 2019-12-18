package com.deyatech.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.enums.MaterialUsePlaceEnum;
import com.deyatech.common.enums.WaterMarkTypeEnum;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.resource.entity.Setting;
import com.deyatech.resource.mapper.SettingMapper;
import com.deyatech.resource.service.SettingService;
import com.deyatech.resource.vo.SettingVo;
import com.deyatech.station.feign.StationFeign;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    StationFeign stationFeign;
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
        boolean flag;
        StringBuilder oldUrls = new StringBuilder();
        StringBuilder newUrls = new StringBuilder();
        if (StrUtil.isNotEmpty(setting.getId())) {
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
                updateWrapper.set("watermark_point_size", null);
                updateWrapper.set("watermark_fill_color", null);
            } else {
                // 图片
                if (WaterMarkTypeEnum.PICTURE.getCode() == setting.getWatermarkType()) {
                    updateWrapper.set("watermark_word", null);
                    updateWrapper.set("watermark_point_size", null);
                    updateWrapper.set("watermark_fill_color", null);
                    // 文字
                } else if (WaterMarkTypeEnum.WORD.getCode() == setting.getWatermarkType()) {
                    updateWrapper.set("watermark_url", null);
                    updateWrapper.set("watermark_transparency", null);
                    updateWrapper.set("watermark_width", null);
                    updateWrapper.set("watermark_height", null);
                }
            }
            updateWrapper.eq("id_", setting.getId());
            if (StrUtil.isNotEmpty(setting.getStationGroupId())) {
                Setting settingDB = getById(setting.getId());
                checkUrl(settingDB.getIcoUrl(), setting.getIcoUrl(), oldUrls, newUrls);
                checkUrl(settingDB.getWatermarkUrl(), setting.getWatermarkUrl(), oldUrls, newUrls);
            }
            flag = super.update(setting, updateWrapper);
        } else {
            if (StrUtil.isNotEmpty(setting.getStationGroupId())) {
                checkUrl(null, setting.getIcoUrl(), oldUrls, newUrls);
                checkUrl(null, setting.getWatermarkUrl(), oldUrls, newUrls);
            }

            flag = super.save(setting);
        }
        if (StrUtil.isNotEmpty(setting.getStationGroupId())) {
            String oldUrl = oldUrls.toString();
            if(StrUtil.isNotEmpty(oldUrl)) {
                oldUrl = oldUrl.substring(1);
            }
            String newUrl = newUrls.toString();
            if (StrUtil.isNotEmpty(newUrl)) {
                newUrl = newUrl.substring(1);
            }
            stationFeign.markMaterialUsePlace(oldUrl, newUrl, MaterialUsePlaceEnum.RESOURCE_SETTING.getCode());
            // 有图片水印
            if (YesNoEnum.YES.getCode() == setting.getWatermarkEnable() && WaterMarkTypeEnum.PICTURE.getCode() == setting.getWatermarkType()) {
                stationFeign.watermarkHandle(setting.getStationGroupId(), setting.getWatermarkUrl());
            }
        }
        return flag;
    }

    private void checkUrl(String oldUrl, String newUrl, StringBuilder oldUrls, StringBuilder newUrls) {
        // 原来有
        if (StrUtil.isNotEmpty(oldUrl)) {
            // 现在有
            if (StrUtil.isNotEmpty(newUrl)) {
                // 不相等处理
                if (!oldUrl.equals(newUrl)) {
                    oldUrls.append(",");
                    oldUrls.append(oldUrl);
                    newUrls.append(",");
                    newUrls.append(newUrl);
                }
            } else {
                oldUrls.append(",");
                oldUrls.append(oldUrl);
            }

            // 原来没
        } else {
            // 现在有
            if (StrUtil.isNotEmpty(newUrl)) {
                newUrls.append(",");
                newUrls.append(newUrl);
            }
        }
    }

    /**
     * 获取站点设置
     *
     * @param siteId
     * @return
     */
    public Setting getSetting(String siteId) {
        Setting setting;
        QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(siteId)) {
            queryWrapper.eq("station_group_id", siteId);
            setting = super.getOne(queryWrapper);
            // 指定站点ID没有查到，再查全局配置
            if (Objects.isNull(setting)) {
                queryWrapper = new QueryWrapper<>();
                queryWrapper.isNull("station_group_id");
                setting = super.getOne(queryWrapper);
            }
        } else {
            queryWrapper.isNull("station_group_id");
            setting = super.getOne(queryWrapper);
        }
        return setting;
    }

}
