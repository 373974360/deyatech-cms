package com.deyatech.appeal.entity;

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
 * @since 2019-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("appeal_model")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class Model extends BaseEntity {

    @ApiModelProperty(value = "业务名称", dataType = "String")
    @TableField("model_name")
    private String modelName;

    @ApiModelProperty(value = "参与人员1:会员，2:所有人", dataType = "Integer", example = "1")
    @TableField("participant")
    private Integer participant;

    @ApiModelProperty(value = "自动发布1是，2否", dataType = "Integer", example = "1")
    @TableField("auto_publish")
    private Integer autoPublish;

    @ApiModelProperty(value = "办理时限", dataType = "Integer", example = "1")
    @TableField("limit_day")
    private Integer limitDay;

    @ApiModelProperty(value = "提醒件", dataType = "Integer", example = "1")
    @TableField("reminder_day")
    private Integer reminderDay;

    @ApiModelProperty(value = "黄牌件", dataType = "Integer", example = "1")
    @TableField("yellow_day")
    private Integer yellowDay;

    @ApiModelProperty(value = "红牌件", dataType = "Integer", example = "1")
    @TableField("red_day")
    private Integer redDay;

    @ApiModelProperty(value = "业务模式1:转发 2:部门直投", dataType = "Integer", example = "1")
    @TableField("bus_type")
    private Integer busType;

    @ApiModelProperty(value = "部门间转办1是，2否", dataType = "Integer", example = "1")
    @TableField("dept_transfer")
    private Integer deptTransfer;

    @ApiModelProperty(value = "主管部门引用部门ID", dataType = "String")
    @TableField("competent_dept")
    private String competentDept;

    @ApiModelProperty(value = "参与部门部门ID数组", dataType = "String")
    @TableField("part_dept")
    private String partDept;

    @ApiModelProperty(value = "业务码", dataType = "String")
    @TableField("bus_code")
    private String busCode;

    @ApiModelProperty(value = "间隔符1", dataType = "String")
    @TableField("spacera")
    private String spacera;

    @ApiModelProperty(value = "日期码", dataType = "String")
    @TableField("day_code")
    private String dayCode;

    @ApiModelProperty(value = "间隔符2", dataType = "String")
    @TableField("spacerb")
    private String spacerb;

    @ApiModelProperty(value = "随机码位数", dataType = "Integer", example = "1")
    @TableField("randomcode_count")
    private Integer randomcodeCount;

    @ApiModelProperty(value = "查询码位数", dataType = "Integer", example = "1")
    @TableField("querycode_count")
    private Integer querycodeCount;

    @ApiModelProperty(value = "是否开启工作流1:是，2否", dataType = "Integer", example = "1")
    @TableField("workflow_type")
    private Integer workflowType;

    @ApiModelProperty(value = "工作流ID", dataType = "String", example = "1")
    @TableField("workflow_id")
    private String workflowId;

    @ApiModelProperty(value = "表单模板", dataType = "String", example = "1")
    @TableField("form_templet")
    private String formTemplet;

    @ApiModelProperty(value = "列表模板", dataType = "String", example = "1")
    @TableField("list_templet")
    private String listTemplet;

    @ApiModelProperty(value = "正文模板", dataType = "String", example = "1")
    @TableField("view_templet")
    private String viewTemplet;

    @ApiModelProperty(value = "打印模板后台用", dataType = "String", example = "1")
    @TableField("print_templet")
    private String printTemplet;

}
