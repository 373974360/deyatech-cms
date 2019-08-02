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
 * 站群
 * </p>
 *
 * @author lee.
 * @since 2019-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("resource_station_group")
@ApiModel(value = "站群对象", description = "站群", parent = BaseEntity.class)
public class StationGroup extends BaseEntity {

    @ApiModelProperty(value = "网站名称", dataType = "String")
    @TableField("name_")
    private String name;

    @ApiModelProperty(value = "网站英文名称", dataType = "String")
    @TableField("english_name")
    private String englishName;

    @ApiModelProperty(value = "网站简称", dataType = "String")
    @TableField("abbreviation_")
    private String abbreviation;

    @ApiModelProperty(value = "描述", dataType = "String")
    @TableField("description_")
    private String description;

    @ApiModelProperty(value = "排序号", dataType = "Integer", example = "1")
    @TableField("sort_no")
    private Integer sortNo;

    @ApiModelProperty(value = "分类编号", dataType = "String")
    @TableField("station_group_classification_id")
    private String stationGroupClassificationId;

}
