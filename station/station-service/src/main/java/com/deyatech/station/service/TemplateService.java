package com.deyatech.station.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.entity.User;
import com.deyatech.common.base.BaseService;
import com.deyatech.station.entity.CatalogTemplate;
import com.deyatech.station.entity.Template;
import com.deyatech.station.vo.CatalogAggregationVo;
import com.deyatech.station.vo.TemplateDynamicFormVo;
import com.deyatech.station.vo.TemplateVo;

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
    IPage<TemplateVo> pageByTemplate(TemplateVo entity);

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
    void genStaticPage(TemplateVo templateVo,String messageCode);

    void addStaticPageTask(Map<String,Object> maps,String messageCode);

    /**
     * 生成索引
     *
     * @param templateVo
     * @return
     */
    boolean reindex(TemplateVo templateVo,String messageCode);

    /**
     * 缓存前台栏目列表前10页
     * @param catId
     * */
    void cacheCatalogList(String catId);

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
     * 根据ID查看详情
     * 前台专用
     * @param id
     * @return
     */
    TemplateVo getTemplateById(String id);
    /**
     * 批量将对象转换为vo内容模板
     * 前台专用
     * @param templateVo
     * @return
     */
    TemplateVo setViewVoProperties(TemplateVo templateVo);

    /**
     * 批量将对象转换为vo内容模板
     * 前台专用
     * @param templates
     * @return
     */
    List<TemplateVo> setViewVoProperties(Collection<TemplateVo> templates);
    /**
     * 前台检索
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
    void resetTemplateIndexCode(String siteId, String start, String end, String part, int number);
    void resetTemplateIndexCodeHandler(Map<String,Object> param);

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
    /**
     * 统计栏目的内容数
     *
     * @return
     */
    List<Map<String, Object>> countCatalogTemplate();

    /**
     * 用户
     *
     * @return
     */
    List<User> getAllUser();

    /**
     * 部门
     *
     * @return
     */
    List<Department> getAllDepartment();

    /**
     * 获取登陆用户代办理任务列表
     *
     * @param templateVo
     * @return
     */
    IPage<TemplateVo> getLoginUserTaskList(TemplateVo templateVo);

    /**
     * 内容审核通过
     *
     * @param template
     * @return
     */
    boolean contentFinish(Template template);

    /**
     * 内容审核拒绝
     *
     * @param template
     * @return
     */
    boolean contentReject(Template template);

    /**
     * 删除内容到回收站
     *
     * @param ids
     * @return
     */
    boolean recycleByIds(List<String> ids);

    /**
     * 从回收站还原内容
     *
     * @param ids
     * @return
     */
    boolean backByIds(List<String> ids);

    /**
     * 退稿的再送审
     *
     * @param ids
     * @return
     */
    boolean verifyByIds(List<String> ids);

    /**
     * 撤销
     *
     * @param ids
     * @return
     */
    boolean cancelByIds(List<String> ids);

    /**
     * 发布
     *
     * @param ids
     * @return
     */
    boolean publishByIds(List<String> ids);

    /**
     * 删除静态页和索引
     *
     * @param templateVo
     * @return
     */
    void deletePageAndIndexById(TemplateVo templateVo);

    /**
     * 添加静态页和索引
     *
     * @param templateVo
     * @return
     */
    void addPageAndIndexById(TemplateVo templateVo);

    /**
     * 启动工作流
     *
     * @param templateVo
     */
    void startWorkflow(TemplateVo templateVo);
}
