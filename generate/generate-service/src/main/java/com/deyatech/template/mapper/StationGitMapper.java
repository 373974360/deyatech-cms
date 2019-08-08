package com.deyatech.template.mapper;

import com.deyatech.template.entity.StationGit;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.template.vo.StationGitVo;

import java.util.List;

/**
 * <p>
 * 站点git模板地址信息 Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-08-06
 */
public interface StationGitMapper extends BaseMapper<StationGit> {

    List<StationGitVo> listByStationGroupAndStationGit();

}
