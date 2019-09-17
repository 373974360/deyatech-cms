package com.deyatech.interview.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.deyatech.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 访谈模型
 * </p>
 *
 * @author lee.
 * @since 2019-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("interview_model")
@ApiModel(value = "访谈模型对象", description = "访谈模型", parent = BaseEntity.class)
public class Model extends BaseEntity {

    @ApiModelProperty(value = "分类ID", dataType = "String")
    @TableField("category_id")
    private String categoryId;

    @ApiModelProperty(value = "访谈名称", dataType = "String")
    @TableField("name_")
    private String name;

    @ApiModelProperty(value = "访谈时间", dataType = "String")
    @TableField("time_")
    private String time;

    @ApiModelProperty(value = "访谈图片", dataType = "String")
    @TableField("cover_")
    private String cover;

    @ApiModelProperty(value = "访谈简介", dataType = "String")
    @TableField("description_")
    private String description;

    @ApiModelProperty(value = "直播地址", dataType = "String")
    @TableField("live_url")
    private String liveUrl;

    @ApiModelProperty(value = "视频地址", dataType = "String")
    @TableField("video_url")
    private String videoUrl;

    @ApiModelProperty(value = "访谈图片", dataType = "String")
    @TableField("images_")
    private String images;

    @ApiModelProperty(value = "访谈内容", dataType = "String")
    @TableField("content_")
    private String content;

    @ApiModelProperty(value = "访谈状态(0为预告,1为直播,2为往期)", dataType = "Integer", example = "1")
    @TableField("status_")
    private Integer status;

}
