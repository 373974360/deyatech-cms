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
 * 内容表单顺
 * </p>
 *
 * @author lee.
 * @since 2019-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_template_form_order")
@ApiModel(value = "内容表单顺对象", description = "内容表单顺", parent = BaseEntity.class)
public class TemplateFormOrder extends BaseEntity {

    @ApiModelProperty(value = "元数据集编号", dataType = "String")
    @TableField("collection_id")
    private String collectionId;

    @ApiModelProperty(value = "元数据编号", dataType = "String")
    @TableField("metadata_id")
    private String metadataId;

    @ApiModelProperty(value = "页码", dataType = "Integer", example = "1")
    @TableField("page_number")
    private Integer pageNumber;

    @ApiModelProperty(value = "页名", dataType = "String")
    @TableField("page_name")
    private String pageName;

    @ApiModelProperty(value = "排序号", dataType = "Integer")
    @TableField("sort_no")
    private Integer sortNo;

    /**
     * 记录状态，0为禁用，1为启用，-1为已删除
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "记录状态", dataType = "Integer", notes = "0为禁用，1为启用，-1为已删除", example = "1")
    private Integer enable;

    /**
     * 备注
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "备注", dataType = "String")
    private String remark;

    /**
     * 数据记录创建者
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "数据记录创建者", dataType = "String", hidden = true)
    private String createBy;

    /**
     * 数据记录创建时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "数据记录创建时间", dataType = "Date", hidden = true)
    private Date createTime;

    /**
     * 数据记录更新者
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "数据记录更新者", dataType = "String", hidden = true)
    private String updateBy;

    /**
     * 数据记录更新时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "数据记录更新时间", dataType = "Date", hidden = true)
    private Date updateTime;

    /**
     * 乐观锁字段
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "乐观锁字段", dataType = "Integer", hidden = true)
    private Integer version;
}
