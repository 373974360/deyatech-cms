package com.deyatech.appeal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-09-24
 */
public interface RecordService extends BaseService<Record> {

    /**
     * 单个将对象转换为vo
     *
     * @param record
     * @return
     */
    RecordVo setVoProperties(Record record);

    /**
     * 批量将对象转换为vo
     *
     * @param records
     * @return
     */
    List<RecordVo> setVoProperties(Collection records);


    IPage<RecordVo> pageRecordByBean(Record record,String[] timeFrame);

    /**
     * 根据业务ID得到随机查询码，用于前台页面添加诉求时产生查询码
     *
     * @param modelId
     * @return String
     */
    String getQueryCode(String modelId);

    /**
     * 根据业务ID得到信件编码
     *
     * @param modelId
     * @return String
     */
    String getAppealCode(String modelId);
}
