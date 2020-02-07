package com.deyatech.resource.entity;

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
 * 站部门管理员
 * </p>
 *
 * @author ycx
 * @since 2020-02-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("resource_station_group_department_admin")
@ApiModel(value = "站部门管理员对象", description = "站部门管理员", parent = BaseEntity.class)
public class StationGroupDepartmentAdmin extends BaseEntity {

    @ApiModelProperty(value = "站点编号", dataType = "String")
    @TableField("site_id")
    private String siteId;

    @ApiModelProperty(value = "部门编号", dataType = "String")
    @TableField("department_id")
    private String departmentId;

    @ApiModelProperty(value = "用户编号", dataType = "String")
    @TableField("user_Id")
    private String userId;

    /**
     * 记录状态，0为禁用，1为启用，-1为已删除
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "记录状态", dataType = "Integer", notes = "0为禁用，1为启用，-1为已删除", example = "1")
    private Integer enable;

    /**
     * 备注
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "备注", dataType = "String")
    private String remark;

    /**
     * 数据记录创建者
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "数据记录创建者", dataType = "String", hidden = true)
    private String createBy;

    /**
     * 数据记录创建时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "数据记录创建时间", dataType = "Date", hidden = true)
    private Date createTime;

    /**
     * 数据记录更新者
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "数据记录更新者", dataType = "String", hidden = true)
    private String updateBy;

    /**
     * 数据记录更新时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "数据记录更新时间", dataType = "Date", hidden = true)
    private Date updateTime;

    /**
     * 乐观锁字段
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "乐观锁字段", dataType = "Integer", hidden = true)
    private Integer version;
}
