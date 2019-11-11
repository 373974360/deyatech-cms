package com.deyatech.zsds.vo;

import com.deyatech.zsds.entity.LogisticsDistribution;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物流配送体系扩展对象
 * </p>
 *
 * @author csm
 * @since 2019-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "物流配送体系扩展对象", description = "物流配送体系扩展对象", parent = LogisticsDistribution.class)
public class LogisticsDistributionVo extends LogisticsDistribution {

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

}
