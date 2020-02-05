package com.deyatech.statistics.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.entity.User;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.common.Constants;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.enums.ContentStatusEnum;
import com.deyatech.station.entity.Catalog;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.statistics.mapper.UserDataMapper;
import com.deyatech.statistics.service.UserDataService;
import com.deyatech.statistics.vo.UserDataQueryVo;
import com.deyatech.statistics.vo.UserDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  用户数据统计 服务实现类
 * </p>
 *
 * @author ycx
 * @since 2020-01-06
 */
@Slf4j
@Service
public class UserDataServiceImpl implements UserDataService {

    @Autowired
    UserDataMapper userDataMapper;
    @Autowired
    AdminFeign adminFeign;
    @Autowired
    StationFeign stationFeign;

    /**
     * 部门映射
     *
     * @return
     */
    private Map<String, Department> getDepartmentMap() {
        List<Department> departmentList = adminFeign.getAllDepartments().getData();
        return departmentList.stream().collect(Collectors.toMap(Department::getId, d->d));
    }

    /**
     * 格式化
     *
     * @return
     */
    private NumberFormat getNumberFormat() {
        NumberFormat fraction = NumberFormat.getNumberInstance();
        fraction.setMaximumFractionDigits(2);
        fraction.setRoundingMode(RoundingMode.DOWN);//舍去
        return fraction;
    }

    /**
     * 检索用户部门统计数据
     *
     * @param queryVo
     * @return
     */
    @Override
    public Map<String, Object> getDepartmentUserTreeDataList(UserDataQueryVo queryVo) throws Exception {
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        // 树
        List<UserDataVo> tree = new ArrayList<>();
        // 图表数据
        Collection<UserDataVo> users = new ArrayList<>();
        // 设置用户类型
        setQueryType(queryVo);
        // 获取用户发稿量数据映射
        Map<String, UserDataVo> userDataMap = getDepartmentUserDataMap(queryVo);
        if (Objects.nonNull(userDataMap)) {
            // 用户所属部门及父部门ID
            Set<String> userDepartmentIdSet = new HashSet<>();
            // 部门映射
            Map<String, Department> departmentMap = getDepartmentMap();
            // 格式化
            NumberFormat fraction = getNumberFormat();
            // 发稿区间天数
            double days = getUserContentDataDays(queryVo);
            // 设置数据
            setDepartmentUserData(userDataMap, departmentMap, userDepartmentIdSet, fraction, days);
            // 装配部门树
            assembleDepartmentTree(tree, userDataMap, departmentMap, userDepartmentIdSet);
            // 计算树值
            calculateDepartmentTree(tree, fraction, days);
            // 图表数据
            users = userDataMap.values();
        }
        result.put("tree", tree);
        result.put("users", users);
        return result;
    }

    /**
     * 获取当前用户类型
     *
     * @param queryVo
     */
    private void setQueryType(UserDataQueryVo queryVo) {
        // 站点管理员
        List<String> siteIds = userDataMapper.getUserManagedSiteId(UserContextHelper.getUserId());
        if (CollectionUtil.isNotEmpty(siteIds) && siteIds.contains(queryVo.getSiteId())) {
            queryVo.setType("site");
        } else {
            // 部门管理员
            List<String> departmentIds = userDataMapper.getUserManagedDepartment(UserContextHelper.getUserId());
            if (CollectionUtil.isNotEmpty(departmentIds)) {
                queryVo.setType("department");
                queryVo.setDepartmentIds(departmentIds);
            }
            // 用户自己
            else {
                queryVo.setType("user");
                queryVo.setUserId(UserContextHelper.getUserId());
            }
        }
    }

    /**
     * 获取用户发稿量数据映射
     *
     * @param queryVo
     * @return
     */
    private Map<String, UserDataVo> getDepartmentUserDataMap(UserDataQueryVo queryVo) {
        // 用户数据映射
        Map<String, UserDataVo> userDataMap = null;
        // 统计用户状态发稿量
        List<Map<String, Object>> userContentDatas = userDataMapper.countUserContentData(queryVo);
        if (CollectionUtil.isNotEmpty(userContentDatas)) {
            userDataMap = new HashMap<>();
            for (Map<String, Object> data : userContentDatas) {
                String userId = (String) data.get("userId");
                int contentStatus = (int) data.get("contentStatus");
                long contentNumber = (long) data.get("contentNumber");
                UserDataVo userDataVo = userDataMap.get(userId);
                // 对象不存在
                if (Objects.isNull(userDataVo)) {
                    userDataVo = new UserDataVo();
                    // 用户ID
                    userDataVo.setUserId(userId);
                    userDataMap.put(userId, userDataVo);
                }
                // 已发布
                if (ContentStatusEnum.PUBLISH.getCode() == contentStatus) {
                    userDataVo.setPublish(contentNumber);
                }
                // 待审核
                else if (ContentStatusEnum.VERIFY.getCode() == contentStatus) {
                    userDataVo.setVerify(contentNumber);
                }
            }
        }
        return userDataMap;
    }

    /**
     * 发稿区间天数
     *
     * @param queryVo
     * @return
     * @throws ParseException
     */
    private double getUserContentDataDays(UserDataQueryVo queryVo) throws ParseException {
        // 开始日期
        String minDate = userDataMapper.getUserContentDataMinDate(queryVo);
        // 结束日期
        String maxDate = userDataMapper.getUserContentDataMaxDate(queryVo);
        // 发稿区间天数
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        double days = (format.parse(maxDate).getTime() - format.parse(minDate).getTime()) / 86400000 + 1;
        return days;
    }

    /**
     * 计算用户数据
     *
     * @param userDataMap
     * @param departmentMap
     * @param fraction
     * @param days
     * @return
     * @throws ParseException
     */
    private void setDepartmentUserData(Map<String, UserDataVo> userDataMap,
                             Map<String, Department> departmentMap,
                             Set<String> userDepartmentIdSet,
                             NumberFormat fraction,
                             double days) {
        // 发稿人信息
        List<User> userInfoList = userDataMapper.getUserInfo(userDataMap.keySet().stream().collect(Collectors.toList()));
        Map<String, User> userInfoMap = userInfoList.stream().collect(Collectors.toMap(User::getId, u->u));
        for (UserDataVo userDataVo : userDataMap.values()) {
            // 用户信息
            User user = userInfoMap.get(userDataVo.getUserId());
            // 用户所在部门
            userDepartmentIdSet.add(user.getDepartmentId());
            Department department = departmentMap.get(user.getDepartmentId());
            if(StrUtil.isNotEmpty(department.getTreePosition())) {
                String[] parentIds = department.getTreePosition().substring(1).split("&");
                for (String parentId : parentIds) {
                    userDepartmentIdSet.add(parentId);
                }
                String[] split = department.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                userDataVo.setLevel(split.length + 1);
            } else {
                userDataVo.setLevel(Constants.DEFAULT_ROOT_LEVEL + 1);
            }
            userDataVo.setParentId(user.getDepartmentId());
            userDataVo.setValue(userDataVo.getUserId());
            userDataVo.setLabel(user.getName());
            userDataVo.setChildren(null);
            userDataVo.setUserName(user.getName());
            userDataVo.setDepartmentId("");
            userDataVo.setDepartmentName("");
            // 计算用户数据
            calculateUserData(userDataVo, fraction, days);
        }
    }

    /**
     * 装配部门树
     *
     * @param tree
     * @param userDataMap
     * @param departmentMap
     * @param userDepartmentIdSet
     */
    private void assembleDepartmentTree(List<UserDataVo> tree,
                              Map<String, UserDataVo> userDataMap,
                              Map<String, Department> departmentMap,
                              Set<String> userDepartmentIdSet) {
        List<UserDataVo> list = new ArrayList<>();
        // 用户部门数据
        for (String id : userDepartmentIdSet) {
            Department department = departmentMap.get(id);
            UserDataVo userDataVo = new UserDataVo();
            if(StrUtil.isNotBlank(department.getTreePosition())){
                String[] split = department.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                userDataVo.setLevel(split.length);
            }else{
                userDataVo.setLevel(Constants.DEFAULT_ROOT_LEVEL);
            }
            userDataVo.setParentId(department.getParentId());
            userDataVo.setValue(department.getId());
            userDataVo.setLabel(department.getName());
            userDataVo.setChildren(null);
            userDataVo.setUserId("");
            userDataVo.setUserName("");
            userDataVo.setDepartmentId(id);
            userDataVo.setDepartmentName(department.getName());
            list.add(userDataVo);
        }
        // 用户数据
        userDataMap.values().stream().forEach(ud -> list.add(ud));
        // 组装树
        assembleTree(tree, list);
    }

    /**
     * 组装树
     *
     * @param tree
     * @param list
     */
    private void assembleTree(List<UserDataVo> tree, Collection<UserDataVo> list) {
        for (UserDataVo userDataVo : list) {
            if (ObjectUtil.equal(userDataVo.getParentId(), Constants.ZERO)) {
                tree.add(userDataVo);
            }
            for (UserDataVo child: list) {
                if (ObjectUtil.equal(child.getParentId(), userDataVo.getValue())) {
                    if (ObjectUtil.isNull(userDataVo.getChildren())) {
                        List<UserDataVo> children = CollectionUtil.newArrayList();
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
     * 计算树的值
     *
     * @param tree
     * @param fraction
     * @param days
     */
    private void calculateDepartmentTree(List<UserDataVo> tree, NumberFormat fraction, double days) {
        for (UserDataVo userDataVo : tree) {
            long[] childrenValue = getDepartmentChildrenData(userDataVo.getChildren(), fraction, days);
            userDataVo.setPublish(userDataVo.getPublish() + childrenValue[0]);
            userDataVo.setVerify(userDataVo.getVerify() + childrenValue[1]);
            calculateUserData(userDataVo, fraction, days);
        }
    }

    /**
     * 获取子项的值
     *
     * @param children
     * @param fraction
     * @param days
     */
    private long[]  getDepartmentChildrenData(List<UserDataVo> children, NumberFormat fraction, double days) {
        long[] value = new long[]{0, 0};
        if (CollectionUtil.isEmpty(children)) {
            return value;
        }
        for (UserDataVo userDataVo : children) {
            long[] childrenValue = getDepartmentChildrenData(userDataVo.getChildren(), fraction, days);
            userDataVo.setPublish(userDataVo.getPublish() + childrenValue[0]);
            userDataVo.setVerify(userDataVo.getVerify() + childrenValue[1]);
            calculateUserData(userDataVo, fraction, days);
            value[0] += userDataVo.getPublish();
            value[1] += userDataVo.getVerify();
        }
        return value;
    }

    /**
     * 计算用户数据
     *
     * @param userDataVo
     * @param fraction
     * @param days
     */
    private void calculateUserData(UserDataVo userDataVo, NumberFormat fraction, double days) {
        // 总发稿量 = 已发布 + 待审核
        userDataVo.setTotal(userDataVo.getPublish() + userDataVo.getVerify());
        if (userDataVo.getTotal() > 0) {
            // 采纳率 = 已发布 / 总发稿量
            if (userDataVo.getPublish() > 0) {
                userDataVo.setAdoptRate(fraction.format(((double) userDataVo.getPublish() / userDataVo.getTotal()) * 100) + "%");
            }
            // 日平均
            userDataVo.setDayAverage(fraction.format(userDataVo.getTotal() / days));
            // 周平均
            userDataVo.setWeekAverage(fraction.format(userDataVo.getTotal() / days / 7));
            // 月平均
            userDataVo.setMonthAverage(fraction.format(userDataVo.getTotal() / days / 30));
        }
    }


    /**====================================================================================================*/

    /**
     * 检索用户栏目统计数据
     *
     * @param queryVo
     * @return
     */
    @Override
    public Map<String, Object> getUserCatalogDataList(UserDataQueryVo queryVo) throws Exception {
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        // 返回树
        List<UserDataVo> tree = new ArrayList<>();
        // 图表数据
        Collection<UserDataVo> catalogs = new ArrayList<>();
        // 用户数据映射
        Map<String, UserDataVo> userCatalogDataMap = getUserCatalogDataMap(queryVo);
        if (Objects.nonNull(userCatalogDataMap)) {
            // 格式化
            NumberFormat fraction = getNumberFormat();
            // 发稿区间天数
            double days = getUserCatalogContentDataDays(queryVo);
            // 设置数据
            setUserCatalogData(userCatalogDataMap, fraction, days);
            // 组装树
            assembleTree(tree, userCatalogDataMap.values());
            // 计算树的值
            calculateUserCatalogTree(tree, fraction, days);
            // 图表数据
            catalogs = userCatalogDataMap.values();
        }
        result.put("tree", tree);
        result.put("catalogs", catalogs);
        return result;
    }

    /**
     * 获取用户栏目发稿量数据映射
     *
     * @param queryVo
     * @return
     */
    private Map<String, UserDataVo> getUserCatalogDataMap(UserDataQueryVo queryVo) {
        Map<String, UserDataVo> userCatalogDataMap = null;
        // 统计用户状态发稿量
        List<Map<String, Object>> userCatalogContentDatas = userDataMapper.countUserCatalogContentData(queryVo);
        if (CollectionUtil.isNotEmpty(userCatalogContentDatas)) {
            userCatalogDataMap = new HashMap<>();
            for (Map<String, Object> data : userCatalogContentDatas) {
                String catalogId = (String) data.get("catalogId");
                int contentStatus = (int) data.get("contentStatus");
                long contentNumber = (long) data.get("contentNumber");
                UserDataVo userDataVo = userCatalogDataMap.get(catalogId);
                // 对象不存在
                if (Objects.isNull(userDataVo)) {
                    userDataVo = new UserDataVo();
                    // 栏目ID
                    userDataVo.setCatalogId(catalogId);
                    userCatalogDataMap.put(catalogId, userDataVo);
                }
                // 已发布
                if (ContentStatusEnum.PUBLISH.getCode() == contentStatus) {
                    userDataVo.setPublish(contentNumber);
                    userDataVo.setPublishSum(contentNumber);
                    userDataVo.setPublishShow(String.valueOf(contentNumber));
                }
                // 待审核
                else if (ContentStatusEnum.VERIFY.getCode() == contentStatus) {
                    userDataVo.setVerify(contentNumber);
                    userDataVo.setVerifySum(contentNumber);
                    userDataVo.setVerifyShow(String.valueOf(contentNumber));
                }
            }
        }
        return userCatalogDataMap;
    }

    /**
     * 用户栏目数据区间天数
     *
     * @param queryVo
     * @return
     * @throws ParseException
     */
    private double getUserCatalogContentDataDays(UserDataQueryVo queryVo) throws ParseException {
        // 开始日期
        String minDate = userDataMapper.getUserCatalogContentDataMinDate(queryVo);
        // 结束日期
        String maxDate = userDataMapper.getUserCatalogContentDataMaxDate(queryVo);
        // 发稿区间天数
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        double days = (format.parse(maxDate).getTime() - format.parse(minDate).getTime()) / 86400000 + 1;
        return  days;
    }

    /**
     * 设置用户数据
     *
     * @param userCatalogDataMap
     * @param fraction
     * @param days
     */
    private void setUserCatalogData(Map<String, UserDataVo> userCatalogDataMap, NumberFormat fraction, double days) {
        // 栏目信息
        List<Catalog> catalogInfoList = userDataMapper.getCatalogInfo(userCatalogDataMap.keySet().stream().collect(Collectors.toList()));
        Map<String, Catalog> catalogInfoMap = catalogInfoList.stream().collect(Collectors.toMap(Catalog::getId, u->u));
        for (UserDataVo userDataVo : userCatalogDataMap.values()) {
            // 栏目信息
            Catalog catalog = catalogInfoMap.get(userDataVo.getCatalogId());
            if(StrUtil.isNotEmpty(catalog.getTreePosition())) {
                String[] split = catalog.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                userDataVo.setLevel(split.length);
            }else{
                userDataVo.setLevel(Constants.DEFAULT_ROOT_LEVEL);
            }
            userDataVo.setParentId(catalog.getParentId());
            userDataVo.setValue(userDataVo.getCatalogId());
            userDataVo.setLabel(catalog.getName());
            userDataVo.setChildren(null);
            userDataVo.setCatalogName(catalog.getName());
            // 计算用户数据
            calculateUserData(userDataVo, fraction, days);
        }
    }

    /**
     * 计算树的值
     *
     * @param tree
     * @param fraction
     * @param days
     */
    private void calculateUserCatalogTree(List<UserDataVo> tree, NumberFormat fraction, double days) {
        for (UserDataVo userDataVo : tree) {
            long[] childrenValue = getUserCatalogChildrenData(userDataVo.getChildren(), fraction, days);
            userDataVo.setPublishSum(userDataVo.getPublishSum() + childrenValue[0]);
            userDataVo.setVerifySum(userDataVo.getVerifySum() + childrenValue[1]);
            // 计算合计值
            calculateUserDataSum(userDataVo, CollectionUtil.isEmpty(userDataVo.getChildren()), fraction, days);
        }
    }

    /**
     * 获取子项的值
     *
     * @param children
     * @param fraction
     * @param days
     */
    private long[]  getUserCatalogChildrenData(List<UserDataVo> children, NumberFormat fraction, double days) {
        long[] value = new long[]{0, 0};
        if (CollectionUtil.isEmpty(children)) {
            return value;
        }
        for (UserDataVo userDataVo : children) {
            long[] childrenValue = getUserCatalogChildrenData(userDataVo.getChildren(), fraction, days);
            userDataVo.setPublishSum(userDataVo.getPublishSum() + childrenValue[0]);
            userDataVo.setVerifySum(userDataVo.getVerifySum() + childrenValue[1]);
            // 计算合计值
            calculateUserDataSum(userDataVo, CollectionUtil.isEmpty(userDataVo.getChildren()), fraction, days);
            value[0] += userDataVo.getPublishSum();
            value[1] += userDataVo.getVerifySum();
        }
        return value;
    }

    /**
     * 计算用户数据
     *
     * @param userDataVo
     * @param isLeaf
     * @param fraction
     * @param days
     */
    private void calculateUserDataSum(UserDataVo userDataVo, boolean isLeaf, NumberFormat fraction, double days) {
        // 总发稿量 = 已发布 + 待审核
        userDataVo.setTotalSum(userDataVo.getPublishSum() + userDataVo.getVerifySum());
        if (userDataVo.getTotalSum() > 0) {
            // 采纳率 = 已发布 / 总发稿量
            if (userDataVo.getPublishSum() > 0) {
                userDataVo.setAdoptRateSum(fraction.format(((double) userDataVo.getPublishSum() / userDataVo.getTotalSum()) * 100) + "%");
            }
            // 日平均
            userDataVo.setDayAverageSum(fraction.format(userDataVo.getTotalSum() / days));
            // 周平均
            userDataVo.setWeekAverageSum(fraction.format(userDataVo.getTotalSum() / days / 7));
            // 月平均
            userDataVo.setMonthAverageSum(fraction.format(userDataVo.getTotalSum() / days / 30));
        }
        // 叶子
        if (isLeaf) {
            userDataVo.setTotalShow(String.valueOf(userDataVo.getTotal()));
            userDataVo.setPublishShow(String.valueOf(userDataVo.getPublish()));
            userDataVo.setVerifyShow(String.valueOf(userDataVo.getVerify()));
            userDataVo.setAdoptRateShow(String.valueOf(userDataVo.getAdoptRate()));
            userDataVo.setDayAverageShow(String.valueOf(userDataVo.getDayAverage()));
            userDataVo.setWeekAverageShow(String.valueOf(userDataVo.getWeekAverage()));
            userDataVo.setMonthAverageShow(String.valueOf(userDataVo.getMonthAverage()));
        } else {
            userDataVo.setTotalShow(userDataVo.getTotalSum() + " (" + userDataVo.getTotal() + ")");
            userDataVo.setPublishShow(userDataVo.getPublishSum() + " (" + userDataVo.getPublish() + ")");
            userDataVo.setVerifyShow(userDataVo.getVerifySum() + " (" + userDataVo.getVerify() + ")");
            userDataVo.setAdoptRateShow(userDataVo.getAdoptRateSum() + " (" + userDataVo.getAdoptRate() + ")");
            userDataVo.setDayAverageShow(userDataVo.getDayAverageSum() + " (" + userDataVo.getDayAverage() + ")");
            userDataVo.setWeekAverageShow(userDataVo.getWeekAverageSum() + " (" + userDataVo.getWeekAverage() + ")");
            userDataVo.setMonthAverageShow(userDataVo.getMonthAverageSum() + " (" + userDataVo.getMonthAverage() + ")");
        }
    }
}
