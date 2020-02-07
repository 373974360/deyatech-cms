package com.deyatech.statistics.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.common.Constants;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.enums.ContentStatusEnum;
import com.deyatech.resource.feign.ResourceFeign;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.statistics.vo.DepartmentUserDataQueryVo;
import com.deyatech.statistics.vo.DepartmentUserDataResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门用户统计数据服务
 */
public class DepartmentUserDataService {
    @Autowired
    AdminFeign adminFeign;
    @Autowired
    ResourceFeign resourceFeign;
    @Autowired
    StationFeign stationFeign;

    /**
     * 部门映射
     *
     * @return
     */
    public Map<String, Department> getDepartmentMap() {
        List<Department> departmentList = adminFeign.getAllDepartments().getData();
        return departmentList.stream().collect(Collectors.toMap(Department::getId, d->d));
    }

    /**
     * 格式化
     *
     * @return
     */
    public NumberFormat getNumberFormat() {
        NumberFormat fraction = NumberFormat.getNumberInstance();
        fraction.setMaximumFractionDigits(2);
        fraction.setRoundingMode(RoundingMode.DOWN);//舍去
        return fraction;
    }

    /**
     * 获取用户类型
     *
     * @param siteId
     */
    public String getUserType(String siteId) {
        // 是否站点管理员
        boolean isAdmin = resourceFeign.isSiteAdmin(siteId, UserContextHelper.getUserId()).getData();
        if (isAdmin) {
            return "site";
        } else {
            // 部门管理员
            isAdmin = resourceFeign.isSiteDepartmentAdmin(siteId, UserContextHelper.getUserId()).getData();
            if (isAdmin) {
                return "department";
            }
            // 用户自己
            else {
                return "user";
            }
        }
    }

    /**
     * 计算结果数据
     *
     * @param resultVo
     * @param fraction
     * @param days
     */
    public void calculateResultData(DepartmentUserDataResultVo resultVo, NumberFormat fraction, double days) {
        // 总发稿量 = 已发布 + 待审核
        resultVo.setTotal(resultVo.getPublish() + resultVo.getVerify());
        if (resultVo.getTotal() > 0) {
            // 采纳率 = 已发布 / 总发稿量
            if (resultVo.getPublish() > 0) {
                resultVo.setAdoptRate(fraction.format(((double) resultVo.getPublish() / resultVo.getTotal()) * 100) + "%");
            } else {
                resultVo.setAdoptRate("0");
            }
            // 日平均
            resultVo.setDayAverage(fraction.format(resultVo.getTotal() / days));
            // 周平均
            resultVo.setWeekAverage(fraction.format(resultVo.getTotal() / days / 7));
            // 月平均
            resultVo.setMonthAverage(fraction.format(resultVo.getTotal() / days / 30));
        } else {
            resultVo.setAdoptRate("0");
            // 日平均
            resultVo.setDayAverage("0");
            // 周平均
            resultVo.setWeekAverage("0");
            // 月平均
            resultVo.setMonthAverage("0");
        }
    }

    /**
     * 装配部门列表
     *
     * @param departmentMap
     * @param departmentIdSet
     * @return
     */
    List<DepartmentUserDataResultVo> assembleDepartmentList(Map<String, Department> departmentMap, Set<String> departmentIdSet) {
        List<DepartmentUserDataResultVo> list = new ArrayList<>();
        // 部门数据
        for (String departmentId : departmentIdSet) {
            DepartmentUserDataResultVo resultVo = new DepartmentUserDataResultVo();
            Department department = departmentMap.get(departmentId);
            if(StrUtil.isNotBlank(department.getTreePosition())){
                String[] split = department.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                resultVo.setLevel(split.length);
            }else{
                resultVo.setLevel(Constants.DEFAULT_ROOT_LEVEL);
            }
            resultVo.setDepartmentId(department.getId());
            resultVo.setDepartmentName(department.getName());
            resultVo.setParentId(department.getParentId());
            resultVo.setValue(department.getId());
            resultVo.setLabel(department.getName());
            resultVo.setChildren(null);
            list.add(resultVo);
        }
        return list;
    }

    /**
     * 装配栏目列表
     *
     * @param catalogMap
     * @param catalogIdSet
     * @return
     */
    List<DepartmentUserDataResultVo> assembleCatalogList(Map<String, Catalog> catalogMap, Set<String> catalogIdSet) {
        List<DepartmentUserDataResultVo> list = new ArrayList<>();
        // 栏目数据
        for (String catalogId : catalogIdSet) {
            DepartmentUserDataResultVo resultVo = new DepartmentUserDataResultVo();
            Catalog catalog = catalogMap.get(catalogId);
            if(StrUtil.isNotBlank(catalog.getTreePosition())){
                String[] split = catalog.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                resultVo.setLevel(split.length);
            }else{
                resultVo.setLevel(Constants.DEFAULT_ROOT_LEVEL);
            }
            resultVo.setCatalogId(catalog.getId());
            resultVo.setCatalogName(catalog.getName());
            resultVo.setParentId(catalog.getParentId());
            resultVo.setValue(catalog.getId());
            resultVo.setLabel(catalog.getName());
            resultVo.setChildren(null);
            list.add(resultVo);
        }
        return list;
    }

    /**
     * 组装树
     *
     * @param tree
     * @param list
     */
    public void assembleTree(List<DepartmentUserDataResultVo> tree, Collection<DepartmentUserDataResultVo> list) {
        for (DepartmentUserDataResultVo userDataVo : list) {
            if (ObjectUtil.equal(userDataVo.getParentId(), Constants.ZERO)) {
                tree.add(userDataVo);
            }
            for (DepartmentUserDataResultVo child: list) {
                if (ObjectUtil.equal(child.getParentId(), userDataVo.getValue())) {
                    if (ObjectUtil.isNull(userDataVo.getChildren())) {
                        List<DepartmentUserDataResultVo> children = CollectionUtil.newArrayList();
                        children.add(child);
                        userDataVo.setChildren(children);
                    } else {
                        userDataVo.getChildren().add(child);
                    }
                }
            }
        }
    }

    /**
     * 计算树合计值
     *
     * @param tree
     * @param fraction
     * @param days
     */
    public void calculateTreeSum(List<DepartmentUserDataResultVo> tree, NumberFormat fraction, double days) {
        for (DepartmentUserDataResultVo resultVo : tree) {
            long[] childrenValue = getChildrenSum(resultVo.getChildren(), fraction, days);
            resultVo.setPublishSum(resultVo.getPublishSum() + childrenValue[0]);
            resultVo.setVerifySum(resultVo.getVerifySum() + childrenValue[1]);
            // 计算合计值
            calculateDataSum(resultVo, CollectionUtil.isEmpty(resultVo.getChildren()), fraction, days);
        }
    }

    /**
     * 获取子项合计值
     *
     * @param children
     * @param fraction
     * @param days
     */
    public long[]  getChildrenSum(List<DepartmentUserDataResultVo> children, NumberFormat fraction, double days) {
        long[] value = new long[]{0, 0};
        if (CollectionUtil.isEmpty(children)) {
            return value;
        }
        for (DepartmentUserDataResultVo resultVo : children) {
            long[] childrenValue = getChildrenSum(resultVo.getChildren(), fraction, days);
            resultVo.setPublishSum(resultVo.getPublishSum() + childrenValue[0]);
            resultVo.setVerifySum(resultVo.getVerifySum() + childrenValue[1]);
            // 计算合计值
            calculateDataSum(resultVo, CollectionUtil.isEmpty(resultVo.getChildren()), fraction, days);
            value[0] += resultVo.getPublishSum();
            value[1] += resultVo.getVerifySum();
        }
        return value;
    }

    /**
     * 计算合计值
     *
     * @param resultVo
     * @param isLeaf
     * @param fraction
     * @param days
     */
    public void calculateDataSum(DepartmentUserDataResultVo resultVo, boolean isLeaf, NumberFormat fraction, double days) {
        // 总发稿量 = 已发布 + 待审核
        resultVo.setTotalSum(resultVo.getPublishSum() + resultVo.getVerifySum());
        if (resultVo.getTotalSum() > 0) {
            // 采纳率 = 已发布 / 总发稿量
            if (resultVo.getPublishSum() > 0) {
                resultVo.setAdoptRateSum(fraction.format(((double) resultVo.getPublishSum() / resultVo.getTotalSum()) * 100) + "%");
            }
            // 日平均
            resultVo.setDayAverageSum(fraction.format(resultVo.getTotalSum() / days));
            // 周平均
            resultVo.setWeekAverageSum(fraction.format(resultVo.getTotalSum() / days / 7));
            // 月平均
            resultVo.setMonthAverageSum(fraction.format(resultVo.getTotalSum() / days / 30));
        }
        // 叶子
        if (isLeaf) {
            resultVo.setTotalShow(String.valueOf(resultVo.getTotal()));
            resultVo.setPublishShow(String.valueOf(resultVo.getPublish()));
            resultVo.setVerifyShow(String.valueOf(resultVo.getVerify()));
            resultVo.setAdoptRateShow(String.valueOf(resultVo.getAdoptRate()));
            resultVo.setDayAverageShow(String.valueOf(resultVo.getDayAverage()));
            resultVo.setWeekAverageShow(String.valueOf(resultVo.getWeekAverage()));
            resultVo.setMonthAverageShow(String.valueOf(resultVo.getMonthAverage()));
        } else {
            resultVo.setTotalShow(resultVo.getTotalSum() + " (" + resultVo.getTotal() + ")");
            resultVo.setPublishShow(resultVo.getPublishSum() + " (" + resultVo.getPublish() + ")");
            resultVo.setVerifyShow(resultVo.getVerifySum() + " (" + resultVo.getVerify() + ")");
            resultVo.setAdoptRateShow(resultVo.getAdoptRateSum() + " (" + resultVo.getAdoptRate() + ")");
            resultVo.setDayAverageShow(resultVo.getDayAverageSum() + " (" + resultVo.getDayAverage() + ")");
            resultVo.setWeekAverageShow(resultVo.getWeekAverageSum() + " (" + resultVo.getWeekAverage() + ")");
            resultVo.setMonthAverageShow(resultVo.getMonthAverageSum() + " (" + resultVo.getMonthAverage() + ")");
        }
    }


    /**
     * 获取栏目发稿量数据映射
     *
     * @param queryVo
     * @return
     */
    public Map<String, DepartmentUserDataResultVo> getCatalogDataMap(DepartmentUserDataQueryVo queryVo, List<Map<String, Object>> catalogContentDatas) {
        Map<String, DepartmentUserDataResultVo> resultVoMap = null;
        if (CollectionUtil.isNotEmpty(catalogContentDatas)) {
            resultVoMap = new HashMap<>();
            for (Map<String, Object> data : catalogContentDatas) {
                String catalogId = (String) data.get("catalogId");
                int contentStatus = (int) data.get("contentStatus");
                long contentNumber = (long) data.get("contentNumber");
                DepartmentUserDataResultVo resultVo = resultVoMap.get(catalogId);
                // 对象不存在
                if (Objects.isNull(resultVo)) {
                    resultVo = new DepartmentUserDataResultVo();
                    // 栏目ID
                    resultVo.setCatalogId(catalogId);
                    resultVoMap.put(catalogId, resultVo);
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
     * 设置栏目数据
     *
     * @param dataResultMap
     * @param fraction
     * @param days
     */
    public void setCatalogData(Map<String, DepartmentUserDataResultVo> dataResultMap, Map<String, Catalog> catalogMap, Set<String> catalogIdSet, NumberFormat fraction, double days) {
        for (DepartmentUserDataResultVo resultVo : dataResultMap.values()) {
            // 栏目信息
            Catalog catalog = catalogMap.get(resultVo.getCatalogId());
            catalogIdSet.add(catalog.getId());
            if(StrUtil.isNotEmpty(catalog.getTreePosition())) {
                String[] parentIds = catalog.getTreePosition().substring(1).split("&");
                for (String parentId : parentIds) {
                    catalogIdSet.add(parentId);
                }
                String[] split = catalog.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                resultVo.setLevel(split.length);
            }else{
                resultVo.setLevel(Constants.DEFAULT_ROOT_LEVEL);
            }
            resultVo.setParentId(catalog.getParentId());
            resultVo.setValue(resultVo.getCatalogId());
            resultVo.setLabel(catalog.getName());
            resultVo.setChildren(null);
            resultVo.setCatalogName(catalog.getName());
            // 计算用户数据
            calculateResultData(resultVo, fraction, days);
        }
    }

    /**
     * 装配部门树
     *
     * @param tree
     * @param resultVoMap
     * @param catalogMap
     * @param catalogIdSet
     */
    private void assembleCatalogTree(List<DepartmentUserDataResultVo> tree,
                                        Map<String, DepartmentUserDataResultVo> resultVoMap,
                                        Map<String, Catalog> catalogMap,
                                        Set<String> catalogIdSet) {
        // 装配栏目树
        List<DepartmentUserDataResultVo> list = assembleCatalogList(catalogMap, catalogIdSet);
        // 栏目数据
        for(DepartmentUserDataResultVo resultVo : list) {
            // 设置结果数据
            setResultData(resultVo, resultVoMap.get(resultVo.getCatalogId()));
        }
        // 组装树
        assembleTree(tree, list);
    }

    /**
     * 设置结果数据
     *
     * @param resultVo
     * @param data
     */
    public void setResultData(DepartmentUserDataResultVo resultVo, DepartmentUserDataResultVo data) {
        if (Objects.nonNull(data)) {
            resultVo.setTotal(data.getTotal());
            resultVo.setPublish(data.getPublish());
            resultVo.setPublishSum(data.getPublishSum());
            resultVo.setVerify(data.getVerify());
            resultVo.setVerifySum(data.getVerifySum());
            resultVo.setAdoptRate(data.getAdoptRate());
            resultVo.setDayAverage(data.getDayAverage());
            resultVo.setWeekAverage(data.getWeekAverage());
            resultVo.setMonthAverage(data.getMonthAverage());
        } else {
            resultVo.setTotal(0);
            resultVo.setPublish(0);
            resultVo.setPublishSum(0);
            resultVo.setVerify(0);
            resultVo.setVerifySum(0);
            resultVo.setAdoptRate("0");
            resultVo.setDayAverage("0");
            resultVo.setWeekAverage("0");
            resultVo.setMonthAverage("0");
        }
    }

    /**
     * 栏目统计结果
     *
     * @param queryVo
     * @param catalogContentDatas
     * @return
     * @throws ParseException
     */
    public Map<String, Object> getCatalogDataResult(DepartmentUserDataQueryVo queryVo, List<Map<String, Object>> catalogContentDatas) throws ParseException {
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        // 返回树
        List<DepartmentUserDataResultVo> tree = new ArrayList<>();
        // 图表数据
        Collection<DepartmentUserDataResultVo> catalogs = new ArrayList<>();
        // 用户数据映射
        Map<String, DepartmentUserDataResultVo> resultVoMap = getCatalogDataMap(queryVo, catalogContentDatas);
        if (Objects.nonNull(resultVoMap)) {
            Set<String> catalogIdSet = new HashSet<>();
            // 格式化
            NumberFormat fraction = getNumberFormat();
            // 发稿区间天数
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            double days = (format.parse(queryVo.getEndTime()).getTime() - format.parse(queryVo.getStartTime()).getTime()) / 86400000 + 1;
            // 栏目信息
            Map<String, Catalog> catalogMap = stationFeign.getCatalogInfoMap(queryVo.getSiteId()).getData();
            // 设置数据
            setCatalogData(resultVoMap, catalogMap, catalogIdSet, fraction, days);
            // 组装树
            assembleCatalogTree(tree, resultVoMap, catalogMap, catalogIdSet);
            // 计算树的值
            calculateTreeSum(tree, fraction, days);
            // 图表数据
            catalogs = resultVoMap.values();
        }
        result.put("tree", tree);
        result.put("catalogs", catalogs);
        return result;
    }
}
