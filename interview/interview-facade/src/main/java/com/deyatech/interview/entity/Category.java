package com.deyatech.interview.entity;

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
 * 访谈分类
 * </p>
 *
 * @author lee.
 * @since 2019-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("interview_category")
@ApiModel(value = "访谈分类对象", description = "访谈分类", parent = BaseEntity.class)
public class Category extends BaseEntity {

    @ApiModelProperty(value = "分类名称", dataType = "String")
    @TableField("name_")
    private String name;

    @ApiModelProperty(value = "站点ID", dataType = "String")
    @TableField("site_id")
    private String siteId;

    @ApiModelProperty(value = "列表页模板", dataType = "String")
    @TableField("list_page_template")
    private String listPageTemplate;

    @ApiModelProperty(value = "详情页模板", dataType = "String")
    @TableField("detail_page_template")
    private String detailPageTemplate;
}
