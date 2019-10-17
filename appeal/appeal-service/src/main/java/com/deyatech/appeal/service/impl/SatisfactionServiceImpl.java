package com.deyatech.appeal.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.appeal.entity.Satisfaction;
import com.deyatech.appeal.vo.SatisfactionVo;
import com.deyatech.appeal.mapper.SatisfactionMapper;
import com.deyatech.appeal.service.SatisfactionService;
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
 * @since 2019-09-24
 */
@Service
public class SatisfactionServiceImpl extends BaseServiceImpl<SatisfactionMapper, Satisfaction> implements SatisfactionService {

    /**
     * 单个将对象转换为vo
     *
     * @param satisfaction
     * @return
     */
    @Override
    public SatisfactionVo setVoProperties(Satisfaction satisfaction){
        SatisfactionVo satisfactionVo = new SatisfactionVo();
        BeanUtil.copyProperties(satisfaction, satisfactionVo);
        return satisfactionVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param satisfactions
     * @return
     */
    @Override
    public List<SatisfactionVo> setVoProperties(Collection satisfactions){
        List<SatisfactionVo> satisfactionVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(satisfactions)) {
            for (Object satisfaction : satisfactions) {
                SatisfactionVo satisfactionVo = new SatisfactionVo();
                BeanUtil.copyProperties(satisfaction, satisfactionVo);
                satisfactionVos.add(satisfactionVo);
            }
        }
        return satisfactionVos;
    }

    @Override
    public IPage pageBySatisfaction(Satisfaction satisfaction) {
        QueryWrapper<Satisfaction> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(satisfaction.getName())){
            queryWrapper.and(i -> i
                    .like("name", satisfaction.getName())
                    .or()
                    .like("score", satisfaction.getName())
            );
        }
        IPage pages = super.page(this.getPageByBean(satisfaction), queryWrapper);
        pages.setRecords(setVoProperties(pages.getRecords()));
        return pages;
    }
}
