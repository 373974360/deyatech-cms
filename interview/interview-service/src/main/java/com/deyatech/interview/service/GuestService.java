package com.deyatech.interview.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.interview.entity.Guest;
import com.deyatech.interview.vo.GuestVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  访谈嘉宾 服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-28
 */
public interface GuestService extends BaseService<Guest> {

    /**
     * 单个将对象转换为vo访谈嘉宾
     *
     * @param guest
     * @return
     */
    GuestVo setVoProperties(Guest guest);

    /**
     * 批量将对象转换为vo访谈嘉宾
     *
     * @param guests
     * @return
     */
    List<GuestVo> setVoProperties(Collection guests);

    /**
     * 检索嘉宾
     *
     * @param guest
     * @return
     */
    IPage<GuestVo> selectGuestByModelNameJobType(Guest guest);
}
