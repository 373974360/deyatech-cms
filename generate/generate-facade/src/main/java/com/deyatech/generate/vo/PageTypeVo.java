package com.deyatech.generate.vo;

import com.deyatech.generate.entity.PageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = PageType.class)
public class PageTypeVo extends PageType {
    @ApiModelProperty(value = "树结构中显示的名称", dataType = "String")
    private String label;

    @ApiModelProperty(value = "树结构中子节点对象集合", dataType = "List<StationPageTypeVo>")
    private List<PageTypeVo> children;

    @ApiModelProperty(value = "树结构中的层级", dataType = "String")
    private Integer level;

    @ApiModelProperty(value = "页面数量", dataType = "Integer")
    private Integer count;
}
