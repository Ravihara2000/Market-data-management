package com.Market.Data.Management.System.controller;

import com.Market.Data.Management.System.annotation.SecureCoreController;
import com.Market.Data.Management.System.dto.MarketDataDto;
import com.Market.Data.Management.System.dto.MarketDto;
import com.Market.Data.Management.System.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecureCoreController("api/v1/market-data")
public class MarketDataController {
    @Autowired
    private MarketDataService marketDataService;

    @PostMapping
    public ResponseEntity<String> postMarketData(@RequestBody MarketDataDto marketDataDto){
        marketDataService.postMarketData(marketDataDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MarketDto> getBySourceAndSymbol(@RequestParam(value = "source") String source, @RequestParam(value = "symbol") String symbol){
        MarketDto marketDto = marketDataService.getBySourceAndSymbol(source,symbol);
        return ResponseEntity.ok(marketDto);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMarketData(@RequestParam(value = "source") String source, @RequestParam(value = "symbol") String symbol){
        marketDataService.deleteMarketData(source,symbol);
        return ResponseEntity.ok().build();
    }
}
