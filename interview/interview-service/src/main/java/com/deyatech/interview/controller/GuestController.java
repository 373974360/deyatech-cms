package com.deyatech.interview.controller;

import com.deyatech.common.context.SpringContextHelper;
import com.deyatech.interview.entity.Guest;
import com.deyatech.interview.vo.GuestVo;
import com.deyatech.interview.service.GuestService;
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
 * 访谈嘉宾 前端控制器
 * </p>
 * @author: lee.
 * @since 2019-08-28
 */
@Slf4j
@RestController
@RequestMapping("/interview/guest")
@Api(tags = {"访谈嘉宾接口"})
public class GuestController extends BaseController {
    @Autowired
    GuestService guestService;

    /**
     * 单个保存或者更新访谈嘉宾
     *
     * @param guest
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value="单个保存或者更新访谈嘉宾", notes="根据访谈嘉宾对象保存或者更新访谈嘉宾信息")
    @ApiImplicitParam(name = "guest", value = "访谈嘉宾对象", required = true, dataType = "Guest", paramType = "query")
    public RestResult<Boolean> saveOrUpdate(Guest guest) {
        log.info(String.format("保存或者更新访谈嘉宾: %s ", JSONUtil.toJsonStr(guest)));
        boolean result = guestService.saveOrUpdate(guest);
        return RestResult.ok(result);
    }

    /**
     * 批量保存或者更新访谈嘉宾
     *
     * @param guestList
     * @return
     */
    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value="批量保存或者更新访谈嘉宾", notes="根据访谈嘉宾对象集合批量保存或者更新访谈嘉宾信息")
    @ApiImplicitParam(name = "guestList", value = "访谈嘉宾对象集合", required = true, allowMultiple = true, dataType = "Guest", paramType = "query")
    public RestResult<Boolean> saveOrUpdateBatch(Collection<Guest> guestList) {
        log.info(String.format("批量保存或者更新访谈嘉宾: %s ", JSONUtil.toJsonStr(guestList)));
        boolean result = guestService.saveOrUpdateBatch(guestList);
        return RestResult.ok(result);
    }

    /**
     * 根据Guest对象属性逻辑删除访谈嘉宾
     *
     * @param guest
     * @return
     */
    @PostMapping("/removeByGuest")
    @ApiOperation(value="根据Guest对象属性逻辑删除访谈嘉宾", notes="根据访谈嘉宾对象逻辑删除访谈嘉宾信息")
    @ApiImplicitParam(name = "guest", value = "访谈嘉宾对象", required = true, dataType = "Guest", paramType = "query")
    public RestResult<Boolean> removeByGuest(Guest guest) {
        log.info(String.format("根据Guest对象属性逻辑删除访谈嘉宾: %s ", guest));
        boolean result = guestService.removeByBean(guest);
        return RestResult.ok(result);
    }


    /**
     * 根据ID批量逻辑删除访谈嘉宾
     *
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    @ApiOperation(value="根据ID批量逻辑删除访谈嘉宾", notes="根据访谈嘉宾对象ID批量逻辑删除访谈嘉宾信息")
    @ApiImplicitParam(name = "ids", value = "访谈嘉宾对象ID集合", required = true, allowMultiple = true, dataType = "Serializable", paramType = "query")
    public RestResult<Boolean> removeByIds(@RequestParam("ids[]") List<String> ids) {
        log.info(String.format("根据id批量删除访谈嘉宾: %s ", JSONUtil.toJsonStr(ids)));
        boolean result = guestService.removeByIds(ids);
        return RestResult.ok(result);
    }

    /**
     * 根据Guest对象属性获取访谈嘉宾
     *
     * @param guest
     * @return
     */
    @GetMapping("/getByGuest")
    @ApiOperation(value="根据Guest对象属性获取访谈嘉宾", notes="根据访谈嘉宾对象属性获取访谈嘉宾信息")
    @ApiImplicitParam(name = "guest", value = "访谈嘉宾对象", required = false, dataType = "Guest", paramType = "query")
    public RestResult<GuestVo> getByGuest(Guest guest) {
        guest = guestService.getByBean(guest);
        GuestVo guestVo = guestService.setVoProperties(guest);
        log.info(String.format("根据id获取访谈嘉宾：%s", JSONUtil.toJsonStr(guestVo)));
        return RestResult.ok(guestVo);
    }

    /**
     * 根据Guest对象属性检索所有访谈嘉宾
     *
     * @param guest
     * @return
     */
    @GetMapping("/listByGuest")
    @ApiOperation(value="根据Guest对象属性检索所有访谈嘉宾", notes="根据Guest对象属性检索所有访谈嘉宾信息")
    @ApiImplicitParam(name = "guest", value = "访谈嘉宾对象", required = false, dataType = "Guest", paramType = "query")
    public RestResult<Collection<GuestVo>> listByGuest(Guest guest) {
        Collection<Guest> guests = guestService.listByBean(guest);
        Collection<GuestVo> guestVos = guestService.setVoProperties(guests);
        log.info(String.format("根据Guest对象属性检索所有访谈嘉宾: %s ",JSONUtil.toJsonStr(guestVos)));
        return RestResult.ok(guestVos);
    }

    /**
     * 根据Guest对象属性分页检索访谈嘉宾
     *
     * @param guest
     * @return
     */
    @GetMapping("/pageByGuest")
    @ApiOperation(value="根据Guest对象属性分页检索访谈嘉宾", notes="根据Guest对象属性分页检索访谈嘉宾信息")
    @ApiImplicitParam(name = "guest", value = "访谈嘉宾对象", required = false, dataType = "Guest", paramType = "query")
    public RestResult<IPage<GuestVo>> pageByGuest(Guest guest) {
        IPage<GuestVo> guests = guestService.pageByBean(guest);
        guests.setRecords(guestService.setVoProperties(guests.getRecords()));
        log.info(String.format("根据Guest对象属性分页检索访谈嘉宾: %s ",JSONUtil.toJsonStr(guests)));
        return RestResult.ok(guests);
    }

    /**
     * 根据Guest对象属性分页检索访谈嘉宾
     *
     * @param guest
     * @return
     */
    @GetMapping("/pageGuestByModelNameJobType")
    @ApiOperation(value="根据Guest对象属性分页检索访谈嘉宾", notes="根据Guest对象属性分页检索访谈嘉宾信息")
    @ApiImplicitParam(name = "guest", value = "访谈嘉宾对象", required = false, dataType = "Guest", paramType = "query")
    public RestResult<IPage<GuestVo>> pageGuestByModelNameJobType(Guest guest) {
        IPage<GuestVo> guests = guestService.selectGuestByModelNameJobType(guest);
        guests.setRecords(guestService.setVoProperties(guests.getRecords()));
        log.info(String.format("根据Guest对象属性分页检索访谈嘉宾: %s ",JSONUtil.toJsonStr(guests)));
        return RestResult.ok(guests);
    }
}
