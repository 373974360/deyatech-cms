package com.deyatech.resource.vo;

import com.deyatech.resource.entity.Domain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = Domain.class)
public class DomainVo extends Domain {

    /**
     * 网站名称
     */
    @ApiModelProperty(value = "网站名称", dataType = "String")
    private String stationGroupName;

}
