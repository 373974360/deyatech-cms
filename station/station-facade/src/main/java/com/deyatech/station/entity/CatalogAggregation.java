package com.deyatech.station.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.deyatech.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 聚合栏目
 * </p>
 *
 * @author csm.
 * @since 2019-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_catalog_aggregation")
@ApiModel(value = "聚合栏目对象", description = "聚合栏目", parent = BaseEntity.class)
public class CatalogAggregation extends BaseEntity {

    @ApiModelProperty(value = "栏目id", dataType = "String")
    @TableField("cms_catalog_id")
    private String cmsCatalogId;

    @ApiModelProperty(value = "关键字", dataType = "String")
    @TableField("keyword_")
    private String keyword;

    @ApiModelProperty(value = "发布机构", dataType = "String")
    @TableField("publish_organization")
    private String publishOrganization;

    @ApiModelProperty(value = "发布时间段", dataType = "String")
    @TableField("publish_time")
    private String publishTime;

    @ApiModelProperty(value = "发布人", dataType = "String")
    @TableField("publisher")
    private String publisher;

}
