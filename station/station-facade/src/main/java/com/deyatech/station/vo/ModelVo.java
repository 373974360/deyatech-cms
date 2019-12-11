package com.deyatech.station.vo;

import com.deyatech.station.entity.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 内容模型扩展对象
 * </p>
 *
 * @author csm.
 * @since 2019-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "内容模型扩展对象", description = "内容模型扩展对象", parent = Model.class)
public class ModelVo extends Model {

    /**
     * 关联元数据集名称
     */
    private String metaDataCollectionName;
    /**
     * 栏目数
     */
    private int catalogNum;
}
