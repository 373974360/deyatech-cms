package com.deyatech.appeal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.vo.RecordVo;
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
 * @since 2019-09-24
 */
public interface RecordService extends BaseService<Record> {

    /**
     * 单个将对象转换为vo
     *
     * @param record
     * @return
     */
    RecordVo setVoProperties(Record record);

    /**
     * 批量将对象转换为vo
     *
     * @param records
     * @return
     */
    List<RecordVo> setVoProperties(Collection records);


    IPage<RecordVo> pageRecordByBean(Record record,String[] timeFrame, String userDepartmentId);

    /**
     * 根据业务ID得到随机查询码，用于前台页面添加诉求时产生查询码
     *
     * @param modelId
     * @return String
     */
    String getQueryCode(String modelId);

    /**
     * 根据业务ID得到信件编码
     *
     * @param modelId
     * @return String
     */
    String getAppealCode(String modelId);


    /**
     * 根据业务ID得到参与部门
     *
     * @param modelId
     * @return List
     */
    List<DepartmentVo> getCompetentDept(String modelId);



    /**
     * 网站前台根据条件获取诉求列表
     *
     * @param maps
     * @param page
     * @param pageSize
     * @return Ipage
     */
    IPage<RecordVo> getAppealList(Map<String, Object> maps, Integer page, Integer pageSize);

    /**
     * 网站前台根据诉求编码和查询码查询诉求信息
     *
     * @param sqCode
     * @param queryCode
     * @return RecordVo
     */
    RecordVo queryAppeal(String sqCode,String queryCode);


    /**
     * 网站前台提交诉求信息
     *
     * @param record
     * @return Record
     */
    Record insertAppeal(Record record);

    /**
     * 根据业务ID，信件类型得到总数
     * @param maps countType 信件类型 all 所有,wsl 未受理信件,sl 受理信件　bj　办结信件（回复件）
     * 			   countData years本年度  yesterday昨天的  ultimo 上月的 instant 本月 cur_data 当天
     * @return String
     */
    String getAllAppealCount(Map<String, Object> maps);

    /**
     * 统计模型使用件数
     *
     * @return
     */
    List<RecordVo> countModel();

    /**
     * 统计诉求目的使用件数
     *
     * @return
     */
    List<RecordVo> countPurpose();




    /**
     * 录入诉求信息
     *
     * @param record
     * @return Record
     */
    boolean addAppeal(Record record);
}
