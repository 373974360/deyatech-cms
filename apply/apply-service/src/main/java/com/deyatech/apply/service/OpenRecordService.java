package com.deyatech.apply.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.apply.entity.OpenRecord;
import com.deyatech.apply.vo.OpenRecordVo;
import com.deyatech.apply.vo.RecordMenuVo;
import com.deyatech.common.base.BaseService;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @Author lee.
 * @since 2019-10-16
 */
public interface OpenRecordService extends BaseService<OpenRecord> {

    /**
     * 单个将对象转换为vo
     *
     * @param openRecord
     * @return
     */
    OpenRecordVo setVoProperties(OpenRecord openRecord);

    /**
     * 批量将对象转换为vo
     *
     * @param openRecords
     * @return
     */
    List<OpenRecordVo> setVoProperties(Collection openRecords);


    IPage<OpenRecordVo> pageOpenRecordByBean(OpenRecord record,String[] timeFrame, String userDepartmentId);

    /**
     * 根据业务ID得到随机查询码，用于前台页面添加诉求时产生查询码
     *
     * @param configId
     * @return String
     */
    String getQueryCode(String configId);

    /**
     * 根据业务ID得到信件编码
     *
     * @param configId
     * @return String
     */
    String getYsqCode(String configId);

    /**
     * 根据业务ID得到参与部门
     *
     * @param modelId
     * @return String
     */
    List<DepartmentVo> getCompetentDept(String modelId);

    /**
     * 网站前台提交诉求信息
     *
     * @param openRecord
     * @return OpenRecord
     */
    OpenRecord insertOpenRecord(OpenRecord openRecord);


    /**
     * 网站前台根据诉求编码和查询码查询诉求信息
     *
     * @param sqCode
     * @param queryCode
     * @return OpenRecordVo
     */
    OpenRecordVo queryApplyOpen(String sqCode,String queryCode);

    /**
     * 网站前台根据条件获取诉求列表
     *
     * @param maps
     * @param page
     * @param pageSize
     * @return Ipage
     */
    IPage<OpenRecordVo> getApplyOpenList(Map<String, Object> maps, Integer page, Integer pageSize);

    /**
     * 模型使用统计
     * @return
     */
    List<OpenRecordVo> countOpenModel();

    List<RecordMenuVo> resetTreeLabel(String userDepartmentId);


}
