package com.deyatech.resource.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
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
@TableName("resource_station_group_classification")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class StationGroupClassification extends BaseEntity {

    @ApiModelProperty(value = "分类名称", dataType = "String")
    @TableField("name_")
    private String name;

    @ApiModelProperty(value = "分类英文名称", dataType = "String")
    @TableField("english_name")
    private String englishName;

    @ApiModelProperty(value = "上级分类编号", dataType = "String")
    @TableField("parent_id")
    private String parentId;

    @ApiModelProperty(value = "树结构中的索引位置", dataType = "String")
    @TableField(value = "tree_position", strategy = FieldStrategy.IGNORED)
    private String treePosition;

    @ApiModelProperty(value = "排序号", dataType = "Integer", example = "1")
    @TableField("sort_no")
    private Integer sortNo;

}
