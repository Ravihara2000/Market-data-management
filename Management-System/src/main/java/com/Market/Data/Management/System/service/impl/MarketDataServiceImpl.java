package com.Market.Data.Management.System.service.impl;

import com.Market.Data.Management.System.dto.MarketDataDto;
import com.Market.Data.Management.System.dto.MarketDto;
import com.Market.Data.Management.System.model.ConsolidatedMarketData;
import com.Market.Data.Management.System.model.MarketData;
import com.Market.Data.Management.System.repository.ConsolidatedMarketDataRepo;
import com.Market.Data.Management.System.repository.MarketDataRepo;
import com.Market.Data.Management.System.service.MarketDataService;
import com.Market.Data.Management.System.transformers.ConsolidatedMarketDataTransformers;
import com.Market.Data.Management.System.transformers.MarketDataTransformer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class MarketDataServiceImpl implements MarketDataService {
    @Autowired
    private MarketDataRepo marketDataRepo;

    @Autowired
    private ConsolidatedMarketDataRepo consolidatedMarketDataRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ConsolidatedMarketDataServiceImpl consolidatedMarketDataService;


    @Override
    @Cacheable(value = "marketData", key = "#marketDataDto.getSymbol()")
    public void postMarketData(MarketDataDto marketDataDto) {
        MarketData marketData = marketDataRepo
                .findBySymbolAndSource(marketDataDto.getSymbol(), marketDataDto.getSource())
                .orElseGet(() -> new MarketData(marketDataDto.getSymbol(), marketDataDto.getSource()));

        marketData.setLastTradePrice(marketDataDto.getLastTradePrice());
        marketData.setBidPrice(marketDataDto.getBidPrice());
        marketData.setMidPrice(marketDataDto.getMidPrice());
        marketData.setAskPrice(marketDataDto.getAskPrice());
        marketData.setMarketTimeStamp(marketDataDto.getMarketTimeStamp());
        marketData.setDependsOnSymbol(marketDataDto.getDependsOnSymbol());
        marketData.setLastCouponDate(marketDataDto.getLastCouponDate());
        marketData.setInterestRate(marketDataDto.getInterestRate());
        marketData.setVolatility(marketDataDto.getVolatility());

        marketDataRepo.save(marketData);
        consolidatedMarketDataService.recompute(marketData.getSymbol());
    }

    @Override
    @Cacheable(value = "marketData", key = "#symbol")
    public MarketDto getBySourceAndSymbol(String source, String symbol) {
        Optional<MarketData> marketData = marketDataRepo.findBySymbolAndSource(symbol, source);
        return marketData.map(MarketDataTransformer::entityToDto).orElse(null);
    }

    @Override
    @Transactional
    @Cacheable(value = "marketData", key = "#symbol")
    public void deleteMarketData(String source, String symbol) {
        MarketData m = marketDataRepo.deleteBySymbolAndSource(symbol, source);
        ConsolidatedMarketData c = consolidatedMarketDataRepository.deleteBySymbolAndSource(symbol, source);

        if (m != null && c != null) {
            auditService.sendToAudit(Instant.now(), c, null, "DELETE");
            auditService.sendToAudit(Instant.now(), m, null, "DELETE");
        } else if (m != null && c == null) {
            auditService.sendToAudit(Instant.now(), m, null, "DELETE");
        } else{
            auditService.sendToAudit(Instant.now(), c, null, "DELETE");

        }
    }
}
