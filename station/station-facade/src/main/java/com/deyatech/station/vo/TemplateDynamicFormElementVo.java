package com.deyatech.station.vo;

import com.deyatech.admin.entity.Metadata;
import lombok.Data;

/**
 * 内容动态表单元素
 */
@Data
public class TemplateDynamicFormElementVo {
    /**
     * 结构
     */
    private Metadata struct;
    /**
     * 数据
     */
    private Object data;
}
