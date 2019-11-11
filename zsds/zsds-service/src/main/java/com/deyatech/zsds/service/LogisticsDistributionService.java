package com.deyatech.zsds.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.zsds.entity.LogisticsDistribution;
import com.deyatech.zsds.vo.LogisticsDistributionVo;
import com.deyatech.common.base.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  物流配送体系 服务类
 * </p>
 *
 * @Author csm
 * @since 2019-11-08
 */
public interface LogisticsDistributionService extends BaseService<LogisticsDistribution> {

    /**
     * 单个将对象转换为vo物流配送体系
     *
     * @param logisticsDistribution
     * @return
     */
    LogisticsDistributionVo setVoProperties(LogisticsDistribution logisticsDistribution);

    /**
     * 批量将对象转换为vo物流配送体系
     *
     * @param logisticsDistributions
     * @return
     */
    List<LogisticsDistributionVo> setVoProperties(Collection logisticsDistributions);

    IPage<LogisticsDistributionVo> pageByLogisticsDistributionVo(LogisticsDistributionVo logisticsDistributionVo);

    /**
     * 上传Excel
     * @param file
     * @return
     */
    Map importExcel(MultipartFile file);

    List<LogisticsDistributionVo> getLogisticsInfoByDate(String dateStr);

    List<LogisticsDistributionVo> getLogisticsInfoByDateRange(String startDate, String endDate);

    List<LogisticsDistributionVo> getTodayLogisticsInfo();
}
