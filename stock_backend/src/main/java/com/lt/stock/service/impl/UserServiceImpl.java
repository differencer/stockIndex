package com.lt.stock.service.impl;

import com.lt.stock.common.Response;
import com.lt.stock.common.enums.Number;
import com.lt.stock.common.enums.ResponseCode;
import com.lt.stock.mapper.SysUserMapper;
import com.lt.stock.pojo.entity.SysPermission;
import com.lt.stock.pojo.entity.SysUser;
import com.lt.stock.pojo.dto.LoginRequestDto;
import com.lt.stock.pojo.vo.ConditionQueryUserReq;
import com.lt.stock.pojo.vo.ConditionQueryUserResp;
import com.lt.stock.pojo.vo.LoginResponseVo;
import com.lt.stock.pojo.vo.PermissionRespNodeVo;
import com.lt.stock.service.UserService;
import com.lt.stock.utils.IdWorker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/6 19:11
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Response<LoginResponseVo> login(LoginRequestDto loginRequestDto) {
        if (loginRequestDto == null) {
            return Response.error(ResponseCode.DATA_ERROR.getMessage());
        }
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        String code = loginRequestDto.getCode();
        String rKey = loginRequestDto.getRkey();
        if (StringUtils.isBlank(code)) {
            return Response.error(ResponseCode.SYSTEM_VERIFY_CODE_NOT_EMPTY.getMessage());
        }
        if (StringUtils.isAnyBlank(username, password, rKey)) {
            return Response.error(ResponseCode.DATA_ERROR.getMessage());
        }
        // 从 Redis 中获取验证码
        String rCode = stringRedisTemplate.opsForValue().get(rKey);
        // 验证码过期
        if (StringUtils.isBlank(rCode)) {
            return Response.error(ResponseCode.SYSTEM_VERIFY_CODE_EXPIRED.getMessage());
        }
        // 验证码错误
        if (!code.equals(rCode)) {
            return Response.error(ResponseCode.SYSTEM_VERIFY_CODE_ERROR.getMessage());
        }
        // redis 清除 key
        stringRedisTemplate.delete(rKey);
        // 根据用户名查询用户信息
        SysUser user = sysUserMapper.findByUserName(username);
        // 判断用户是否存在，存在则进行密码校验对比
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return Response.error(ResponseCode.SYSTEM_PASSWORD_ERROR.getMessage());
        }
        // 判断用户状态
        Integer status = user.getStatus();
        if (status == Number.Two.getNumber()) {
            return Response.error(ResponseCode.SYSTEM_USERNAME_LOCKED.getMessage());
        }
        LoginResponseVo loginResponseVo = new LoginResponseVo();
        BeanUtils.copyProperties(user, loginResponseVo);
        return Response.ok(loginResponseVo);
    }

    @Override
    public Response<Map<String, String>> generateCaptcha() {
        // 生成4位数字验证码
        String code = RandomStringUtils.randomNumeric(4);
        // 生成唯一id
        String rkey = String.valueOf(idWorker.nextId());
        // 验证码存入 redis 中，有效期为1分钟
        stringRedisTemplate.opsForValue().set(rkey, code, 60, TimeUnit.SECONDS);
        // 返回数据
        Map<String, String> map = new HashMap<>(2);
        map.put("rkey", rkey);
        map.put("code", code);
        return Response.ok(map);
    }

    /**
     * @param id   父权限id
     * @param list 权限数据
     * @return 子权限集合
     */
    private List<SysPermission> getChildMenus(String id, List<SysPermission> list) {
        // 创建容器 存放子权限
        List<SysPermission> children = new ArrayList<>();
        // 根据传过来的父权限id查询所有子权限
        list.forEach(s -> {
            if (id.equals(s.getPid())) {
                children.add(s);
            }
        });
        children.forEach(s -> s.setChildren(getChildMenus(s.getId(), list)));
        return children;
    }

    private List<PermissionRespNodeVo> copyChildren(List<SysPermission> sysList, List<PermissionRespNodeVo> perList) {
        if (sysList != null && sysList.size() != 0) {
            for (SysPermission sysp : sysList) {
                PermissionRespNodeVo p = new PermissionRespNodeVo();
                p.setChildren(new ArrayList<PermissionRespNodeVo>());
                // 类型转换
                p.setId(sysp.getId());
                p.setTitle(sysp.getTitle());
                p.setIcon(sysp.getIcon());
                p.setPath(sysp.getUrl());
                p.setName(sysp.getName());
                perList.add(p);
                // 判断子权限是否还是子权限
                if(sysp.getChildren().size() !=0){
                    copyChildren(sysp.getChildren(),p.getChildren());
                }
            }
        }
        return perList;
    }



    /*
        功能描述：多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
        服务路径：/api/users
        服务方法：Post
     */
    @Override
    public Response<ConditionQueryUserResp> conditionsQueryUser(ConditionQueryUserReq req) {
        // 1 创建一个容器存放数据
        ConditionQueryUserResp resp = new ConditionQueryUserResp();
        // 2 调用mapper层对象
        List<SysUser> users = sysUserMapper.conditionsQueryUser(req);
        // 3 封装数据
        resp.setTotalRows(users.size());
        int pageSize = Integer.parseInt(req.getPageSize());
        Integer pageCount = (users.size() + pageSize - 1) / pageSize;
        resp.setTotalPages(pageCount);
        resp.setPageNum(Integer.parseInt(req.getPageNum()));
        resp.setPageSize(pageSize);
        resp.setSize(users.size());
        resp.setRows(users);
        return Response.ok(resp);
    }

    /*
        功能描述：添加用户信息
        服务路径：/api/users
        测试路径: /api/addUsers
        服务方法：Post
     */
    @Override
    public Response addUsers(SysUser adduser) {
        // 调用mapper层方法
        adduser.setId(new IdWorker().nextId() + "");
        sysUserMapper.insertUser(adduser);
        return Response.ok("操作成功");
    }


}
