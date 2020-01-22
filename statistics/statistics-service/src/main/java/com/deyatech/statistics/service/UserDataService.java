package com.deyatech.statistics.service;

import com.deyatech.statistics.vo.UserDataQueryVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  用户数据统计 服务类
 * </p>
 *
 * @author ycx
 * @since 2020-01-06
 */
public interface UserDataService {
    /**
     * 检索用户部门统计数据
     *
     * @param queryVo
     * @return
     */
    Map<String, List<Integer>> getDepartmentUserTreeDataList(UserDataQueryVo queryVo);
}
