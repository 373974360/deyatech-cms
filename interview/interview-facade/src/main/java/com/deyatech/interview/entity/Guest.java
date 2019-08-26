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
 * 访谈嘉宾
 * </p>
 *
 * @author lee.
 * @since 2019-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("interview_guest")
@ApiModel(value = "访谈嘉宾对象", description = "访谈嘉宾", parent = BaseEntity.class)
public class Guest extends BaseEntity {

    @ApiModelProperty(value = "模型ID", dataType = "String")
    @TableField("model_id")
    private String modelId;

    @ApiModelProperty(value = "姓名", dataType = "String")
    @TableField("name_")
    private String name;

    @ApiModelProperty(value = "照片", dataType = "String")
    @TableField("photo_")
    private String photo;

    @ApiModelProperty(value = "职务", dataType = "String")
    @TableField("job_")
    private String job;

    @ApiModelProperty(value = "嘉宾类型(1为主持人，2为嘉宾)", dataType = "String")
    @TableField("type_")
    private String type;

}
