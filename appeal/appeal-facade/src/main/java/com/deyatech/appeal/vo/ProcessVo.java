package com.deyatech.appeal.vo;

import com.deyatech.appeal.entity.Process;
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
@ApiModel(value = "扩展对象", description = "扩展对象", parent = Process.class)
public class ProcessVo extends Process {

    /**
     * 受理部门名称
     * */
    private String proDeptName;
    /**
     * 移交部门名称
     * */
    private String toDeptName;
    /**
     * 处理人名称
     * */
    private String createUserName;
}
