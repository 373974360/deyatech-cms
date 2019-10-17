package com.deyatech.assembly.service;

import com.deyatech.assembly.entity.ApplyOpenModel;
import com.deyatech.assembly.vo.ApplyOpenModelVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-16
 */
public interface ApplyOpenModelService extends BaseService<ApplyOpenModel> {

    /**
     * 单个将对象转换为vo
     *
     * @param applyOpenModel
     * @return
     */
    ApplyOpenModelVo setVoProperties(ApplyOpenModel applyOpenModel);

    /**
     * 批量将对象转换为vo
     *
     * @param applyOpenModels
     * @return
     */
    List<ApplyOpenModelVo> setVoProperties(Collection applyOpenModels);
}
