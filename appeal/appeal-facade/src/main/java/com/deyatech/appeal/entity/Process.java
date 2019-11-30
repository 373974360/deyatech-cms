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

    @ApiModelProperty(value = "处理类型 1:转办，2回复，3发布，4退回，5判重，6无效，7延期，8不予受理", dataType = "Integer", example = "1")
    @TableField("pro_type")
    private Integer proType;

    @ApiModelProperty(value = "处理意见", dataType = "String")
    @TableField("pro_content")
    private String proContent;

    @ApiModelProperty(value = "处理部门", dataType = "String")
    @TableField("pro_dept_id")
    private String proDeptId;

    @ApiModelProperty(value = "移交部门", dataType = "String")
    @TableField("to_dept_id")
    private String toDeptId;

    @ApiModelProperty(value = "处理时间", dataType = "LocalDateTime")
    @TableField("pro_time")
    private Date proTime;

}
