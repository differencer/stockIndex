package com.lt.stock.controller;

import com.lt.stock.common.Response;
import com.lt.stock.pojo.dto.LoginRequestDto;
import com.lt.stock.pojo.entity.SysUser;
import com.lt.stock.pojo.vo.ConditionQueryUserReq;
import com.lt.stock.pojo.vo.ConditionQueryUserResp;
import com.lt.stock.pojo.vo.LoginResponseVo;
import com.lt.stock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/6 18:10
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
//    @PostMapping("/login")
//    public Response<LoginResponseVo> login(@RequestBody LoginRequestDto loginRequestDto) {
//        return userService.login(loginRequestDto);
//    }

    /**
     * 生成验证码
     *
     * @return map:{code: xxx, rkey: xxx}
     */
    @GetMapping("/captcha")
    public Response<Map<String, String>> generateCaptcha() {
        return userService.generateCaptcha();
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Response<String> getUser() {
        return Response.ok("hello");
    }

    /*
         功能描述：多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
         服务路径：/api/users
         服务方法：Post
      */
    @PostMapping("/users")
    public Response<ConditionQueryUserResp> conditionsQueryUser(@RequestBody ConditionQueryUserReq req){
        return userService.conditionsQueryUser(req);
    }
    /*
        功能描述：添加用户信息
        服务路径：/api/users
        测试路径: /api/addUsers
        服务方法：Post
     */
    @PostMapping("/addUsers")
    public Response addUsers(@RequestBody SysUser adduser){
        return userService.addUsers(adduser);
    }

}
