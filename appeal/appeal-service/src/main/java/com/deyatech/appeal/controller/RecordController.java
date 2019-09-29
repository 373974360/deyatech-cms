package com.deyatech.appeal.controller;

import cn.hutool.core.util.StrUtil;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.vo.RecordVo;
import com.deyatech.appeal.service.RecordService;
import com.deyatech.common.entity.RestResult;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
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
 * @since 2019-09-24
 */
@Slf4j
@RestController
@RequestMapping("/appeal/record")
@Api(tags = {"接口"})
public class RecordController extends BaseController {
    @Autowired
    RecordService recordService;

    /**
     * 单个保存或者更新
     *
     * @param record
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "record", value = "对象", required = true, dataType = "Record", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Record record) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(record)));
        if(StrUtil.isBlank(record.getId())){
            record.setQueryCode(recordService.getQueryCode(record.getModelId()));
            record.setSqCode(recordService.getAppealCode(record.getModelId()));
        }
        record.setProDeptId(record.getDeptId());
        boolean result = recordService.saveOrUpdate(record);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param recordList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "recordList", value = "对象集合", required = true, allowMultiple = true, dataType = "Record", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Record> recordList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(recordList)));
        boolean result = recordService.saveOrUpdateBatch(recordList);
        return RestResult.ok(result);
    }

    /**
     * 根据Record对象属性逻辑删除
     *
     * @param record
     * @return
     */
    @PostMapping("/removeByRecord")
    @ApiOperation(value="根据Record对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "record", value = "对象", required = true, dataType = "Record", paramType = "query")
    public RestResult<Boolean> removeByRecord(Record record) {
        log.info(String.format("根据Record对象属性逻辑删除: %s ", record));
        boolean result = recordService.removeByBean(record);
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
        boolean result = recordService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Record对象属性获取
     *
     * @param record
     * @return
     */
    @GetMapping("/getByRecord")
    @ApiOperation(value="根据Record对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "record", value = "对象", required = false, dataType = "Record", paramType = "query")
    public RestResult<RecordVo> getByRecord(Record record) {
        record = recordService.getByBean(record);
        RecordVo recordVo = recordService.setVoProperties(record);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(recordVo)));
        return RestResult.ok(recordVo);
    }

    /**
     * 根据Record对象属性检索所有
     *
     * @param record
     * @return
     */
    @GetMapping("/listByRecord")
    @ApiOperation(value="根据Record对象属性检索所有", notes="根据Record对象属性检索所有信息")
    @ApiImplicitParam(name = "record", value = "对象", required = false, dataType = "Record", paramType = "query")
    public RestResult<Collection<RecordVo>> listByRecord(Record record) {
        Collection<Record> records = recordService.listByBean(record);
        Collection<RecordVo> recordVos = recordService.setVoProperties(records);
        log.info(String.format("根据Record对象属性检索所有: %s ",JSONUtil.toJsonStr(recordVos)));
        return RestResult.ok(recordVos);
    }

    /**
     * 根据Record对象属性分页检索
     *
     * @param record
     * @return
     */
    @GetMapping("/pageByRecord")
    @ApiOperation(value="根据Record对象属性分页检索", notes="根据Record对象属性分页检索信息")
    @ApiImplicitParam(name = "record", value = "对象", required = false, dataType = "Record", paramType = "query")
    public RestResult<IPage<RecordVo>> pageByRecord(Record record,String[] timeFrame) {
        IPage<RecordVo> records = recordService.pageRecordByBean(record,timeFrame);
        log.info(String.format("根据Record对象属性分页检索: %s ",JSONUtil.toJsonStr(records)));
        return RestResult.ok(records);
    }

}
