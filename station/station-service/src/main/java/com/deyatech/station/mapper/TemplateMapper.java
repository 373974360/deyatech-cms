package com.deyatech.station.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.entity.User;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.assembly.entity.CustomizationTableHead;
import com.deyatech.station.entity.Template;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.vo.CatalogVo;
import com.deyatech.station.vo.TemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 内容模板 Mapper 接口
 * </p>
 *
 * @Author csm.
 * @since 2019-08-06
 */
public interface TemplateMapper extends BaseMapper<Template> {

    List<Template> pageTemplateListForRestIndexCode(
            @Param("siteId") String siteId,
            @Param("start") String start,
            @Param("end") String end,
            @Param("offset") int offset,
            @Param("size") int size
    );

    /**
     * 根据ID查询对象
     * @param id
     * @return
     */
    TemplateVo queryTemplateById(@Param("id") String id);


    /**
     * 分页查询
     * @param map
     * @return
     */
    List<TemplateVo> getTemplateList(@Param("map") Map<String,Object> map);
    int getTemplateListCount(@Param("map") Map<String,Object> map);

    /**
     * 更新信息状态
     *
     * @param ids
     * @param status
     * @return
     */
    int updateStatusByIds(@Param("ids") List<String> ids, @Param("status") int status);

    /**
     * 更新权重
     *
     * @param sortNo
     * @param id
     * @return
     */
    int updateSortNoById(@Param("sortNo") int sortNo, @Param("id") String id);

    /**
     * 更新置顶
     *
     * @param flagTop
     * @param id
     * @return
     */
    int updateFlagTopById(@Param("flagTop") boolean flagTop, @Param("id") String id);

    /**
     * 统计栏目的内容数
     *
     * @return
     */
    List<Map<String, Object>> countCatalogTemplate();

    /**
     * 获取登陆用户代办理任务列表
     *
     * @param templateVo
     * @return
     */
    IPage<TemplateVo> getLoginUserTaskList(@Param("page") Page page, @Param("templateVo") TemplateVo templateVo);

    /**
     * 分页查询
     *
     * @param template
     * @param catalogIdList
     * @param userIdList
     * @param offset
     * @param size
     * @return
     */
    List<TemplateVo> pageByTemplate(
            @Param("template") Template template,
            @Param("catalogIdList") List<String> catalogIdList,
            @Param("userIdList") List<String> userIdList,
            @Param("offset") long offset,
            @Param("size") long size
    );

    /**
     * 统计
     *
     * @param template
     * @param catalogIdList
     * @param userIdList
     * @return
     */
    Map<String, Object> countByTemplate(
            @Param("template") Template template,
            @Param("catalogIdList") List<String> catalogIdList,
            @Param("userIdList") List<String> userIdList
    );

    /**
     * 用户有权限的栏目
     *
     * @param siteId
     * @param userId
     * @return
     */
    List<CatalogVo> getUserRoleCatalog(@Param("siteId") String siteId, @Param("userId") String userId);

    /**
     * 用户角色权限
     *
     * @param userId
     * @return
     */
    List<String> getUserRoleAuthority(@Param("userId") String userId);

    /**
     * 用户所在部门的用户ID
     *
     * @param userId
     * @return
     */
    List<String> getUserIdOfUserDepartment(@Param("userId") String userId);

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
}
