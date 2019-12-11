package com.deyatech.station.vo;

import com.deyatech.station.entity.Page;
import com.deyatech.station.entity.PageCatalog;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/9/10 18:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "页面栏目关联扩展对象", description = "页面栏目关联扩展对象", parent = Page.class)
public class PageCatalogVo extends PageCatalog {
}
