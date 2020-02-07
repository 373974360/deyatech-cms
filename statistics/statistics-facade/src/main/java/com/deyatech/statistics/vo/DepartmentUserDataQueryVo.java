package com.deyatech.statistics.vo;

import lombok.Data;

import java.util.List;

/**
 * 部门用户统计检索条件
 *
 * @author ycx
 * @since 2020-01-21
 */
@Data
public class DepartmentUserDataQueryVo {
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 站点ID
     */
    private String siteId;
    /**
     * 用户类型
     */
    private String type;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 栏目ID
     */
    private String catalogId;
    private long page;
    private long size;
}
