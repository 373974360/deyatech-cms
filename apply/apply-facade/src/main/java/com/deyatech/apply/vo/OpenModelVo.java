package com.deyatech.apply.vo;

import com.deyatech.apply.entity.OpenModel;
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
 * @since 2020-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = OpenModel.class)
public class OpenModelVo extends OpenModel {

    /**
     * 表单地址
     * */
    private String formUrl;
    /**
     * 使用计数
     */
    private long usageCount;
}
