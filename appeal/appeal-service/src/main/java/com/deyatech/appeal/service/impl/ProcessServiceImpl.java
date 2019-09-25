package com.deyatech.appeal.service.impl;

import com.deyatech.appeal.entity.Process;
import com.deyatech.appeal.vo.ProcessVo;
import com.deyatech.appeal.mapper.ProcessMapper;
import com.deyatech.appeal.service.ProcessService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-09-24
 */
@Service
public class ProcessServiceImpl extends BaseServiceImpl<ProcessMapper, Process> implements ProcessService {

    /**
     * 单个将对象转换为vo
     *
     * @param process
     * @return
     */
    @Override
    public ProcessVo setVoProperties(Process process){
        ProcessVo processVo = new ProcessVo();
        BeanUtil.copyProperties(process, processVo);
        return processVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param processs
     * @return
     */
    @Override
    public List<ProcessVo> setVoProperties(Collection processs){
        List<ProcessVo> processVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(processs)) {
            for (Object process : processs) {
                ProcessVo processVo = new ProcessVo();
                BeanUtil.copyProperties(process, processVo);
                processVos.add(processVo);
            }
        }
        return processVos;
    }
}
