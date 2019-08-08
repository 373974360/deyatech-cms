package com.deyatech.station.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.station.entity.Model;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.vo.ModelVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 内容模型 Mapper 接口
 * </p>
 *
 * @Author csm.
 * @since 2019-08-06
 */
public interface ModelMapper extends BaseMapper<Model> {

    /**
     * 分页查询
     * @param page
     * @param model
     * @return
     */
    IPage<Model> pageByBean(@Param("page") Page<Model> page, @Param("model") Model model);
}
