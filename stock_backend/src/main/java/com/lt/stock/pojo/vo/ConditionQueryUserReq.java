package com.lt.stock.pojo.vo;

import lombok.Data;
/*
    多条件综合查询的请求参数封装
 */
@Data
public class ConditionQueryUserReq {
    private String pageNum;
    private String pageSize;
    private String username;
    private String nickName;
    private String startTime;
    private String endTime;
}