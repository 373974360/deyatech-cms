package com.deyatech.content.vo;

import com.deyatech.content.entity.ReviewProcess;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 内容审核流程扩展对象
 * </p>
 *
 * @author csm.
 * @since 2019-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "内容审核流程扩展对象", description = "内容审核流程扩展对象", parent = ReviewProcess.class)
public class ReviewProcessVo extends ReviewProcess {
}
