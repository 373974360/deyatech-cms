package com.deyatech.assembly.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.assembly.entity.ApplyOpenModel;
import com.deyatech.assembly.vo.ApplyOpenModelVo;
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
public interface ApplyOpenModelMapper extends BaseMapper<ApplyOpenModel> {

    IPage<ApplyOpenModelVo> pageByApplyOpenModel(@Param("page") Page page, @Param("model") ApplyOpenModel model);
}
