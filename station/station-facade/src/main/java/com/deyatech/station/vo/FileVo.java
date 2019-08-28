package com.deyatech.station.vo;

import com.deyatech.station.entity.File;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 上传文件信息扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "上传文件信息扩展对象", description = "上传文件信息扩展对象", parent = File.class)
public class FileVo extends File {
}
