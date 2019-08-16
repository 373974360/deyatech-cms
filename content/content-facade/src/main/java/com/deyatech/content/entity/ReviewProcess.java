package com.deyatech.content.entity;

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
 * 内容审核流程
 * </p>
 *
 * @author csm.
 * @since 2019-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("content_review_process")
@ApiModel(value = "内容审核流程对象", description = "内容审核流程", parent = BaseEntity.class)
public class ReviewProcess extends BaseEntity {

    @ApiModelProperty(value = "内容id", dataType = "String")
    @TableField("content_id")
    private String contentId;

    @ApiModelProperty(value = "工作流id", dataType = "String")
    @TableField("workflow_id")
    private String workflowId;

    @ApiModelProperty(value = "审核生命周期状态：0.启动 1.审核 2.完成", dataType = "Integer", example = "1")
    @TableField("status_")
    private Integer status;

}
