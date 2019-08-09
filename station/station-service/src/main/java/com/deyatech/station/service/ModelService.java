package com.deyatech.station.service;

import com.deyatech.station.entity.Model;
import com.deyatech.station.vo.ModelVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  内容模型 服务类
 * </p>
 *
 * @Author csm.
 * @since 2019-08-06
 */
public interface ModelService extends BaseService<Model> {

    /**
     * 单个将对象转换为vo内容模型
     *
     * @param model
     * @return
     */
    ModelVo setVoProperties(Model model);

    /**
     * 批量将对象转换为vo内容模型
     *
     * @param models
     * @return
     */
    List<ModelVo> setVoProperties(Collection models);

    /**
     * 判断Model对象中文名称是否存在
     *
     * @param model
     * @return
     */
    boolean checkNameExist(Model model);

    /**
     * 判断Model对象英文名称是否存在
     *
     * @param model
     * @return
     */
    boolean checkEnglishNameExist(Model model);
}
