package com.deyatech.resource.vo;

import com.deyatech.resource.entity.StationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站群扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "站群扩展对象", description = "站群扩展对象", parent = StationGroup.class)
public class StationGroupVo extends StationGroup {
}