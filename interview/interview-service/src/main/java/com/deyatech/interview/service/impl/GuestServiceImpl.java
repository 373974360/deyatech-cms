package com.deyatech.interview.service.impl;

import com.deyatech.interview.entity.Guest;
import com.deyatech.interview.vo.GuestVo;
import com.deyatech.interview.mapper.GuestMapper;
import com.deyatech.interview.service.GuestService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;

/**
 * <p>
 * 访谈嘉宾 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-26
 */
@Service
public class GuestServiceImpl extends BaseServiceImpl<GuestMapper, Guest> implements GuestService {

    /**
     * 单个将对象转换为vo访谈嘉宾
     *
     * @param guest
     * @return
     */
    @Override
    public GuestVo setVoProperties(Guest guest){
        GuestVo guestVo = new GuestVo();
        BeanUtil.copyProperties(guest, guestVo);
        return guestVo;
    }

    /**
     * 批量将对象转换为vo访谈嘉宾
     *
     * @param guests
     * @return
     */
    @Override
    public List<GuestVo> setVoProperties(Collection guests){
        List<GuestVo> guestVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(guests)) {
            for (Object guest : guests) {
                GuestVo guestVo = new GuestVo();
                BeanUtil.copyProperties(guest, guestVo);
                guestVos.add(guestVo);
            }
        }
        return guestVos;
    }
}
