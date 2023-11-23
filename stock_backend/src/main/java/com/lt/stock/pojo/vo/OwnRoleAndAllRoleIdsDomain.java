package com.lt.stock.pojo.vo;


import com.lt.stock.pojo.entity.SysRole;
import lombok.Data;

import java.util.List;

/*
    用户具有的角色信息，以及所有角色信息
 */
@Data
public class OwnRoleAndAllRoleIdsDomain {
    private List<String> ownRoleIds; // 用户具有的角色id
    private List<SysRole> allRole; // 所有角色的信息
}
