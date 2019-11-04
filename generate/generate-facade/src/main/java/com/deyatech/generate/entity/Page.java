package com.deyatech.generate.entity;

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
 * 页面管理
 * </p>
 *
 * @author csm.
 * @since 2019-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_page")
@ApiModel(value = "页面管理对象", description = "页面管理", parent = BaseEntity.class)
public class Page extends BaseEntity {

    @ApiModelProperty(value = "页面名称", dataType = "String")
    @TableField("page_name")
    private String pageName;

    @ApiModelProperty(value = "英文名称", dataType = "String")
    @TableField("page_english_name")
    private String pageEnglishName;

    @ApiModelProperty(value = "页面路径", dataType = "String")
    @TableField("page_path")
    private String pagePath;

    @ApiModelProperty(value = "模板地址", dataType = "String")
    @TableField("template_path")
    private String templatePath;

    @ApiModelProperty(value = "站点id", dataType = "String")
    @TableField("site_id")
    private String siteId;

    @ApiModelProperty(value = "更新频率", dataType = "Integer")
    @TableField("page_interval")
    private Integer pageInterval;

    @ApiModelProperty(value = "是否自动更新", dataType = "Integer")
    @TableField("auto_update")
    private Integer autoUpdate;
}
