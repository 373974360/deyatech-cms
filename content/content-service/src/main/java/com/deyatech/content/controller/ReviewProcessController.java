package com.deyatech.content.controller;

import com.deyatech.content.entity.ReviewProcess;
import com.deyatech.content.vo.ReviewProcessVo;
import com.deyatech.content.service.ReviewProcessService;
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
 * 内容审核流程 前端控制器
 * </p>
 * @author: csm.
 * @since 2019-08-14
 */
@Slf4j
@RestController
@RequestMapping("/content/reviewProcess")
@Api(tags = {"内容审核流程接口"})
public class ReviewProcessController extends BaseController {
    @Autowired
    ReviewProcessService reviewProcessService;

    /**
     * 单个保存或者更新内容审核流程
     *
     * @param reviewProcess
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新内容审核流程", notes="根据内容审核流程对象保存或者更新内容审核流程信息")
    @ApiImplicitParam(name = "reviewProcess", value = "内容审核流程对象", required = true, dataType = "ReviewProcess", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(ReviewProcess reviewProcess) {
        log.info(String.format("保存或者更新内容审核流程: %s ", JSONUtil.toJsonStr(reviewProcess)));
        boolean result = reviewProcessService.saveOrUpdate(reviewProcess);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新内容审核流程
     *
     * @param reviewProcessList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新内容审核流程", notes="根据内容审核流程对象集合批量保存或者更新内容审核流程信息")
    @ApiImplicitParam(name = "reviewProcessList", value = "内容审核流程对象集合", required = true, allowMultiple = true, dataType = "ReviewProcess", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<ReviewProcess> reviewProcessList) {
        log.info(String.format("批量保存或者更新内容审核流程: %s ", JSONUtil.toJsonStr(reviewProcessList)));
        boolean result = reviewProcessService.saveOrUpdateBatch(reviewProcessList);
        return RestResult.ok(result);
    }

    /**
     * 根据ReviewProcess对象属性逻辑删除内容审核流程
     *
     * @param reviewProcess
     * @return
     */
    @PostMapping("/removeByReviewProcess")
    @ApiOperation(value="根据ReviewProcess对象属性逻辑删除内容审核流程", notes="根据内容审核流程对象逻辑删除内容审核流程信息")
    @ApiImplicitParam(name = "reviewProcess", value = "内容审核流程对象", required = true, dataType = "ReviewProcess", paramType = "query")
    public RestResult<Boolean> removeByReviewProcess(ReviewProcess reviewProcess) {
        log.info(String.format("根据ReviewProcess对象属性逻辑删除内容审核流程: %s ", reviewProcess));
        boolean result = reviewProcessService.removeByBean(reviewProcess);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除内容审核流程
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除内容审核流程", notes="根据内容审核流程对象ID批量逻辑删除内容审核流程信息")
    @ApiImplicitParam(name = "ids", value = "内容审核流程对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除内容审核流程: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = reviewProcessService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据ReviewProcess对象属性获取内容审核流程
     *
     * @param reviewProcess
     * @return
     */
    @GetMapping("/getByReviewProcess")
    @ApiOperation(value="根据ReviewProcess对象属性获取内容审核流程", notes="根据内容审核流程对象属性获取内容审核流程信息")
    @ApiImplicitParam(name = "reviewProcess", value = "内容审核流程对象", required = false, dataType = "ReviewProcess", paramType = "query")
    public RestResult<ReviewProcessVo> getByReviewProcess(ReviewProcess reviewProcess) {
        reviewProcess = reviewProcessService.getByBean(reviewProcess);
        ReviewProcessVo reviewProcessVo = reviewProcessService.setVoProperties(reviewProcess);
        log.info(String.format("根据id获取内容审核流程：%s", JSONUtil.toJsonStr(reviewProcessVo)));
        return RestResult.ok(reviewProcessVo);
    }

    /**
     * 根据ReviewProcess对象属性检索所有内容审核流程
     *
     * @param reviewProcess
     * @return
     */
    @GetMapping("/listByReviewProcess")
    @ApiOperation(value="根据ReviewProcess对象属性检索所有内容审核流程", notes="根据ReviewProcess对象属性检索所有内容审核流程信息")
    @ApiImplicitParam(name = "reviewProcess", value = "内容审核流程对象", required = false, dataType = "ReviewProcess", paramType = "query")
    public RestResult<Collection<ReviewProcessVo>> listByReviewProcess(ReviewProcess reviewProcess) {
        Collection<ReviewProcess> reviewProcesss = reviewProcessService.listByBean(reviewProcess);
        Collection<ReviewProcessVo> reviewProcessVos = reviewProcessService.setVoProperties(reviewProcesss);
        log.info(String.format("根据ReviewProcess对象属性检索所有内容审核流程: %s ",JSONUtil.toJsonStr(reviewProcessVos)));
        return RestResult.ok(reviewProcessVos);
    }

    /**
     * 根据ReviewProcess对象属性分页检索内容审核流程
     *
     * @param reviewProcess
     * @return
     */
    @GetMapping("/pageByReviewProcess")
    @ApiOperation(value="根据ReviewProcess对象属性分页检索内容审核流程", notes="根据ReviewProcess对象属性分页检索内容审核流程信息")
    @ApiImplicitParam(name = "reviewProcess", value = "内容审核流程对象", required = false, dataType = "ReviewProcess", paramType = "query")
    public RestResult<IPage<ReviewProcessVo>> pageByReviewProcess(ReviewProcess reviewProcess) {
        IPage<ReviewProcessVo> reviewProcesss = reviewProcessService.pageByBean(reviewProcess);
        reviewProcesss.setRecords(reviewProcessService.setVoProperties(reviewProcesss.getRecords()));
        log.info(String.format("根据ReviewProcess对象属性分页检索内容审核流程: %s ",JSONUtil.toJsonStr(reviewProcesss)));
        return RestResult.ok(reviewProcesss);
    }

}
