package com.deyatech.station.entity;

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
 * 上传文件信息
 * </p>
 *
 * @author lee.
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_material")
@ApiModel(value = "上传文件信息对象", description = "上传文件信息", parent = BaseEntity.class)
public class Material extends BaseEntity {

    @ApiModelProperty(value = "文件名称", dataType = "String")
    @TableField("name_")
    private String name;

    @ApiModelProperty(value = "文件类型", dataType = "String")
    @TableField("type_")
    private String type;

    @ApiModelProperty(value = "文件相对路径", dataType = "String")
    @TableField("url_")
    private String url;

    @ApiModelProperty(value = "文件绝对路径", dataType = "String")
    @TableField("path_")
    private String path;

}
