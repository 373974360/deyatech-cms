package com.deyatech.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.deyatech.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author lee.
 * @since 2019-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("monitor_group")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class Group extends BaseEntity {

    @ApiModelProperty(value = "任务组名称", dataType = "String")
    @TableField("group_name")
    private String groupName;

    @ApiModelProperty(value = "触发模式；1：固定时刻触发，2：日历周期触发", dataType = "Integer", example = "1")
    @TableField("trigger_type")
    private Integer triggerType;

    @ApiModelProperty(value = "固定时刻触发间隔时间；单位：秒", dataType = "Integer", example = "1")
    @TableField("increment_seconds")
    private Integer incrementSeconds;

    @ApiModelProperty(value = "日历周期触发时间，单位：HH:mm:ss", dataType = "String")
    @TableField("calendar_time")
    private String calendarTime;

    @ApiModelProperty(value = "日历周期触发类型；1：每日，2：每周", dataType = "Integer", example = "1")
    @TableField("calendar_type")
    private Integer calendarType;

    @ApiModelProperty(value = "日历周期触发每周几", dataType = "Integer", example = "1")
    @TableField("calendar_workday")
    private String calendarWorkday;

    @ApiModelProperty(value = "最近一次检测时间", dataType = "String")
    @TableField("last_dtime")
    private String lastDtime;

    @ApiModelProperty(value = "下一次检查时间", dataType = "String")
    @TableField("next_dtime")
    private String nextDtime;

    @ApiModelProperty(value = "任务状态；1：运行中，2：空闲", dataType = "Integer", example = "1")
    @TableField("run_type")
    private Integer runType;

}
