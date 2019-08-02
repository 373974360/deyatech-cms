package com.deyatech.resource.service.impl;

import com.deyatech.resource.entity.Domain;
import com.deyatech.resource.vo.DomainVo;
import com.deyatech.resource.mapper.DomainMapper;
import com.deyatech.resource.service.DomainService;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Service;
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
}
