package com.deyatech.statistics.vo;

import com.deyatech.statistics.entity.TemplateAccess;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 扩展对象
 * </p>
 *
 * @author lee.
 * @since 2020-01-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "扩展对象", description = "扩展对象", parent = TemplateAccess.class)
public class TemplateAccessVo extends TemplateAccess {

    public String catalogName;
    public Integer count;

    public String startTime;
    public String endTime;
    public String catId;
    public String siteId;
    public List<String> catalogIdList;
    public Integer accessType;

}
