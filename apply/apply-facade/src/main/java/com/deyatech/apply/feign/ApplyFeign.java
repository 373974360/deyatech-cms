package com.deyatech.apply.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.apply.entity.OpenRecord;
import com.deyatech.apply.vo.OpenModelVo;
import com.deyatech.apply.vo.OpenRecordVo;
import com.deyatech.common.entity.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/16 15:15
 */
@FeignClient(value = "apply-service")
public interface ApplyFeign {

    /**
     * 网站前台根据条件获取信息列表
     *
     * @param maps
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/feign/apply/getApplyOpenList")
    RestResult<Page<OpenRecordVo>> getApplyOpenList(@RequestBody Map<String, Object> maps, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize);
    /**
     * 网站前台根据依申请编码和查询码查询依申请信息
     *
     * @param ysqCode
     * @param queryCode
     * @return
     */
    @RequestMapping(value = "/feign/apply/queryApplyOpen")
    RestResult<OpenRecordVo> queryApplyOpen(@RequestParam("ysqCode") String ysqCode, @RequestParam("queryCode") String queryCode);

    /**
     * 依申请提交
     * @param record
     * @return
     */
    @RequestMapping(value = "/feign/apply/insertApplyOpen")
    RestResult insertApplyOpen(@RequestBody OpenRecord record);
    /**
     * 依申请公开详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/feign/apply/getApplyOpenById")
    RestResult<OpenRecordVo> getApplyOpenById(@RequestParam("id") String id);

    /**
     * 依申请公开业务模型
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/feign/apply/getApplyOpenModelById")
    RestResult<OpenModelVo> getApplyOpenModelById(@RequestParam("id") String id);

    /**
     * 根据模型ID获取参与部门
     * @param modelId
     * @return
     */
    @RequestMapping(value = "/feign/apply/getPartDept")
    RestResult<List<DepartmentVo>> getPartDept(@RequestParam("modelId") String modelId);

}
