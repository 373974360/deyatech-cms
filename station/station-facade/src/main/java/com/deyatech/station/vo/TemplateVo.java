package com.deyatech.station.vo;

import com.deyatech.admin.vo.MetadataCollectionVo;
import com.deyatech.station.entity.Template;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * <p>
 * 内容模板扩展对象
 * </p>
 *
 * @author csm.
 * @since 2019-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "内容模板扩展对象", description = "内容模板扩展对象", parent = Template.class)
public class TemplateVo extends Template {

    /**
     * id字符串数组
     */
    private String ids;
    /**
     * MQ 消息代码
     */
    private String code;
    /**
     * 索引
     */
    private String index;
    /**
     * 内容模型名称
     */
    private String contentModelName;
    /**
     * 工作流key
     */
    private String workflowKey;

    /**
     * 元数据附加在信息都保存在此map中
     */
    private String contentMapStr;

    /**
     * 元数据附加在信息都保存在此map中
     */
    private Map content;

    /**
     * 元数据集id
     */
    private String metaDataCollectionId;

    /**
     * 元数据集
     */
    private MetadataCollectionVo metadataCollectionVo;

    /**
     * 缩略图上传路径
     */
    private String thumbnailUrl;

    /**
     * 录入人姓名
     */
    private String inputUserName;

    /**
     * 审核人姓名
     */
    private String verifyUserName;

    /**
     * 草稿标记
     */
    private String draftFlag;
}
