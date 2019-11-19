package com.deyatech.station.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.station.vo.ResourceManagementVo;
import com.deyatech.station.vo.TemplateVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-11-13
 */
public interface ResourceManagementMapper extends BaseMapper {

    /**
     * 翻页检索资源
     *
     * @param page
     * @param resource
     * @return
     */
    IPage<TemplateVo> pageByResourceManagement(@Param("page") IPage page, @Param("resource") ResourceManagementVo resource);
}
