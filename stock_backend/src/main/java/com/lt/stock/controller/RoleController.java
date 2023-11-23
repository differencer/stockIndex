package com.lt.stock.controller;


import com.lt.stock.common.Response;
import com.lt.stock.pojo.vo.OwnRoleAndAllRoleIdsDomain;
import com.lt.stock.pojo.vo.UpdateRoleInfoReq;
import com.lt.stock.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class RoleController {
    @Autowired
    private RoleService roleService;
    /*
    功能描述：获取用户具有的角色信息，以及所有角色信息
    服务路径：/user/roles/{userId}
    服务方法：Get
    请求参数：String userId
 */
    @GetMapping("/user/roles/{userId}")
    public Response<OwnRoleAndAllRoleIdsDomain> queryRolesById(@PathVariable String userId){
        return roleService.queryRolesById(userId);
    }


    /*
        功能描述：更新用户角色信息
        服务路径：/user/roles
        服务方法：Put
     */
    @PutMapping("/user/roles")
    public Response updateRoleInfoById(@RequestBody UpdateRoleInfoReq req){
        return roleService.updateRoleInfoById(req);
    }
}
