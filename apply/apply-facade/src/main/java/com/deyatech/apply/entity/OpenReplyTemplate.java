package com.deyatech.apply.entity;

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
 * @since 2020-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("apply_open_reply_template")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class OpenReplyTemplate extends BaseEntity {

    @ApiModelProperty(value = "模板标题", dataType = "String")
    @TableField("title_")
    private String title;

    @ApiModelProperty(value = "模板内容", dataType = "String")
    @TableField("content_")
    private String content;

}
