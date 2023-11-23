package com.lt.stock.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/*
    个股交易流水行情数据查询--查询最新交易流水
 */
@Data
public class StockRtLastTradeInfo {
    private String date; // 当前时间 精确到分钟
    private Long tradeAmt; // 交易量
    private BigDecimal tradeVol; // 交易金额
    private BigDecimal tradePrice; // 交易价格
}