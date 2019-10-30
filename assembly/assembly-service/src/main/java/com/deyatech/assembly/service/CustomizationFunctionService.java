package com.deyatech.assembly.service;

import com.deyatech.assembly.entity.CustomizationFunction;
import com.deyatech.assembly.vo.CustomizationFunctionVo;
import com.deyatech.assembly.vo.CustomizationTableHeadVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-30
 */
public interface CustomizationFunctionService extends BaseService<CustomizationFunction> {

    /**
     * 单个将对象转换为vo
     *
     * @param customizationFunction
     * @return
     */
    CustomizationFunctionVo setVoProperties(CustomizationFunction customizationFunction);

    /**
     * 批量将对象转换为vo
     *
     * @param customizationFunctions
     * @return
     */
    List<CustomizationFunctionVo> setVoProperties(Collection customizationFunctions);

    /**
     * 获取所有定制功能
     *
     * @return
     */
    List<CustomizationFunctionVo> getAllCustomizationFunction();

    /**
     * 获取表头数据
     *
     * @param type
     * @return
     */
    List<CustomizationTableHeadVo> getTableHeadData(String type);
}
