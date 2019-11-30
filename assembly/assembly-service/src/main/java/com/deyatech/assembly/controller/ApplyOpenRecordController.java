package com.deyatech.assembly.controller;

import cn.hutool.core.util.StrUtil;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.assembly.entity.ApplyOpenRecord;
import com.deyatech.assembly.vo.ApplyOpenRecordVo;
import com.deyatech.assembly.service.ApplyOpenRecordService;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.entity.CascaderResult;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.utils.CascaderUtil;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.deyatech.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 * @author: lee.
 * @since 2019-10-16
 */
@Slf4j
@RestController
@RequestMapping("/assembly/applyOpenRecord")
@Api(tags = {"接口"})
public class ApplyOpenRecordController extends BaseController {
    @Autowired
    ApplyOpenRecordService applyOpenRecordService;
    @Autowired
    AdminFeign adminFeign;

    /**
     * 单个保存或者更新
     *
     * @param applyOpenRecord
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "applyOpenRecord", value = "对象", required = true, dataType = "ApplyOpenRecord", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(ApplyOpenRecord applyOpenRecord) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(applyOpenRecord)));
        if(StrUtil.isBlank(applyOpenRecord.getId())){
            applyOpenRecordService.insertApplyOpenRecord(applyOpenRecord);
        }else{
            applyOpenRecordService.updateById(applyOpenRecord);
        }
        return RestResult.ok();
    }

    /**
     * 批量保存或者更新
     *
     * @param applyOpenRecordList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "applyOpenRecordList", value = "对象集合", required = true, allowMultiple = true, dataType = "ApplyOpenRecord", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<ApplyOpenRecord> applyOpenRecordList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(applyOpenRecordList)));
        boolean result = applyOpenRecordService.saveOrUpdateBatch(applyOpenRecordList);
        return RestResult.ok(result);
    }

    /**
     * 根据ApplyOpenRecord对象属性逻辑删除
     *
     * @param applyOpenRecord
     * @return
     */
    @PostMapping("/removeByApplyOpenRecord")
    @ApiOperation(value="根据ApplyOpenRecord对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "applyOpenRecord", value = "对象", required = true, dataType = "ApplyOpenRecord", paramType = "query")
    public RestResult<Boolean> removeByApplyOpenRecord(ApplyOpenRecord applyOpenRecord) {
        log.info(String.format("根据ApplyOpenRecord对象属性逻辑删除: %s ", applyOpenRecord));
        boolean result = applyOpenRecordService.removeByBean(applyOpenRecord);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除", notes="根据对象ID批量逻辑删除信息")
    @ApiImplicitParam(name = "ids", value = "对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = applyOpenRecordService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据ApplyOpenRecord对象属性获取
     *
     * @param applyOpenRecord
     * @return
     */
    @GetMapping("/getByApplyOpenRecord")
    @ApiOperation(value="根据ApplyOpenRecord对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "applyOpenRecord", value = "对象", required = false, dataType = "ApplyOpenRecord", paramType = "query")
    public RestResult<ApplyOpenRecordVo> getByApplyOpenRecord(ApplyOpenRecord applyOpenRecord) {
        applyOpenRecord = applyOpenRecordService.getByBean(applyOpenRecord);
        ApplyOpenRecordVo applyOpenRecordVo = applyOpenRecordService.setVoProperties(applyOpenRecord);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(applyOpenRecordVo)));
        return RestResult.ok(applyOpenRecordVo);
    }

    /**
     * 根据ApplyOpenRecord对象属性检索所有
     *
     * @param applyOpenRecord
     * @return
     */
    @GetMapping("/listByApplyOpenRecord")
    @ApiOperation(value="根据ApplyOpenRecord对象属性检索所有", notes="根据ApplyOpenRecord对象属性检索所有信息")
    @ApiImplicitParam(name = "applyOpenRecord", value = "对象", required = false, dataType = "ApplyOpenRecord", paramType = "query")
    public RestResult<Collection<ApplyOpenRecordVo>> listByApplyOpenRecord(ApplyOpenRecord applyOpenRecord) {
        Collection<ApplyOpenRecord> applyOpenRecords = applyOpenRecordService.listByBean(applyOpenRecord);
        Collection<ApplyOpenRecordVo> applyOpenRecordVos = applyOpenRecordService.setVoProperties(applyOpenRecords);
        log.info(String.format("根据ApplyOpenRecord对象属性检索所有: %s ",JSONUtil.toJsonStr(applyOpenRecordVos)));
        return RestResult.ok(applyOpenRecordVos);
    }

    /**
     * 根据ApplyOpenRecord对象属性分页检索
     *
     * @param applyOpenRecord
     * @return
     */
    @GetMapping("/pageByApplyOpenRecord")
    @ApiOperation(value="根据ApplyOpenRecord对象属性分页检索", notes="根据ApplyOpenRecord对象属性分页检索信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyOpenRecord", value = "对象", required = false, dataType = "ApplyOpenRecord", paramType = "query"),
            @ApiImplicitParam(name = "timeFrame", value = "时间取件", required = false, dataType = "String[]", paramType = "query"),
            @ApiImplicitParam(name = "userDepartmentId", value = "用户部门", required = true, dataType = "String", paramType = "query")
    })
    public RestResult<IPage<ApplyOpenRecordVo>> pageByApplyOpenRecord(ApplyOpenRecord applyOpenRecord,String[] timeFrame, String userDepartmentId) {
        IPage<ApplyOpenRecordVo> applyOpenRecords = applyOpenRecordService.pageApplyOpenRecordByBean(applyOpenRecord,timeFrame,userDepartmentId);
        log.info(String.format("根据ApplyOpenRecord对象属性分页检索: %s ",JSONUtil.toJsonStr(applyOpenRecords)));
        return RestResult.ok(applyOpenRecords);
    }

    /**
     * 根据Record对象属性分页检索
     *
     * @param modelId
     * @return
     */
    @GetMapping("/getCompetentDept")
    @ApiOperation(value="根据业务模型获取参与部门", notes="根据业务模型获取参与部门")
    @ApiImplicitParam(name = "modelId", value = "业务模型ID", required = false, dataType = "String", paramType = "query")
    public RestResult<List<CascaderResult>> getCompetentDept(String modelId) {
        Collection<DepartmentVo> departmentVos = applyOpenRecordService.getCompetentDept(modelId);
        List<CascaderResult> cascaderResults = CascaderUtil.getResult("Id", "Name","TreePosition","0", departmentVos);
        log.info(String.format("获取系统部门信息的级联对象: %s ",JSONUtil.toJsonStr(cascaderResults)));
        return RestResult.ok(cascaderResults);
    }

}
