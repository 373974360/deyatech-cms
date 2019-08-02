package com.deyatech.resource.service;

import com.deyatech.resource.entity.Domain;
import com.deyatech.resource.vo.DomainVo;
import com.deyatech.common.base.BaseService;
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
}
