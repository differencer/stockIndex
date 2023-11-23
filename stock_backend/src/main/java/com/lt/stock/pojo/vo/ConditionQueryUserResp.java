package com.lt.stock.pojo.vo;

import com.lt.stock.pojo.entity.SysUser;
import lombok.Data;

import java.util.List;

/*
    综合条件查询
 */
@Data
public class ConditionQueryUserResp {
    private Integer totalRows;
    private Integer totalPages;
    private Integer pageNum;
    private Integer pageSize;
    private Integer size;
    private List<SysUser> rows;
}