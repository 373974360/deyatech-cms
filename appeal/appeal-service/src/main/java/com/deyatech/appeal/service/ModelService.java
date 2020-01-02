package com.deyatech.appeal.service;

import com.deyatech.appeal.entity.Model;
import com.deyatech.appeal.vo.ModelVo;
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
public interface ModelService extends BaseService<Model> {

    /**
     * 单个将对象转换为vo
     *
     * @param model
     * @return
     */
    ModelVo setVoProperties(Model model);

    /**
     * 批量将对象转换为vo
     *
     * @param models
     * @return
     */
    List<ModelVo> setVoProperties(Collection models);

    /**
     * 返回
     * @param departmentIds
     * @return
     */
    long countModelByDepartmentId(List<String> departmentIds);
}
