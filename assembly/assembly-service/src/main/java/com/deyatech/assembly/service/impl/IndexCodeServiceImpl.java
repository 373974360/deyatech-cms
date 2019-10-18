package com.deyatech.assembly.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.assembly.entity.IndexCode;
import com.deyatech.assembly.mapper.IndexCodeMapper;
import com.deyatech.assembly.service.IndexCodeService;
import com.deyatech.assembly.vo.IndexCodeVo;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.vo.TemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 索引编码规则 服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-18
 */
@Service
public class IndexCodeServiceImpl extends BaseServiceImpl<IndexCodeMapper, IndexCode> implements IndexCodeService {

    @Autowired
    StationFeign stationFeign;

    /**
     * 索引码重置
     *
     * @param siteId
     * @param start
     * @param end
     * @return
     */
    @Override
    public boolean reset(String siteId, String start, String end) {
        RestResult<List<TemplateVo>> result = stationFeign.getResetTemplate(siteId, start, end);
        if (Objects.nonNull(result) && CollectionUtil.isNotEmpty(result.getData())) {
            List<TemplateVo> templateVos = result.getData();


            QueryWrapper<IndexCode> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("site_id", siteId);
            IndexCode indexCode = getOne(queryWrapper);
            if (YesNoEnum.YES.getCode() == indexCode.getCodeChecked()) {

            }

        }
        return true;
    }

    /**
     * 单个将对象转换为vo索引编码规则
     *
     * @param indexCode
     * @return
     */
    @Override
    public IndexCodeVo setVoProperties(IndexCode indexCode){
        IndexCodeVo indexCodeVo = new IndexCodeVo();
        BeanUtil.copyProperties(indexCode, indexCodeVo);
        return indexCodeVo;
    }

    /**
     * 批量将对象转换为vo索引编码规则
     *
     * @param indexCodes
     * @return
     */
    @Override
    public List<IndexCodeVo> setVoProperties(Collection indexCodes){
        List<IndexCodeVo> indexCodeVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(indexCodes)) {
            for (Object indexCode : indexCodes) {
                IndexCodeVo indexCodeVo = new IndexCodeVo();
                BeanUtil.copyProperties(indexCode, indexCodeVo);
                indexCodeVos.add(indexCodeVo);
            }
        }
        return indexCodeVos;
    }


}
