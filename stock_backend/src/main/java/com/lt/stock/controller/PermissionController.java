package com.lt.stock.controller;


import com.lt.stock.common.Response;
import com.lt.stock.pojo.entity.SysPermission;
import com.lt.stock.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName PermissionController
 * @Description TODO
 * @Author 久思
 * @Date 2023/10/8 10:25
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api")
@CrossOrigin
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    /**
     * 树状结构回显权限集合,底层通过递归获取权限数据集合
     * @return
     */
    @GetMapping("/permissions/tree/all")
    public Response<List<SysPermission>> treeNode() {
        return permissionService.treeNode();
    }
}
