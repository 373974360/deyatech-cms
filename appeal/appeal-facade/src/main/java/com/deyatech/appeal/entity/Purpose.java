package com.deyatech.appeal.entity;

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
 * @since 2019-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("appeal_purpose")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class Purpose extends BaseEntity {

    @ApiModelProperty(value = "诉求目的名称", dataType = "String")
    @TableField("purpose_name")
    private String purposeName;

}
