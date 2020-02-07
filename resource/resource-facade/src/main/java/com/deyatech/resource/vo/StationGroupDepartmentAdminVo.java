package com.deyatech.resource.vo;

import com.deyatech.resource.entity.StationGroupDepartmentAdmin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站部门管理员扩展对象
 * </p>
 *
 * @author ycx
 * @since 2020-02-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "站部门管理员扩展对象", description = "站部门管理员扩展对象", parent = StationGroupDepartmentAdmin.class)
public class StationGroupDepartmentAdminVo extends StationGroupDepartmentAdmin {
}
