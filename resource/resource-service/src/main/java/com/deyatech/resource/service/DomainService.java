package com.deyatech.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseService;
import com.deyatech.resource.entity.Domain;
import com.deyatech.resource.vo.DomainVo;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
public interface DomainService extends BaseService<Domain> {

    /**
     * 单个将对象转换为vo
     *
     * @param domain
     * @return
     */
    DomainVo setVoProperties(Domain domain);

    /**
     * 批量将对象转换为vo
     *
     * @param domains
     * @return
     */
    List<DomainVo> setVoProperties(Collection domains);

    /**
     * 根据条件翻页查询网站
     *
     * @param domainVo
     * @return
     */
    IPage<DomainVo> pageSelectByDomainVo(DomainVo domainVo);

    /**
     * 根据网站编号统计域名件数
     *
     * @param id
     * @param stationGroupId
     * @param name
     * @return
     */
    long countNameByStationGroupId(String id, String stationGroupId, String name);

    /**
     * 修改状态根据编号
     *
     * @param id
     * @param flag
     * @return
     */
    long runOrStopStationById(String id, String flag);

    /**
     * 更新或保存
     * @param entity
     * @return
     */
    boolean saveOrUpdate(Domain entity);

    /**
     * 更新主域名
     *
     * @param id
     * @param stationGroupId
     * @return
     */
    long updateSignByIdAndStationGroupId(String id, String stationGroupId);
}
