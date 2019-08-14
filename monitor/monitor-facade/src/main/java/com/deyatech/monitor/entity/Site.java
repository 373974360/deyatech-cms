package com.deyatech.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.deyatech.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 监控配置表
 * </p>
 *
 * @author lee.
 * @since 2019-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("monitor_site")
@ApiModel(value = "监控配置表对象", description = "监控配置表", parent = BaseEntity.class)
public class Site extends BaseEntity {

    @ApiModelProperty(value = "站点名称", dataType = "String")
    @TableField("site_name")
    private String siteName;

    @ApiModelProperty(value = "站点域名", dataType = "String")
    @TableField("site_domain")
    private String siteDomain;

}
