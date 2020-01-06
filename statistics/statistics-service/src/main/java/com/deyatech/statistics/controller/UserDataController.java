package com.deyatech.statistics.controller;

import com.deyatech.common.base.BaseController;
import com.deyatech.statistics.service.UserDataService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户数据统计 前端控制器
 * </p>
 * @author: ycx
 * @since 2020-01-06
 */
@Slf4j
@RestController
@RequestMapping("/statistics/userdata")
@Api(tags = {"用户数据统计"})
public class UserDataController extends BaseController {
    @Autowired
    UserDataService userDataService;
}
