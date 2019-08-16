package com.deyatech.content.service.impl;

import com.deyatech.content.entity.ReviewProcess;
import com.deyatech.content.vo.ReviewProcessVo;
import com.deyatech.content.mapper.ReviewProcessMapper;
import com.deyatech.content.service.ReviewProcessService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 内容审核流程 服务实现类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-14
 */
@Service
public class ReviewProcessServiceImpl extends BaseServiceImpl<ReviewProcessMapper, ReviewProcess> implements ReviewProcessService {

    /**
     * 单个将对象转换为vo内容审核流程
     *
     * @param reviewProcess
     * @return
     */
    @Override
    public ReviewProcessVo setVoProperties(ReviewProcess reviewProcess){
        ReviewProcessVo reviewProcessVo = new ReviewProcessVo();
        BeanUtil.copyProperties(reviewProcess, reviewProcessVo);
        return reviewProcessVo;
    }

    /**
     * 批量将对象转换为vo内容审核流程
     *
     * @param reviewProcesss
     * @return
     */
    @Override
    public List<ReviewProcessVo> setVoProperties(Collection reviewProcesss){
        List<ReviewProcessVo> reviewProcessVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(reviewProcesss)) {
            for (Object reviewProcess : reviewProcesss) {
                ReviewProcessVo reviewProcessVo = new ReviewProcessVo();
                BeanUtil.copyProperties(reviewProcess, reviewProcessVo);
                reviewProcessVos.add(reviewProcessVo);
            }
        }
        return reviewProcessVos;
    }
}
