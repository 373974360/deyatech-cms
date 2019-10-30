package com.deyatech.assembly.vo;

import com.deyatech.assembly.entity.CustomizationFunction;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = CustomizationFunction.class)
public class CustomizationFunctionVo extends CustomizationFunction {
    /**
     * 数据列表
     */
    private List<CustomizationTableHeadVo> headList;
}
