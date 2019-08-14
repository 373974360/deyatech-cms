package com.deyatech.monitor.service;

import com.deyatech.monitor.entity.Group;
import com.deyatech.monitor.vo.GroupVo;
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
public interface GroupService extends BaseService<Group> {

    /**
     * 单个将对象转换为vo
     *
     * @param group
     * @return
     */
    GroupVo setVoProperties(Group group);

    /**
     * 批量将对象转换为vo
     *
     * @param groups
     * @return
     */
    List<GroupVo> setVoProperties(Collection groups);


    /**
     * 查询空闲任务并且已到执行时间
     * */
    List<Group> getGroupList(String currTime);
}
