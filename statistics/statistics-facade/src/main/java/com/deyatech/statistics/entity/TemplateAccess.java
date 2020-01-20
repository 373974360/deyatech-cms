package com.deyatech.statistics.entity;

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
 * 
 * </p>
 *
 * @author lee.
 * @since 2020-01-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_template_access")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class TemplateAccess extends BaseEntity {

    @ApiModelProperty(value = "内容ID", dataType = "String")
    @TableField("info_id")
    private String infoId;

    @ApiModelProperty(value = "栏目ID", dataType = "String")
    @TableField("cat_id")
    private String catId;

    @ApiModelProperty(value = "内容标题", dataType = "String")
    @TableField("info_title")
    private String infoTitle;

    @ApiModelProperty(value = "访问者IP", dataType = "String")
    @TableField("access_ip")
    private String accessIp;

    @ApiModelProperty(value = "URL", dataType = "String")
    @TableField("access_url")
    private String accessUrl;

    @ApiModelProperty(value = "时间", dataType = "String")
    @TableField("access_time")
    private String accessTime;

    @ApiModelProperty(value = "日期", dataType = "String")
    @TableField("access_day")
    private String accessDay;

    @ApiModelProperty(value = "月份", dataType = "String")
    @TableField("access_month")
    private String accessMonth;

    @ApiModelProperty(value = "年份", dataType = "String")
    @TableField("access_year")
    private String accessYear;

    @ApiModelProperty(value = "访问域名", dataType = "String")
    @TableField("site_domain")
    private String siteDomain;

    @ApiModelProperty(value = "站点ID", dataType = "String")
    @TableField("site_id")
    private String siteId;

}
