package com.deyatech.zsds.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.zsds.entity.LogisticsDistribution;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.zsds.vo.LogisticsDistributionVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 物流配送体系 Mapper 接口
 * </p>
 *
 * @Author csm
 * @since 2019-11-08
 */
public interface LogisticsDistributionMapper extends BaseMapper<LogisticsDistribution> {

    IPage<LogisticsDistributionVo> pageByLogisticsDistributionVo(@Param("page") IPage<LogisticsDistribution> page,
                                                                 @Param("logisticsDistributionVo") LogisticsDistributionVo logisticsDistributionVo);

    List<LogisticsDistributionVo> getLogisticsInfoByDate(@Param("dateStr") String dateStr);

    List<LogisticsDistributionVo> getLogisticsInfoByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<LogisticsDistributionVo> getTodayLogisticsInfo();
}
