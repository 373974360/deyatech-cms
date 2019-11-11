package com.deyatech.zsds.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.zsds.entity.AgriculturalProductsUpDown;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.zsds.vo.AgriculturalProductsUpDownVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 农产品上行/下行 Mapper 接口
 * </p>
 *
 * @Author csm
 * @since 2019-11-08
 */
public interface AgriculturalProductsUpDownMapper extends BaseMapper<AgriculturalProductsUpDown> {

    IPage<AgriculturalProductsUpDownVo> pageByAgriculturalProductsUpDownVo(@Param("page") IPage<AgriculturalProductsUpDown> page,
                                                                           @Param("agriculturalProductsUpDownVo") AgriculturalProductsUpDownVo agriculturalProductsUpDownVo);

    List<AgriculturalProductsUpDownVo> getAgriculturalInfoByDate(@Param("dateStr") String dateStr);

    List<AgriculturalProductsUpDownVo> getAgriculturalInfoByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<AgriculturalProductsUpDownVo> getTodayAgriculturalInfo();
}
