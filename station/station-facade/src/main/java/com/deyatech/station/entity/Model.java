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
 * 内容模型
 * </p>
 *
 * @author csm.
 * @since 2019-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_model")
@ApiModel(value = "内容模型对象", description = "内容模型", parent = BaseEntity.class)
public class Model extends BaseEntity {

    @ApiModelProperty(value = "描述", dataType = "String")
    @TableField("description_")
    private String description;

    @ApiModelProperty(value = "英文名称", dataType = "String")
    @TableField("english_name")
    private String englishName;

    @ApiModelProperty(value = "关联元数据集id", dataType = "String")
    @TableField("meta_data_collection_id")
    private String metaDataCollectionId;

    @ApiModelProperty(value = "内容模型名称", dataType = "String")
    @TableField("name_")
    private String name;

}
