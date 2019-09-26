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

    @ApiModelProperty(value = "公开意愿 1:是，2：否", dataType = "Integer", example = "1")
    @TableField("is_open")
    private Integer isOpen;

    @ApiModelProperty(value = "来信内容", dataType = "String")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "是否发布 1:是，2否", dataType = "Integer", example = "1")
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

    @ApiModelProperty(value = "信件状态 1:未处理，2:已受理，3:办理中，4:已办结，5:无效件，6:重复件", dataType = "Integer")
    @TableField("flag")
    private Integer flag;


}
