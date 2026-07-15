package com.Market.Data.Management.System.transformers;

import com.Market.Data.Management.System.dto.ConsolidatedMarketDataDto;
import com.Market.Data.Management.System.model.ConsolidatedMarketData;

public class ConsolidatedMarketDataTransformers {
    public static ConsolidatedMarketDataDto entityToDto (ConsolidatedMarketData consolidatedMarketData){
        ConsolidatedMarketDataDto consolidatedMarketDataDto = new ConsolidatedMarketDataDto();

        consolidatedMarketDataDto.setSymbol(consolidatedMarketData.getSymbol());
        consolidatedMarketDataDto.setLastTradePrice(consolidatedMarketData.getLastTradePrice());
        consolidatedMarketDataDto.setBidPrice(consolidatedMarketData.getBidPrice());
        consolidatedMarketDataDto.setMidPrice(consolidatedMarketData.getMidPrice());
        consolidatedMarketDataDto.setAskPrice(consolidatedMarketData.getAskPrice());
        consolidatedMarketDataDto.setMarketTimeStamp(consolidatedMarketData.getMarketTimeStamp());
        consolidatedMarketDataDto.setDependsOnSymbol(consolidatedMarketData.getDependsOnSymbol());
        consolidatedMarketDataDto.setSource(consolidatedMarketData.getSource());
        consolidatedMarketDataDto.setLastCouponDate(consolidatedMarketData.getLastCouponDate());
        consolidatedMarketDataDto.setInterestRate(consolidatedMarketData.getInterestRate());
        consolidatedMarketDataDto.setVolatility(consolidatedMarketData.getVolatility());
        consolidatedMarketDataDto.setAccruedInterest(consolidatedMarketData.getAccruedInterest());
        consolidatedMarketDataDto.setTheoreticalPrice(consolidatedMarketData.getTheoreticalPrice());

        return consolidatedMarketDataDto;


    }
}
