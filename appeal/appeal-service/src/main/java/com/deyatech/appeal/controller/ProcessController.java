package com.deyatech.appeal.controller;

import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.appeal.entity.Process;
import com.deyatech.appeal.entity.Record;
import com.deyatech.appeal.service.RecordService;
import com.deyatech.appeal.vo.ProcessVo;
import com.deyatech.appeal.service.ProcessService;
import com.deyatech.common.context.UserContextHelper;
import com.deyatech.common.entity.RestResult;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
@RequestMapping("/appeal/process")
@Api(tags = {"接口"})
public class ProcessController extends BaseController {
    @Autowired
    ProcessService processService;
    @Autowired
    AdminFeign adminFeign;
    @Autowired
    RecordService recordService;

    /**
     * 单个保存或者更新
     *
     * @param process
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新", notes="根据对象保存或者更新信息")
    @ApiImplicitParam(name = "process", value = "对象", required = true, dataType = "Process", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Process process,Record record) {
        log.info(String.format("保存或者更新: %s ", JSONUtil.toJsonStr(process)));
        UserVo userVo = adminFeign.getUserByUserId(UserContextHelper.getUserId()).getData();
        process.setProDeptId(userVo.getDepartmentId());
        Record oldRecord = recordService.getById(process.getSqId());
        boolean result = false;
        //办理状态为 受理信件（1） 设置信件状态为 已受理（2）
        if(process.getProType() == 1){
            oldRecord.setFlag(2);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setReplyTime(process.getProTime());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 置为无效（2）设置信件状态为 无效（5）
        if(process.getProType() == 2){
            oldRecord.setFlag(5);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setReplyTime(new Date());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 申请延期（3）设置信件状态为 办理中（3）
        if(process.getProType() == 3){
            oldRecord.setFlag(3);
        }
        //办理状态为 转办信件（4） 设置信件状态为 办理中（3）
        if(process.getProType() == 4){
            oldRecord.setFlag(3);
            oldRecord.setProDeptId(process.getToDeptId());
        }
        //办理状态为 回复信件（5） 设置信件状态为 已办结（4）
        if(process.getProType() == 5){
            oldRecord.setFlag(4);
            oldRecord.setReplyDeptId(userVo.getDepartmentId());
            oldRecord.setIsOpen(record.getIsOpen());
            oldRecord.setIsPublish(record.getIsPublish());
            oldRecord.setReplyTime(process.getProTime());
            oldRecord.setReplyContent(process.getProContent());
        }
        //办理状态为 退回信件（6） 设置信件状态为 办理中（3）
        if(process.getProType() == 6){
            oldRecord.setFlag(3);
            oldRecord.setProDeptId(oldRecord.getDeptId());
            process.setToDeptId(oldRecord.getDeptId());
        }
        //办理状态为 发布信件（7）
        if(process.getProType() == 7){
            oldRecord.setTitle(record.getTitle());
            oldRecord.setContent(record.getContent());
            oldRecord.setIsOpen(record.getIsOpen());
            oldRecord.setIsPublish(record.getIsPublish());
            oldRecord.setReplyTime(process.getProTime());
            oldRecord.setReplyContent(process.getProContent());
        }
        result = recordService.saveOrUpdate(oldRecord);
        if(result && process.getProType() < 7){
            processService.saveOrUpdate(process);
        }
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新
     *
     * @param processList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新", notes="根据对象集合批量保存或者更新信息")
    @ApiImplicitParam(name = "processList", value = "对象集合", required = true, allowMultiple = true, dataType = "Process", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Process> processList) {
        log.info(String.format("批量保存或者更新: %s ", JSONUtil.toJsonStr(processList)));
        boolean result = processService.saveOrUpdateBatch(processList);
        return RestResult.ok(result);
    }

    /**
     * 根据Process对象属性逻辑删除
     *
     * @param process
     * @return
     */
    @PostMapping("/removeByProcess")
    @ApiOperation(value="根据Process对象属性逻辑删除", notes="根据对象逻辑删除信息")
    @ApiImplicitParam(name = "process", value = "对象", required = true, dataType = "Process", paramType = "query")
    public RestResult<Boolean> removeByProcess(Process process) {
        log.info(String.format("根据Process对象属性逻辑删除: %s ", process));
        boolean result = processService.removeByBean(process);
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
        boolean result = processService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Process对象属性获取
     *
     * @param process
     * @return
     */
    @GetMapping("/getByProcess")
    @ApiOperation(value="根据Process对象属性获取", notes="根据对象属性获取信息")
    @ApiImplicitParam(name = "process", value = "对象", required = false, dataType = "Process", paramType = "query")
    public RestResult<ProcessVo> getByProcess(Process process) {
        process = processService.getByBean(process);
        ProcessVo processVo = processService.setVoProperties(process);
        log.info(String.format("根据id获取：%s", JSONUtil.toJsonStr(processVo)));
        return RestResult.ok(processVo);
    }

    /**
     * 根据Process对象属性检索所有
     *
     * @param process
     * @return
     */
    @GetMapping("/listByProcess")
    @ApiOperation(value="根据Process对象属性检索所有", notes="根据Process对象属性检索所有信息")
    @ApiImplicitParam(name = "process", value = "对象", required = false, dataType = "Process", paramType = "query")
    public RestResult<Collection<ProcessVo>> listByProcess(Process process) {
        process.setSortSql("create_time asc");
        Collection<Process> processs = processService.listByBean(process);
        Collection<ProcessVo> processVos = processService.setVoProperties(processs);
        log.info(String.format("根据Process对象属性检索所有: %s ",JSONUtil.toJsonStr(processVos)));
        return RestResult.ok(processVos);
    }

    /**
     * 根据Process对象属性分页检索
     *
     * @param process
     * @return
     */
    @GetMapping("/pageByProcess")
    @ApiOperation(value="根据Process对象属性分页检索", notes="根据Process对象属性分页检索信息")
    @ApiImplicitParam(name = "process", value = "对象", required = false, dataType = "Process", paramType = "query")
    public RestResult<IPage<ProcessVo>> pageByProcess(Process process) {
        IPage<ProcessVo> processs = processService.pageByBean(process);
        processs.setRecords(processService.setVoProperties(processs.getRecords()));
        log.info(String.format("根据Process对象属性分页检索: %s ",JSONUtil.toJsonStr(processs)));
        return RestResult.ok(processs);
    }

}
