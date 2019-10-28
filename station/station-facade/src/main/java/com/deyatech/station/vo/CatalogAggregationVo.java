package com.deyatech.station.vo;

import com.deyatech.station.entity.CatalogAggregation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 聚合栏目扩展对象
 * </p>
 *
 * @author csm.
 * @since 2019-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "聚合栏目扩展对象", description = "聚合栏目扩展对象", parent = CatalogAggregation.class)
public class CatalogAggregationVo extends CatalogAggregation {

    private String publisherName;
    private String publishOrganizationTreePosition;
}
