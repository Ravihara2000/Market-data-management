package com.Market.Data.Management.System.service.impl;

import com.Market.Data.Management.System.dto.ConsolidatedMarketDataDto;
import com.Market.Data.Management.System.exception.ResourceNotFoundException;
import com.Market.Data.Management.System.model.ConsolidatedMarketData;
import com.Market.Data.Management.System.model.MarketData;
import com.Market.Data.Management.System.repository.ConsolidatedMarketDataRepo;
import com.Market.Data.Management.System.repository.MarketDataRepo;
import com.Market.Data.Management.System.service.ConsolidatedMarketDataService;
import com.Market.Data.Management.System.transformers.ConsolidatedMarketDataTransformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ConsolidatedMarketDataServiceImpl implements ConsolidatedMarketDataService {

    @Autowired
    private AuditService auditService;

    @Autowired
    private ConsolidatedMarketDataRepo consolidatedMarketDataRepo;

    @Autowired
    private MarketDataRepo marketDataRepo;

    private static final Logger logger = LoggerFactory.getLogger(ConsolidatedMarketDataServiceImpl.class);

    public void recompute(String symbol) {
        List<MarketData> marketDataList = marketDataRepo.findBySymbolOrderByMarketTimeStampAsc(symbol);

        if (marketDataList.isEmpty()) {
            Optional<ConsolidatedMarketData> consolidatedMarketData = consolidatedMarketDataRepo.findBySymbol(symbol);
            consolidatedMarketData.ifPresent(consolidatedMarketDataRepo::delete);
            logger.info("No remaining source records for symbol={}, consolidated view removed", symbol);
            return;
        }

        Optional<ConsolidatedMarketData> consolidatedMarketDataOptional = consolidatedMarketDataRepo.findBySymbol(symbol);
        ConsolidatedMarketData consolidatedMarketData;

        consolidatedMarketData = consolidatedMarketDataOptional.orElseGet(() -> new ConsolidatedMarketData(symbol));

        for (MarketData marketData : marketDataList) {
            if (marketData.getBidPrice() != null) {
                consolidatedMarketData.setBidPrice(marketData.getBidPrice());
            }
            if (marketData.getAskPrice() != null) {
                consolidatedMarketData.setAskPrice(marketData.getAskPrice());
            }
            if (marketData.getMidPrice() != null) {
                consolidatedMarketData.setMidPrice(marketData.getMidPrice());
            }
            if (marketData.getLastTradePrice() != null) {
                consolidatedMarketData.setLastTradePrice(marketData.getLastTradePrice());
            }
            if (marketData.getMarketTimeStamp() != null) {
                consolidatedMarketData.setMarketTimeStamp(marketData.getMarketTimeStamp());
            }
            if (marketData.getDependsOnSymbol() != null) {
                consolidatedMarketData.setDependsOnSymbol(marketData.getDependsOnSymbol());
            }
            if (marketData.getSource() != null) {
                consolidatedMarketData.setSource(marketData.getSource());
            }
            if (marketData.getLastCouponDate() != null) {
                consolidatedMarketData.setLastCouponDate(marketData.getLastCouponDate());
            }
            if (marketData.getInterestRate() != null) {
                consolidatedMarketData.setInterestRate(marketData.getInterestRate());
            }
            if (marketData.getVolatility() != null) {
                consolidatedMarketData.setVolatility(marketData.getVolatility());
            }

            assert marketData.getLastCouponDate() != null;
            BigDecimal dayCount = BigDecimal.valueOf(ChronoUnit.DAYS.between(
                    marketData.getLastCouponDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    LocalDate.now()
            ));
            if(marketData.getLastTradePrice() != null && marketData.getInterestRate() != null && dayCount.compareTo(BigDecimal.ZERO)>0){
                consolidatedMarketData.setAccruedInterest(calculateAccruedInterest(marketData.getLastTradePrice(), marketData.getInterestRate(), dayCount));
            }

            if (marketData.getSymbol().equals("depends_on_symbol") && marketData.getLastTradePrice() != null && marketData.getVolatility() != null) {
                consolidatedMarketData.setTheoreticalPrice(marketData.getLastTradePrice().multiply(marketData.getVolatility()));
            }
            consolidatedMarketDataRepo.save(consolidatedMarketData);
            consolidatedMarketData.setUpdatedAt(Instant.now());
            logger.info("Recomputed consolidated view for symbol={} from {} source(s)", symbol, marketDataList.size());

            auditService.sendToAudit(Instant.now(),consolidatedMarketData,null,"UPDATED");
        }

    }

    @Override
    @Cacheable(value = "consolidatedMarketData", key = "#symbol")
    public ConsolidatedMarketDataDto getConsolidatedMarketDataBySymbol(String symbol) {
        logger.debug("Fetching consolidated market data for symbol={}", symbol);

        ConsolidatedMarketData consolidatedMarketData = consolidatedMarketDataRepo.findBySymbol(symbol)
                .orElseThrow(() -> {
                    logger.warn("Consolidated market data not found for symbol={}", symbol);
                    return new ResourceNotFoundException("No consolidated market data found for symbol: " + symbol);
                });

        return ConsolidatedMarketDataTransformers.entityToDto(consolidatedMarketData);
    }

    @Override
    @Cacheable(value = "consolidatedMarketData", key = "#symbols")
    public List<ConsolidatedMarketDataDto> getAllConsolidated(List<String> symbols) {
        if(symbols.isEmpty()) {
            throw new IllegalArgumentException("At least one symbol must be provided");
        }
        List<ConsolidatedMarketData> consolidatedMarketDataList = consolidatedMarketDataRepo.findBySymbolIn(symbols);
        List<ConsolidatedMarketDataDto> consolidatedMarketDataDtoList = new ArrayList<>();

        for(ConsolidatedMarketData c : consolidatedMarketDataList) {
            ConsolidatedMarketDataDto consolidatedMarketDataDto = ConsolidatedMarketDataTransformers.entityToDto(c);
            consolidatedMarketDataDtoList.add(consolidatedMarketDataDto);
        }

        return consolidatedMarketDataDtoList;
    }

    public BigDecimal calculateAccruedInterest(BigDecimal lastTradePrice, BigDecimal interestRate, BigDecimal dayCount) {
        return lastTradePrice.multiply(interestRate).divide(dayCount);
    }
}
