package com.deyatech.zsds.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.deyatech.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 农产品上行/下行
 * </p>
 *
 * @author csm
 * @since 2019-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("zsds_agricultural_products_up_down")
@ApiModel(value = "农产品上行/下行对象", description = "农产品上行/下行", parent = BaseEntity.class)
public class AgriculturalProductsUpDown extends BaseEntity {

    @ApiModelProperty(value = "部门编号", dataType = "String")
    @TableField("department_id")
    private String departmentId;

    @ApiModelProperty(value = "网络销售额度", dataType = "BigDecimal")
    @TableField("online_sales_quota")
    private Double onlineSalesQuota;

}
