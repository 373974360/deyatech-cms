package com.deyatech.station.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.deyatech.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 栏目
 * </p>
 *
 * @author csm.
 * @since 2019-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("station_catalog")
@ApiModel(value = "栏目对象", description = "栏目", parent = BaseEntity.class)
public class Catalog extends BaseEntity {

    @ApiModelProperty(value = "站点id", dataType = "String")
    @TableField("site_id")
    private String siteId;

    @ApiModelProperty(value = "父节点id", dataType = "String")
    @TableField("parent_id")
    private String parentId;

    @ApiModelProperty(value = "栏目名称", dataType = "String")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "栏目别名", dataType = "String")
    @TableField("alias_name")
    private String aliasName;

    @ApiModelProperty(value = "英文名称", dataType = "String")
    @TableField("ename")
    private String ename;

    @ApiModelProperty(value = "是否显示", dataType = "String")
    @TableField("showable")
    private String showable;

    @ApiModelProperty(value = "外部链接地址", dataType = "String")
    @TableField("link_url")
    private String linkUrl;

    @ApiModelProperty(value = "工作流ID", dataType = "String")
    @TableField("workflow_id")
    private String workflowId;

    @ApiModelProperty(value = "工作流key", dataType = "String")
    @TableField("workflow_key")
    private String workflowKey;

    @ApiModelProperty(value = "首页模板", dataType = "String")
    @TableField("index_template")
    private String indexTemplate;

    @ApiModelProperty(value = "列表页模板", dataType = "String")
    @TableField("list_template")
    private String listTemplate;

    @ApiModelProperty(value = "排序号", dataType = "Integer", example = "1")
    @TableField("sort_no")
    private Integer sortNo;

    @ApiModelProperty(value = "在树结构中位置", dataType = "String")
    @TableField("tree_position")
    private String treePosition;

    @ApiModelProperty(value = "状态", dataType = "Integer", example = "1")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "是否允许评论", dataType = "Integer", example = "1")
    @TableField("allow_comment")
    private Integer allowComment;

    @ApiModelProperty(value = "是否允许分享: 0.否 1.是", dataType = "Integer", example = "1")
    @TableField("allow_share")
    private Integer allowShare;

    @ApiModelProperty(value = "应用id", dataType = "String")
    @TableField("application_id")
    private String applicationId;

    @ApiModelProperty(value = "属性id", dataType = "String")
    @TableField("attribute_id")
    private String attributeId;

    @ApiModelProperty(value = "自动发布:0.否 1.是", dataType = "Integer", example = "1")
    @TableField("auto_release")
    private Integer autoRelease;

    @ApiModelProperty(value = "内容对象id", dataType = "String")
    @TableField("contect_object_id")
    private String contectObjectId;

    @ApiModelProperty(value = "显示条数", dataType = "Integer", example = "1")
    @TableField("display_number")
    private Integer displayNumber;

    @ApiModelProperty(value = "生成栏目首页:0.否 1.是", dataType = "Integer", example = "1")
    @TableField("generate_home")
    private Integer generateHome;

    @ApiModelProperty(value = "在导航中显示: 0.否 1.是", dataType = "Integer", example = "1")
    @TableField("navigation_show_able")
    private Integer navigationShowAble;

    @ApiModelProperty(value = "参与人员:0.会员 1.所有人", dataType = "Integer", example = "1")
    @TableField("participant")
    private Integer participant;

    @ApiModelProperty(value = "在树中显示: 0.否 1.是", dataType = "Integer", example = "1")
    @TableField("tree_show_able")
    private Integer treeShowAble;

    @ApiModelProperty(value = "是否启用工作流: 0.否 1.是", dataType = "Integer", example = "1")
    @TableField("workflow_enable")
    private Integer workflowEnable;

    @ApiModelProperty(value = "路径名，如果是多级栏目则用/分隔各级ename", dataType = "String")
    @TableField("path_name")
    private String pathName;

    @ApiModelProperty(value = "内容模型id", dataType = "String")
    @TableField("content_model_id")
    private String contentModelId;

    @ApiModelProperty(value = "是否归档: 0.否 1.是", dataType = "Integer", example = "1")
    @TableField("place_on_file")
    private Integer placeOnFile;

    @ApiModelProperty(value = "是否外链: 0.否 1.是", dataType = "Integer", example = "1")
    @TableField("flag_external")
    private Integer flagExternal;

    @ApiModelProperty(value = "是否是聚合栏目: 0.否 1.是", dataType = "Integer", example = "1")
    @TableField("flag_aggregation")
    private Integer flagAggregation;

    @ApiModelProperty(value = "聚合栏目id", dataType = "String")
    @TableField("aggregation_id")
    private String aggregationId;
}
