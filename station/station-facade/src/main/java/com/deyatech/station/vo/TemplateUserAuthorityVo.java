package com.deyatech.station.vo;

import com.deyatech.station.entity.TemplateUserAuthority;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 内容模板用户权限信息扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-10-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "内容模板用户权限信息扩展对象", description = "内容模板用户权限信息扩展对象", parent = TemplateUserAuthority.class)
public class TemplateUserAuthorityVo extends TemplateUserAuthority {
}
