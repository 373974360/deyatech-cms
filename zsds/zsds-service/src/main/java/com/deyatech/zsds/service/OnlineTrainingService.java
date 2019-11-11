package com.deyatech.zsds.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.zsds.entity.OnlineTraining;
import com.deyatech.zsds.vo.OnlineTrainingVo;
import com.deyatech.common.base.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  在线培训系统 服务类
 * </p>
 *
 * @Author csm
 * @since 2019-11-08
 */
public interface OnlineTrainingService extends BaseService<OnlineTraining> {

    /**
     * 单个将对象转换为vo在线培训系统
     *
     * @param onlineTraining
     * @return
     */
    OnlineTrainingVo setVoProperties(OnlineTraining onlineTraining);

    /**
     * 批量将对象转换为vo在线培训系统
     *
     * @param onlineTrainings
     * @return
     */
    List<OnlineTrainingVo> setVoProperties(Collection onlineTrainings);

    IPage<OnlineTrainingVo> pageByOnlineTrainingVo(OnlineTrainingVo onlineTrainingVo);

    /**
     * 上传Excel
     * @param file
     * @return
     */
    Map importExcel(MultipartFile file);

    List<OnlineTrainingVo> getOnlineInfoByDate(String dateStr);

    List<OnlineTrainingVo> getOnlineInfoByDateRange(String startDate, String endDate);

    List<OnlineTrainingVo> getTodayOnlineInfo();
}
