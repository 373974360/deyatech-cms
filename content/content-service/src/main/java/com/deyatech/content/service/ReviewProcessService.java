package com.deyatech.content.service;

import com.deyatech.content.entity.ReviewProcess;
import com.deyatech.content.vo.ReviewProcessVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  内容审核流程 服务类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-14
 */
public interface ReviewProcessService extends BaseService<ReviewProcess> {

    /**
     * 单个将对象转换为vo内容审核流程
     *
     * @param reviewProcess
     * @return
     */
    ReviewProcessVo setVoProperties(ReviewProcess reviewProcess);

    /**
     * 批量将对象转换为vo内容审核流程
     *
     * @param reviewProcesss
     * @return
     */
    List<ReviewProcessVo> setVoProperties(Collection reviewProcesss);
}
