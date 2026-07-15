package com.Market.Data.Management.System.service;

import com.Market.Data.Management.System.dto.ConsolidatedMarketDataDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConsolidatedMarketDataService {

    ConsolidatedMarketDataDto getConsolidatedMarketDataBySymbol(String symbol);


    List<ConsolidatedMarketDataDto> getAllConsolidated(List<String> symbols);
}
