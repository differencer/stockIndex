package com.lt.stock.service;

import com.lt.stock.common.Response;
import com.lt.stock.pojo.entity.SysPermission;
import com.lt.stock.pojo.vo.PermissionRespNodeVo;

import java.util.List;

/**
 * @description:
 * @author: TaoTao
 * @date: 2023/11/14 18:46
 */
public interface PermissionService {
    List<PermissionRespNodeVo> getTree(List<SysPermission> permissionList, String pid, boolean isOnlyMenuType);

    Response<List<SysPermission>> treeNode();
}
