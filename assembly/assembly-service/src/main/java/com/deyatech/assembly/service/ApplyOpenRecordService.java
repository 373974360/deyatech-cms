package com.deyatech.assembly.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-16
 */
public interface ApplyOpenRecordService extends BaseService<ApplyOpenRecord> {

    /**
     * 单个将对象转换为vo
     *
     * @param applyOpenRecord
     * @return
     */
    ApplyOpenRecordVo setVoProperties(ApplyOpenRecord applyOpenRecord);

    /**
     * 批量将对象转换为vo
     *
     * @param applyOpenRecords
     * @return
     */
    List<ApplyOpenRecordVo> setVoProperties(Collection applyOpenRecords);


    IPage<ApplyOpenRecordVo> pageApplyOpenRecordByBean(ApplyOpenRecord applyOpenRecord, String[] timeFrame);

    /**
     * 根据业务ID得到随机查询码，用于前台页面添加诉求时产生查询码
     *
     * @param configId
     * @return String
     */
    String getQueryCode(String configId);

    /**
     * 根据业务ID得到信件编码
     *
     * @param configId
     * @return String
     */
    String getYsqCode(String configId);
}
