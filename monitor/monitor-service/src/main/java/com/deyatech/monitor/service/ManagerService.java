package com.deyatech.monitor.service;

import com.deyatech.monitor.entity.Manager;
import com.deyatech.monitor.vo.ManagerVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-07-29
 */
public interface ManagerService extends BaseService<Manager> {

    /**
     * 单个将对象转换为vo
     *
     * @param manager
     * @return
     */
    ManagerVo setVoProperties(Manager manager);

    /**
     * 批量将对象转换为vo
     *
     * @param managers
     * @return
     */
    List<ManagerVo> setVoProperties(Collection managers);
}
