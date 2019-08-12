package com.deyatech.monitor.service.impl;

import com.deyatech.monitor.entity.Manager;
import com.deyatech.monitor.vo.ManagerVo;
import com.deyatech.monitor.mapper.ManagerMapper;
import com.deyatech.monitor.service.ManagerService;
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
 * @since 2019-07-29
 */
@Service
public class ManagerServiceImpl extends BaseServiceImpl<ManagerMapper, Manager> implements ManagerService {

    /**
     * 单个将对象转换为vo
     *
     * @param manager
     * @return
     */
    @Override
    public ManagerVo setVoProperties(Manager manager){
        ManagerVo managerVo = new ManagerVo();
        BeanUtil.copyProperties(manager, managerVo);
        return managerVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param managers
     * @return
     */
    @Override
    public List<ManagerVo> setVoProperties(Collection managers){
        List<ManagerVo> managerVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(managers)) {
            for (Object manager : managers) {
                ManagerVo managerVo = new ManagerVo();
                BeanUtil.copyProperties(manager, managerVo);
                managerVos.add(managerVo);
            }
        }
        return managerVos;
    }
}
