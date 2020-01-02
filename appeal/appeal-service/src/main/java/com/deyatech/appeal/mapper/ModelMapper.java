package com.deyatech.appeal.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.appeal.entity.Model;
import com.deyatech.appeal.vo.ModelVo;
import com.deyatech.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 翻页检索
     * @param page
     * @param model
     * @return
     */
    IPage<ModelVo> pageByModel(@Param("page") Page page, @Param("model") Model model);
}
