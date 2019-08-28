package com.deyatech.interview.vo;

import com.deyatech.interview.entity.Guest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 访谈嘉宾扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "访谈嘉宾扩展对象", description = "访谈嘉宾扩展对象", parent = Guest.class)
public class GuestVo extends Guest {

    /**
     * 模型名称
     */
    @ApiModelProperty(value = "模型名称", dataType = "String")
    private String modelName;
}
