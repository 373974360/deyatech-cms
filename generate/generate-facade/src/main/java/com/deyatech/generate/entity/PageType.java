package com.deyatech.generate.entity;

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
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_page_type")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class PageType extends BaseEntity {

    @ApiModelProperty(value = "上级节点id", dataType = "String")
    @TableField("parent_id")
    private String parentId;

    @ApiModelProperty(value = "树结构中的位置", dataType = "String")
    @TableField("tree_position")
    private String treePosition;

    @ApiModelProperty(value = "排序号", dataType = "Integer", example = "1")
    @TableField("sort_no")
    private Integer sortNo;

    @ApiModelProperty(value = "站点id", dataType = "String")
    @TableField("site_id")
    private String siteId;

    @ApiModelProperty(value = "分类名称", dataType = "String")
    @TableField("name_")
    private String name;

}
