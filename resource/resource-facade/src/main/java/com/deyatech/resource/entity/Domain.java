package com.deyatech.resource.entity;

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
 * @since 2019-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("resource_domain")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class Domain extends BaseEntity {

    @ApiModelProperty(value = "域名名称", dataType = "String")
    @TableField("name_")
    private String name;

    @ApiModelProperty(value = "域名英文名称", dataType = "String")
    @TableField("english_name")
    private String englishName;

    @ApiModelProperty(value = "描述", dataType = "String")
    @TableField("description_")
    private String description;

    @ApiModelProperty(value = "排序号", dataType = "Integer", example = "1")
    @TableField("sort_no")
    private Integer sortNo;

    @ApiModelProperty(value = "站群编号", dataType = "String")
    @TableField("station_group_id")
    private String stationGroupId;

    @ApiModelProperty(value = "端口", dataType = "Integer", example = "1")
    @TableField("port_")
    private Integer port;

}
