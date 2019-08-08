package com.deyatech.station.vo;

import com.deyatech.station.entity.ModelTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 内容模型模版扩展对象
 * </p>
 *
 * @author csm.
 * @since 2019-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "内容模型模版扩展对象", description = "内容模型模版扩展对象", parent = ModelTemplate.class)
public class ModelTemplateVo extends ModelTemplate {

    /**
     * 站点名称
     */
    private String siteName;

}
