package com.lt.stock.pojo.vo;

import lombok.Data;

import java.util.List;

/*
    功能描述：更新用户角色信息
 */
@Data
public class UpdateRoleInfoReq {
    private String userId;
    private List<String> roleIds;
}
