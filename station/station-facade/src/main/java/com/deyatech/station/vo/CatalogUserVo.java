package com.deyatech.station.vo;

import com.deyatech.station.entity.CatalogUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 栏目用户关联信息扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-10-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "栏目用户关联信息扩展对象", description = "栏目用户关联信息扩展对象", parent = CatalogUser.class)
public class CatalogUserVo extends CatalogUser {
}
