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
}
