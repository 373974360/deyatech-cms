package com.deyatech.cas.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.deyatech.admin.entity.User;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.admin.vo.UserVo;
import com.deyatech.cas.config.CasConfig;
import com.deyatech.cas.config.JwtConfig;
import com.deyatech.common.Constants;
import com.deyatech.common.entity.RestResult;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.common.jwt.JwtInfo;
import com.deyatech.common.jwt.JwtUtil;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/6 14:14
 */
@Controller
@RequestMapping("/manage/cas/sso/")
public class CasLoginController {



    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private CasConfig casConfig;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    AdminFeign adminFeign;

    @Value("${front-url}")
    private String frontUrl;

    @Value("${domain}")
    private String domain;

    @GetMapping("/login")
    public String login(){
        return "redirect:" + frontUrl + "/#/cas";
    }

    @GetMapping("/getUser")
    @ResponseBody
    public RestResult getUser(HttpServletRequest request){
        Object object= request.getSession().getAttribute("_const_cas_assertion_");
        if (object != null) {
            Assertion assertion = (Assertion) object;
            //获取用户登录名
            String loginName = assertion.getPrincipal().getName();
            UserVo userVo = adminFeign.getByUser(new User().setAccount(loginName)).getData();
            if(StrUtil.isNotBlank(userVo.getId())){
                JwtInfo jwtInfo = new JwtInfo(userVo.getId(), userVo.getAccount(), userVo.getName(), null);
                String token = JwtUtil.generateToken(jwtInfo, jwtConfig.getPriKeyPath(), jwtConfig.getXpire());
                userVo.setToken(token);
                userVo.setPassword(null);
                RestResult<String[]> allPermissionsByUserId = adminFeign.getAllPermissionsByUserId(userVo.getId());
                if (allPermissionsByUserId != null && allPermissionsByUserId.isOk()) {
                    userVo.setPermissions(allPermissionsByUserId.getData());
                }
                return RestResult.ok(userVo);
            }else{
                return RestResult.error("用户未同步");
            }
        } else {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "调用userFeign根据用户名查找用户出错", object);
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("_const_cas_assertion_");
        return "redirect:" + casConfig.getServerUrlPrefix() + "/logout?service=" + frontUrl;
    }
}
