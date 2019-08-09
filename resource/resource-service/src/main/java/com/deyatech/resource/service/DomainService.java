package com.deyatech.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.base.BaseService;
import com.deyatech.resource.entity.Domain;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.vo.DomainVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
     * 根据条件翻页查询域名
     *
     * @param domainVo
     * @return
     */
    IPage<DomainVo> pageSelectByDomainVo(DomainVo domainVo);

    /**
     * 根据条件查询所有域名
     *
     * @param domainVo
     * @return
     */
    Collection<DomainVo> listSelectByDomainVo(DomainVo domainVo);

    /**
     * 统计域名件数
     *
     * @param id
     * @param name
     * @return
     */
    long countName(String id, String name);

    /**
     * 统计英文件数
     *
     * @param id
     * @param englishName
     * @return
     */
    long countEnglishName(String id, String englishName);

    /**
     * 更新或保存
     * @param entity
     * @return
     */
    boolean saveOrUpdateAndNginx(Domain entity);

    /**
     * 根据编号检索域名
     *
     * @param id
     * @return
     */
    Domain getById(Serializable id);

    /**
     * 启用停用域名
     *
     * @param id
     * @param flag
     * @return
     */
    long runOrStopDomainById(String id, String flag);

    /**
     * 删除域名和配置
     *
     * @param idList
     * @param maps
     * @return
     */
    boolean removeDomainsAndConfig(Collection<String> idList, Map<String, Domain> maps);

    /**
     * 更新 站群下所有域名 nginx 配置
     *
     * @param stationGroupId
     */
    void updateStationGroupNginxConfigAndPage(String stationGroupId, String oldStationGroupEnglishName);

    /**
     * 启用停用 站群下所有域名 nginx 配置
     *
     * @param stationGroupId
     */
    void runOrStopStationGroupNginxConfigAndPage(String stationGroupId);

    /**
     * 删除 站群下所有域名 nginx 配置
     *
     * @param ids
     * @param maps
     */
    void removeStationGroupNginxConfigAndPage(List<String> ids, Map<String, StationGroup> maps);
}
