package com.Market.Data.Management.System.controller;

import com.Market.Data.Management.System.annotation.SecureCoreController;
import com.Market.Data.Management.System.dto.ConsolidatedMarketDataDto;
import com.Market.Data.Management.System.service.ConsolidatedMarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@SecureCoreController("api/v1/ConsolidatedMarketData")
public class ConsolidatedMarketDataController {
    @Autowired
    private ConsolidatedMarketDataService consolidatedMarketDataService;

    @GetMapping
    public ResponseEntity<ConsolidatedMarketDataDto> getConsolidatedMarketDataBySymbol(@RequestParam(value = "symbol") String symbol){
        ConsolidatedMarketDataDto consolidatedMarketDataDto =consolidatedMarketDataService.getConsolidatedMarketDataBySymbol(symbol);
        return ResponseEntity.ok(consolidatedMarketDataDto);
    }

    @GetMapping("/consolidated-by-symbol")
    public List<ResponseEntity<ConsolidatedMarketDataDto>> getAllConsolidated(@RequestParam List<String> symbols){
        List<ConsolidatedMarketDataDto> consolidatedMarketDataDtoList = consolidatedMarketDataService.getAllConsolidated(symbols);
        return consolidatedMarketDataDtoList.stream().map(ResponseEntity::ok).toList();
    }
}
