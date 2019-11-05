package com.deyatech.station.entity;

import cn.hutool.core.map.MapUtil;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    // name 中文名称
    // briefName 字段名
    // dataType 数据类型
    // controlType 控件类型
    // controlLength 控件长度
    // checkModel 校验方式
    // dataLength 数据长度
    // dataSource 数据来源
    // dictionaryId 数据字典
    // mandatory 选填控制
    public static Map<String, String> baseFields() {
        // 控件长度: 半行1 整行2
        // 数据库字段,字段中文名称,数据类型_数据长度_空间类型_控件长度_必填_校验方式_数据来源
        Map<String, String> base = MapUtil.newHashMap();
        base.put("101", "title,标题,string_200_inputElement_2_1_0_0");
        base.put("102", "source,来源,string_200_inputElement_1_1_0_0");
        base.put("103", "author,作者姓名,string_30_inputElement_1_1_0_0");
        base.put("104", "sort_no,权重,int_8_inputElement_1_1_positiveInteger_0");
        base.put("105", "resource_category,资源分类,string_200_selectElement_1_0_0_resourceCategory");
        base.put("106", "resource_summary,摘要,text_500_textareaElement_2_0_0_0");
        base.put("107", "keyword,关键字,string_200_tagElement_2_0_0_0");
        base.put("108", "thumbnail,缩略图,string_200_imageElement_2_0_0_0");
        base.put("109", "flag_external,外链,int_1_switchElement_1_0_0_0");
        base.put("110", "flag_top,置顶,int_1_switchElement_1_0_0_0");
        base.put("111", "resource_content,正文,string_10000_richTextElement_2_0_0_0");
        return base;
    }


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
