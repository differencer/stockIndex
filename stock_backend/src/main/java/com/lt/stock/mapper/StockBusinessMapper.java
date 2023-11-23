package com.lt.stock.mapper;

import com.lt.stock.pojo.entity.StockBusiness;
import com.lt.stock.pojo.vo.StockRtDescriptionVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author teng
 * @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
 * @createDate 2023-01-06 18:23:05
 * @Entity com.lt.stock.pojo.entity.StockBusiness
 */
@Repository
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

    /**
     * 获取所有股票的code
     */
    List<String> getAllStockCode();

    StockRtDescriptionVo queryRtStockBusiness(@Param("code") String code);
}
