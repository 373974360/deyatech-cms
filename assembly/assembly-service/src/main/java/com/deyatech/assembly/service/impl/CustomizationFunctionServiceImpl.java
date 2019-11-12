package com.deyatech.assembly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deyatech.assembly.entity.CustomizationFunction;
import com.deyatech.assembly.vo.CustomizationFunctionVo;
import com.deyatech.assembly.mapper.CustomizationFunctionMapper;
import com.deyatech.assembly.service.CustomizationFunctionService;
import com.deyatech.assembly.vo.CustomizationTableHeadVo;
import com.deyatech.common.base.BaseServiceImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.enums.CustomizationTypeEnum;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

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
public class CustomizationFunctionServiceImpl extends BaseServiceImpl<CustomizationFunctionMapper, CustomizationFunction> implements CustomizationFunctionService {
    private ObjectMapper mapper = new ObjectMapper();
    /**
     * 单个将对象转换为vo
     *
     * @param customizationFunction
     * @return
     */
    @Override
    public CustomizationFunctionVo setVoProperties(CustomizationFunction customizationFunction){
        CustomizationFunctionVo customizationFunctionVo = new CustomizationFunctionVo();
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
    public List<CustomizationFunctionVo> setVoProperties(Collection customizationFunctions){
        List<CustomizationFunctionVo> customizationFunctionVos = CollectionUtil.newArrayList();
        if (CollectionUtil.isNotEmpty(customizationFunctions)) {
            for (Object customizationFunction : customizationFunctions) {
                CustomizationFunctionVo customizationFunctionVo = new CustomizationFunctionVo();
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
    public List<CustomizationFunctionVo> getAllCustomizationFunction() {
        String userId = UserContextHelper.getUserId();
        QueryWrapper<CustomizationFunction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<CustomizationFunction> dbList = super.list(queryWrapper);
        Map<String, CustomizationFunction> map = new HashMap<>();
        if (CollectionUtil.isNotEmpty(dbList)) {
            map = dbList.stream().collect(Collectors.toMap(CustomizationFunction::getType, c -> c));
        }
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, CustomizationTableHeadVo.class);
        List<CustomizationFunctionVo> list = new ArrayList<>();
        List<CustomizationTableHeadVo> headList = null;
        for (CustomizationTypeEnum ct : CustomizationTypeEnum.values()) {
            CustomizationFunction cf = map.get(ct.getCode());
            if (Objects.isNull(cf)) {
                cf = new CustomizationFunction();
                cf.setUserId(userId);
                cf.setName(ct.getValue());
                cf.setType(ct.getCode());
                String data = "[]";
                try {
                    data = mapper.writeValueAsString(CustomizationTableHeadVo.getDefault(ct.getCode()));
                } catch (Exception e) {
                }
                cf.setData(data);
            }
            try {
                headList = mapper.readValue(cf.getData(), javaType);
            } catch (IOException e) {
            }
            CustomizationFunctionVo vo = setVoProperties(cf);
            vo.setHeadList(headList);
            list.add(vo);
        }
        return list;
    }

    /**
     * 获取表头数据
     *
     * @param type
     * @return
     */
    @Override
    public List<CustomizationTableHeadVo> getTableHeadData(String type) {
        return getCustomizationFunction(type).getHeadList().stream().filter(CustomizationTableHeadVo::isShow).collect(Collectors.toList());
    }

    /**
     * 获取定制功能
     *
     * @return
     */
    @Override
    public CustomizationFunctionVo getCustomizationFunction(String type) {
        QueryWrapper<CustomizationFunction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", UserContextHelper.getUserId());
        queryWrapper.eq("type_", type);
        CustomizationFunction customizationFunction = super.getOne(queryWrapper);
        List<CustomizationTableHeadVo> list = null;
        if (Objects.nonNull(customizationFunction)) {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, CustomizationTableHeadVo.class);
            try {
                list = mapper.readValue(customizationFunction.getData(), javaType);
            } catch (IOException e) {
                list = new ArrayList<>();
            }
        } else {
            list = CustomizationTableHeadVo.getDefault(type);
        }
        CustomizationFunctionVo customizationFunctionVo = setVoProperties(customizationFunction);
        customizationFunctionVo.setHeadList(list);
        return customizationFunctionVo;
    }
}
