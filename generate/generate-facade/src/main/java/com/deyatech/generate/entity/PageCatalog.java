package com.deyatech.generate.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.deyatech.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/9/10 18:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_page_catalog")
@ApiModel(value = "页面栏目关联对象", description = "页面栏目关联对象", parent = BaseEntity.class)
public class PageCatalog extends BaseEntity {

    @ApiModelProperty(value = "栏目ID", dataType = "String")
    @TableField("cat_id")
    private String catId;

    @ApiModelProperty(value = "页面ID", dataType = "String")
    @TableField("page_id")
    private String pageId;
}
