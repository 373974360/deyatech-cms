package com.deyatech.interview.vo;

import com.deyatech.interview.entity.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 访谈模型扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "访谈模型扩展对象", description = "访谈模型扩展对象", parent = Model.class)
public class ModelVo extends Model {

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", dataType = "String")
    private String categoryName;

    /**
     * 嘉宾信息
     * */
    private List<GuestVo> guestVoList;

    /**
     * 详情地址
     * */
    private String url;
}
