package com.lt.stock.service;



import com.lt.stock.common.Response;
import com.lt.stock.pojo.vo.OwnRoleAndAllRoleIdsDomain;
import com.lt.stock.pojo.vo.UpdateRoleInfoReq;

public interface RoleService {
    /*
    功能描述：获取用户具有的角色信息，以及所有角色信息
    服务路径：/user/roles/{userId}
    服务方法：Get
    请求参数：String userId
 */
    Response<OwnRoleAndAllRoleIdsDomain> queryRolesById(String id);
    /*
        功能描述：更新用户角色信息
        服务路径：/user/roles
        服务方法：Put
    */
    Response updateRoleInfoById(UpdateRoleInfoReq req);
}
