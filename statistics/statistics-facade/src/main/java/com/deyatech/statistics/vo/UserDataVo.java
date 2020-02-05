package com.deyatech.statistics.vo;

import lombok.Data;

import java.util.List;

/**
 * 用户统计数据
 *
 * @author ycx
 * @since 2020-01-21
 */
@Data
public class UserDataVo {
    /**
     * 用户ID或者部门ID
     */
    private String value;
    private String label;
    private Integer level;
    private List<UserDataVo> children;
    private String parentId;
    /**
     * 部门
     */
    private String departmentId;
    private String departmentName;
    /**
     * 用户
     */
    private String userId;
    private String userName;
    /**
     * 栏目
     */
    private String catalogId;
    private String catalogName;
    /**
     * 总发稿量
     */
    private long total;
    private long totalSum;
    private String totalShow;
    /**
     * 已发布发稿量
     */
    private long publish;
    private long publishSum;
    private String publishShow;
    /**
     * 待审核发稿量
     */
    private long verify;
    private long verifySum;
    private String verifyShow;
    /**
     * 采纳率
     */
    private String adoptRate;
    private String adoptRateSum;
    private String adoptRateShow;
    /**
     * 日平均发稿量
     */
    private String dayAverage;
    private String dayAverageSum;
    private String dayAverageShow;
    /**
     * 周平均发稿量
     */
    private String weekAverage;
    private String weekAverageSum;
    private String weekAverageShow;
    /**
     * 月平均发稿量
     */
    private String monthAverage;
    private String monthAverageSum;
    private String monthAverageShow;
}
