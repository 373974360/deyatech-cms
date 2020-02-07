package com.deyatech.statistics.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.entity.User;
import com.deyatech.common.Constants;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.enums.ContentStatusEnum;
import com.deyatech.statistics.mapper.UserDataMapper;
import com.deyatech.statistics.service.UserDataService;
import com.deyatech.statistics.vo.DepartmentUserDataQueryVo;
import com.deyatech.statistics.vo.DepartmentUserDataResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class UserDataServiceImpl extends DepartmentUserDataService implements UserDataService {
    @Autowired
    UserDataMapper userDataMapper;

    /**
     * 检索用户部门统计数据
     *
     * @param queryVo
     * @return
     */
    @Override
    public Map<String, Object> getDepartmentUserData(DepartmentUserDataQueryVo queryVo) throws Exception {
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        // 树
        List<DepartmentUserDataResultVo> tree = new ArrayList<>();
        // 图表数据
        Collection<DepartmentUserDataResultVo> users = new ArrayList<>();
        queryVo.setUserId(UserContextHelper.getUserId());
        // 设置用户类型
        queryVo.setType(getUserType(queryVo.getSiteId()));
        // 获取用户发稿量数据映射
        Map<String, DepartmentUserDataResultVo> resultVoMap = getDepartmentUserDataMap(queryVo);
        if (Objects.nonNull(resultVoMap)) {
            // 格式化
            NumberFormat fraction = getNumberFormat();
            if (StrUtil.isEmpty(queryVo.getStartTime()) || StrUtil.isEmpty(queryVo.getEndTime())) {
                // 开始日期
                queryVo.setStartTime(userDataMapper.getUserContentDataMinDate(queryVo));
                // 结束日期
                queryVo.setEndTime(userDataMapper.getUserContentDataMaxDate(queryVo));
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // 发稿区间天数
            double days = (format.parse(queryVo.getEndTime()).getTime() - format.parse(queryVo.getStartTime()).getTime()) / 86400000 + 1;
            // 用户所属部门及父部门ID
            Set<String> departmentIdSet = new HashSet<>();
            // 部门映射
            Map<String, Department> departmentMap = getDepartmentMap();
            // 设置数据
            setDepartmentUserData(resultVoMap, departmentMap, departmentIdSet, fraction, days);
            // 装配部门树
            assembleDepartmentTree(tree, resultVoMap, departmentMap, departmentIdSet);
            // 计算树值
            calculateDepartmentTree(tree, fraction, days);
            // 图表数据
            users = resultVoMap.values();
        }
        result.put("tree", tree);
        result.put("users", users);
        return result;
    }

    /**
     * 获取用户发稿量数据映射
     *
     * @param queryVo
     * @return
     */
    private Map<String, DepartmentUserDataResultVo> getDepartmentUserDataMap(DepartmentUserDataQueryVo queryVo) {
        // 用户数据映射
        Map<String, DepartmentUserDataResultVo> resultVoMap = null;
        // 统计用户状态发稿量
        List<Map<String, Object>> userContentDatas = userDataMapper.countUserContentData(queryVo);
        if (CollectionUtil.isNotEmpty(userContentDatas)) {
            resultVoMap = new HashMap<>();
            for (Map<String, Object> data : userContentDatas) {
                String userId = (String) data.get("userId");
                int contentStatus = (int) data.get("contentStatus");
                long contentNumber = (long) data.get("contentNumber");
                DepartmentUserDataResultVo userDataVo = resultVoMap.get(userId);
                // 对象不存在
                if (Objects.isNull(userDataVo)) {
                    userDataVo = new DepartmentUserDataResultVo();
                    // 用户ID
                    userDataVo.setUserId(userId);
                    resultVoMap.put(userId, userDataVo);
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
    private void setDepartmentUserData(Map<String, DepartmentUserDataResultVo> resultVoMap,
                             Map<String, Department> departmentMap,
                             Set<String> userDepartmentIdSet,
                             NumberFormat fraction,
                             double days) {
        // 发稿人信息
        List<User> userInfoList = userDataMapper.getUserInfo(resultVoMap.keySet().stream().collect(Collectors.toList()));
        Map<String, User> userInfoMap = userInfoList.stream().collect(Collectors.toMap(User::getId, u->u));
        for (DepartmentUserDataResultVo resultVo : resultVoMap.values()) {
            // 用户信息
            User user = userInfoMap.get(resultVo.getUserId());
            // 用户所在部门
            userDepartmentIdSet.add(user.getDepartmentId());
            Department department = departmentMap.get(user.getDepartmentId());
            if(StrUtil.isNotEmpty(department.getTreePosition())) {
                String[] parentIds = department.getTreePosition().substring(1).split("&");
                for (String parentId : parentIds) {
                    userDepartmentIdSet.add(parentId);
                }
                String[] split = department.getTreePosition().split(Constants.DEFAULT_TREE_POSITION_SPLIT);
                resultVo.setLevel(split.length + 1);
            } else {
                resultVo.setLevel(Constants.DEFAULT_ROOT_LEVEL + 1);
            }
            resultVo.setParentId(user.getDepartmentId());
            resultVo.setValue(user.getId());
            resultVo.setLabel(user.getName());
            resultVo.setChildren(null);
            resultVo.setUserName(user.getName());
            resultVo.setDepartmentId("");
            resultVo.setDepartmentName("");
            // 计算结果数据
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
        // 用户数据
        resultVoMap.values().stream().forEach(ud -> list.add(ud));
        // 组装树
        assembleTree(tree, list);
    }



    /**
     * 计算树的值
     *
     * @param tree
     * @param fraction
     * @param days
     */
    private void calculateDepartmentTree(List<DepartmentUserDataResultVo> tree, NumberFormat fraction, double days) {
        for (DepartmentUserDataResultVo resultVo : tree) {
            long[] childrenValue = getDepartmentChildrenData(resultVo.getChildren(), fraction, days);
            resultVo.setPublish(resultVo.getPublish() + childrenValue[0]);
            resultVo.setVerify(resultVo.getVerify() + childrenValue[1]);
            // 计算结果数据
            calculateResultData(resultVo, fraction, days);
        }
    }

    /**
     * 获取子项的值
     *
     * @param children
     * @param fraction
     * @param days
     */
    private long[]  getDepartmentChildrenData(List<DepartmentUserDataResultVo> children, NumberFormat fraction, double days) {
        long[] value = new long[]{0, 0};
        if (CollectionUtil.isEmpty(children)) {
            return value;
        }
        for (DepartmentUserDataResultVo userDataVo : children) {
            long[] childrenValue = getDepartmentChildrenData(userDataVo.getChildren(), fraction, days);
            userDataVo.setPublish(userDataVo.getPublish() + childrenValue[0]);
            userDataVo.setVerify(userDataVo.getVerify() + childrenValue[1]);
            // 计算结果数据
            calculateResultData(userDataVo, fraction, days);
            value[0] += userDataVo.getPublish();
            value[1] += userDataVo.getVerify();
        }
        return value;
    }


    /**====================================================================================================*/

    /**
     * 检索用户栏目统计数据
     *
     * @param queryVo
     * @return
     */
    @Override
    public Map<String, Object> getUserCatalogData(DepartmentUserDataQueryVo queryVo) throws Exception {
        if (StrUtil.isEmpty(queryVo.getStartTime()) || StrUtil.isEmpty(queryVo.getEndTime())) {
            // 开始日期
            queryVo.setStartTime(userDataMapper.getUserCatalogContentDataMinDate(queryVo));
            // 结束日期
            queryVo.setEndTime(userDataMapper.getUserCatalogContentDataMaxDate(queryVo));
        }
        // 统计用户状态发稿量
        List<Map<String, Object>> catalogContentDatas = userDataMapper.countCatalogContentData(queryVo);
        // 栏目统计结果
        return getCatalogDataResult(queryVo, catalogContentDatas);
    }


    /**
     * 检索用户栏目内容数据
     *
     * @param queryVo
     * @return
     */
    @Override
    public IPage<DepartmentUserDataResultVo> getUserCatalogTemplateData(DepartmentUserDataQueryVo queryVo) {
        Page page = new Page();
        page.setCurrent(queryVo.getPage());
        page.setSize(queryVo.getSize());
        return userDataMapper.pageUserCatalogTemplate(page, queryVo);
    }

}
