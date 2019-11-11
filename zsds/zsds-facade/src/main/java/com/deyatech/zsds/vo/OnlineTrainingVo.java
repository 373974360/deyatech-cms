package com.deyatech.zsds.vo;

import com.deyatech.zsds.entity.OnlineTraining;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 在线培训系统扩展对象
 * </p>
 *
 * @author csm
 * @since 2019-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "在线培训系统扩展对象", description = "在线培训系统扩展对象", parent = OnlineTraining.class)
public class OnlineTrainingVo extends OnlineTraining {

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;
}
