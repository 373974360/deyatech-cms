package com.deyatech.resource.vo;

import com.deyatech.resource.entity.Setting;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = Setting.class)
public class SettingVo extends Setting {

    @ApiModelProperty(value = "站群名", dataType = "String")
    private String stationGroupName;
}
