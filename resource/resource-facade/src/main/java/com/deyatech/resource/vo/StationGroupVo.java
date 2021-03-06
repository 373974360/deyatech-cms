package com.deyatech.resource.vo;

import com.deyatech.resource.entity.StationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站点扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "站点扩展对象", description = "站点扩展对象", parent = StationGroup.class)
public class StationGroupVo extends StationGroup {

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", dataType = "String")
    private String stationGroupClassificationName;

    /**
     * 分类树结构索引位置
     */
    @ApiModelProperty(value = "分类树结构索引位置", dataType = "String")
    private String stationGroupClassificationTreePosition;

    /**
     * 管理机构名称
     */
    @ApiModelProperty(value = "管理机构名称", dataType = "String")
    private String departmentName;

    /**
     * 管理机构树结构索引位置
     */
    @ApiModelProperty(value = "管理机构树结构索引位置", dataType = "String")
    private String departmentTreePosition;

    /**
     * 包含范围名称
     */
    @ApiModelProperty(value = "包含范围名称", dataType = "String")
    private String rangeDepartmentName;

    /**
     * 包含范围树结构索引位置
     */
    @ApiModelProperty(value = "包含范围树结构索引位置", dataType = "String")
    private String rangeDepartmentTreePosition;

    /**
     * 站点关联的用户
     */
    private Integer userCount;

}
