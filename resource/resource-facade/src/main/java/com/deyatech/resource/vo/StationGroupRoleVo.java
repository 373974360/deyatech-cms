package com.deyatech.resource.vo;

import com.deyatech.resource.entity.StationGroupRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站点角色关联扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-10-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "站点角色关联扩展对象", description = "站点角色关联扩展对象", parent = StationGroupRole.class)
public class StationGroupRoleVo extends StationGroupRole {
}
