package com.deyatech.appeal.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.appeal.entity.Purpose;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.entity.RecordSatisfaction;
import com.deyatech.appeal.entity.Satisfaction;
import com.deyatech.appeal.vo.ModelVo;
import com.deyatech.appeal.vo.PurposeVo;
import com.deyatech.appeal.vo.RecordSatisfactionVo;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.common.entity.RestResult;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/10/30 20:19
 */
@FeignClient(value = "appeal-service")
public interface AppealFeign {

    /**
     * 网站前台根据条件获取信息列表
     *
     * @param maps
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/feign/appeal/getAppealList")
    RestResult<Page<RecordVo>> getAppealList(@RequestBody Map<String, Object> maps, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize);
    /**
     * 网站前台根据诉求编码和查询码查询诉求信息
     *
     * @param sqCode
     * @param queryCode
     * @return
     */
    @RequestMapping(value = "/feign/appeal/queryAppeal")
    RestResult<RecordVo> queryAppeal(@RequestParam("sqCode") String sqCode, @RequestParam("queryCode") String queryCode);

    /**
     * 诉求提交
     * @param record
     * @return
     */
    @RequestMapping(value = "/feign/appeal/insertAppeal")
    RestResult insertAppeal(@RequestBody Record record);

    /**
     * 诉求详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/feign/appeal/getAppealById")
    RestResult<RecordVo> getAppealById(@RequestParam("id") String id);

    /**
     * 诉求业务模型
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/feign/appeal/getModelById")
    RestResult<ModelVo> getModelById(@RequestParam("id") String id);



    /**
     * 获取诉求目的
     *
     * @return
     */
    @RequestMapping(value = "/feign/appeal/getAllPurpose")
    RestResult<List<Purpose>> getAllPurpose();



    /**
     * 根据模型ID获取参与部门
     * @param modelId
     * @return
     */
    @RequestMapping(value = "/feign/appeal/getPartDept")
    RestResult<List<DepartmentVo>> getPartDept(@RequestParam("modelId") String modelId);

    /**
     * 获取满意度指标
     *
     * @return
     */
    @RequestMapping(value = "/feign/appeal/getAllSatisfaction")
    RestResult<List<Satisfaction>> getAllSatisfaction();

    /**
     * 满意度评价提交
     * @param recordSatisfaction
     * @return
     */
    @RequestMapping(value = "/feign/appeal/insertAppealSatis")
    RestResult insertAppealSatis(@RequestBody RecordSatisfaction recordSatisfaction);

    /**
     * 根据信件ID获取满意度评价结果
     * @param appealId
     * @return
     */
    @RequestMapping(value = "/feign/appeal/getAppealSatisCountByAppealId")
    RestResult<List<RecordSatisfactionVo>> getAppealSatisCountByAppealId(@RequestParam("appealId") String appealId);

}
