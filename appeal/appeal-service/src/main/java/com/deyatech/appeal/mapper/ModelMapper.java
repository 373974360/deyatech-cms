package com.deyatech.appeal.mapper;

import com.deyatech.appeal.entity.Model;
import com.deyatech.common.base.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-09-24
 */
public interface ModelMapper extends BaseMapper<Model> {

    /**
     * 统计部门模型数量
     *
     * @param departmentId
     * @return
     */
    long countModelByDepartmentId(String departmentId);
}
