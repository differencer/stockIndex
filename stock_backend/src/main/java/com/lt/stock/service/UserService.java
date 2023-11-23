package com.lt.stock.service;

import com.lt.stock.common.Response;
import com.lt.stock.pojo.dto.LoginRequestDto;
import com.lt.stock.pojo.entity.SysUser;
import com.lt.stock.pojo.vo.ConditionQueryUserReq;
import com.lt.stock.pojo.vo.ConditionQueryUserResp;
import com.lt.stock.pojo.vo.LoginResponseVo;

import java.util.Map;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/6 19:11
 */
public interface UserService {
    Response<LoginResponseVo> login(LoginRequestDto loginRequestDto);

    Response<Map<String, String>> generateCaptcha();


    /*
        功能描述：多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
        服务路径：/api/users
        服务方法：Post
     */
    Response<ConditionQueryUserResp> conditionsQueryUser(ConditionQueryUserReq req);
    /*
        功能描述：添加用户信息
        服务路径：/api/users
        测试路径: /api/addUsers
        服务方法：Post
     */
    Response addUsers(SysUser adduser);
}
