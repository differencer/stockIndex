package com.lt.stock.controller;

import com.lt.stock.common.PageResult;
import com.lt.stock.common.Response;
import com.lt.stock.common.enums.ResponseCode;
import com.lt.stock.pojo.vo.StockRtDescriptionVo;
import com.lt.stock.pojo.vo.WeeklineVo;
import com.lt.stock.pojo.vo.*;
import com.lt.stock.service.StockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: taotao
 * @date: 2023/1/6 18:28
 */
@RestController
@RequestMapping("/api/quot")
@CrossOrigin
public class StockController {
    @Autowired
    private StockService stockService;

    /**
     * 获取国内最新大盘指数
     */
    @GetMapping("/index/all")
    public Response<List<InnerMarketResponseVo>> getInnerIndexAll() {
        return stockService.getInnerIndexAll();
    }

    /**
     * 外盘指数行情数据查询，根据时间和大盘点数降序排序取前4
     */
    @GetMapping("/external/index")
    public Response<List<OuterMarketResponseVo>> getOuterMarketAll() {
        return stockService.getOuterMarketAll();
    }

    /**
     * 获取沪深两市板块分时行情数据，以交易时间和交易总金额降序查询，取前10条数据
     */
    @GetMapping("/sector/all")
    public Response<List<StockBlockResponseVo>> getStockBlockRtInfoAllLimit() {
        return stockService.getStockBlockRtInfoAllLimit();
    }

    /**
     * 获取沪深两市个股最新交易数据，并按涨幅降序排序查询前10条数据
     */
    @GetMapping("/stock/increase")
    public Response<List<StockUpDownResponseVo>> getStockUpDownAllLimit() {
        return stockService.getStockUpDownAllLimit();
    }

    /**
     * 沪深两市个股行情列表查询 ,以时间顺序和涨幅分页查询
     *
     * @param page     当前页
     * @param pageSize 每页大小
     */
    @GetMapping("/stock/all")
    public Response<PageResult<StockUpDownResponseVo>> stockPage(Integer page, Integer pageSize) {
        if (page == null || pageSize == null) {
            return Response.error(ResponseCode.DATA_ERROR.getMessage());
        }
        if (page <= 0 || pageSize <= 0) {
            return Response.error(ResponseCode.DATA_ERROR.getMessage());
        }
        return stockService.stockPage(page, pageSize);
    }

    /**
     * 将指定页数据导出到excel表下
     *
     * @param response response
     * @param page     当前页
     * @param pageSize 每页大小
     */
    @GetMapping("/stock/export")
    public void stockExport(HttpServletResponse response, Integer page, Integer pageSize) {
        stockService.stockExport(response, page, pageSize);
    }

    /**
     * 沪深两市涨跌停分时行情数据查询，查询T日每分钟的涨跌停数据（T：当前股票交易日）
     *
     * @return map:{upList:涨停数据统计，downList:跌停数据统计}
     */
    @GetMapping("/stock/updown/count")
    public Response<Map> getStockUpDownCount() {
        return stockService.getStockUpDownCount();
    }

    /**
     * 统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     *
     * @return map:{"volList":
     * [{"count": 3926392,"time": "202112310930"},......],
     * "yesVolList":
     * [{"count": 3926392,"time": "202112310930"},......]}
     */
    @GetMapping("/stock/tradevol")
    public Response<Map> getStockTradeAccountCount() {
        return stockService.getStockTradeAccountCount();
    }

    /**
     * 统计指定股票T日每分钟的交易数据
     *
     * @param code 股票编码
     */
    @GetMapping("/stock/screen/time-sharing")
    public Response<List<StockMinuteResponseVo>> getStockMinute(String code) {
        if (StringUtils.isBlank(code)) {
            return Response.error(ResponseCode.DATA_ERROR.getMessage());
        }
        return stockService.getStockMinute(code);
    }

    /**
     * 统计当前时间下（精确到分钟），股票在各个涨跌区间的数量
     */
    @GetMapping("/stock/updown")
    public Response<Map> getStockUpDown() {
        return stockService.getStockUpDown();
    }

    /**
     * 个股日K数据查询 ，可以根据时间区间查询数日的K线数据  默认查询历史20天的数据
     *
     * @param code 股票编码
     */
    @GetMapping("/stock/screen/dkline")
    public Response<List<StockDayResponseVo>> getStockDay(String code) {
        if (StringUtils.isBlank(code)) {
            return Response.error(ResponseCode.DATA_ERROR.getMessage());
        }
        return stockService.getStockDay(code);
    }

    /**
     * 根据输入的个股代码，进行模糊查询，返回证券代码和证券名称
     *
     * @param searchStr 只接受代码模糊查询，不支持文字查询
     */
    @GetMapping("/stock/search")
    public Response<List<StockSearchResponseVo> > getStockSearch(String searchStr) {
        if (StringUtils.isBlank(searchStr)) {
            return Response.error(ResponseCode.DATA_ERROR.getMessage());
        }
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(searchStr);
        if (!matcher.find()) {
            return Response.error(ResponseCode.NO_CHINESE_DATA.getMessage());
        }
        return stockService.getStockSearch(searchStr);
    }

    /*
    功能描述：个股主营业务查询接口
    服务路径：/api/quot/stock/describe
    服务方法：GET
    请求参数：code #股票编码
 */
    @GetMapping("/stock/describe")
    public Response<StockRtDescriptionVo> queryRtStockBusiness(String code){
        return stockService.queryRtStockBusiness(code);
    }


    /*
    功能描述：统计每周内的股票数据信息，信息包含：
    股票ID、 一周内最高价、 一周内最低价 、周1开盘价、周5的收盘价、
    整周均价、以及一周内最大交易日期（一般是周五所对应日期）;
    服务路径：/api/quot/stock/screen/weekkline
    服务方法：GET
    请求参数：code //股票编码
 */
    @GetMapping("/stock/screen/weekkline")
    public Response<List<WeeklineVo>> getRtStockWeekline(String code){
        return stockService.getRtStockWeekline(code);
    }

    /*
    功能描述：
    获取个股最新分时行情数据，主要包含：
    开盘价、前收盘价、最新价、最高价、最低价、成交金额和成交量、交易时间信息;
    服务路径：/api/quot/stock/screen/second/detail
    服务方法：GET
    请求参数：code //股票编码
    请求频率：每分钟
 */
    @GetMapping("/stock/screen/second/detail")
    public Response<List<StockMinuteResponseVo>> getStockRtMinuteInfo(String code){
        return stockService.getStockMinute(code);
    }

    /*
     功能描述：个股交易流水行情数据查询--查询最新交易流水，按照交易时间降序取前10
     服务路径：/quot/stock/screen/second
     服务方法：GET
     请求频率：5秒
     *********服务路径不是 api/quot/........
  */
    @GetMapping("/stock/screen/second")
    public Response<List<StockRtLastTradeInfo>> queryStockRtLastTradeInfo(String code){
        return stockService.queryStockRtLastTradeInfo(code);
    }
}
