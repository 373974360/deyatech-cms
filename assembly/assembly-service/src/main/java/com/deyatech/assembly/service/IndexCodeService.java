package com.deyatech.assembly.service;

import com.deyatech.assembly.entity.IndexCode;
import com.deyatech.assembly.vo.IndexCodeVo;
import com.deyatech.common.base.BaseService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  索引编码规则 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-18
 */
public interface IndexCodeService extends BaseService<IndexCode> {

    /**
     * 单个将对象转换为vo索引编码规则
     *
     * @param indexCode
     * @return
     */
    IndexCodeVo setVoProperties(IndexCode indexCode);

    /**
     * 批量将对象转换为vo索引编码规则
     *
     * @param indexCodes
     * @return
     */
    List<IndexCodeVo> setVoProperties(Collection indexCodes);

    /**
     * 索引码重置
     *
     * @param siteId
     * @param start
     * @param end
     * @return
     */
    boolean reset(String siteId, String start, String end);
}
