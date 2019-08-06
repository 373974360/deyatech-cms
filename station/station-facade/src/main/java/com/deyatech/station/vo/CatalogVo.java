package com.deyatech.station.vo;

import com.deyatech.station.entity.Catalog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * <p>
 * 栏目扩展对象
 * </p>
 *
 * @author csm.
 * @since 2019-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "栏目扩展对象", description = "栏目扩展对象", parent = Catalog.class)
public class CatalogVo extends Catalog {
    @ApiModelProperty(value = "树结构中显示的名称", dataType = "String")
    private String label;

    @ApiModelProperty(value = "树结构中子节点对象集合", dataType = "List<CatalogVo>")
    private List<CatalogVo> children;

    @ApiModelProperty(value = "树结构中的层级", dataType = "String")
    private Integer level;
}
