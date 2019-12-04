package com.deyatech.assembly.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpStatus;
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
import com.deyatech.common.exception.BusinessException;
import com.deyatech.resource.entity.StationGroup;
import com.deyatech.resource.feign.ResourceFeign;
import com.deyatech.station.feign.StationFeign;
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
     * 获取下一个索引码根据站点ID
     *
     * @param siteId
     * @return
     */
    @Override
    public synchronized String getNextIndexCodeBySiteId(String siteId) {
        QueryWrapper<IndexCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site_id", siteId);
        IndexCode indexCode = getOne(queryWrapper);
        if (Objects.isNull(indexCode)) {
            return null;
        }
        String nextIndexCode = getFixedPart(siteId, indexCode) + indexCode.getNextSerial();
        int value = Integer.parseInt(indexCode.getNextSerial());
        value += 1;
        indexCode.setNextSerial(String.format("%0" + indexCode.getNumber() + "d", value));
        updateById(indexCode);
        return nextIndexCode;
    }

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
        QueryWrapper<IndexCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site_id", siteId);
        IndexCode indexCode = getOne(queryWrapper);
        // 信息流水号位数
        RestResult<String> result = stationFeign.resetTemplateIndexCode(siteId, start, end, getFixedPart(siteId, indexCode), indexCode.getNumber());
        if (Objects.isNull(result)) {
            return false;
        }
        int value = Integer.parseInt(result.getData());
        if (value > 0) {
            value += 1;
            indexCode.setNextSerial(String.format("%0" + indexCode.getNumber() + "d", value));
            updateById(indexCode);
            return true;
        } else {
            return false;
        }
    }

    private String getFixedPart(String siteId, IndexCode indexCode) {
        StringBuilder part = new StringBuilder();
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
        return part.toString();
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
