package com.lt.stock.service.impl;


import com.lt.stock.common.Response;
import com.lt.stock.mapper.SysRoleMapper;
import com.lt.stock.mapper.SysUserRoleMapper;
import com.lt.stock.pojo.entity.SysRole;
import com.lt.stock.pojo.vo.OwnRoleAndAllRoleIdsDomain;
import com.lt.stock.pojo.vo.UpdateRoleInfoReq;
import com.lt.stock.service.RoleService;
import com.lt.stock.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    /*
    功能描述：获取用户具有的角色信息，以及所有角色信息
    服务路径：/user/roles/{userId}
    服务方法：Get
    请求参数：String userId
 */
    @Override
    public Response<OwnRoleAndAllRoleIdsDomain> queryRolesById(String id) {
        List<String> ids = sysRoleMapper.queryRolesById(id);
        List<SysRole> allRole = sysRoleMapper.queryAllRole();
        OwnRoleAndAllRoleIdsDomain domain = new OwnRoleAndAllRoleIdsDomain();
        domain.setOwnRoleIds(ids);
        domain.setAllRole(allRole);
        return Response.ok(domain);
    }
    /*
        功能描述：更新用户角色信息
        服务路径：/user/roles
        服务方法：Put
     */
    @Override
    public Response updateRoleInfoById(UpdateRoleInfoReq req) {
        // 调用mapper层方法
        sysUserRoleMapper.deleteByUserId(req.getUserId());
        List<String> roleIds = req.getRoleIds();
        for (String roleId : roleIds) {
            long primaryKey = new IdWorker().nextId();
            sysUserRoleMapper.inserByUserRoleIds(primaryKey,req.getUserId(),roleId,new Date());
        }
        return Response.ok("操作成功");
    }
}
