package com.deyatech.station.vo;

import com.deyatech.station.entity.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 页面管理扩展对象
 * </p>
 *
 * @author csm.
 * @since 2019-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "页面管理扩展对象", description = "页面管理扩展对象", parent = Page.class)
public class PageVo extends Page {
}
