package com.deyatech.apply.vo;

import lombok.Data;

import java.util.List;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/14 19:13
 */
@Data
public class RecordMenuVo {
    private String label;
    private Integer isPublish;
    private Integer applyFlag;
    private Integer applyStatus;
    private Integer alarmFlag;
    private Integer superviseFlag;
    List<RecordMenuVo> children;
}
