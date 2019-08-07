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
     * 根据站群编号统计域名件数
     *
     * @param id
     * @param stationGroupId
     * @param name
     * @return
     */
    long countNameByStationGroupId(String id, String stationGroupId, String name);

    /**
     * 启用停用域名
     *
     * @param id
     * @param flag
     * @return
     */
    long runOrStopStationById(String id, String flag);

    boolean saveUpdate (Domain entity);

    /**
     * 更新或保存
     * @param entity
     * @return
     */
    boolean saveOrUpdateAndNginx(Domain entity);

    /**
     * 更新主域名
     *
     * @param id
     * @param stationGroupId
     * @return
     */
    long updateSignByIdAndStationGroupId(String id, String stationGroupId);

    /**
     * 获取站群下的主域名
     *
     * @param stationGroupId
     * @return
     */
    DomainVo getMainByByStationGroupId(String stationGroupId);

    /**
     * 删除站点配置文件
     *
     * @param stationGroup
     */
    void deleteNginxConfig(StationGroup stationGroup);

    /**
     * 禁用站点配置文件
     *
     * @param stationGroup
     */
    void disableNginxConfig(StationGroup stationGroup);

    /**
     * 启用站点配置文件
     */
    void enableNginxConfig(StationGroup stationGroup);

    /**
     * 根据编号检索域名
     *
     * @param id
     * @return
     */
    Domain getById(Serializable id);

    /**
     * 从配置文件中添加或移除域名
     *
     * @param stationGroupEnglishName
     * @param domainName
     * @param flag
     */
    void addOrRemoveDomainFromConfig(String stationGroupEnglishName, String domainName, String flag);

    /**
     * 删除域名和配置
     *
     * @param idList
     * @param maps
     * @return
     */
    boolean removeDomainsAndConfig(Collection<String> idList, Map<String, Domain> maps);

    /**
     * 修改状态根据站群编号
     *
     * @param stationGroupId
     * @param enable
     * @return
     */
    long updateEnableByStationGroupId(String stationGroupId, int enable);

    /**
     * 获取nginx端口
     *
     * @return
     */
    String getNginxPort();
}
