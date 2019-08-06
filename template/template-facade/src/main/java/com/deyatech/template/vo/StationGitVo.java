package com.deyatech.template.vo;

import com.deyatech.template.entity.StationGit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站点git模板地址信息扩展对象
 * </p>
 *
 * @author lee.
 * @since 2019-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "站点git模板地址信息扩展对象", description = "站点git模板地址信息扩展对象", parent = StationGit.class)
public class StationGitVo extends StationGit {

    private String siteName;
    private String tempSiteId;
}
