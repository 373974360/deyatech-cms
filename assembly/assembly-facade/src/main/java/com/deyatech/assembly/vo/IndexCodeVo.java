package com.deyatech.assembly.vo;

import com.deyatech.assembly.entity.IndexCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 索引编码规则扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "索引编码规则扩展对象", description = "索引编码规则扩展对象", parent = IndexCode.class)
public class IndexCodeVo extends IndexCode {
}
