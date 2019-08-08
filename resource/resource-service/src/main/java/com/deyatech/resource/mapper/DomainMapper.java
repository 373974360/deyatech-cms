package com.deyatech.resource.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.resource.entity.Domain;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.resource.vo.DomainVo;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
public interface DomainMapper extends BaseMapper<Domain> {

    /**
     * 根据条件翻页查询域名
     *
     * @param page
     * @param domainVo
     * @return
     */
    IPage<DomainVo> pageSelectByDomainVo(@Param("page") Page page, @Param("domainVo") DomainVo domainVo);

    /**
     * 根据条件查询所有域名
     *
     * @param domainVo
     * @return
     */
    Collection<DomainVo> listSelectByDomainVo(@Param("domainVo") DomainVo domainVo);

    /**
     * 根据站群编号统计域名件数
     *
     * @param id
     * @param stationGroupId
     * @param name
     * @return
     */
    long countNameByStationGroupId(@Param("id") String id, @Param("stationGroupId") String stationGroupId, @Param("name") String name);

    /**
     * 修改状态根据编号
     *
     * @param id
     * @param enable
     * @return
     */
    long updateEnableById(@Param("id") String id, @Param("enable") int enable);

    /**
     * 修改状态根据站群编号
     *
     * @param stationGroupId
     * @param enable
     * @return
     */
    long updateEnableByStationGroupId(@Param("stationGroupId") String stationGroupId, @Param("enable") int enable);

    /**
     * 更新主域名标记
     *
     * @param stationGroupId
     * @return
     */
    long updateSignByStationGroupId(@Param("stationGroupId") String stationGroupId, @Param("sign") String sign);

    /**
     * 更新主域名标记
     *
     * @param id
     * @return
     */
    long updateSignById(@Param("id") String id, @Param("sign") String sign);

    /**
     * 获取站群下的主域名
     *
     * @param stationGroupId
     * @return
     */
    DomainVo getMainByByStationGroupId(@Param("stationGroupId") String stationGroupId);

    /**
     * 根据编号检索域名
     *
     * @return
     */
    Domain getDomainById(@Param("id") Serializable id);
}
