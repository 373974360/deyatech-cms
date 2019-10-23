package com.deyatech.resource.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.resource.entity.StationGroupUser;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.resource.vo.StationGroupUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 站点用户关联 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-09-12
 */
public interface StationGroupUserMapper extends BaseMapper<StationGroupUser> {
    /**
     * 所有用户信息
     * @param page
     * @param stationGroupUserVo
     * @return
     */
    IPage<StationGroupUserVo> pageByStationGroupUserVo(@Param("page") IPage<StationGroupUser> page, @Param("stationGroupUserVo") StationGroupUserVo stationGroupUserVo);

    /**
     * 删除站点用户关联根据站点编号
     *
     * @param list
     * @return
     */
    int removeByStationGroupId(List<String> list);
}
