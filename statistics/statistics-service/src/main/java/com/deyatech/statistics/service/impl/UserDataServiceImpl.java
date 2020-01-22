package com.deyatech.statistics.service.impl;

import com.deyatech.statistics.mapper.UserDataMapper;
import com.deyatech.statistics.service.UserDataService;
import com.deyatech.statistics.vo.UserDataQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 检索用户部门统计数据
     *
     * @param queryVo
     * @return
     */
    @Override
    public Map<String, List<Integer>> getDepartmentUserTreeDataList(UserDataQueryVo queryVo) {
        Map<String, List<Integer>> result = new HashMap<>();
        List<Map<String, Object>> userStatusData = userDataMapper.countDepartmentUserTreeData(queryVo);

        String minDate = userDataMapper.getUserDepartmentCatalogTemplatMinDate(queryVo);
        String maxDate = userDataMapper.getUserDepartmentCatalogTemplatMaxDate(queryVo);
        // 栏目 标题 发布时间 发布\入库机构 采纳率 发稿总量 平均日/周/月发稿量
        return result;
    }
}
