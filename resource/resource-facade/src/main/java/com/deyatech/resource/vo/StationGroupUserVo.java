package com.deyatech.resource.vo;

import com.deyatech.resource.entity.StationGroupUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站群用户关联扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-09-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "站群用户关联扩展对象", description = "站群用户关联扩展对象", parent = StationGroupUser.class)
public class StationGroupUserVo extends StationGroupUser {
}
