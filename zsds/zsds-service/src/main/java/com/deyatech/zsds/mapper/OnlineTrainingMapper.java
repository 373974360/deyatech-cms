package com.deyatech.zsds.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.zsds.entity.OnlineTraining;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.zsds.vo.OnlineTrainingVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 在线培训系统 Mapper 接口
 * </p>
 *
 * @Author csm
 * @since 2019-11-08
 */
public interface OnlineTrainingMapper extends BaseMapper<OnlineTraining> {

    IPage<OnlineTrainingVo> pageByOnlineTrainingVo(@Param("page") IPage<OnlineTraining> page,
                                                   @Param("onlineTrainingVo") OnlineTrainingVo onlineTrainingVo);

    List<OnlineTrainingVo> getOnlineInfoByDate(@Param("dateStr") String dateStr);

    List<OnlineTrainingVo> getOnlineInfoByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<OnlineTrainingVo> getTodayOnlineInfo();
}
