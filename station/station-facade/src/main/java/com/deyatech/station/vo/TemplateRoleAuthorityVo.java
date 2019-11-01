package com.deyatech.station.vo;

import com.deyatech.station.entity.TemplateRoleAuthority;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色内容权限扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "角色内容权限扩展对象", description = "角色内容权限扩展对象", parent = TemplateRoleAuthority.class)
public class TemplateRoleAuthorityVo extends TemplateRoleAuthority {
}
