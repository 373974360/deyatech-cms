package com.deyatech.assembly.service.impl;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.assembly.entity.CustomizationTableHead;
import com.deyatech.assembly.vo.CustomizationTableHeadVo;
import com.deyatech.assembly.mapper.CustomizationFunctionMapper;
import com.deyatech.assembly.service.CustomizationFunctionService;
import com.deyatech.assembly.vo.CustomizationTableHeadItemVo;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.enums.CustomizationTypeEnum;
import com.deyatech.common.exception.BusinessException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-30
 */
@Service
public class CustomizationFunctionServiceImpl extends BaseServiceImpl<CustomizationFunctionMapper, CustomizationTableHead> implements CustomizationFunctionService {
    private ObjectMapper mapper = new ObjectMapper();
    /**
     * 单个将对象转换为vo
     *
     * @param customizationFunction
     * @return
     */
    @Override
    public CustomizationTableHeadVo setVoProperties(CustomizationTableHead customizationFunction){
        CustomizationTableHeadVo customizationFunctionVo = new CustomizationTableHeadVo();
        BeanUtil.copyProperties(customizationFunction, customizationFunctionVo);
        return customizationFunctionVo;
    }

    /**
     * 批量将对象转换为vo
     *
     * @param customizationFunctions
     * @return
     */
    @Override
    public List<CustomizationTableHeadVo> setVoProperties(Collection customizationFunctions){
        List<CustomizationTableHeadVo> customizationFunctionVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(customizationFunctions)) {
            for (Object customizationFunction : customizationFunctions) {
                CustomizationTableHeadVo customizationFunctionVo = new CustomizationTableHeadVo();
                BeanUtil.copyProperties(customizationFunction, customizationFunctionVo);
                customizationFunctionVos.add(customizationFunctionVo);
            }
        }
        return customizationFunctionVos;
    }

    /**
     * 获取所有定制功能
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CustomizationTableHeadVo> getAllCustomizationFunction() {
        String userId = UserContextHelper.getUserId();
        QueryWrapper<CustomizationTableHead> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        // 检索用户的表头顺数据
        List<CustomizationTableHead> allTableHeadList = super.list(queryWrapper);
        // 类型数据映射
        Map<String, CustomizationTableHead> map;
        if (CollectionUtil.isNotEmpty(allTableHeadList)) {
            map = allTableHeadList.stream().collect(Collectors.toMap(CustomizationTableHead::getType, c -> c));
        } else {
            map = new HashMap<>();
        }
        List<CustomizationTableHeadVo> list = new ArrayList<>();
        for (CustomizationTypeEnum ct : CustomizationTypeEnum.values()) {
            list.add(getHeadList(ct.getValue(), ct.getCode(), map.get(ct.getCode())));
        }
        return list;
    }

    /**
     * 获取表头数据
     *
     * @param name
     * @param type
     * @param tableHead
     * @return
     */
    private CustomizationTableHeadVo getHeadList(String name, String type, CustomizationTableHead tableHead) {
        String userId = UserContextHelper.getUserId();
        List<CustomizationTableHeadItemVo> defaultHeadList = CustomizationTableHeadItemVo.getDefault(type);
        List<CustomizationTableHeadItemVo> headList;
        if (Objects.isNull(tableHead)) {
            tableHead = new CustomizationTableHead();
            tableHead.setUserId(userId);
            tableHead.setName(name);
            tableHead.setType(type);
            headList = defaultHeadList;
        } else {
            try {
                JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, CustomizationTableHeadItemVo.class);
                headList = mapper.readValue(tableHead.getData(), javaType);
            } catch (IOException e) {
                e.printStackTrace();
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "排序表头取得失败");
            }
            // 数据库中的件数和配置的件数不同时，删除数据库中的数据，重新排序
            if (CollectionUtil.isNotEmpty(headList) && headList.size() != defaultHeadList.size()) {
                super.removeById(tableHead.getId());
                headList = defaultHeadList;
            }
        }
        CustomizationTableHeadVo tableHeadVo = setVoProperties(tableHead);
        tableHeadVo.setHeadList(headList);
        return tableHeadVo;
    }

    /**
     * 获取定制功能
     *
     * @return
     */
    @Override
    public CustomizationTableHeadVo getCustomizationFunction(String name, String type) {
        QueryWrapper<CustomizationTableHead> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", UserContextHelper.getUserId());
        queryWrapper.eq("type_", type);
        return getHeadList(name, type, super.getOne(queryWrapper));
    }

    /**
     * 获取表头数据
     *
     * @param type
     * @return
     */
    @Override
    public List<CustomizationTableHeadItemVo> getTableHeadData(String name, String type) {
        return getCustomizationFunction(name, type).getHeadList().stream().filter(CustomizationTableHeadItemVo::isShow).collect(Collectors.toList());
    }
}
