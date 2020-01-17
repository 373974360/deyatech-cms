package com.deyatech.apply.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.deyatech.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author lee.
 * @since 2020-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("apply_open_record")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class OpenRecord extends BaseEntity {

    @ApiModelProperty(value = "业务ID", dataType = "String")
    @TableField("model_id")
    private String modelId;

    @ApiModelProperty(value = "申请编码", dataType = "String")
    @TableField("ysq_code")
    private String ysqCode;

    @ApiModelProperty(value = "查询编码", dataType = "String")
    @TableField("query_code")
    private String queryCode;

    @ApiModelProperty(value = "申请人类型；1=公民，2=法人或者其他组织", dataType = "Integer", example = "1")
    @TableField("ysq_type")
    private Integer ysqType;

    @ApiModelProperty(value = "姓名", dataType = "String")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "工作单位", dataType = "String")
    @TableField("company")
    private String company;

    @ApiModelProperty(value = "证件名称", dataType = "String")
    @TableField("card_name")
    private String cardName;

    @ApiModelProperty(value = "证件号码", dataType = "String")
    @TableField("card_code")
    private String cardCode;

    @ApiModelProperty(value = "组织机构代码", dataType = "String")
    @TableField("org_code")
    private String orgCode;

    @ApiModelProperty(value = "营业执照代码", dataType = "String")
    @TableField("licence")
    private String licence;

    @ApiModelProperty(value = "法人代表", dataType = "String")
    @TableField("legalperson")
    private String legalperson;

    @ApiModelProperty(value = "联系人", dataType = "String")
    @TableField("linkman")
    private String linkman;

    @ApiModelProperty(value = "联系电话", dataType = "String")
    @TableField("tel")
    private String tel;

    @ApiModelProperty(value = "联系传真", dataType = "String")
    @TableField("fax")
    private String fax;

    @ApiModelProperty(value = "手机号码", dataType = "String")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "电子邮箱", dataType = "String")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "通讯地址", dataType = "String")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "邮政编码", dataType = "String")
    @TableField("postcode")
    private String postcode;

    @ApiModelProperty(value = "所需信息内容描述", dataType = "String")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "用途描述", dataType = "String")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "获取信息方式", dataType = "Integer", example = "1")
    @TableField("get_method")
    private Integer getMethod;

    @ApiModelProperty(value = "信息指定提供方式", dataType = "Integer", example = "1")
    @TableField("offer_type")
    private Integer offerType;

    @ApiModelProperty(value = "是否发布；1=是，0=否", dataType = "Integer", example = "1")
    @TableField("is_publish")
    private Integer isPublish;

    @ApiModelProperty(value = "收件部门 网民留言时候提交的部门", dataType = "String")
    @TableField("dept_id")
    private String deptId;

    @ApiModelProperty(value = "处理部门 管理员转办时候指定的目标部门", dataType = "String")
    @TableField("pro_dept_id")
    private String proDeptId;

    @ApiModelProperty(value = "回复内容", dataType = "String")
    @TableField("reply_content")
    private String replyContent;

    @ApiModelProperty(value = "回复时间", dataType = "LocalDateTime")
    @TableField("reply_time")
    private Date replyTime;

    @ApiModelProperty(value = "回复部门", dataType = "String")
    @TableField("reply_dept_id")
    private String replyDeptId;

    @ApiModelProperty(value = "处理截止日期限制 受理后，计时开始，默认值从业务模型中取", dataType = "LocalDateTime")
    @TableField("time_limit")
    private Date timeLimit;

    @ApiModelProperty(value = "督办标识 0：未督办  1：已督办", dataType = "Integer", example = "1")
    @TableField("supervise_flag")
    private Integer superviseFlag;

    @ApiModelProperty(value = "超期未办警示标识 0：正常	1：预警	2：黄牌	3：红牌", dataType = "Integer", example = "1")
    @TableField("alarm_flag")
    private Integer alarmFlag;

    @ApiModelProperty(value = "处理状态 0：待处理	1：处理中	2：待审核（发布审核）3：已办结", dataType = "Integer", example = "1")
    @TableField("apply_status")
    private Integer applyStatus;

    @ApiModelProperty(value = "信件标识 0：正常信件（默认值）-1：无效信件1：重复信件2：不予受理信件", dataType = "Integer", example = "1")
    @TableField("apply_flag")
    private Integer applyFlag;

}
