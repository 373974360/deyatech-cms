package com.deyatech.station.entity;

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
 * 内容模型模版
 * </p>
 *
 * @author csm.
 * @since 2019-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_model_template")
@ApiModel(value = "内容模型模版对象", description = "内容模型模版", parent = BaseEntity.class)
public class ModelTemplate extends BaseEntity {

    @ApiModelProperty(value = "栏目id", dataType = "String")
    @TableField("cms_catalog_id")
    private String cmsCatalogId;

    @ApiModelProperty(value = "站点id", dataType = "String")
    @TableField("site_id")
    private String siteId;

    @ApiModelProperty(value = "模版路径", dataType = "String")
    @TableField("template_path")
    private String templatePath;

    @ApiModelProperty(value = "内容模型id", dataType = "String")
    @TableField("content_model_id")
    private String contentModelId;

    @ApiModelProperty(value = "默认标记", dataType = "Boolean")
    @TableField("default_flag")
    private Boolean defaultFlag;

}
