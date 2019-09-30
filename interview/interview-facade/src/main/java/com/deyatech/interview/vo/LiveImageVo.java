package com.deyatech.interview.vo;

import lombok.Data;

/**
 * 直播图片
 */
@Data
public class LiveImageVo {
    /**
     * 键
     */
    private String key;
    /**
     * 名称
     */
    private String name;
    /**
     * 消息
     */
    private String url;
    /**
     * 模型
     */
    private String modelId;
}
