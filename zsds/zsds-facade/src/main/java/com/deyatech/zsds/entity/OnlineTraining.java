package com.deyatech.zsds.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.deyatech.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 在线培训系统
 * </p>
 *
 * @author csm
 * @since 2019-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("zsds_online_training")
@ApiModel(value = "在线培训系统对象", description = "在线培训系统", parent = BaseEntity.class)
public class OnlineTraining extends BaseEntity {

    @ApiModelProperty(value = "部门编号", dataType = "String")
    @TableField("department_id")
    private String departmentId;

    @ApiModelProperty(value = "培训期数", dataType = "Integer", example = "1")
    @TableField("period_")
    private Integer period;

    @ApiModelProperty(value = "本期培训人数", dataType = "Integer", example = "1")
    @TableField("number_")
    private Integer number;

}
