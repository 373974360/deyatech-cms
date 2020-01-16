package com.deyatech.apply.controller;

import cn.hutool.core.util.StrUtil;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.DepartmentVo;
import com.deyatech.apply.entity.OpenRecord;
import com.deyatech.apply.service.OpenRecordService;
import com.deyatech.apply.vo.OpenRecordVo;
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
@RequestMapping("/apply/openRecord")
@Api(tags = {"接口"})
public class OpenRecordController extends BaseController {
    @Autowired
    OpenRecordService openRecordService;
    @Autowired
    AdminFeign adminFeign;

    /**
     * 单个保存或者更新
     *
     * @param openRecord
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "openRecord", value = "对象", required = true, dataType = "openRecord", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(OpenRecord openRecord) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(openRecord)));
        if(StrUtil.isBlank(openRecord.getId())){
            openRecordService.insertOpenRecord(openRecord);
        }else{
            openRecordService.updateById(openRecord);
        }
        return RestResult.ok();
    }

    /**
     * 批量保存或者更新
     *
     * @param openRecordList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "openRecordList", value = "对象集合", required = true, allowMultiple = true, dataType = "openRecord", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<OpenRecord> openRecordList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(openRecordList)));
        boolean result = openRecordService.saveOrUpdateBatch(openRecordList);
        return RestResult.ok(result);
    }

    /**
     * 根据openRecord对象属性逻辑删除
     *
     * @param openRecord
     * @return
     */
    @PostMapping("/removeByOpenRecord")
    @ApiOperation(value="根据openRecord对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "openRecord", value = "对象", required = true, dataType = "openRecord", paramType = "query")
    public RestResult<Boolean> removeByOpenRecord(OpenRecord openRecord) {
        log.info(String.format("根据openRecord对象属性逻辑删除: %s ", openRecord));
        boolean result = openRecordService.removeByBean(openRecord);
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
        boolean result = openRecordService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据openRecord对象属性获取
     *
     * @param openRecord
     * @return
     */
    @GetMapping("/getByOpenRecord")
    @ApiOperation(value="根据openRecord对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "openRecord", value = "对象", required = false, dataType = "openRecord", paramType = "query")
    public RestResult<OpenRecordVo> getByOpenRecord(OpenRecord openRecord) {
        openRecord = openRecordService.getByBean(openRecord);
        OpenRecordVo openRecordVo = openRecordService.setVoProperties(openRecord);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(openRecordVo)));
        return RestResult.ok(openRecordVo);
    }

    /**
     * 根据openRecord对象属性检索所有
     *
     * @param openRecord
     * @return
     */
    @GetMapping("/listByOpenRecord")
    @ApiOperation(value="根据openRecord对象属性检索所有", notes="根据openRecord对象属性检索所有信息")
    @ApiImplicitParam(name = "openRecord", value = "对象", required = false, dataType = "openRecord", paramType = "query")
    public RestResult<Collection<OpenRecordVo>> listByOpenRecord(OpenRecord openRecord) {
        Collection<OpenRecord> openRecords = openRecordService.listByBean(openRecord);
        Collection<OpenRecordVo> openRecordVos = openRecordService.setVoProperties(openRecords);
        log.info(String.format("根据openRecord对象属性检索所有: %s ",JSONUtil.toJsonStr(openRecordVos)));
        return RestResult.ok(openRecordVos);
    }

    /**
     * 根据openRecord对象属性分页检索
     *
     * @param openRecord
     * @return
     */
    @GetMapping("/pageByOpenRecord")
    @ApiOperation(value="根据openRecord对象属性分页检索", notes="根据openRecord对象属性分页检索信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openRecord", value = "对象", required = false, dataType = "openRecord", paramType = "query"),
            @ApiImplicitParam(name = "timeFrame", value = "时间取件", required = false, dataType = "String[]", paramType = "query"),
            @ApiImplicitParam(name = "userDepartmentId", value = "用户部门", required = true, dataType = "String", paramType = "query")
    })
    public RestResult<IPage<OpenRecordVo>> pageByOpenRecord(OpenRecord openRecord,String[] timeFrame, String userDepartmentId) {
        IPage<OpenRecordVo> openRecords = openRecordService.pageOpenRecordByBean(openRecord,timeFrame,userDepartmentId);
        log.info(String.format("根据openRecord对象属性分页检索: %s ",JSONUtil.toJsonStr(openRecords)));
        return RestResult.ok(openRecords);
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
        Collection<DepartmentVo> departmentVos = openRecordService.getCompetentDept(modelId);
        List<CascaderResult> cascaderResults = CascaderUtil.getResult("Id", "Name","TreePosition","0", departmentVos);
        log.info(String.format("获取系统部门信息的级联对象: %s ",JSONUtil.toJsonStr(cascaderResults)));
        return RestResult.ok(cascaderResults);
    }



    /**
     * 初始化树菜单
     *
     * @param userDepartmentId
     * @return
     */
    @GetMapping("/reloadTreeData")
    @ApiOperation(value="根据条件查询重复的诉求列表", notes="初始化树菜单")
    @ApiImplicitParam(name = "userDepartmentId", value = "部门ID", required = false, dataType = "String", paramType = "query")
    public RestResult reloadTreeData(String userDepartmentId) {
        return RestResult.ok(openRecordService.resetTreeLabel(userDepartmentId));
    }

}
