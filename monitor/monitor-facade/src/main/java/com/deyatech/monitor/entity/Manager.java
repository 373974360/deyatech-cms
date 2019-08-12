package com.deyatech.monitor.entity;

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
 * @since 2019-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("monitor_manager")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class Manager extends BaseEntity {

    @ApiModelProperty(value = "管理员姓名", dataType = "String")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "管理员手机号", dataType = "String")
    @TableField("user_phone")
    private String userPhone;

}
