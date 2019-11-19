package com.deyatech.assembly.vo;

import com.deyatech.assembly.entity.CustomizationTableHead;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 定制表头项
 * </p>
 *
 * @author lee.
 * @since 2019-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = CustomizationTableHead.class)
public class CustomizationTableHeadVo extends CustomizationTableHead {
    /**
     * 数据列表
     */
    private List<CustomizationTableHeadItemVo> headList;
}
