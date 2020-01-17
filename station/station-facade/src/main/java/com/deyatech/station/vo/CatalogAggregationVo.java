package com.deyatech.station.vo;

import cn.hutool.core.util.StrUtil;
import com.deyatech.station.entity.CatalogAggregation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 聚合栏目扩展对象
 * </p>
 *
 * @author csm.
 * @since 2019-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "聚合栏目扩展对象", description = "聚合栏目扩展对象", parent = CatalogAggregation.class)
public class CatalogAggregationVo extends CatalogAggregation {

    private String publisherName;
    private String publishOrganizationTreePosition;
    /**
     * 规则所属栏目ID
     */
    private String ownerCatalogId;

    /**
     * 检索条件
     */
    List<String> catalogIdList;
    List<String> keyList;
    String startTime;
    String endTime;

    /**
     * 检索条件分析条件
     */
    public void analysisCondition() {
        // 栏目
        if (StrUtil.isNotEmpty(this.getCmsCatalogId())) {
            catalogIdList = new ArrayList<>();
            String[] catalogArray = this.getCmsCatalogId().split("&");
            if (catalogArray != null && catalogArray.length > 0) {
                for (String item : catalogArray) {
                    String[] idArray = item.split(",");
                    if (idArray != null && idArray.length > 0) {
                        // 去掉前面的层级，添加选中的栏目ID
                        catalogIdList.add(idArray[idArray.length - 1]);
                    }
                }
            }
        }
        // 关键字
        if (StrUtil.isNotEmpty(this.getKeyword())) {
            keyList = Arrays.asList(this.getKeyword().split(","));
        }
        // 发布时间段
        if (StrUtil.isNotEmpty(this.getPublishTime())) {
            String[] timeArray = this.getPublishTime().split(",");
            startTime = timeArray[0];
            startTime = startTime.substring(0, startTime.length() - 3);
            endTime = timeArray[1];
            endTime = endTime.substring(0, endTime.length() - 3);
        }
    }
}
