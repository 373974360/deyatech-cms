package com.deyatech.station.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.deyatech.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 内容模板
 * </p>
 *
 * @author csm.
 * @since 2019-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_template")
@ApiModel(value = "内容模板对象", description = "内容模板", parent = BaseEntity.class)
public class Template extends BaseEntity {

    @ApiModelProperty(value = "站点id", dataType = "String")
    @TableField("site_id")
    private String siteId;

    @ApiModelProperty(value = "模板路径", dataType = "String")
    @TableField("template_path")
    private String templatePath;

    @ApiModelProperty(value = "栏目ID", dataType = "String")
    @TableField("cms_catalog_id")
    private String cmsCatalogId;

    @ApiModelProperty(value = "内容模型ID", dataType = "String")
    @TableField("content_model_id")
    private String contentModelId;

    @ApiModelProperty(value = "内容唯一ID", dataType = "String")
    @TableField("content_id")
    private String contentId;

    @ApiModelProperty(value = "内容发布状态：1-草稿，2-已发布", dataType = "Integer", example = "1")
    @TableField("status_")
    private Integer status;

    @ApiModelProperty(value = "内容模型模板ID", dataType = "String")
    @TableField("content_model_template_id")
    private String contentModelTemplateId;

    @ApiModelProperty(value = "URL", dataType = "String")
    @TableField("url_")
    private String url;

    @ApiModelProperty(value = "作者姓名", dataType = "String")
    @TableField("author_")
    private String author;

    @ApiModelProperty(value = "编辑姓名", dataType = "String")
    @TableField("editor_")
    private String editor;

    @ApiModelProperty(value = "来源", dataType = "String")
    @TableField("source_")
    private String source;

    @ApiModelProperty(value = "缩略图", dataType = "String")
    @TableField("thumbnail_")
    private String thumbnail;

    @ApiModelProperty(value = "标题", dataType = "String")
    @TableField("title_")
    private String title;

    @ApiModelProperty(value = "是否允许搜索到", dataType = "Boolean")
    @TableField("flag_search")
    private Boolean flagSearch;

    @ApiModelProperty(value = "排序号", dataType = "Integer", example = "1")
    @TableField("sort_no")
    private Integer sortNo;

    @ApiModelProperty(value = "是否置顶", dataType = "Boolean")
    @TableField("flag_top")
    private Boolean flagTop;

    @ApiModelProperty(value = "浏览次数", dataType = "Integer", example = "1")
    @TableField("views_")
    private Integer views;

    @ApiModelProperty(value = "是否是外链", dataType = "Boolean")
    @TableField("flag_external")
    private Boolean flagExternal;

    @ApiModelProperty(value = "资源摘要", dataType = "String")
    @TableField("resource_summary")
    private String resourceSummary;

    @ApiModelProperty(value = "资源内容", dataType = "String")
    @TableField("resource_content")
    private String resourceContent;

    @ApiModelProperty(value = "资源分类", dataType = "String")
    @TableField("resource_category")
    private String resourceCategory;

    @TableField("resource_publication_date")
    @ApiModelProperty(value = "资源发布日期", dataType = "Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date resourcePublicationDate;

    @ApiModelProperty(value = "关键字", dataType = "String")
    @TableField("keyword_")
    private String keyword;

    @ApiModelProperty(value = "索引编码", dataType = "String")
    @TableField("index_code")
    private String indexCode;




}
