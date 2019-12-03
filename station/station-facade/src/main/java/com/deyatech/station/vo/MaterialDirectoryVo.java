package com.deyatech.station.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 材料目录
 * </p>
 *
 * @author lee.
 * @since 2019-08-28
 */
@Data
public class MaterialDirectoryVo {
    /**
     * 目录名
     */
    private String label;
    /**
     * 子目录
     */
    private List<MaterialDirectoryVo> children;
    /**
     * 绝对路径
     */
    private String path;
    /**
     * 搜索名
     */
    private String name;
    private Long page;
    private Long size;
    private String siteId;
}
