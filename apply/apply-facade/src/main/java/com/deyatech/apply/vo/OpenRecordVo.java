package com.deyatech.apply.vo;

import com.deyatech.apply.entity.OpenRecord;
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
 * @since 2020-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = OpenRecord.class)
public class OpenRecordVo extends OpenRecord {

    private String deptName;
    private String proDeptName;

    private String modelName;

    private String replyDeptName;

    /**
     * 详情地址
     * */
    private String url;

    private long number;
}
