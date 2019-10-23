package com.deyatech.resource.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.common.base.BaseMapper;
import com.deyatech.resource.entity.Domain;
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
     * 统计域名件数
     *
     * @param id
     * @param name
     * @return
     */
    long countName(@Param("id") String id, @Param("name") String name);

    /**
     * 统计英文件数
     *
     * @param id
     * @param englishName
     * @return
     */
    long countEnglishName(@Param("id") String id, @Param("englishName") String englishName);

    /**
     * 修改状态根据编号
     *
     * @param id
     * @param enable
     * @return
     */
    long updateEnableById(@Param("id") String id, @Param("enable") int enable);
    long updateEnableByIds(@Param("list") Collection<String> list, @Param("enable") int enable);

    /**
     * 修改状态根据站点编号
     *
     * @param stationGroupId
     * @param enable
     * @return
     */
    long updateEnableByStationGroupId(@Param("stationGroupId") String stationGroupId, @Param("enable") int enable);

    /**
     * 根据编号检索域名
     *
     * @return
     */
    Domain getDomainById(@Param("id") Serializable id);

    /**
     * 统计站点下的域名
     *
     * @param stationGroupId
     * @return
     */
    long countDomainByStationGroupId(@Param("stationGroupId") String stationGroupId);

    /**
     * 更新域名
     *
     * @param domain
     * @return
     */
    long updateDomainById(Domain domain);

    /**
     * 根据编号检索域名
     *
     * @param list
     * @return
     */
    Collection<DomainVo> selectDomainByIds(@Param("list") Collection<String> list);

    /**
     * 检索站点根据站点编号检索
     *
     * @param stationGroupId
     * @return
     */
    Collection<DomainVo> selectDomainByStationGroupId(@Param("stationGroupId") String stationGroupId);
}
