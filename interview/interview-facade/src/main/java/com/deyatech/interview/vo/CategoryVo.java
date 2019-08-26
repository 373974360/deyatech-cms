package com.deyatech.interview.vo;

import com.deyatech.interview.entity.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 访谈分类扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "访谈分类扩展对象", description = "访谈分类扩展对象", parent = Category.class)
public class CategoryVo extends Category {
    /**
     * 站点名称
     */
    @ApiModelProperty(value = "站点名称", dataType = "String")
    private String siteName;
}
