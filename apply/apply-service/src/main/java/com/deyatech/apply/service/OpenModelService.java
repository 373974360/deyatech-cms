package com.deyatech.apply.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.apply.entity.OpenModel;
import com.deyatech.apply.vo.OpenModelVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-16
 */
public interface OpenModelService extends BaseService<OpenModel> {

    /**
     * 单个将对象转换为vo
     *
     * @param openModel
     * @return
     */
    OpenModelVo setVoProperties(OpenModel openModel);

    /**
     * 批量将对象转换为vo
     *
     * @param openModels
     * @return
     */
    List<OpenModelVo> setVoProperties(Collection openModels);

    /**
     * 翻页检索
     *
     * @param openModel
     * @return
     */
    IPage<OpenModelVo> pageByOpenModel(OpenModel openModel);
}
