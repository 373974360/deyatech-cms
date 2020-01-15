package com.deyatech.station.vo;

import com.deyatech.station.entity.CatalogTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 栏目内容关联扩展对象
 * </p>
 *
 * @author ycx
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "栏目内容关联扩展对象", description = "栏目内容关联扩展对象", parent = CatalogTemplate.class)
public class CatalogTemplateVo extends CatalogTemplate {
}
