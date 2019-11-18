package com.deyatech.assembly.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.vo.ApplyOpenModelVo;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
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
 * @Date: 2019/11/5 14:24
 */
@FeignClient(value = "assembly-service")
public interface AssemblyFeign {

    /**
     * 网站前台根据条件获取信息列表
     *
     * @param maps
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/feign/assembly/getApplyOpenList")
    RestResult<Page<ApplyOpenRecordVo>> getApplyOpenList(@RequestBody Map<String, Object> maps, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize);
    /**
     * 网站前台根据依申请编码和查询码查询依申请信息
     *
     * @param ysqCode
     * @param queryCode
     * @return
     */
    @RequestMapping(value = "/feign/assembly/queryApplyOpen")
    RestResult<ApplyOpenRecordVo> queryApplyOpen(@RequestParam("ysqCode") String ysqCode, @RequestParam("queryCode") String queryCode);

    /**
     * 依申请提交
     * @param record
     * @return
     */
    @RequestMapping(value = "/feign/assembly/insertApplyOpen")
    RestResult insertApplyOpen(@RequestBody ApplyOpenRecord record);
    /**
     * 依申请公开详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/feign/assembly/getApplyOpenById")
    RestResult<ApplyOpenRecordVo> getApplyOpenById(@RequestParam("id") String id);

    /**
     * 依申请公开业务模型
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/feign/assembly/getApplyOpenModelById")
    RestResult<ApplyOpenModelVo> getApplyOpenModelById(@RequestParam("id") String id);

    /**
     * 根据模型ID获取参与部门
     * @param modelId
     * @return
     */
    @RequestMapping(value = "/feign/assembly/getPartDept")
    RestResult<List<DepartmentVo>> getPartDept(@RequestParam("modelId") String modelId);

    /**
     * 获取下一个索引码根据站点ID
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/feign/assembly/indexCode/getPartDept")
    RestResult<String> getNextIndexCodeBySiteId(@RequestParam("siteId") String siteId);
}
