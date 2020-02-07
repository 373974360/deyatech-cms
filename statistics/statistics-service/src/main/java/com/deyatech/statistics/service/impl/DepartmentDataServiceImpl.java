package com.deyatech.statistics.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.entity.Department;
import com.deyatech.common.enums.ContentStatusEnum;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.statistics.mapper.DepartmentDataMapper;
import com.deyatech.statistics.service.DepartmentDataService;
import com.deyatech.statistics.vo.DepartmentUserDataQueryVo;
import com.deyatech.statistics.vo.DepartmentUserDataResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  部门数据统计 服务类
 * </p>
 *
 * @author ycx
 * @since 2020-02-05
 */
@Service
public class DepartmentDataServiceImpl extends DepartmentUserDataService implements DepartmentDataService {
    @Autowired
    DepartmentDataMapper departmentDataMapper;
    /**
     * 检索部门统计数据
     *
     * @param queryVo
     * @return
     */
    @Override
    public Map<String, Object> getDepartmentData(DepartmentUserDataQueryVo queryVo) throws Exception {
        // 结果
        Map<String, Object> result = new HashMap<>();
        // 树
        List<DepartmentUserDataResultVo> tree = new ArrayList<>();
        // 图表数据
        Collection<DepartmentUserDataResultVo> departments = new ArrayList<>();
        // 获取用户类型
        String type = getUserType(queryVo.getSiteId());
        if (StrUtil.isNotEmpty(type) && !"site".equals(type) && !"department".equals(type)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "没有查看部门统计的权限");
        }
        // 设置用户类型
        queryVo.setType(type);
        // 获取部门发稿量数据映射
        Map<String, DepartmentUserDataResultVo> resultVoMap = getDepartmentDataMap(queryVo);
        if (Objects.nonNull(resultVoMap)) {
            // 格式化
            NumberFormat fraction = getNumberFormat();
            if (StrUtil.isEmpty(queryVo.getStartTime()) || StrUtil.isEmpty(queryVo.getEndTime())) {
                // 开始日期
                queryVo.setStartTime(departmentDataMapper.getDepartmentContentDataMinDate(queryVo));
                // 结束日期
                queryVo.setEndTime(departmentDataMapper.getDepartmentContentDataMaxDate(queryVo));
            }
            // 发稿区间天数
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            double days = (format.parse(queryVo.getEndTime()).getTime() - format.parse(queryVo.getStartTime()).getTime()) / 86400000 + 1;
            // 部门映射
            Map<String, Department> departmentMap = getDepartmentMap();
            // 部门及父部门ID
            Set<String> departmentIdSet = new HashSet<>();
            // 设置数据
            setDepartmentData(resultVoMap, departmentMap, departmentIdSet, fraction, days);
            // 装配部门树
            assembleDepartmentTree(tree, resultVoMap, departmentMap, departmentIdSet);
            // 计算树值
            calculateTreeSum(tree, fraction, days);
            // 图表数据
            departments = resultVoMap.values();
        }
        // 树
        result.put("tree", tree);
        // 图表数据
        result.put("departments", departments);
        return result;
    }

    /**
     * 获取用户发稿量数据映射
     *
     * @param queryVo
     * @return
     */
    private Map<String, DepartmentUserDataResultVo> getDepartmentDataMap(DepartmentUserDataQueryVo queryVo) {
        // 用户数据映射
        Map<String, DepartmentUserDataResultVo> resultVoMap = null;
        // 统计用户状态发稿量
        List<Map<String, Object>> departmentContentDatas = departmentDataMapper.countDepartmentContentData(queryVo);
        if (CollectionUtil.isNotEmpty(departmentContentDatas)) {
            resultVoMap = new HashMap<>();
            for (Map<String, Object> data : departmentContentDatas) {
                String departmentId = (String) data.get("departmentId");
                int contentStatus = (int) data.get("contentStatus");
                long contentNumber = (long) data.get("contentNumber");
                DepartmentUserDataResultVo resultVo = resultVoMap.get(departmentId);
                // 对象不存在
                if (Objects.isNull(resultVo)) {
                    resultVo = new DepartmentUserDataResultVo();
                    // 部门ID
                    resultVo.setDepartmentId(departmentId);
                    resultVoMap.put(departmentId, resultVo);
                }
                // 已发布
                if (ContentStatusEnum.PUBLISH.getCode() == contentStatus) {
                    resultVo.setPublish(contentNumber);
                    resultVo.setPublishSum(contentNumber);
                }
                // 待审核
                else if (ContentStatusEnum.VERIFY.getCode() == contentStatus) {
                    resultVo.setVerify(contentNumber);
                    resultVo.setVerifySum(contentNumber);
                }
            }
        }
        return resultVoMap;
    }

    /**
     * 计算用户数据
     *
     * @param resultVoMap
     * @param departmentMap
     * @param fraction
     * @param days
     * @return
     * @throws ParseException
     */
    private void setDepartmentData(Map<String, DepartmentUserDataResultVo> resultVoMap,
                                       Map<String, Department> departmentMap,
                                       Set<String> departmentIdSet,
                                       NumberFormat fraction,
                                       double days) {
        for (DepartmentUserDataResultVo resultVo : resultVoMap.values()) {
            Department department = departmentMap.get(resultVo.getDepartmentId());
            departmentIdSet.add(department.getId());
            if(StrUtil.isNotEmpty(department.getTreePosition())) {
                String[] parentIds = department.getTreePosition().substring(1).split("&");
                for (String parentId : parentIds) {
                    departmentIdSet.add(parentId);
                }
            }
            resultVo.setParentId(department.getId());
            resultVo.setValue(department.getId());
            resultVo.setLabel(department.getName());
            resultVo.setChildren(null);
            resultVo.setDepartmentId(department.getId());
            resultVo.setDepartmentName(department.getName());
            // 计算用户数据
            calculateResultData(resultVo, fraction, days);
        }
    }

    /**
     * 装配部门树
     *
     * @param tree
     * @param resultVoMap
     * @param departmentMap
     * @param departmentIdSet
     */
    private void assembleDepartmentTree(List<DepartmentUserDataResultVo> tree,
                                        Map<String, DepartmentUserDataResultVo> resultVoMap,
                                        Map<String, Department> departmentMap,
                                        Set<String> departmentIdSet) {
        // 装配部门树
        List<DepartmentUserDataResultVo> list = assembleDepartmentList(departmentMap, departmentIdSet);
        // 部门数据
        for(DepartmentUserDataResultVo resultVo : list) {
            // 设置结果数据
            setResultData(resultVo, resultVoMap.get(resultVo.getDepartmentId()));
        }
        // 组装树
        assembleTree(tree, list);
    }

    /**=====================================================================*/

    /**
     * 检索部门栏目统计数据
     *
     * @param queryVo
     * @return
     */
    @Override
    public Map<String, Object> getDepartmentCatalogData(DepartmentUserDataQueryVo queryVo) throws Exception {
        if (StrUtil.isEmpty(queryVo.getStartTime()) || StrUtil.isEmpty(queryVo.getEndTime())) {
            // 开始日期
            queryVo.setStartTime(departmentDataMapper.getCatalogContentDataMinDate(queryVo));
            // 结束日期
            queryVo.setEndTime(departmentDataMapper.getCatalogContentDataMaxDate(queryVo));
        }
        // 统计用户状态发稿量
        List<Map<String, Object>> catalogContentDatas = departmentDataMapper.countCatalogContentData(queryVo);
        // 栏目统计结果
        return getCatalogDataResult(queryVo, catalogContentDatas);
    }

    /**
     * 检索部门栏目内容数据
     *
     * @param queryVo
     * @return
     */
    @Override
    public IPage<DepartmentUserDataResultVo> getDepartmentCatalogTemplateData(DepartmentUserDataQueryVo queryVo) {
        Page page = new Page();
        page.setCurrent(queryVo.getPage());
        page.setSize(queryVo.getSize());
        return departmentDataMapper.pageDepartmentCatalogTemplate(page, queryVo);
    }
}
