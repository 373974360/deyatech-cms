package com.deyatech.resource.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseController;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.UploadFileTypeEnum;
import com.deyatech.resource.entity.Setting;
import com.deyatech.resource.service.SettingService;
import com.deyatech.resource.vo.SettingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 * @author: lee.
 * @since 2019-08-09
 */
@Slf4j
@RestController
@RequestMapping("/resource/setting")
@Api(tags = {"接口"})
public class SettingController extends BaseController {
    @Autowired
    SettingService settingService;

    /**
     * 单个保存或者更新
     *
     * @param setting
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "setting", value = "对象", required = true, dataType = "Setting", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Setting setting) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(setting)));
        boolean result = settingService.saveOrUpdateExtend(setting);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param settingList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "settingList", value = "对象集合", required = true, allowMultiple = true, dataType = "Setting", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Setting> settingList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(settingList)));
        boolean result = settingService.saveOrUpdateBatch(settingList);
        return RestResult.ok(result);
    }

    /**
     * 根据Setting对象属性逻辑删除
     *
     * @param setting
     * @return
     */
    @PostMapping("/removeBySetting")
    @ApiOperation(value="根据Setting对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "setting", value = "对象", required = true, dataType = "Setting", paramType = "query")
    public RestResult<Boolean> removeBySetting(Setting setting) {
        log.info(String.format("根据Setting对象属性逻辑删除: %s ", setting));
        boolean result = settingService.removeByBean(setting);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除", notes="根据对象ID批量逻辑删除信息")
    @ApiImplicitParam(name = "ids", value = "对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = settingService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Setting对象属性获取
     *
     * @param setting
     * @return
     */
    @GetMapping("/getBySetting")
    @ApiOperation(value="根据Setting对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "setting", value = "对象", required = false, dataType = "Setting", paramType = "query")
    public RestResult<SettingVo> getBySetting(Setting setting) {
        setting = settingService.getByBean(setting);
        SettingVo settingVo = settingService.setVoProperties(setting);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(settingVo)));
        return RestResult.ok(settingVo);
    }

    /**
     * 根据编号获取设置
     *
     * @param stationGroupId
     * @return
     */
    @GetMapping("/getSettingByStationGroupId")
    @ApiOperation(value="根据编号获取设置", notes="根据编号获取设置")
    @ApiImplicitParam(name = "id", value = "对象", required = false, dataType = "id", paramType = "query")
    public RestResult<SettingVo> getSettingByStationGroupId(@RequestParam(required = false) String stationGroupId) {
        log.info(String.format("根据编号获取设置：stationGroupId = %s", stationGroupId));
        Setting setting = settingService.getSettingByStationGroupId(stationGroupId);
        SettingVo settingVo = settingService.setVoProperties(setting);
        return RestResult.ok(settingVo);
    }


    /**
     * 根据Setting对象属性检索所有
     *
     * @param setting
     * @return
     */
    @GetMapping("/listBySetting")
    @ApiOperation(value="根据Setting对象属性检索所有", notes="根据Setting对象属性检索所有信息")
    @ApiImplicitParam(name = "setting", value = "对象", required = false, dataType = "Setting", paramType = "query")
    public RestResult<Collection<SettingVo>> listBySetting(Setting setting) {
        Collection<Setting> settings = settingService.listByBean(setting);
        Collection<SettingVo> settingVos = settingService.setVoProperties(settings);
        log.info(String.format("根据Setting对象属性检索所有: %s ",JSONUtil.toJsonStr(settingVos)));
        return RestResult.ok(settingVos);
    }

    /**
     * 根据Setting对象属性分页检索
     *
     * @param setting
     * @return
     */
    @GetMapping("/pageBySetting")
    @ApiOperation(value="根据Setting对象属性分页检索", notes="根据Setting对象属性分页检索信息")
    @ApiImplicitParam(name = "setting", value = "对象", required = false, dataType = "Setting", paramType = "query")
    public RestResult<IPage<SettingVo>> pageBySetting(Setting setting) {
        IPage<SettingVo> settings = settingService.pageByBean(setting);
        settings.setRecords(settingService.setVoProperties(settings.getRecords()));
        log.info(String.format("根据Setting对象属性分页检索: %s ",JSONUtil.toJsonStr(settings)));
        return RestResult.ok(settings);
    }

    /**
     * 获取站点上传附件类型和大小
     *
     * @param siteId
     * @return
     */
    @RequestMapping("/getUploadFileTypeAndSize")
    @ApiOperation(value="获取站点上传附件类型和大小", notes="获取站点上传附件类型和大小")
    @ApiImplicitParam(name = "siteId", value = "站点", required = false, dataType = "String", paramType = "query")
    public RestResult getUploadFileTypeAndSize(@RequestParam(value = "siteId", required = false) String siteId) {
        Setting setting = settingService.getSetting(siteId);
        List<String> types;
        int size;
        if (Objects.isNull(setting)) {
            types = new ArrayList<>();
            Arrays.stream(UploadFileTypeEnum.values()).forEach(t -> types.add(t.getCode()));
            size = 2;
        } else {
            types = Arrays.asList(setting.getUploadFileType().split(","));
            size = setting.getUploadFileSize();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("types", types);
        data.put("size", size);
        return RestResult.ok(data);
    }



    /**
     * 获取站点设置
     *
     * @param siteId
     * @return
     */
    @RequestMapping("/getStationSetting")
    @ApiOperation(value="获取站点设置", notes="获取站点设置")
    @ApiImplicitParam(name = "siteId", value = "站点", required = false, dataType = "String", paramType = "query")
    public RestResult<Setting> getStationSetting(@RequestParam(value = "siteId", required = false) String siteId) {
        return RestResult.ok(settingService.getSetting(siteId));
    }
}
