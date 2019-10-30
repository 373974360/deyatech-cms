package com.deyatech.appeal.entity;

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
 * @since 2019-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("appeal_process")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class Process extends BaseEntity {

    @ApiModelProperty(value = "诉求ID", dataType = "String")
    @TableField("sq_id")
    private String sqId;

    @ApiModelProperty(value = "处理类型 1:受理，2:设为效件，3:申请延期，4:转办，5:回复，6:退回，7:同意延期", dataType = "Integer", example = "1")
    @TableField("pro_type")
    private Integer proType;

    @ApiModelProperty(value = "处理意见", dataType = "String")
    @TableField("pro_content")
    private String proContent;

    @ApiModelProperty(value = "延期理由", dataType = "String")
    @TableField("reasons_delay")
    private String reasonsDelay;

    @ApiModelProperty(value = "延期时间", dataType = "LocalDateTime")
    @TableField("reasons_time")
    private Date reasonsTime;

    @ApiModelProperty(value = "处理部门", dataType = "String")
    @TableField("pro_dept_id")
    private String proDeptId;

    @ApiModelProperty(value = "移交部门", dataType = "String")
    @TableField("to_dept_id")
    private String toDeptId;

    @ApiModelProperty(value = "处理时间", dataType = "LocalDateTime")
    @TableField("pro_time")
    private Date proTime;

    @ApiModelProperty(value = "延期审核状态", dataType = "Integer")
    @TableField("reasons_status")
    private Integer reasonsStatus;

}
