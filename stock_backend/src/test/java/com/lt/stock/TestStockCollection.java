package com.lt.stock;

import com.lt.stock.service.StockTimerTaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: taotao
 * @date: 2023/11/19 16:51
 */
@SpringBootTest
@Slf4j
public class TestStockCollection {

    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    @Test
    public void testCollectionInner() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long start = System.currentTimeMillis();
        String start_f = sdf.format(new Date(start));
        log.info("开始时间："+start_f);
        stockTimerTaskService.collectInnerMarketInfo();
        long end=System.currentTimeMillis();
        String end_f = sdf.format(new Date(end));
        log.info("结束时间："+start_f);
        log.info("执行时长："+(end-start)+ "毫秒");
    }

    @Test
    public void testCollectAShareInfo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long start = System.currentTimeMillis();
        String start_f = sdf.format(new Date(start));
        log.info("开始时间："+start_f);
        stockTimerTaskService.collectAShareInfo();
        long end=System.currentTimeMillis();
        String end_f = sdf.format(new Date(end));
        log.info("结束时间："+start_f);
        log.info("执行时长："+(start-end)+ "毫秒");
    }

    @Test
    public void testCollectStockSectorRtIndex() {
        stockTimerTaskService.collectStockSectorRtIndex();
    }
}
