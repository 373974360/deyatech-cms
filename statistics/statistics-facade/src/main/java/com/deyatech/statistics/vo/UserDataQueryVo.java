package com.deyatech.statistics.vo;

import lombok.Data;

/**
 * 部门用户统计检索条件
 *
 * @author ycx
 * @since 2020-01-21
 */
@Data
public class UserDataQueryVo {
    /**
     * 站点ID
     */
    private String siteId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 栏目ID
     */
    private String catalogId;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
}
