package com.deyatech.assembly.vo;

import com.deyatech.common.enums.CustomizationTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 定制表头
 * </p>
 *
 * @author lee.
 * @since 2019-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "栏目表头数据", description = "栏目表头数据", parent = CustomizationTableHeadItemVo.class)
public class CustomizationTableHeadItemVo {
    /**
     * 标签
     */
    private String label;
    /**
     * 数据字段
     */
    private String prop;
    /**
     * 显示
     */
    private boolean show;
    /**
     * 禁止编辑
     */
    private boolean disabled;

    public CustomizationTableHeadItemVo(){}

    public CustomizationTableHeadItemVo(String label, String prop) {
        this(label, prop, true, false);
    }

    public CustomizationTableHeadItemVo(String label, String prop, boolean show, boolean disabled) {
        this.label = label;
        this.prop = prop;
        this.show = show;
        this.disabled = disabled;
    }

    public static List<CustomizationTableHeadItemVo> getDefault(String type) {
        if (CustomizationTypeEnum.TABLE_HEAD_CATALOG.getCode().equals(type)) {
            return getCatalogDefault();
        } else if (CustomizationTypeEnum.TABLE_HEAD_CONTENT.getCode().equals(type)) {
            return getContentDefault();
        }
        return null;
    }

    /**
     * 栏目默认数据
     *
     * @return
     */
    public static List<CustomizationTableHeadItemVo> getCatalogDefault() {
        List<CustomizationTableHeadItemVo> list = new ArrayList<>();
        list.add(new CustomizationTableHeadItemVo("栏目名称","name", true, true));
        list.add(new CustomizationTableHeadItemVo("栏目别名","aliasName"));
        list.add(new CustomizationTableHeadItemVo("英文名称","ename"));
        list.add(new CustomizationTableHeadItemVo("隐藏","allowHidden"));
        list.add(new CustomizationTableHeadItemVo("归档","placeOnFile"));
        list.add(new CustomizationTableHeadItemVo("排序","sortNo"));
        return list;
    }

    /**
     * 内容默认数据
     *
     * @return
     */
    public static List<CustomizationTableHeadItemVo> getContentDefault() {
        List<CustomizationTableHeadItemVo> list = new ArrayList<>();
        list.add(new CustomizationTableHeadItemVo("标题","title", true, true));
        list.add(new CustomizationTableHeadItemVo("来源","source"));
        list.add(new CustomizationTableHeadItemVo("发布时间","resourcePublicationDate"));
        list.add(new CustomizationTableHeadItemVo("发布状态","status"));
        list.add(new CustomizationTableHeadItemVo("权重","sortNo"));
        list.add(new CustomizationTableHeadItemVo("置顶","flagTop"));
        list.add(new CustomizationTableHeadItemVo("外链","flagExternal"));
        list.add(new CustomizationTableHeadItemVo("缩略图","thumbnail"));
        list.add(new CustomizationTableHeadItemVo("作者姓名","author", false, false));
        list.add(new CustomizationTableHeadItemVo("资源分类","resourceCategory", false, false));
        list.add(new CustomizationTableHeadItemVo("模板路径","templatePath", false, false));
        list.add(new CustomizationTableHeadItemVo("索引编码","indexCode", false, false));
        list.add(new CustomizationTableHeadItemVo("内容模型","contentModelName", false, false));
        list.add(new CustomizationTableHeadItemVo("创建者姓名","createUserName", false, false));
        list.add(new CustomizationTableHeadItemVo("创建者部门","createUserDepartmentName", false, false));
        list.add(new CustomizationTableHeadItemVo("创建时间","createTime", false, false));
        list.add(new CustomizationTableHeadItemVo("更新者姓名","updateUserName", false, false));
        list.add(new CustomizationTableHeadItemVo("更新者部门","updateUserDepartmentName", false, false));
        list.add(new CustomizationTableHeadItemVo("更新时间","updateTime", false, false));
        return list;
    }

}
