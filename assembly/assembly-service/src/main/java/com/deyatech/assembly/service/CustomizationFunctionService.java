package com.deyatech.assembly.service;

import com.deyatech.assembly.entity.CustomizationTableHead;
import com.deyatech.assembly.vo.CustomizationTableHeadVo;
import com.deyatech.assembly.vo.CustomizationTableHeadItemVo;
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
public interface CustomizationFunctionService extends BaseService<CustomizationTableHead> {

    /**
     * 单个将对象转换为vo
     *
     * @param customizationFunction
     * @return
     */
    CustomizationTableHeadVo setVoProperties(CustomizationTableHead customizationFunction);

    /**
     * 批量将对象转换为vo
     *
     * @param customizationFunctions
     * @return
     */
    List<CustomizationTableHeadVo> setVoProperties(Collection customizationFunctions);

    /**
     * 获取所有定制功能
     *
     * @return
     */
    List<CustomizationTableHeadVo> getAllCustomizationFunction();
    /**
     * 获取定制功能
     *
     * @return
     */
    CustomizationTableHeadVo getCustomizationFunction(String name, String type);
    /**
     * 获取表头数据
     *
     * @param type
     * @return
     */
    List<CustomizationTableHeadItemVo> getTableHeadData(String name, String type);


}
