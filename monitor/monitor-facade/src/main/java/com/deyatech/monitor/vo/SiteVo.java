package com.deyatech.monitor.vo;

import com.deyatech.monitor.entity.Site;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监控配置表扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "监控配置表扩展对象", description = "监控配置表扩展对象", parent = Site.class)
public class SiteVo extends Site {
}
