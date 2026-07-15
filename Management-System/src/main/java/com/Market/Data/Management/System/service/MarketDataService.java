package com.Market.Data.Management.System.service;

import com.Market.Data.Management.System.dto.MarketDataDto;
import com.Market.Data.Management.System.dto.MarketDto;
import org.springframework.stereotype.Service;

@Service
public interface MarketDataService {
    void postMarketData(MarketDataDto marketDataDto);

    MarketDto getBySourceAndSymbol(String source, String symbol);

    void deleteMarketData(String source, String symbol);
}
