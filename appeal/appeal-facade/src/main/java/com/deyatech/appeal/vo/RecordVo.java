package com.deyatech.appeal.vo;

import com.deyatech.appeal.entity.Record;
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
 * @since 2019-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = Record.class)
public class RecordVo extends Record {

    /**
     * 收件部门名称
     * */
    private String deptName;
    /**
     * 处理部门名称
     * */
    private String proDeptName;
    /**
     * 回复部门名称
     * */
    private String replyDeptName;
    /**
     * 业务模型
     * */
    private String modelName;
    /**
     * 诉求目的
     * */
    private String purposeName;

    /**
     * 收件部门
     */
    private String treePosition;

    /**
     * 回复部门
     */
    private String replyTreePosition;

    /**
     * 处理状态名称
     */
    private String flagName;

    /**
     * 详情地址
     * */
    private String url;
}
