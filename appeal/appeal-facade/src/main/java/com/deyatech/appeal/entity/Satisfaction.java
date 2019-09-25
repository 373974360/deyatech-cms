package com.deyatech.appeal.entity;

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
 * 
 * </p>
 *
 * @author lee.
 * @since 2019-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("appeal_satisfaction")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class Satisfaction extends BaseEntity {

    @ApiModelProperty(value = "指标名称", dataType = "String")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "分值", dataType = "Integer", example = "1")
    @TableField("score")
    private Integer score;

    @ApiModelProperty(value = "排序", dataType = "Integer", example = "1")
    @TableField("sort")
    private Integer sort;

}
