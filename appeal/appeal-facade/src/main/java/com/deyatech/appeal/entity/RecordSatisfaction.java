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
 * @since 2019-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("appeal_record_satisfaction")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class RecordSatisfaction extends BaseEntity {

    @ApiModelProperty(value = "信件ID", dataType = "String")
    @TableField("appeal_id")
    private String appealId;

    @ApiModelProperty(value = "指标ID", dataType = "String")
    @TableField("satis_id")
    private String satisId;

}
