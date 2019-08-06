package com.deyatech.template.entity;

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
 * 站点git模板地址信息
 * </p>
 *
 * @author lee.
 * @since 2019-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("template_station_git")
@ApiModel(value = "站点git模板地址信息对象", description = "站点git模板地址信息", parent = BaseEntity.class)
public class StationGit extends BaseEntity {

    @TableField("git_url")
    private String gitUrl;

    @TableField("site_id")
    private String siteId;

}
