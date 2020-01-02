package com.deyatech.appeal.vo;

import com.deyatech.appeal.entity.Purpose;
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
 * @since 2019-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = Purpose.class)
public class PurposeVo extends Purpose {
    /**
     * 使用计数
     */
    private long usageCount;
}
