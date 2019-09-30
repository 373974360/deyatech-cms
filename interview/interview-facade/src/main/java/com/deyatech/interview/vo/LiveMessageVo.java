package com.deyatech.interview.vo;

import lombok.Data;

/**
 * 直播消息
 */
@Data
public class LiveMessageVo {
    /**
     * 键
     */
    private String key;
    /**
     * 类型(主持人、嘉宾)
     */
    private int type;
    /**
     * 消息
     */
    private String message;
    /**
     * 模型
     */
    private String modelId;
}
