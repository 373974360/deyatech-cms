package com.deyatech.assembly.entity;

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
 * 索引编码规则
 * </p>
 *
 * @author lee.
 * @since 2019-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_index_code")
@ApiModel(value = "索引编码规则对象", description = "索引编码规则", parent = BaseEntity.class)
public class IndexCode extends BaseEntity {

    @ApiModelProperty(value = "站点编码", dataType = "String")
    @TableField("site_id")
    private String siteId;

    @ApiModelProperty(value = "部门编码是否选择", dataType = "String")
    @TableField("code_checked")
    private Integer codeChecked;

    @ApiModelProperty(value = "部门编码分隔符", dataType = "String")
    @TableField("code_delimiter")
    private String codeDelimiter;

    @ApiModelProperty(value = "固定值是否选择", dataType = "String")
    @TableField("value_checked")
    private Integer valueChecked;

    @ApiModelProperty(value = "固定值", dataType = "String")
    @TableField("value_")
    private String value;

    @ApiModelProperty(value = "固定值分隔符", dataType = "String")
    @TableField("value_delimiter")
    private String valueDelimiter;

    @ApiModelProperty(value = "日期格式是否选择", dataType = "String")
    @TableField("format_checked")
    private Integer formatChecked;

    @ApiModelProperty(value = "日期格式", dataType = "String")
    @TableField("format_")
    private String format;

    @ApiModelProperty(value = "日期格式分隔符", dataType = "String")
    @TableField("format_delimiter")
    private String formatDelimiter;

    @ApiModelProperty(value = "流水号位数", dataType = "Integer", example = "1")
    @TableField("number_")
    private Integer number;

    @ApiModelProperty(value = "流水号", dataType = "String")
    @TableField("next_serial")
    private String nextSerial;

}
