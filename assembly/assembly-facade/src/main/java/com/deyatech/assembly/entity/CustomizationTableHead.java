package com.deyatech.assembly.entity;

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
 * 定制表头
 * </p>
 *
 * @author lee.
 * @since 2019-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("customization_table_head")
@ApiModel(value = "定制表头", description = "定制表头", parent = BaseEntity.class)
public class CustomizationTableHead extends BaseEntity {

    @ApiModelProperty(value = "用户编号", dataType = "String")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "名称", dataType = "String")
    @TableField("name_")
    private String name;

    @ApiModelProperty(value = "类型", dataType = "String")
    @TableField("type_")
    private String type;

    @ApiModelProperty(value = "数据", dataType = "String")
    @TableField("data_")
    private String data;

}
