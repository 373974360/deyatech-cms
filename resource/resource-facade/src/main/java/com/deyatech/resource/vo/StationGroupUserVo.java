package com.deyatech.resource.vo;

import com.deyatech.resource.entity.StationGroupUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站点用户关联扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-09-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "站点用户关联扩展对象", description = "站点用户关联扩展对象", parent = StationGroupUser.class)
public class StationGroupUserVo extends StationGroupUser {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 用户工号
     */
    private String empNo;

    /**
     * 用户登录账号
     */
    private String account;

    /**
     * 管理员
     */
    private Integer admin;

    /**
     * 部门id
     */
    private String departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 用户所在部门层级ID
     */
    private String userTreePositionId;

    /**
     * 用户所在部门层级名称
     */
    private String userTreePositionName;

    /**
     * 是否可以选择
     */
    private Boolean selectable = true;
}
