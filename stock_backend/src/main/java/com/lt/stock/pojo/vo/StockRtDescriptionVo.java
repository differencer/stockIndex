package com.lt.stock.pojo.vo;

import lombok.Data;
/*
    个股描述Domain
 */
@Data
public class StockRtDescriptionVo {
    private String code; // 股票编码
    private String trade; // 行业名称
    private String business; // 公司主营业务
    private String name; // 公司名称
}