package com.deyatech.assembly.vo;

import com.deyatech.common.enums.CustomizationTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 栏目表头数据
 * </p>
 *
 * @author lee.
 * @since 2019-10-30
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "栏目表头数据", description = "栏目表头数据", parent = CustomizationTableHeadVo.class)
public class CustomizationTableHeadVo {
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

    public CustomizationTableHeadVo() {
        this.label = "";
        this.prop = "";
        this.show = true;
    }

    public CustomizationTableHeadVo(String label, String prop, boolean show) {
        this.label = label;
        this.prop = prop;
        this.show = show;
    }

    public static List<CustomizationTableHeadVo> getDefault(String type) {
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
    public static List<CustomizationTableHeadVo> getCatalogDefault() {
        List<CustomizationTableHeadVo> list = new ArrayList<>();
        list.add(new CustomizationTableHeadVo("栏目名称","name", true));
        list.add(new CustomizationTableHeadVo("栏目别名","aliasName", true));
        list.add(new CustomizationTableHeadVo("英文名称","ename", true));
        list.add(new CustomizationTableHeadVo("隐藏","allowHidden", true));
        list.add(new CustomizationTableHeadVo("归档","placeOnFile", true));
        list.add(new CustomizationTableHeadVo("排序","sortNo", true));
        return list;
    }

    /**
     * 内容默认数据
     *
     * @return
     */
    public static List<CustomizationTableHeadVo> getContentDefault() {
        List<CustomizationTableHeadVo> list = new ArrayList<>();
        list.add(new CustomizationTableHeadVo("标题","title", true));
        list.add(new CustomizationTableHeadVo("发布时间","resourcePublicationDate", true));
        list.add(new CustomizationTableHeadVo("权重","sortNo", true));
        list.add(new CustomizationTableHeadVo("置顶","flagTop", true));
        list.add(new CustomizationTableHeadVo("录入人","inputUserName", true));
        return list;
    }

}
