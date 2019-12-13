package com.deyatech.station.vo;

import com.deyatech.admin.vo.MetadataCollectionVo;
import com.deyatech.station.entity.Template;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
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
     * 来源名
     */
    private String sourceName;
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
     * 工作流id
     */
    private String workflowId;
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
     * 草稿标记
     */
    private String draftFlag;

    /**
     * 站点名
     */
    private String siteName;

    /**
     * 栏目名
     */
    private String cmsCatalogName;

    /**
     * 路径名
     */
    private String cmsCatalogPathName;

    /**
     * 资源名
     */
    private String resourceCategoryName;

    /**
     * 资源管理元数据内容
     */
    private List<ResourceManagementVo> contentList;

    /**
     * 创建者姓名
     */
    private String createUserName;

    /**
     * 创建者部门
     */
    private String createUserDepartmentName;

    /**
     * 更新者姓名
     */
    private String updateUserName;

    /**
     * 更新者部门
     */
    private String updateUserDepartmentName;

}
