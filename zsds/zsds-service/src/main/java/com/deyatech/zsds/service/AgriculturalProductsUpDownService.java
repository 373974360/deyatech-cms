package com.deyatech.zsds.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.zsds.entity.AgriculturalProductsUpDown;
import com.deyatech.zsds.vo.AgriculturalProductsUpDownVo;
import com.deyatech.common.base.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  农产品上行/下行 服务类
 * </p>
 *
 * @Author csm
 * @since 2019-11-08
 */
public interface AgriculturalProductsUpDownService extends BaseService<AgriculturalProductsUpDown> {

    /**
     * 单个将对象转换为vo农产品上行/下行
     *
     * @param agriculturalProductsUpDown
     * @return
     */
    AgriculturalProductsUpDownVo setVoProperties(AgriculturalProductsUpDown agriculturalProductsUpDown);

    /**
     * 批量将对象转换为vo农产品上行/下行
     *
     * @param agriculturalProductsUpDowns
     * @return
     */
    List<AgriculturalProductsUpDownVo> setVoProperties(Collection agriculturalProductsUpDowns);

    IPage<AgriculturalProductsUpDownVo> pageByAgriculturalProductsUpDownVo(AgriculturalProductsUpDownVo agriculturalProductsUpDownVo);

    /**
     * 上传Excel
     * @param file
     * @return
     */
    Map importExcel(MultipartFile file);

    List<AgriculturalProductsUpDownVo> getAgriculturalInfoByDate(String dateStr);

    List<AgriculturalProductsUpDownVo> getAgriculturalInfoByDateRange(String startDate, String endDate);

    List<AgriculturalProductsUpDownVo> getTodayAgriculturalInfo();
}
