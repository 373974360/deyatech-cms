package com.deyatech.station.vo;

import com.deyatech.station.entity.Template;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 内容模板扩展对象
 * </p>
 *
 * @author csm.
 * @since 2019-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "内容模板扩展对象", description = "内容模板扩展对象", parent = Template.class)
public class TemplateVo extends Template {

    /**
     * id字符串数组
     */
    private String ids;
    /**
     * MQ 消息代码
     */
    private String code;
    /**
     * 索引
     */
    private String index;
    /**
     * 内容模型名称
     */
    private String contentModelName;
}
