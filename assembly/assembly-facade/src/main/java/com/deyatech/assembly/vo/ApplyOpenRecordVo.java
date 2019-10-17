package com.deyatech.assembly.vo;

import com.deyatech.assembly.entity.ApplyOpenRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = ApplyOpenRecord.class)
public class ApplyOpenRecordVo extends ApplyOpenRecord {

    private String doDeptName;

    private String modelName;
}
