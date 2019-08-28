package com.deyatech.interview.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.interview.entity.Guest;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.interview.vo.GuestVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 访谈嘉宾 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-08-28
 */
public interface GuestMapper extends BaseMapper<Guest> {

    /**
     * 检索嘉宾
     *
     * @param page
     * @param guest
     * @return
     */
    IPage<GuestVo> selectGuestByModelNameJobType(@Param("page") IPage<Guest> page, @Param("guest") Guest guest);
}
