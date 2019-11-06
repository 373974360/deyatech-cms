package com.deyatech.station.vo;

import com.deyatech.station.entity.TemplateFormOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 内容表单顺扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-11-04
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "内容表单顺扩展对象", description = "内容表单顺扩展对象", parent = TemplateFormOrder.class)
public class TemplateFormOrderVo extends TemplateFormOrder {
    /**
     * 元数据名称
     */
    private String metadataName;
}
