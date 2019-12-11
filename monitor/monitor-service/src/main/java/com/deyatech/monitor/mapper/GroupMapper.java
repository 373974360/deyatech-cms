package com.deyatech.monitor.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.monitor.entity.Group;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.monitor.vo.GroupVo;
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

    /**
     * 翻页检索
     *
     * @param page
     * @param group
     * @return
     */
    IPage<GroupVo> pageByGroup(@Param("page") IPage<Group> page, @Param("group") Group group);
}
