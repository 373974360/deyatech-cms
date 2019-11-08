package com.deyatech.station.vo;

import com.deyatech.admin.entity.Metadata;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 内容动态表单
 */
@Data
public class TemplateDynamicFormVo {
    /**
     * 页码
     */
    private int pageNumber;
    /**
     * 页名
     */
    private String pageName;
    /**
     * 页表单对象
     */
    private Map<String, Object> pageModel;
    /**
     * 下拉框、单选框、复选框的数据
     */
    private Map<String, Object> pageList;
    /**
     * 页表单行
     */
    List<List<Metadata>> rows;
}