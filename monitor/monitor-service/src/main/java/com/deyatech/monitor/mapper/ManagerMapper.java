package com.deyatech.monitor.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.monitor.entity.Manager;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.monitor.vo.ManagerVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-07-29
 */
public interface ManagerMapper extends BaseMapper<Manager> {

    /**
     * 翻页检索
     *
     * @param page
     * @param manager
     * @return
     */
    IPage<ManagerVo> pageByManager(@Param("page") IPage<Manager> page, @Param("manager") Manager manager);

}
