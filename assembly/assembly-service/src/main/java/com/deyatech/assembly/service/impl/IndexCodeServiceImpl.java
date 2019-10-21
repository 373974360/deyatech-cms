package com.deyatech.assembly.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.assembly.entity.IndexCode;
import com.deyatech.assembly.mapper.IndexCodeMapper;
import com.deyatech.assembly.service.IndexCodeService;
import com.deyatech.assembly.vo.IndexCodeVo;
import com.deyatech.common.base.BaseServiceImpl;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.YesNoEnum;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.feign.ResourceFeign;
import com.deyatech.station.feign.StationFeign;
import com.deyatech.station.vo.TemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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
    @Autowired
    ResourceFeign resourceFeign;
    @Autowired
    AdminFeign adminFeign;

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
        StringBuilder part = new StringBuilder();
        QueryWrapper<IndexCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site_id", siteId);
        IndexCode indexCode = getOne(queryWrapper);
        // 部门社会信用码
        if (YesNoEnum.YES.getCode() == indexCode.getCodeChecked()) {
            RestResult<StationGroup> resultStation = resourceFeign.getStationGroupById(siteId);
            if (Objects.nonNull(resultStation) && Objects.nonNull(resultStation.getData())) {
                StationGroup stationGroup = resultStation.getData();
                RestResult<Department> resultDepartment = adminFeign.getDepartmentById(stationGroup.getDepartmentId());
                if (Objects.nonNull(resultDepartment) && Objects.nonNull(resultDepartment.getData())) {
                    Department department = resultDepartment.getData();
                    part.append(department.getCode());
                    part.append(indexCode.getCodeDelimiter());
                }
            }
        }
        // 固定值
        if (YesNoEnum.YES.getCode() == indexCode.getValueChecked()) {
            part.append(indexCode.getValue());
            part.append(indexCode.getValueDelimiter());
        }
        // 日期哥特式
        if (YesNoEnum.YES.getCode() == indexCode.getFormatChecked()) {
            SimpleDateFormat format = new SimpleDateFormat(indexCode.getFormat());
            part.append(format.format(new Date()));
            part.append(indexCode.getFormatDelimiter());
        }
        // 信息流水号位数
        RestResult<Integer> result = stationFeign.resetTemplateIndex(siteId, start, end, part.toString(), indexCode.getNumber());
        if (Objects.nonNull(result) && result.getData() > 0) {
            int value = result.getData();
            value += 1;
            QueryWrapper<IndexCode> query = new QueryWrapper<>();
            query.eq("site_id", siteId);
            IndexCode ic = getOne(query);
            ic.setNextSerial(String.format("%0" + indexCode.getNumber() + "d", value));
            updateById(ic);
            return true;
        } else {
            return false;
        }
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
