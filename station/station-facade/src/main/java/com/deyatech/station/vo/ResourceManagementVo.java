package com.deyatech.station.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-11-13
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象")
public class ResourceManagementVo {
    public ResourceManagementVo() {}

    public ResourceManagementVo(String label, String field, String value) {
        this.label = label;
        this.field = field;
        this.value = value;
    }

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码", dataType = "Long", example = "1")
    private Long page;

    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数", dataType = "Long", example = "1")
    private Long size;

    /**
     * 站点编号
     */
    @ApiModelProperty(value = "站点编号", dataType = "String")
    private String siteId;

    /**
     * 字段标签
     */
    @ApiModelProperty(value = "字段标签", dataType = "String")
    private String label;

    /**
     * 字段名
     */
    @ApiModelProperty(value = "字段名", dataType = "String")
    private String field;

    /**
     * 字段值
     */
    @ApiModelProperty(value = "字段值", dataType = "String")
    private String value;
}
