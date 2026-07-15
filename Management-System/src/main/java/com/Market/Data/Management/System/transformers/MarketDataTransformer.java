package com.Market.Data.Management.System.transformers;

import com.Market.Data.Management.System.dto.MarketDataDto;
import com.Market.Data.Management.System.model.MarketData;

public class MarketDataTransformer {
    public static MarketData dtoToEntity(MarketDataDto marketDataDto) {
        MarketData marketData = new MarketData();

        marketData.setId(marketDataDto.getId());
        marketData.setSymbol(marketDataDto.getSymbol());
        marketData.setLastTradePrice(marketDataDto.getLastTradePrice());
        marketData.setBidPrice(marketDataDto.getBidPrice());
        marketData.setMidPrice(marketDataDto.getMidPrice());
        marketData.setAskPrice(marketDataDto.getAskPrice());
        marketData.setMarketTimeStamp(marketDataDto.getMarketTimeStamp());
        marketData.setDependsOnSymbol(marketDataDto.getDependsOnSymbol());
        marketData.setSource(marketDataDto.getSource());
        marketData.setLastCouponDate(marketDataDto.getLastCouponDate());
        marketData.setInterestRate(marketDataDto.getInterestRate());
        marketData.setVolatility(marketDataDto.getVolatility());

        return marketData;
    }

    public static MarketDataDto entityToDto(MarketData marketData) {
        MarketDataDto marketDataDto = new MarketDataDto();

        marketDataDto.setId(marketData.getId());
        marketDataDto.setSymbol(marketData.getSymbol());
        marketDataDto.setLastTradePrice(marketData.getLastTradePrice());
        marketDataDto.setBidPrice(marketData.getBidPrice());
        marketDataDto.setMidPrice(marketData.getMidPrice());
        marketDataDto.setAskPrice(marketData.getAskPrice());
        marketDataDto.setMarketTimeStamp(marketData.getMarketTimeStamp());
        marketDataDto.setDependsOnSymbol(marketData.getDependsOnSymbol());
        marketDataDto.setSource(marketData.getSource());
        marketDataDto.setLastCouponDate(marketData.getLastCouponDate());
        marketDataDto.setInterestRate(marketData.getInterestRate());
        marketDataDto.setVolatility(marketData.getVolatility());

        return marketDataDto;
    }
}
