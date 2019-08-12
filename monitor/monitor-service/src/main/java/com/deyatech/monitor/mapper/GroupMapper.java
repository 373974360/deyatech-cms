package com.deyatech.monitor.mapper;

import com.deyatech.monitor.entity.Group;
import com.deyatech.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-07-29
 */
public interface GroupMapper extends BaseMapper<Group> {

    List<Group> getGroupList(@Param("currTime") String currTime);
}
