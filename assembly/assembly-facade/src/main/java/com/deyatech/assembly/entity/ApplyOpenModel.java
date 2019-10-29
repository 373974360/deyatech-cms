package com.deyatech.assembly.entity;

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
 * 
 * </p>
 *
 * @author lee.
 * @since 2019-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class ApplyOpenModel extends BaseEntity {

    @ApiModelProperty(value = "业务名称", dataType = "String")
    @TableField("model_name")
    private String modelName;

    @ApiModelProperty(value = "参与方式", dataType = "Integer", example = "1")
    @TableField("must_member")
    private Integer mustMember;

    @ApiModelProperty(value = "自动发布", dataType = "Integer", example = "1")
    @TableField("is_auto_publish")
    private Integer isAutoPublish;

    @ApiModelProperty(value = "提醒方式", dataType = "Integer", example = "1")
    @TableField("remind_type")
    private Integer remindType;

    @ApiModelProperty(value = "信件编码字头", dataType = "String")
    @TableField("code_pre")
    private String codePre;

    @ApiModelProperty(value = "日期码", dataType = "String")
    @TableField("code_rule")
    private String codeRule;

    @ApiModelProperty(value = "随机码位数", dataType = "Integer", example = "1")
    @TableField("code_num")
    private Integer codeNum;

    @ApiModelProperty(value = "办理时限", dataType = "Integer", example = "1")
    @TableField("time_limit")
    private Integer timeLimit;

    @ApiModelProperty(value = "查询码位数", dataType = "Integer", example = "1")
    @TableField("query_num")
    private Integer queryNum;

    @ApiModelProperty(value = "申请表地址", dataType = "String")
    @TableField("file_url")
    private String fileUrl;

    @ApiModelProperty(value = "表单页模板", dataType = "String")
    @TableField("template_form")
    private String templateForm;

    @ApiModelProperty(value = "列表页模板", dataType = "String")
    @TableField("template_list")
    private String templateList;

    @ApiModelProperty(value = "详情页模板", dataType = "String")
    @TableField("template_content")
    private String templateContent;

    @ApiModelProperty(value = "回执页模板", dataType = "String")
    @TableField("template_over")
    private String templateOver;

    @ApiModelProperty(value = "打印膜版", dataType = "String")
    @TableField("template_print")
    private String templatePrint;

    @ApiModelProperty(value = "查询页模板", dataType = "String")
    @TableField("template_query")
    private String templateQuery;

    @ApiModelProperty(value = "站点ID", dataType = "String")
    @TableField("site_id")
    private String siteId;

    @ApiModelProperty(value = "部门间转办1是，2否", dataType = "Integer", example = "1")
    @TableField("dept_transfer")
    private Integer deptTransfer;

    @ApiModelProperty(value = "主管部门引用部门ID", dataType = "String")
    @TableField("competent_dept")
    private String competentDept;

    @ApiModelProperty(value = "参与部门部门ID数组", dataType = "String")
    @TableField("part_dept")
    private String partDept;

    @ApiModelProperty(value = "是否开启工作流1:是，2否", dataType = "Integer", example = "1")
    @TableField("workflow_type")
    private Integer workflowType;

    @ApiModelProperty(value = "工作流ID", dataType = "String", example = "1")
    @TableField("workflow_id")
    private String workflowId;

}
