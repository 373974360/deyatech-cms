package com.deyatech.station.view;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.deyatech.admin.entity.Department;
import com.deyatech.admin.entity.User;
import com.deyatech.admin.feign.AdminFeign;
import com.deyatech.common.entity.RestResult;
import com.deyatech.station.view.utils.DecodeData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述：
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/8 10:34
 */
@RestController
@Slf4j
@RequestMapping("/sync")
public class SyncController {

    @Autowired
    AdminFeign adminFeign;

    /**
     * 新增用户
     *
     * @return
     */
    @PostMapping(value = "/syncUser")
    @ResponseBody
    public RestResult syncUser(HttpServletRequest request, HttpServletResponse response){
        //获取请求中的数据。
        String data = request.getParameter("data");
        //数据解密。为 null，数据解密失败。
        data = DecodeData.decrypt(data);
        int successNum = 0;
        if(data != null) {
            //转化为json对象。
            JSONObject jo = JSONUtil.parseObj(data);
            if(null != jo.get("userId") && Validator.isNotNull(jo.get("userId").toString())) {
                boolean bool = (Boolean) adminFeign.insertUser(initUser(jo)).getData();
                if(bool){
                    successNum += 1;
                }
            }
        }
        if(successNum > 0) {
            response.addHeader("ACTION_RESULT", "true");
        } else {
            response.addHeader("ACTION_RESULT", "false");
        }
        return RestResult.ok();
    }

    /**
     * 删除用户
     *
     * @return
     */
    @PostMapping(value = "/deleteUser")
    @ResponseBody
    public RestResult deleteUser(HttpServletRequest request, HttpServletResponse response){
        //获取请求中的数据。
        String data = request.getParameter("data");
        //数据解密。为 null，数据解密失败。
        data = DecodeData.decrypt(data);
        int successNum = 0;
        if(data != null) {
            //转化为json对象。
            JSONObject jo = JSONUtil.parseObj(data);
            if(null != jo.get("userId") && Validator.isNotNull(jo.get("userId").toString())) {
                User user = new User();
                user.setId(jo.getStr("userId"));
                boolean bool = adminFeign.removeUser(user).getData();
                if(bool){
                    successNum += 1;
                }
            }
        }
        if(successNum > 0) {
            response.addHeader("ACTION_RESULT", "true");
        } else {
            response.addHeader("ACTION_RESULT", "false");
        }
        return RestResult.ok();
    }


    /**
     * 新增部门
     *
     * @return
     */
    @PostMapping(value = "/syncDepartment")
    @ResponseBody
    public RestResult syncDepartment(HttpServletRequest request, HttpServletResponse response){
        //获取请求中的数据。
        String data = request.getParameter("data");
        //数据解密。为 null，数据解密失败。
        data = DecodeData.decrypt(data);
        int successNum = 0;
        if(data != null) {
            //转化为json对象。
            JSONObject jo = JSONUtil.parseObj(data);
            if(null != jo.get("deptId") && Validator.isNotNull(jo.get("deptId").toString())) {
                boolean bool = adminFeign.insertDepartment(initDepartment(jo)).getData();
                if(bool){
                    successNum += 1;
                }
            }
        }
        if(successNum > 0) {
            response.addHeader("ACTION_RESULT", "true");
        } else {
            response.addHeader("ACTION_RESULT", "false");
        }
        return RestResult.ok();
    }
    /**
     * 删除部门
     *
     * @return
     */
    @PostMapping(value = "/deleteDepartment")
    @ResponseBody
    public RestResult deleteDepartment(HttpServletRequest request, HttpServletResponse response){
        //获取请求中的数据。
        String data = request.getParameter("data");
        //数据解密。为 null，数据解密失败。
        data = DecodeData.decrypt(data);
        int successNum = 0;
        if(data != null) {
            //转化为json对象。
            JSONObject jo = JSONUtil.parseObj(data);
            if(null != jo.get("deptId") && Validator.isNotNull(jo.get("deptId").toString())) {
                Department department = new Department();
                department.setId(jo.getStr("deptId"));
                boolean bool = adminFeign.removeDepartment(department).getData();
                if(bool){
                    successNum += 1;
                }
            }
        }
        if(successNum > 0) {
            response.addHeader("ACTION_RESULT", "true");
        } else {
            response.addHeader("ACTION_RESULT", "false");
        }
        return RestResult.ok();
    }


    public Department initDepartment(JSONObject jo){
        Department department = new Department();
        //ID
        department.setId(jo.getStr("deptId"));
        //编码
        department.setCode(jo.getStr("codeNum"));
        //名称
        department.setName(jo.getStr("name"));
        //父节点ID
        department.setParentId(jo.getStr("parentId"));
        //备注
        department.setRemark(jo.getStr("remark"));
        return department;
    }
    public User initUser(JSONObject jo){
        User user = new User();
        //ID
        user.setId(jo.getStr("userId"));
        //工号
        user.setEmpNo(jo.getStr("codeNum"));
        //姓名
        user.setName(jo.getStr("userName"));
        //登录名
        user.setAccount(jo.getStr("loginName"));
        //密码
        user.setPassword(jo.getStr("enPwd"));
        //电话
        user.setPhone(jo.getStr("mobile"));
        //归属部门
        user.setDepartmentId(jo.getStr("userDeptIds"));
        String status = jo.getStr("status");
        if(status.equals("true")){
            user.setEnable(1);
        }else{
            user.setEnable(0);
        }
        user.setRemark(jo.getStr("remark"));
        return user;
    }
}
