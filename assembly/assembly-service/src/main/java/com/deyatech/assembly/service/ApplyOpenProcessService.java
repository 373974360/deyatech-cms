package com.deyatech.assembly.service;

import com.deyatech.assembly.entity.ApplyOpenProcess;
import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.vo.ApplyOpenProcessVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-11-30
 */
public interface ApplyOpenProcessService extends BaseService<ApplyOpenProcess> {

    /**
     * 单个将对象转换为vo
     *
     * @param applyOpenProcess
     * @return
     */
    ApplyOpenProcessVo setVoProperties(ApplyOpenProcess applyOpenProcess);

    /**
     * 批量将对象转换为vo
     *
     * @param applyOpenProcesss
     * @return
     */
    List<ApplyOpenProcessVo> setVoProperties(Collection applyOpenProcesss);

    void doProcess(ApplyOpenProcess process, ApplyOpenRecord record);
}
