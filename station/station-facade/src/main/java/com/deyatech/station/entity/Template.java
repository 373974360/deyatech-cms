package com.deyatech.station.entity;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.deyatech.admin.entity.Metadata;
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

    /**
     * 内容管理动态表单基础字段
     * @return
     */
    public static Map<String, Metadata> baseFields() {
        // 控件长度: 半行1 整行2
        // 0    1    2       3       4       5      6      7       8
        // 名称,字段,数据类型,数据长度,空间类型,控件长度,必填,校验方式,数据来源
        Map<String, Metadata> base = MapUtil.newHashMap();
        base.put("101", getMetadata("101","标题","title_","string","100","inputElement","whole",true,null,null, null));
        base.put("102", getMetadata("102","来源","source_","string","100","inputDepartmentCascader","half",true,null,null,null));
        base.put("103", getMetadata("103","作者姓名","author_","string","30","inputElement","half",false,null,null,null));
//        base.put("104", getMetadata("104","权重","sort_no","int","8","inputElement","half",true,"positiveInteger",null,null));
        base.put("105", getMetadata("105","资源分类","resource_category","string","200","selectElement","half",false,null, "dataItem","resource_category"));
        base.put("106", getMetadata("106","摘要","resource_summary","text","500","textareaElement","whole",false,null,null,null));
        base.put("107", getMetadata("107","关键字","keyword_","string","200","inputElement","whole",false,null,null,null));
        base.put("108", getMetadata("108","缩略图","thumbnail_","string","200","imageElement","whole",false,null,null,null));
        base.put("109", getMetadata("109","外链","flag_external","int","10","switchElement","half",true,null,null,null));
//        base.put("110", getMetadata("110","置顶","flag_top", "int","10","switchElement","half",false,null,null,null));
        base.put("111", getMetadata("111","正文","resource_content","string","10000","richTextElement","whole",false,null,null,null));
        base.put("112", getMetadata("112","编辑姓名","editor_","string","30","inputElement","half",false,null,null,null));
        base.put("113", getMetadata("113","发布时间","resource_publication_date","date","30","datetimeElement","half",true,"publicationDate",null,null));
        return base;
    }

    private static Metadata getMetadata(String id, String name, String briefName, String dataType, String dataLength, String controlType, String controlLength, boolean required, String checkModel, String dataSource, String dictionaryId) {
        Metadata md = new Metadata();
        md.setId(id);// ID
        md.setName(name);// 名称
        md.setBriefName(briefName);// 字段
        md.setDataType(dataType);// 数据类型
        md.setDataLength(dataLength);// 数据长度
        md.setControlType(controlType);// 空间类型
        if ("half".equals(controlLength)) {
            md.setControlLength(1);// 控件长度
        } else {
            md.setControlLength(2);// 控件长度
        }
        md.setRequired(required);// 必填
        md.setCheckModel(checkModel);// 校验方式
        md.setDataSource(dataSource);// 数据来源
        md.setDictionaryId(dictionaryId); // 数据字典索引编号
        return md;
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

    @ApiModelProperty(value = "内容发布状态", dataType = "Integer", example = "1")
    @TableField("status_")
    private Integer status;

    @ApiModelProperty(value = "还原状态", dataType = "Integer", example = "1")
    @TableField("original_status")
    private Integer originalStatus;

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

    @ApiModelProperty(value = "是否允许搜索到", dataType = "Integer")
    @TableField("flag_search")
    private Integer flagSearch;

    @ApiModelProperty(value = "排序号", dataType = "Integer", example = "1")
    @TableField("sort_no")
    private Integer sortNo;

    @ApiModelProperty(value = "是否置顶", dataType = "Integer")
    @TableField("flag_top")
    private Integer flagTop;

    @ApiModelProperty(value = "浏览次数", dataType = "Integer", example = "1")
    @TableField("views_")
    private Integer views;

    @ApiModelProperty(value = "是否是外链", dataType = "Integer")
    @TableField("flag_external")
    private Integer flagExternal;

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

    @ApiModelProperty(value = "审核理由", dataType = "String")
    @TableField("reason_")
    private String reason;

    @ApiModelProperty(value = "定时发布标记", dataType = "Integer")
    @TableField("timing_")
    private Integer timing;

}
