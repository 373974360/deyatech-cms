package com.deyatech.appeal.service;

import com.deyatech.appeal.entity.Process;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.vo.ProcessVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-09-24
 */
public interface ProcessService extends BaseService<Process> {

    /**
     * 单个将对象转换为vo
     *
     * @param process
     * @return
     */
    ProcessVo setVoProperties(Process process);

    /**
     * 批量将对象转换为vo
     *
     * @param processs
     * @return
     */
    List<ProcessVo> setVoProperties(Collection processs);

    void doProcess(Process process, Record record);
}
