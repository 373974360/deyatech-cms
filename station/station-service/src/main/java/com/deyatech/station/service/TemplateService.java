package com.deyatech.station.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.station.entity.Template;
import com.deyatech.station.vo.TemplateDynamicFormVo;
import com.deyatech.station.vo.TemplateVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  内容模板 服务类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-06
 */
public interface TemplateService extends BaseService<Template> {
    /**
     * 获取字段
     *
     * @param contentModelId
     * @return
     */
    Map<String, Object> getBaseAndMetaField(String contentModelId);

    /**
     * 获取动态表单
     *
     * @param contentModelId
     * @param templateId
     * @return
     */
    List<TemplateDynamicFormVo> getDynamicForm(String contentModelId, String templateId);

    /**
     * 单个将对象转换为vo内容模板
     *
     * @param template
     * @return
     */
    TemplateVo setVoProperties(Template template);

    /**
     * 批量将对象转换为vo内容模板
     *
     * @param templates
     * @return
     */
    List<TemplateVo> setVoProperties(Collection templates);

    /**
     * 批量将对象转换为vo内容模板
     * 前台专用
     * @param templates
     * @return
     */
    List<TemplateVo> setViewVoProperties(Collection templates);

    /**
     * 保存或更新
     * @param templateVo
     * @return
     */
    boolean saveOrUpdateTemplateVo(TemplateVo templateVo);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    boolean removeByIds(String ids);

    /**
     * 分页查询
     * @param entity
     * @return
     */
    IPage<TemplateVo> pageByTemplate(Template entity);

    /**
     * 判断Template对象标题是否存在
     *
     * @param template
     * @return
     */
    boolean checkTitleExist(Template template);

    /**
     * 生成静态页
     *
     * @param templateVo
     * @return
     */
    boolean genStaticPage(TemplateVo templateVo);

    /**
     * 生成索引
     *
     * @param templateVo
     * @return
     */
    boolean reindex(TemplateVo templateVo);

    /**
     * 删除索引数据
     *
     * @param template
     * @return
     */
    boolean removeIndexData(Template template);


    /************************************************************************************************
     *
     *                                      以下为网站前台调用接口
     *
     * **********************************************************************************************/
    /**
     * 分页查询
     * @param maps
     * @param page
     * @param pageSize
     * @return
     */
    IPage<TemplateVo> getTemplateListView(Map<String, Object> maps, Integer page, Integer pageSize);

    /**
     * 前台检索
     *
     *
     * */
    Map<String, Object> search(Map<String,Object> map);

    /**
     * 统计栏目下的内容
     *
     * @param catalogId
     * @return
     */
    int countTemplateByCatalogId(String catalogId);

    /**
     * 检索站点下的内容
     * @param siteId
     * @param start
     * @param end
     * @param part
     * @param number
     * @return
     */
    String resetTemplateIndexCode(String siteId, String start, String end, String part, int number);

    /**
     * 更新信息状态
     *
     * @param ids
     * @param status
     * @return
     */
    int updateStatusByIds(List<String> ids, int status);

    /**
     * 更新权重
     *
     * @param sortNo
     * @param id
     * @return
     */
    int updateSortNoById(int sortNo, String id);

    /**
     * 更新置顶
     *
     * @param flagTop
     * @param id
     * @return
     */
    int updateFlagTopById(boolean flagTop, String id);

    /**
     * 获取部门编号名称映射
     *
     * @return
     */
    Map<String, String> getDepartmentIdNameMap();

    /**
     * 更新并获取浏览次数
     *
     * @param id
     * @return
     * */
    int getTemplateClickCount(String id);
}
