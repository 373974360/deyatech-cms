package com.deyatech.apply.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.apply.entity.OpenModel;
import com.deyatech.apply.vo.OpenModelVo;
import com.deyatech.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-10-16
 */
public interface OpenModelMapper extends BaseMapper<OpenModel> {

    IPage<OpenModelVo> pageByOpenModel(@Param("page") Page page, @Param("model") OpenModel model);
}
