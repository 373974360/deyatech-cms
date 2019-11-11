package com.deyatech.zsds.vo;

import com.deyatech.zsds.entity.AgriculturalProductsUpDown;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 农产品上行/下行扩展对象
 * </p>
 *
 * @author csm
 * @since 2019-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "农产品上行/下行扩展对象", description = "农产品上行/下行扩展对象", parent = AgriculturalProductsUpDown.class)
public class AgriculturalProductsUpDownVo extends AgriculturalProductsUpDown {

    /**
     * 网络销售额度字符串
     */
    private String onlineSalesQuotaStr;

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
