package com.deyatech.appeal.vo;

import com.deyatech.appeal.entity.RecordSatisfaction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = RecordSatisfaction.class)
public class RecordSatisfactionVo extends RecordSatisfaction {

    /**
     * 评价指标名称
     * */
    private String name;

    /**
     * 投票总数
     * */
    private Integer totle;

}
