package com.deyatech.zsds.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.deyatech.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 物流配送体系
 * </p>
 *
 * @author csm
 * @since 2019-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("zsds_logistics_distribution")
@ApiModel(value = "物流配送体系对象", description = "物流配送体系", parent = BaseEntity.class)
public class LogisticsDistribution extends BaseEntity {

    @ApiModelProperty(value = "部门编号", dataType = "String")
    @TableField("department_id")
    private String departmentId;

    @ApiModelProperty(value = "收货数量", dataType = "Long", example = "1")
    @TableField("received_count")
    private Long receivedCount;

    @ApiModelProperty(value = "发货数量", dataType = "Long", example = "1")
    @TableField("delivered_count")
    private Long deliveredCount;

}
