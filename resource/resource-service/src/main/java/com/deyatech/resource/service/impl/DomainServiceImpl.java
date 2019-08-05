package com.deyatech.resource.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.common.enums.EnableEnum;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.resource.entity.Domain;
import com.deyatech.resource.vo.DomainVo;
import com.deyatech.resource.mapper.DomainMapper;
import com.deyatech.resource.service.DomainService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-08-01
 */
@Service
public class DomainServiceImpl extends BaseServiceImpl<DomainMapper, Domain> implements DomainService {

    /**
     * 单个将对象转换为vo
     *
     * @param domain
     * @return
     */
    @Override
    public DomainVo setVoProperties(Domain domain){
        DomainVo domainVo = new DomainVo();
        BeanUtil.copyProperties(domain, domainVo);
        return domainVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param domains
     * @return
     */
    @Override
    public List<DomainVo> setVoProperties(Collection domains){
        List<DomainVo> domainVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(domains)) {
            for (Object domain : domains) {
                DomainVo domainVo = new DomainVo();
                BeanUtil.copyProperties(domain, domainVo);
                domainVos.add(domainVo);
            }
        }
        return domainVos;
    }

    /**
     * 根据条件翻页查询网站
     *
     * @param domainVo
     * @return
     */
    @Override
    public IPage<DomainVo> pageSelectByDomainVo(DomainVo domainVo) {
        return baseMapper.pageSelectByDomainVo(getPageByBean(domainVo), domainVo);
    }

    /**
     * 根据网站编号统计域名件数
     *
     * @param id
     * @param stationGroupId
     * @param name
     * @return
     */
    @Override
    public long countNameByStationGroupId(String id, String stationGroupId, String name) {
        return baseMapper.countNameByStationGroupId(id, stationGroupId, name);
    }

    /**
     * 添加或保存
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(Domain entity) {
        // 更新网站下其他域名为非主域名
        if (YesNoEnum.YES.getCode().toString().equals(entity.getSign())) {
            baseMapper.updateSignByStationGroupId(entity.getStationGroupId(), YesNoEnum.NO.getCode().toString());
        }
        return super.saveOrUpdate(entity);
    }

    /**
     * 修改状态根据编号
     *
     * @param id
     * @param flag
     * @return
     */
    @Override
    public long runOrStopStationById(String id, String flag) {
        if ("run".equals(flag)) {
            return baseMapper.updateEnableById(id, EnableEnum.ENABLE.getCode());
        } else if ("stop".equals(flag)) {
            return baseMapper.updateEnableById(id, EnableEnum.DISABLE.getCode());
        }
        return 0;
    }

    /**
     * 更新主域名
     *
     * @param id
     * @param stationGroupId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public long updateSignByIdAndStationGroupId(String id, String stationGroupId) {
        // 更新网站下其他域名为非主域名
        baseMapper.updateSignByStationGroupId(stationGroupId, YesNoEnum.NO.getCode().toString());
        // 把当前域名更新成主域名
        return baseMapper.updateSignById(id, YesNoEnum.YES.getCode().toString());
    }
}
