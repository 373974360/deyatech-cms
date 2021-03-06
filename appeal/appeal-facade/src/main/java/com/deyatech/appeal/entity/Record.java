package com.deyatech.appeal.entity;

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
 * @since 2019-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("appeal_record")
@ApiModel(value = "对象", description = "", parent = BaseEntity.class)
public class Record extends BaseEntity {

    @ApiModelProperty(value = "业务ID", dataType = "String")
    @TableField("model_id")
    private String modelId;

    @ApiModelProperty(value = "诉求目的", dataType = "String")
    @TableField("pur_id")
    private String purId;

    @ApiModelProperty(value = "标题", dataType = "String")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "来信人姓名", dataType = "String")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "身份证号码", dataType = "String")
    @TableField("card_id")
    private String cardId;

    @ApiModelProperty(value = "手机号码", dataType = "String")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "电子邮箱", dataType = "String")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "IP地址", dataType = "String")
    @TableField("ip")
    private String ip;

    @ApiModelProperty(value = "通讯地址", dataType = "String")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "信件编码", dataType = "String")
    @TableField("sq_code")
    private String sqCode;

    @ApiModelProperty(value = "查询码", dataType = "String")
    @TableField("query_code")
    private String queryCode;

    @ApiModelProperty(value = "收件部门 网民留言时候提交的部门", dataType = "String")
    @TableField("dept_id")
    private String deptId;

    @ApiModelProperty(value = "处理部门 管理员转办时候指定的目标部门", dataType = "String")
    @TableField("pro_dept_id")
    private String proDeptId;

    @ApiModelProperty(value = "公开意愿 1:是，0否", dataType = "Integer", example = "1")
    @TableField("is_open")
    private Integer isOpen;

    @ApiModelProperty(value = "来信内容", dataType = "String")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "是否发布 1:是，0否", dataType = "Integer", example = "1")
    @TableField("is_publish")
    private Integer isPublish;

    @ApiModelProperty(value = "回复内容", dataType = "String")
    @TableField("reply_content")
    private String replyContent;

    @ApiModelProperty(value = "回复时间", dataType = "LocalDateTime")
    @TableField("reply_time")
    private Date replyTime;

    @ApiModelProperty(value = "回复部门 最终回复网民留言的部门", dataType = "String")
    @TableField("reply_dept_id")
    private String replyDeptId;

    @ApiModelProperty(value = "处理截止日期限制 受理后，计时开始，默认值从业务模型中取", dataType = "LocalDateTime")
    @TableField("time_limit")
    private Date timeLimit;

    @ApiModelProperty(value = "督办标识 0：未督办 1：已督办", dataType = "Integer")
    @TableField("supervise_flag")
    private Integer superviseFlag;

    @ApiModelProperty(value = "超期未办警示标识 0：正常 1：预警 2：黄牌 3：红牌", dataType = "Integer")
    @TableField("alarm_flag")
    private Integer alarmFlag;

    @ApiModelProperty(value = "信件标识 0：正常信件（默认值）-1：无效信件1：重复信件2：不予受理信件", dataType = "Integer")
    @TableField("sq_flag")
    private Integer sqFlag;

    @ApiModelProperty(value = "处理状态 0：待处理 1：处理中 2：待审核（发布审核）3：已办结", dataType = "Integer")
    @TableField("sq_status")
    private Integer sqStatus;

    @ApiModelProperty(value = "回退标识 0：正常 1：退回", dataType = "Integer")
    @TableField("is_back")
    private Integer isBack;

    @ApiModelProperty(value = "延时申请标识 0：正常1：已延时2：申请延时", dataType = "Integer")
    @TableField("limit_flag")
    private Integer limitFlag;

    @ApiModelProperty(value = "延期时间", dataType = "LocalDateTime")
    @TableField("limit_flag_time")
    private Date limitFlagTime;


}
