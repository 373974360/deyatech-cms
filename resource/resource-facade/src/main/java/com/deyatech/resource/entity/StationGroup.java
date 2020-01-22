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
 * 站点
 * </p>
 *
 * @author lee.
 * @since 2019-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("resource_station_group")
@ApiModel(value = "站点对象", description = "站点", parent = BaseEntity.class)
public class StationGroup extends BaseEntity {

    @ApiModelProperty(value = "站点名称", dataType = "String")
    @TableField("name_")
    private String name;

    @ApiModelProperty(value = "站点英文名称", dataType = "String")
    @TableField("english_name")
    private String englishName;

    @ApiModelProperty(value = "站点简称", dataType = "String")
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

    @ApiModelProperty(value = "站点部门用户(方便站点关联用户)", dataType = "String")
    @TableField("department_id")
    private String departmentId;

    @ApiModelProperty(value = "站点部门范围(站点管理员管理范围)", dataType = "String")
    @TableField("range_department_id")
    private String rangeDepartmentId;

    @ApiModelProperty(value = "网站标识", dataType = "String")
    @TableField("site_code")
    private String siteCode;

    @ApiModelProperty(value = "ICP备案", dataType = "String")
    @TableField("icp_code")
    private String icpCode;

    @ApiModelProperty(value = "公安备案", dataType = "String")
    @TableField("police_code")
    private String policeCode;
}
