package com.deyatech.survey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2019/10/9 12:18
 */
@Controller
@RequestMapping("/survey")
public class TextController {

    @RequestMapping("/test")
    public String test(){
        System.out.println("test");
        return "test";
    }
}
