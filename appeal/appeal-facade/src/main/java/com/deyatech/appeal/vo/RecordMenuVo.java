package com.deyatech.appeal.vo;

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
    private Integer sqFlag;
    private Integer sqStatus;
    private Integer isBack;
    private Integer limitFlag;
    private Integer alarmFlag;
    private Integer superviseFlag;
    List<RecordMenuVo> children;
}
