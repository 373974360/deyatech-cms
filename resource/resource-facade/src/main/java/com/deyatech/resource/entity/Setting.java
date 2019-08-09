package com.deyatech.resource.entity;

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
 * @since 2019-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("resource_setting")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class Setting extends BaseEntity {

    @ApiModelProperty(value = "站群ID(为空则全站群共享)", dataType = "String")
    @TableField("station_group_id")
    private String stationGroupId;

    @ApiModelProperty(value = "允许上传的附件类型", dataType = "String")
    @TableField("upload_file_type")
    private String uploadFileType;

    @ApiModelProperty(value = "允许上传文件大小M", dataType = "Integer", example = "1")
    @TableField("upload_file_size")
    private Integer uploadFileSize;

    @ApiModelProperty(value = "是否生成缩略图", dataType = "Integer", example = "1")
    @TableField("thumbnail_enable")
    private Integer thumbnailEnable;

    @ApiModelProperty(value = "缩略图宽度", dataType = "Integer", example = "1")
    @TableField("thumbnail_width")
    private Integer thumbnailWidth;

    @ApiModelProperty(value = "缩略图高度", dataType = "Integer", example = "1")
    @TableField("thumbnail_height")
    private Integer thumbnailHeight;

    @ApiModelProperty(value = "是否生成水印", dataType = "Integer", example = "1")
    @TableField("watermark_enable")
    private Integer watermarkEnable;

    @ApiModelProperty(value = "水印类型：1图片、2文字", dataType = "Integer", example = "1")
    @TableField("watermark_type")
    private Integer watermarkType;

    @ApiModelProperty(value = "水印图片高度", dataType = "Integer", example = "1")
    @TableField("watermark_width")
    private Integer watermarkWidth;

    @ApiModelProperty(value = "水印图片宽度", dataType = "Integer", example = "1")
    @TableField("watermark_height")
    private Integer watermarkHeight;

    @ApiModelProperty(value = "水印透明度%(0不透明~100透明)", dataType = "Integer", example = "1")
    @TableField("watermark_transparency")
    private Integer watermarkTransparency;

    @ApiModelProperty(value = "水印图片url", dataType = "String")
    @TableField("watermark_url")
    private String watermarkUrl;

    @ApiModelProperty(value = "水印文字", dataType = "String")
    @TableField("watermark_word")
    private String watermarkWord;

    @ApiModelProperty(value = "水印位置：九宫格", dataType = "Integer", example = "1")
    @TableField("watermark_position")
    private Integer watermarkPosition;

    @ApiModelProperty(value = "ico图片url", dataType = "String")
    @TableField("ico_url")
    private String icoUrl;

}
